#!/usr/bin/env python3
"""Build update metadata from the exact artifacts selected for a GitHub release."""

import argparse
import hashlib
import json
import pathlib
import re
import struct


def executable_architecture(path: pathlib.Path, platform: str) -> str:
    """Read CPU identity from a packaged launcher, not from the CI runner."""
    with path.open("rb") as source:
        header = source.read(64)
        if platform == "windows":
            if len(header) < 64 or header[:2] != b"MZ":
                raise ValueError("Invalid PE executable")
            offset = struct.unpack_from("<I", header, 60)[0]
            if offset > 1024 * 1024:
                raise ValueError("Invalid PE header offset")
            source.seek(offset)
            pe = source.read(6)
            if len(pe) != 6 or pe[:4] != b"PE\x00\x00":
                raise ValueError("Invalid PE signature")
            machine = struct.unpack_from("<H", pe, 4)[0]
            arch = {0x8664: "x64", 0xAA64: "arm64"}.get(machine)
        elif platform == "macos":
            if len(header) < 8 or header[:4] not in (b"\xcf\xfa\xed\xfe", b"\xfe\xed\xfa\xcf"):
                raise ValueError("Expected a single-architecture 64-bit Mach-O executable")
            order = "<" if header[:4] == b"\xcf\xfa\xed\xfe" else ">"
            cpu = struct.unpack_from(order + "I", header, 4)[0]
            arch = {0x01000007: "x64", 0x0100000C: "arm64"}.get(cpu)
        else:
            raise ValueError("Unsupported desktop platform")
    if arch is None:
        raise ValueError("Unsupported executable architecture")
    return arch


def asset(path: pathlib.Path, platform: str, architecture: str, package_type: str, expected_name: str) -> dict:
    if architecture not in {"x64", "arm64"}:
        raise ValueError(f"Unsupported architecture: {architecture}")
    if path.name != expected_name or not path.is_file():
        raise ValueError(f"Missing or unexpected {platform} artifact: {path}")
    digest = hashlib.sha256()
    size = 0
    with path.open("rb") as source:
        while chunk := source.read(1024 * 1024):
            size += len(chunk)
            digest.update(chunk)
    if size == 0:
        raise ValueError(f"Empty {platform} artifact")
    return {"platform": platform, "architecture": architecture, "packageType": package_type,
            "name": path.name, "size": size, "sha256": digest.hexdigest()}


def create_manifest(version: str, build: str, mac: pathlib.Path, mac_arch: str,
                    windows: pathlib.Path, windows_arch: str) -> dict:
    if not re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+", version) or not re.fullmatch(r"[1-9][0-9]*", build):
        raise ValueError("Version or build number has an invalid format")
    # Architecture is supplied by the build jobs after inspecting packaged binaries.
    prefix = f"Sportsauce-{version}-{build}"
    return {"schemaVersion": 1, "releaseId": f"{version}.{build}", "versionName": version,
            "buildNumber": int(build), "channel": "stable", "assets": [
                asset(mac, "macos", mac_arch, "dmg", prefix + ".dmg"),
                asset(windows, "windows", windows_arch, "exe", prefix + ".exe"),
            ]}


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--version", required=True)
    parser.add_argument("--build", required=True)
    parser.add_argument("--mac", required=True, type=pathlib.Path)
    parser.add_argument("--mac-architecture", required=True)
    parser.add_argument("--windows", required=True, type=pathlib.Path)
    parser.add_argument("--windows-architecture", required=True)
    parser.add_argument("--output", required=True, type=pathlib.Path)
    args = parser.parse_args()
    manifest = create_manifest(args.version, args.build, args.mac, args.mac_architecture,
                               args.windows, args.windows_architecture)
    args.output.write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
