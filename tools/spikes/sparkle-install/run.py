#!/usr/bin/env python3
"""Exercise Sparkle installation against disposable local, ad-hoc signed apps.

Usage: python3 run.py /path/to/extracted/Sparkle-distribution [valid|tampered]
Requires macOS, Xcode command line tools, openssl and Sparkle's sign_update.
The HTTP feed, private key, app identity and app bundles exist only for this run.
"""
import base64
import functools
import http.server
import os
import pathlib
import plistlib
import signal
import subprocess
import sys
import tempfile
import threading
import time
import uuid

if sys.platform != 'darwin' or len(sys.argv) not in (2, 3):
    raise SystemExit(__doc__)
distribution = pathlib.Path(sys.argv[1]).resolve()
scenario = sys.argv[2] if len(sys.argv) == 3 else 'valid'
if scenario not in ('valid', 'tampered'):
    raise SystemExit('scenario must be valid or tampered')
framework, signer = distribution / 'Sparkle.framework', distribution / 'bin/sign_update'
if not framework.is_dir() or not signer.is_file():
    raise SystemExit('Expected Sparkle.framework and bin/sign_update in distribution')

def run(*args):
    return subprocess.run(args, check=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

def stop_fixture(executable):
    # Match the exact disposable executable, never another installed application.
    processes = run('ps', '-axo', 'pid=,command=').stdout.decode().splitlines()
    for line in processes:
        fields = line.strip().split(None, 1)
        if len(fields) == 2 and fields[1] == str(executable):
            try:
                os.kill(int(fields[0]), signal.SIGTERM)
            except ProcessLookupError:
                pass

with tempfile.TemporaryDirectory(prefix='sportsauce-sparkle-install-') as directory:
    work = pathlib.Path(directory)
    executable = work / 'Fixture'
    run('clang', '-fobjc-arc', '-fblocks', '-framework', 'AppKit', '-F', str(distribution),
        '-framework', 'Sparkle', '-Wl,-rpath,@executable_path/../Frameworks',
        str(pathlib.Path(__file__).with_name('Fixture.m')), '-o', str(executable))
    key = work / 'key.der'
    run('openssl', 'genpkey', '-algorithm', 'Ed25519', '-outform', 'DER', '-out', str(key))
    seed = work / 'seed'
    seed.write_bytes(base64.b64encode(key.read_bytes()[-32:]))
    os.chmod(seed, 0o600)
    public_key = base64.b64encode(run('openssl', 'pkey', '-inform', 'DER', '-in', str(key),
                                      '-pubout', '-outform', 'DER').stdout[-32:]).decode()
    handler = functools.partial(http.server.SimpleHTTPRequestHandler, directory=str(work))
    server = http.server.HTTPServer(('127.0.0.1', 0), handler)
    threading.Thread(target=server.serve_forever, daemon=True).start()
    url = f'http://127.0.0.1:{server.server_port}'
    bundle_id = 'com.markettwits.sparklefixture.' + uuid.uuid4().hex
    old_app = work / 'v1/SparkleFixture.app'
    log = work / 'events.log'
    try:
        for version in (1, 2):
            app = work / f'v{version}/SparkleFixture.app'
            mac = app / 'Contents/MacOS'
            mac.mkdir(parents=True)
            run('ditto', str(executable), str(mac / 'Fixture'))
            (app / 'Contents/Frameworks').mkdir()
            run('ditto', str(framework), str(app / 'Contents/Frameworks/Sparkle.framework'))
            plist = {'CFBundleIdentifier': bundle_id, 'CFBundleName': 'SparkleFixture',
                     'CFBundleExecutable': 'Fixture', 'CFBundlePackageType': 'APPL',
                     'CFBundleVersion': str(version), 'CFBundleShortVersionString': f'{version}.0.0',
                     'LSMinimumSystemVersion': '12.0', 'SUFeedURL': url + '/appcast.xml',
                     'SUPublicEDKey': public_key, 'SURequireSignedFeed': True,
                     'SUVerifyUpdateBeforeExtraction': True, 'SUEnableAutomaticChecks': False,
                     'NSAppTransportSecurity': {'NSAllowsLocalNetworking': True}, 'FixtureLog': str(log)}
            (app / 'Contents/Info.plist').write_bytes(plistlib.dumps(plist))
            run('codesign', '--force', '--deep', '--sign', '-', str(app))
        archive = work / 'update.zip'
        run('ditto', '-c', '-k', '--sequesterRsrc', '--keepParent', str(work / 'v2/SparkleFixture.app'), str(archive))
        signature = run(str(signer), '--ed-key-file', str(seed), '-p', str(archive)).stdout.decode().strip()
        feed = work / 'appcast.xml'
        feed.write_text(f'''<?xml version="1.0" encoding="utf-8"?><rss version="2.0" xmlns:sparkle="http://www.andymatuschak.org/xml-namespaces/sparkle"><channel><title>Fixture</title><item><title>2</title><sparkle:version>2</sparkle:version><sparkle:shortVersionString>2.0.0</sparkle:shortVersionString><enclosure url="{url}/update.zip" length="{archive.stat().st_size}" type="application/octet-stream" sparkle:edSignature="{signature}" /></item></channel></rss>''')
        run(str(signer), '--ed-key-file', str(seed), str(feed))
        if scenario == 'tampered':
            # Corrupt signed bytes without changing length or the signed feed.
            with archive.open('r+b') as stream:
                stream.seek(100)
                original = stream.read(1)
                stream.seek(100)
                stream.write(bytes([original[0] ^ 1]))
        run('open', '-n', '-a', str(old_app))
        deadline = time.monotonic() + 45
        events = ''
        while time.monotonic() < deadline:
            time.sleep(.25)
            events = log.read_text() if log.exists() else ''
            if 'started: 2' in events or 'error:' in events or 'notfound:' in events:
                break
        version = plistlib.loads((old_app / 'Contents/Info.plist').read_bytes())['CFBundleVersion']
        print(events, end='')
        print(f'Installed version: {version}')
        if scenario == 'valid':
            assert 'started: 2' in events and version == '2', 'Install and relaunch failed'
        else:
            assert ('started: 1' in events and 'download' in events and 'Code=4005' in events
                    and 'signature' in events.lower() and 'started: 2' not in events and version == '1'), \
                'Tampered update was not rejected for a signature failure'
        print(f'PASS: {scenario}')
    finally:
        stop_fixture(old_app / 'Contents/MacOS/Fixture')
        server.shutdown()
        server.server_close()
