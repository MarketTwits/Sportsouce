#!/usr/bin/env python3
"""Publish a byte-verified Android release, optionally with both desktop packages."""
import argparse
import hashlib
import json
import os
import re
from pathlib import Path
import subprocess
import tempfile
from update_manifest import create_manifest


def gh(*args, missing_ok=False):
    result = subprocess.run(["gh", *args], text=True, capture_output=True)
    if result.returncode:
        if missing_ok and "HTTP 404" in result.stderr:
            return None
        raise RuntimeError(f"gh {' '.join(args[:3])} failed: {result.stderr.strip()}")
    return result.stdout


def find_release(repo, tag):
    pages = json.loads(gh("api", "--paginate", "--slurp", f"repos/{repo}/releases?per_page=100"))
    matches = [release for page in pages for release in page if release["tag_name"] == tag]
    if len(matches) > 1:
        raise RuntimeError("Multiple releases have this tag")
    return matches[0] if matches else None


def check_tag(repo, tag, source_sha):
    ref = gh("api", f"repos/{repo}/git/ref/tags/{tag}", missing_ok=True)
    if ref is not None:
        commit = json.loads(gh("api", f"repos/{repo}/commits/{tag}"))
        if commit["sha"] != source_sha:
            raise RuntimeError("Existing tag points to a different commit")
        return True
    return False


def validate_draft(release, source_sha):
    if not release["draft"] or release["prerelease"] or release["target_commitish"] != source_sha:
        raise RuntimeError("Release is not a matching draft for this commit")


def digest(path):
    sha = hashlib.sha256()
    with path.open("rb") as f:
        while chunk := f.read(1024 * 1024):
            sha.update(chunk)
    return sha.digest()


def verify_published_release(repo, tag, source_sha, release, assets):
    if release["prerelease"] or not check_tag(repo, tag, source_sha):
        raise RuntimeError("Published release has an unexpected tag or channel")
    expected = {asset.name: asset for asset in assets}
    if (len(release["assets"]) != len(expected)
            or {asset["name"] for asset in release["assets"]} != set(expected)
            or any(asset.get("state") != "uploaded" for asset in release["assets"])):
        raise RuntimeError("Published release asset set is incomplete or unexpected")
    with tempfile.TemporaryDirectory(prefix="sportsauce-release-check-") as temp:
        gh("release", "download", tag, "--dir", temp)
        for name, original in expected.items():
            downloaded = Path(temp) / name
            if not downloaded.is_file() or digest(downloaded) != digest(original):
                raise RuntimeError(f"Published asset differs: {name}")
    if not check_tag(repo, tag, source_sha):
        raise RuntimeError("Published release tag disappeared during verification")


def validate_release_assets(tag, assets):
    match = re.fullmatch(r"([0-9]+\.[0-9]+\.[0-9]+)\.([1-9][0-9]*)", tag)
    if not match:
        raise ValueError("Invalid release tag")
    version, build = match.groups()
    parts = version.split(".")
    if (any(str(int(part)) != part or int(part) > 2_147_483_647 for part in parts)
            or int(build) > 9_223_372_036_854_775_807):
        raise ValueError("Release tag cannot be parsed consistently by the updater")
    by_suffix = {path.suffix: path for path in assets if path.suffix in {".apk", ".dmg", ".exe", ".json"}}
    if by_suffix[".apk"].name != f"Sportsauce-{version}-{build}.apk":
        raise ValueError("APK name does not match the release tag")
    if ".json" not in by_suffix:
        return
    try:
        manifest = json.loads(by_suffix[".json"].read_text(encoding="utf-8"))
        entries = manifest["assets"]
        if not isinstance(entries, list) or len(entries) != 2:
            raise ValueError("Expected exactly two desktop manifest entries")
        mac = next(entry for entry in entries if entry["platform"] == "macos")
        windows = next(entry for entry in entries if entry["platform"] == "windows")
        expected = create_manifest(version, build, by_suffix[".dmg"], mac["architecture"],
                                   by_suffix[".exe"], windows["architecture"])
    except (ValueError, KeyError, TypeError, StopIteration) as failure:
        raise ValueError("Invalid desktop release manifest") from failure
    if manifest != expected:
        raise ValueError("Desktop release manifest does not match the selected artifacts")


def publish(repo, tag, source_sha, title, notes, assets):
    assets = [Path(asset) for asset in assets]
    if len(assets) not in (3, 6) or len({p.name for p in assets}) != len(assets) or any(not p.is_file() or p.stat().st_size == 0 for p in assets):
        raise ValueError("Expected three Android assets or six complete release assets")
    expected_suffixes = {".apk", ".txt"} if len(assets) == 3 else {".apk", ".dmg", ".exe", ".json", ".txt"}
    if ({p.suffix for p in assets} != expected_suffixes
            or {p.name for p in assets if p.suffix == '.txt'} != {'whatsnew-ru-RU.txt', 'whatsnew-en-US.txt'}
            or {p.name for p in assets if p.suffix == '.json'} != ({'update-manifest.json'} if len(assets) == 6 else set())):
        raise ValueError("Unexpected release asset types")
    validate_release_assets(tag, assets)
    check_tag(repo, tag, source_sha)
    release = find_release(repo, tag)
    if release is not None and not release["draft"]:
        verify_published_release(repo, tag, source_sha, release, assets)
        return
    if release is None:
        gh("release", "create", tag, "--draft", "--target", source_sha, "--title", title, "--notes", notes)
        release = find_release(repo, tag)
        if release is None:
            raise RuntimeError("Created draft was not found")
    validate_draft(release, source_sha)
    expected = {p.name: p for p in assets}
    existing = {asset["name"] for asset in release["assets"]}
    if len(existing) != len(release["assets"]) or not existing.issubset(expected):
        raise RuntimeError("Draft contains unexpected release assets")
    with tempfile.TemporaryDirectory(prefix="sportsauce-release-") as temp:
        if existing:
            gh("release", "download", tag, "--dir", temp)
            for name in existing:
                path = Path(temp) / name
                if not path.is_file() or digest(path) != digest(expected[name]):
                    raise RuntimeError(f"Existing draft asset differs: {name}")
        missing = [str(p) for p in assets if p.name not in existing]
        if missing:
            gh("release", "upload", tag, *missing)
        # Re-download all files, including previously uploaded ones, before making the draft public.
        verified = Path(temp) / "verified"
        verified.mkdir()
        gh("release", "download", tag, "--dir", str(verified))
        release = find_release(repo, tag)
        if release is None:
            raise RuntimeError("Draft disappeared during upload")
        validate_draft(release, source_sha)
        if (len(release["assets"]) != len(expected)
                or {asset["name"] for asset in release["assets"]} != set(expected)
                or any(asset.get("state") != "uploaded" for asset in release["assets"])):
            raise RuntimeError("Draft asset set is incomplete or unexpected")
        for name, original in expected.items():
            downloaded = verified / name
            if not downloaded.is_file() or digest(downloaded) != digest(original):
                raise RuntimeError(f"Uploaded asset differs: {name}")
    check_tag(repo, tag, source_sha)
    gh("release", "edit", tag, "--draft=false", "--latest", "--title", title, "--notes", notes)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("assets", nargs="+")
    args = parser.parse_args()
    publish(os.environ["GH_REPO"], os.environ["RELEASE_TAG"], os.environ["GITHUB_SHA"],
            os.environ["RELEASE_NAME"], os.environ["RELEASE_NOTES"], args.assets)


if __name__ == "__main__":
    main()
