#!/usr/bin/env python3
"""Generate a signed single-release Sparkle appcast. Does not publish anything."""
import argparse
import base64
import re
import subprocess
import sys
import tempfile
import hashlib
import plistlib
import stat
import struct
import zipfile
import xml.etree.ElementTree as ET
from pathlib import Path

SPARKLE = "http://www.andymatuschak.org/xml-namespaces/sparkle"
FEED_URL = "https://github.com/MarketTwits/Sportsouce/releases/latest/download/appcast.xml"
ET.register_namespace("sparkle", SPARKLE)


def inspect_macho(data):
    if len(data) < 32 or data[:4] not in (b'\xcf\xfa\xed\xfe', b'\xfe\xed\xfa\xcf'):
        raise ValueError("Expected a single-architecture 64-bit Mach-O")
    order = '<' if data[:4] == b'\xcf\xfa\xed\xfe' else '>'
    cpu, _, _, count, size = struct.unpack_from(order + 'IIIII', data, 4)
    if cpu != 0x0100000c or count > 4096 or size > 1024 * 1024 or 32 + size > len(data):
        raise ValueError("Unsupported or invalid Mach-O architecture")
    cursor = 32
    versions = []
    for _ in range(count):
        if cursor + 8 > 32 + size:
            raise ValueError("Invalid Mach-O load commands")
        cmd, length = struct.unpack_from(order + 'II', data, cursor)
        if length < 8 or cursor + length > 32 + size:
            raise ValueError("Invalid Mach-O load command")
        if cmd == 0x32 and length >= 24:
            platform, minimum = struct.unpack_from(order + 'II', data, cursor + 8)
            if platform == 1:
                versions.append(minimum)
        elif cmd == 0x24 and length >= 16:
            versions.append(struct.unpack_from(order + 'I', data, cursor + 8)[0])
        cursor += length
    if cursor != 32 + size or not versions:
        raise ValueError("Missing macOS deployment target")
    return max((v >> 16, (v >> 8) & 255, v & 255) for v in versions)


def inspect_framework_macho(data):
    # Sparkle distributions contain a universal x86_64/arm64 framework.
    # Inspect the arm64 slice that this release actually ships.
    if data[:4] not in (b'\xca\xfe\xba\xbe', b'\xca\xfe\xba\xbf'):
        return inspect_macho(data)
    is_64 = data[:4] == b'\xca\xfe\xba\xbf'
    entry_size = 32 if is_64 else 20
    if len(data) < 8:
        raise ValueError("Invalid universal framework")
    count = struct.unpack_from('>I', data, 4)[0]
    if count == 0 or count > 64 or 8 + count * entry_size > len(data):
        raise ValueError("Invalid universal framework architecture table")
    slices = []
    for index in range(count):
        pos = 8 + index * entry_size
        cpu = struct.unpack_from('>I', data, pos)[0]
        offset, size = struct.unpack_from('>QQ' if is_64 else '>II', data, pos + 8)
        if offset < 8 + count * entry_size or size < 32 or offset + size > len(data):
            raise ValueError("Invalid universal framework slice")
        if cpu == 0x0100000c:
            slices.append(data[offset:offset + size])
    if len(slices) != 1:
        raise ValueError("Framework must contain exactly one arm64 slice")
    return inspect_macho(slices[0])


def inspect_archive(path, version, build, expected_public_key=None):
    with zipfile.ZipFile(path) as archive:
        def read(name, limit):
            entries = [item for item in archive.infolist() if item.filename == name]
            if len(entries) != 1 or entries[0].is_dir() or stat.S_ISLNK(entries[0].external_attr >> 16) or entries[0].file_size > limit:
                raise ValueError("Missing, ambiguous or invalid bundle entry")
            return archive.read(entries[0])
        prefix = 'SportSauce.app/Contents/'
        info = plistlib.loads(read(prefix + 'Info.plist', 131072))
        if (info.get('CFBundleIdentifier') != 'com.markettwits.sportsouce.desktop'
                or info.get('CFBundleShortVersionString') != version
                or str(info.get('CFBundleVersion')) != build
                or info.get('CFBundleExecutable') != 'SportSauce'):
            raise ValueError("Bundle identity does not match release")
        if expected_public_key is not None:
            if info.get('SUPublicEDKey') != expected_public_key:
                raise ValueError("Bundle public key does not match the feed signing key")
            if (info.get('SUFeedURL') != FEED_URL
                    or info.get('SURequireSignedFeed') is not True
                    or info.get('SUVerifyUpdateBeforeExtraction') is not True):
                raise ValueError("Bundle Sparkle feed security configuration is invalid")
        minimum = info.get('LSMinimumSystemVersion', '')
        if not re.fullmatch(r'[0-9]+\.[0-9]+(?:\.[0-9]+)?', minimum):
            raise ValueError("Invalid bundle minimum system version")
        parts = tuple(int(part) for part in minimum.split('.'))
        plist_min = (parts + (0,))[:3]
        executable = inspect_macho(read(prefix + 'MacOS/SportSauce', 64 * 1024 * 1024))
        runtime = inspect_macho(read(prefix + 'runtime/Contents/MacOS/libjli.dylib', 64 * 1024 * 1024))
        framework = inspect_framework_macho(read(prefix + 'Frameworks/Sparkle.framework/Versions/B/Sparkle', 64 * 1024 * 1024))
        return max(plist_min, executable, runtime, framework)


