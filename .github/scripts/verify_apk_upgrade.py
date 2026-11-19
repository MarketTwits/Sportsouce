#!/usr/bin/env python3
"""Verify package identity, increasing version and signing continuity for GitHub APKs."""
import argparse
import hashlib
from pathlib import Path
import re
import subprocess


def command(*args):
    return subprocess.run(args, capture_output=True, text=True, check=True).stdout


def identity(aapt, apk):
    output = command(aapt, 'dump', 'badging', str(apk))
    lines = [line for line in output.splitlines() if line.startswith('package: ')]
    if len(lines) != 1:
        raise ValueError('Expected one APK package declaration')
    attributes = re.findall(r"([A-Za-z][A-Za-z0-9]*)='([^']*)'", lines[0])
    values = dict(attributes)
    if len(values) != len(attributes) or not re.fullmatch(r'[1-9][0-9]*', values.get('versionCode', '')):
        raise ValueError('Invalid APK identity')
    return values.get('name'), int(values['versionCode'])


def signers(apksigner, apk):
    output = command(apksigner, 'verify', '--print-certs', str(apk))
    pattern = re.compile(
        r'(?:(Signer) #[0-9]+|(V[1-4]) Signer(?: #[0-9]+)?):? certificate SHA-256 digest: ([0-9a-fA-F]{64})'
    )
    by_scheme = {}
    for line in output.splitlines():
        if 'certificate SHA-256 digest:' not in line:
            continue
        match = pattern.fullmatch(line)
        if match is None:
            raise ValueError('Invalid APK signing certificates')
        legacy, scheme, value = match.groups()
        signers_for_scheme = by_scheme.setdefault(scheme or legacy, set())
        value = value.lower()
        if value in signers_for_scheme:
            raise ValueError('Invalid APK signing certificates')
        signers_for_scheme.add(value)
    if not by_scheme or any(values != next(iter(by_scheme.values())) for values in by_scheme.values()):
        raise ValueError('Invalid APK signing certificates')
    return next(iter(by_scheme.values()))


def digest(apk):
    result = hashlib.sha256()
    with apk.open('rb') as source:
        for chunk in iter(lambda: source.read(1024 * 1024), b''):
            result.update(chunk)
    return result.digest()


def verify(aapt, apksigner, previous, current):
    previous, current = Path(previous), Path(current)
    old_package, old_version = identity(aapt, previous)
    new_package, new_version = identity(aapt, current)
    if old_package != 'com.markettwits.sportsouce' or new_package != old_package:
        raise ValueError('APK package identity changed')
    if new_version < old_version or (new_version == old_version and digest(previous) != digest(current)):
        raise ValueError('New APK must have a higher versionCode; an identical published APK may be retried')
    if signers(apksigner, previous) != signers(apksigner, current):
        raise ValueError('APK signing certificate changed; verify signing rotation before release')


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--aapt', required=True)
    parser.add_argument('--apksigner', required=True)
    parser.add_argument('--previous', type=Path, required=True)
    parser.add_argument('--current', type=Path, required=True)
    args = parser.parse_args()
    verify(args.aapt, args.apksigner, args.previous, args.current)


if __name__ == '__main__':
    main()
