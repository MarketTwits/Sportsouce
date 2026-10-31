import base64
import os
from pathlib import Path
import subprocess
import plistlib
import struct
import tempfile
import unittest
from unittest.mock import patch
import xml.etree.ElementTree as ET
import zipfile

import generate_sparkle_feed as feed


class SparkleFeedTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        directory = Path(self.temp.name)
        private = subprocess.run(['openssl', 'genpkey', '-algorithm', 'Ed25519', '-outform', 'DER'],
                                 capture_output=True, check=True).stdout
        public = subprocess.run(['openssl', 'pkey', '-inform', 'DER', '-pubout', '-outform', 'DER'],
                                input=private, capture_output=True, check=True).stdout[-32:]
        self.seed = base64.b64encode(private[-32:])
        self.public = base64.b64encode(public).decode()
        self.archive = directory / 'Sportsauce-1.6.1-10422-macos.zip'
        self.create_archive()
        self.output = directory / 'appcast.xml'
        self.signer = directory / 'sign_update'
        self.signer.write_text('#!/bin/sh\nexit 0\n')
        self.signer.chmod(0o700)
        self.args = dict(signer=self.signer, archive=self.archive, output=self.output,
                         version='1.6.1', build='10422', public_key=self.public, seed=self.seed, minimum_os='12.0.0')

    def create_archive(self, cpu=0x0100000c, overrides=None):
        binary = (b'\xcf\xfa\xed\xfe' + struct.pack('<7I', cpu, 0, 2, 1, 24, 0, 0)
                  + struct.pack('<6I', 0x32, 24, 1, 11 << 16, 0, 0))
        framework_arm = (b'\xcf\xfa\xed\xfe' + struct.pack('<7I', 0x0100000c, 0, 6, 1, 24, 0, 0)
                         + struct.pack('<6I', 0x32, 24, 1, 12 << 16, 0, 0))
        framework = b'\xca\xfe\xba\xbe' + struct.pack('>I', 1) + struct.pack('>5I', 0x0100000c, 0, 28, len(framework_arm), 0) + framework_arm
        properties = {'CFBundleIdentifier': 'com.markettwits.sportsouce.desktop',
                               'CFBundleShortVersionString': '1.6.1', 'CFBundleVersion': '10422',
                               'CFBundleExecutable': 'SportSauce', 'LSMinimumSystemVersion': '10.13',
                               'SUPublicEDKey': self.public, 'SUFeedURL': feed.FEED_URL,
                               'SURequireSignedFeed': True, 'SUVerifyUpdateBeforeExtraction': True}
        properties.update(overrides or {})
        info = plistlib.dumps(properties)
        with zipfile.ZipFile(self.archive, 'w') as archive:
            archive.writestr('SportSauce.app/Contents/Info.plist', info)
            archive.writestr('SportSauce.app/Contents/MacOS/SportSauce', binary)
            archive.writestr('SportSauce.app/Contents/runtime/Contents/MacOS/libjli.dylib', binary)
            archive.writestr('SportSauce.app/Contents/Frameworks/Sparkle.framework/Versions/B/Sparkle', framework)

    def test_generates_release_identity_and_sends_secret_only_via_stdin(self):
        calls = []
        def sign(command, seed):
            calls.append((command, seed))
            return base64.b64encode(b's' * 64).decode() if '-p' in command else ''
        with patch.object(feed, 'run_tool', side_effect=sign):
            feed.generate(**self.args)
        root = ET.parse(self.output)
        self.assertEqual('10422', root.findtext('./channel/item/{%s}version' % feed.SPARKLE))
        enclosure = root.find('./channel/item/enclosure')
        self.assertEqual(str(self.archive.stat().st_size), enclosure.get('length'))
        self.assertEqual('12.0.0', root.findtext('./channel/item/{%s}minimumSystemVersion' % feed.SPARKLE))
        self.assertEqual('arm64', root.findtext('./channel/item/{%s}hardwareRequirements' % feed.SPARKLE))
        self.assertIn('/1.6.1.10422/Sportsauce-1.6.1-10422-macos.zip', enclosure.get('url'))
        self.assertEqual(2, len(calls))
        for command, seed in calls:
            self.assertEqual(self.seed, seed)
            self.assertNotIn(self.seed.decode(), command)
            self.assertIn('-', command)

    def test_rejects_wrong_key_and_preserves_existing_output_on_failure(self):
        self.output.write_text('existing')
        with self.assertRaisesRegex(ValueError, 'does not match'):
            feed.generate(**{**self.args, 'public_key': base64.b64encode(b'x' * 32).decode()})
        self.assertEqual('existing', self.output.read_text())
        with patch.object(feed, 'run_tool', side_effect=RuntimeError('signing failed')):
            with self.assertRaises(RuntimeError):
                feed.generate(**self.args)
        self.assertEqual('existing', self.output.read_text())

    def test_rejects_archive_changed_during_signing(self):
        def sign(command, seed):
            self.archive.write_bytes(b'changed')
            return base64.b64encode(b's' * 64).decode()
        with patch.object(feed, 'run_tool', side_effect=sign):
            with self.assertRaisesRegex(ValueError, 'changed'):
                feed.generate(**self.args)
        self.assertFalse(self.output.exists())

    def test_rejects_archive_changed_during_inspection(self):
        inspect = feed.inspect_archive
        def alter_after_inspection(*args):
            minimum = inspect(*args)
            self.archive.write_bytes(b'changed')
            return minimum
        with patch.object(feed, 'inspect_archive', side_effect=alter_after_inspection):
            with self.assertRaisesRegex(ValueError, 'changed while being inspected'):
                feed.generate(**self.args)
        self.assertFalse(self.output.exists())

    def test_rejects_unsupported_cpu_and_understated_minimum(self):
        with self.assertRaisesRegex(ValueError, 'below'):
            feed.generate(**{**self.args, 'minimum_os': '11.0.0'})
        self.create_archive(cpu=0x01000007)
        with self.assertRaisesRegex(ValueError, 'architecture'):
            feed.generate(**self.args)

    def test_rejects_missing_or_invalid_framework(self):
        path = 'SportSauce.app/Contents/Frameworks/Sparkle.framework/Versions/B/Sparkle'
        with zipfile.ZipFile(self.archive) as archive:
            entries = {entry.filename: archive.read(entry) for entry in archive.infolist()}
        for replacement in (None, b'\xca\xfe\xba\xbe\x00\x00\x00\x01',
                            b'\xca\xfe\xba\xbe' + struct.pack('>I', 1) +
                            struct.pack('>5I', 0x01000007, 0, 28, 32, 0) + b'\0' * 32):
            with self.subTest(replacement=replacement):
                with zipfile.ZipFile(self.archive, 'w') as archive:
                    for name, content in entries.items():
                        if name != path:
                            archive.writestr(name, content)
                    if replacement is not None:
                        archive.writestr(path, replacement)
                with self.assertRaises(ValueError):
                    feed.generate(**self.args)
                self.assertFalse(self.output.exists())

    def test_rejects_bundle_that_would_break_or_weaken_future_updates(self):
        self.output.write_text('existing')
        for overrides in (
            {'SUPublicEDKey': base64.b64encode(b'x' * 32).decode()},
            {'SUPublicEDKey': ''},
            {'SUFeedURL': 'https://example.com/appcast.xml'},
            {'SURequireSignedFeed': False},
            {'SUVerifyUpdateBeforeExtraction': False},
            {'SURequireSignedFeed': 'true'},
        ):
            with self.subTest(overrides=overrides):
                self.create_archive(overrides=overrides)
                with self.assertRaises(ValueError):
                    feed.generate(**self.args)
                self.assertEqual('existing', self.output.read_text())

    def test_real_sparkle_signing_when_tool_is_supplied(self):
        signer = os.environ.get('SPORTSAUCE_TEST_SPARKLE_SIGNER')
        if not signer:
            self.skipTest('Sparkle signer is not configured')
        feed.generate(**{**self.args, 'signer': Path(signer)})
        result = subprocess.run([signer, '--verify', '--ed-key-file', '-', str(self.output)],
                                input=self.seed, capture_output=True)
        self.assertEqual(0, result.returncode)
        enclosure = ET.parse(self.output).find('./channel/item/enclosure')
        signature = enclosure.get('{%s}edSignature' % feed.SPARKLE)
        result = subprocess.run([signer, '--verify', '--ed-key-file', '-', str(self.archive), signature],
                                input=self.seed, capture_output=True)
        self.assertEqual(0, result.returncode)
        self.archive.write_bytes(b'modified')
        result = subprocess.run([signer, '--verify', '--ed-key-file', '-', str(self.archive), signature],
                                input=self.seed, capture_output=True)
        self.assertNotEqual(0, result.returncode)


if __name__ == '__main__':
    unittest.main()