def run_tool(command, seed):
    result = subprocess.run(command, input=seed, capture_output=True, check=False)
    if result.returncode:
        # Signing tools may echo sensitive input or paths; do not print their output.
        raise RuntimeError("Sparkle signing command failed")
    return result.stdout.decode("ascii").strip()


def generate(*, signer: Path, archive: Path, output: Path, version: str, build: str,
             public_key: str, seed: bytes, minimum_os: str, openssl: str = "openssl"):
    if not re.fullmatch(r"(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)", version):
        raise ValueError("Invalid version name")
    if not re.fullmatch(r"[1-9][0-9]*", build) or int(build) > 2_147_483_647:
        raise ValueError("Invalid build number")
    expected_name = f"Sportsauce-{version}-{build}-macos.zip"
    if archive.name != expected_name or not archive.is_file() or archive.stat().st_size <= 0:
        raise ValueError("Invalid macOS update archive")
    if not signer.is_file() or output.resolve() in {archive.resolve(), signer.resolve()}:
        raise ValueError("Invalid signing tool or output path")
    if not re.fullmatch(r'(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)', minimum_os):
        raise ValueError("Invalid minimum macOS version")
    try:
        raw_seed = base64.b64decode(seed.strip(), validate=True)
        public_bytes = base64.b64decode(public_key, validate=True)
    except ValueError as failure:
        raise ValueError("Invalid signing key encoding") from failure
    if len(raw_seed) != 32 or len(public_bytes) != 32:
        raise ValueError("Invalid Ed25519 key length")
    # RFC 8410 PKCS#8 wrapper around the 32-byte seed. The seed never enters argv.
    private_der = bytes.fromhex("302e020100300506032b657004220420") + raw_seed
    result = subprocess.run([openssl, "pkey", "-inform", "DER", "-pubout", "-outform", "DER"],
                            input=private_der, capture_output=True, check=False)
    if result.returncode or result.stdout != bytes.fromhex("302a300506032b6570032100") + public_bytes:
        raise ValueError("Private signing key does not match the embedded public key")
    def digest():
        sha = hashlib.sha256()
        with archive.open("rb") as source:
            for chunk in iter(lambda: source.read(1024 * 1024), b""):
                sha.update(chunk)
        return sha.digest()
    archive_digest = digest()
    archive_size = archive.stat().st_size
    if tuple(map(int, minimum_os.split('.'))) < inspect_archive(archive, version, build, public_key):
        raise ValueError("Minimum macOS version is below the packaged binaries requirement")
    if archive.stat().st_size != archive_size or digest() != archive_digest:
        raise ValueError("Update archive changed while being inspected")
    signature = run_tool([str(signer), "--ed-key-file", "-", "-p", str(archive)], seed)
    if archive.stat().st_size != archive_size or digest() != archive_digest:
        raise ValueError("Update archive changed while being signed")
    try:
        if len(base64.b64decode(signature, validate=True)) != 64:
            raise ValueError("Invalid signature size")
    except ValueError as failure:
        raise ValueError("Invalid archive signature") from failure
    root = ET.Element("rss", {"version": "2.0"})
    channel = ET.SubElement(root, "channel")
    ET.SubElement(channel, "title").text = "Sportsauce updates"
    item = ET.SubElement(channel, "item")
    ET.SubElement(item, "title").text = f"Sportsauce {version}"
    ET.SubElement(item, f"{{{SPARKLE}}}version").text = build
    ET.SubElement(item, f"{{{SPARKLE}}}shortVersionString").text = version
    ET.SubElement(item, f"{{{SPARKLE}}}minimumSystemVersion").text = minimum_os
    ET.SubElement(item, f"{{{SPARKLE}}}hardwareRequirements").text = "arm64"
    ET.SubElement(item, "enclosure", {
        "url": f"https://github.com/MarketTwits/Sportsouce/releases/download/{version}.{build}/{expected_name}",
        "length": str(archive_size), "type": "application/octet-stream",
        f"{{{SPARKLE}}}edSignature": signature,
    })
    output.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(prefix="sparkle-feed-", dir=output.parent) as directory:
        staged = Path(directory) / "appcast.xml"
        ET.ElementTree(root).write(staged, encoding="utf-8", xml_declaration=True)
        run_tool([str(signer), "--ed-key-file", "-", str(staged)], seed)
        if archive.stat().st_size != archive_size or digest() != archive_digest:
            raise ValueError("Update archive changed while generating feed")
        staged.replace(output)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--signer", required=True, type=Path)
    parser.add_argument("--archive", required=True, type=Path)
    parser.add_argument("--output", required=True, type=Path)
    parser.add_argument("--version", required=True)
    parser.add_argument("--build", required=True)
    parser.add_argument("--public-key", required=True)
    parser.add_argument("--minimum-os", required=True)
    args = parser.parse_args()
    seed = sys.stdin.buffer.read(1025)
    if len(seed) > 1024:
        raise ValueError("Signing key input is too large")
    generate(signer=args.signer, archive=args.archive, output=args.output,
             version=args.version, build=args.build, public_key=args.public_key, seed=seed, minimum_os=args.minimum_os)


if __name__ == "__main__":
    main()
