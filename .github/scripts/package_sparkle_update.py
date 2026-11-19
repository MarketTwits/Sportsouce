#!/usr/bin/env python3
"""Package a trusted macOS app for a future Sparkle release; never publishes it."""
import argparse
from pathlib import Path
import re
import subprocess
import tempfile

from generate_sparkle_feed import inspect_archive

BUNDLE_ID = 'com.markettwits.sportsouce.desktop'


def run(command):
    result = subprocess.run(command, capture_output=True, text=True, timeout=120, check=False)
    if result.returncode:
        raise RuntimeError(f'{Path(command[0]).name} failed (exit {result.returncode})')
    return result.stdout + result.stderr


def verify_bundle(bundle, team, runner=run):
    if bundle.is_symlink() or not bundle.is_dir() or bundle.name != 'SportSauce.app':
        raise ValueError('Expected an application bundle, not a symlink')
    runner(['/usr/bin/codesign', '--verify', '--deep', '--strict', '--verbose=2', str(bundle)])
    details = runner(['/usr/bin/codesign', '--display', '--verbose=4', str(bundle)])
    def single(key):
        return [line[len(key) + 1:] for line in details.splitlines() if line.startswith(key + '=')]
    signatures = single('Signature size')
    authorities = single('Authority')
    hashes = single('CDHash')
    if (single('Identifier') != [BUNDLE_ID] or single('TeamIdentifier') != [team]
            or len(signatures) != 1 or not signatures[0].isdigit() or int(signatures[0]) <= 0
            or single('Signature') or len(hashes) != 1 or not re.fullmatch(r'[0-9a-fA-F]{40}', hashes[0])
            or not authorities or not authorities[0].startswith('Developer ID Application: ')
            or not authorities[0].endswith(f'({team})')):
        raise ValueError('Bundle signature does not match the expected Developer ID')
    runner(['/usr/sbin/spctl', '--assess', '--type', 'execute', str(bundle)])
    return hashes[0].lower()


def package(bundle, output, version, build, team, minimum_os, runner=run):
    if not re.fullmatch(r'[A-Z0-9]{10}', team):
        raise ValueError('Invalid Developer ID team')
    if (not re.fullmatch(r'(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)', version)
            or not re.fullmatch(r'[1-9][0-9]*', build)):
        raise ValueError('Invalid release version')
    if not re.fullmatch(r'(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)\.(?:0|[1-9][0-9]*)', minimum_os):
        raise ValueError('Invalid minimum macOS version')
    if output.name != f'Sportsauce-{version}-{build}-macos.zip' or output.resolve().is_relative_to(bundle.resolve()):
        raise ValueError('Invalid archive output location or name')
    source_hash = verify_bundle(bundle, team, runner)
    output.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(prefix='sparkle-package-', dir=output.parent) as directory:
        temp = Path(directory)
        archive = temp / output.name
        unpacked = temp / 'unpacked'
        runner(['/usr/bin/ditto', '-c', '-k', '--sequesterRsrc', '--keepParent', str(bundle), str(archive)])
        if tuple(map(int, minimum_os.split('.'))) < inspect_archive(archive, version, build):
            raise ValueError('Minimum macOS version is below the packaged binaries requirement')
        unpacked.mkdir()
        runner(['/usr/bin/ditto', '-x', '-k', str(archive), str(unpacked)])
        extracted_hash = verify_bundle(unpacked / 'SportSauce.app', team, runner)
        if extracted_hash != source_hash or verify_bundle(bundle, team, runner) != source_hash:
            raise ValueError('Application changed during packaging')
        archive.replace(output)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--bundle', required=True, type=Path)
    parser.add_argument('--output', required=True, type=Path)
    parser.add_argument('--version', required=True)
    parser.add_argument('--build', required=True)
    parser.add_argument('--team', required=True)
    parser.add_argument('--minimum-os', required=True)
    args = parser.parse_args()
    package(args.bundle, args.output, args.version, args.build, args.team, args.minimum_os)


if __name__ == '__main__':
    main()
