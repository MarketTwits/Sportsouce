#!/usr/bin/env python3
"""Verify the actual APK identity consumed by the GitHub updater."""
import argparse
from pathlib import Path
import re
import subprocess


def verify(aapt: str, apk: Path, version: str, build: str) -> None:
    if not re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+", version) or not re.fullmatch(r"[1-9][0-9]*", build):
        raise ValueError("Invalid expected version")
    if apk.name != f"Sportsauce-{version}-{build}.apk" or not apk.is_file():
        raise ValueError("Unexpected or missing GitHub APK")
    result = subprocess.run([aapt, "dump", "badging", str(apk)], capture_output=True, text=True, check=True)
    package_lines = [line for line in result.stdout.splitlines() if line.startswith("package: ")]
    if len(package_lines) != 1:
        raise ValueError("Expected exactly one APK package declaration")
    attributes = re.findall(r"([A-Za-z][A-Za-z0-9]*)='([^']*)'", package_lines[0])
    names = [name for name, _ in attributes]
    if len(names) != len(set(names)):
        raise ValueError("Duplicate APK package attribute")
    values = dict(attributes)
    if (values.get("name") != "com.markettwits.sportsouce" or values.get("versionCode") != build
            or values.get("versionName") != version):
        raise ValueError("APK package identity does not match release version")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--aapt", required=True)
    parser.add_argument("--apk", required=True, type=Path)
    parser.add_argument("--version", required=True)
    parser.add_argument("--build", required=True)
    args = parser.parse_args()
    verify(args.aapt, args.apk, args.version, args.build)


if __name__ == "__main__":
    main()
