from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

import package_sparkle_update as package


class PackageSparkleTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.bundle = self.root / 'SportSauce.app'
        self.bundle.mkdir()
        self.output = self.root / 'Sportsauce-1.6.1-10422-macos.zip'
        self.details = ('Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\n'
                        'Signature size=9000\nCDHash=' + 'a' * 40 + '\n'
                        'Authority=Developer ID Application: Example (ABCDE12345)\n')
        self.calls = []

    def runner(self, command):
        self.calls.append(command)
        if '--display' in command:
            return self.details
        if command[0] == '/usr/bin/ditto' and command[1] == '-c':
            Path(command[-1]).write_bytes(b'zip')
        if command[0] == '/usr/bin/ditto' and command[1] == '-x':
            (Path(command[-1]) / 'SportSauce.app').mkdir()
        return ''

    def test_package_verifies_original_and_extracted_bundle_before_publish(self):
        with patch.object(package, 'inspect_archive', return_value=(11, 0, 0)):
            package.package(self.bundle, self.output, '1.6.1', '10422', 'ABCDE12345', '11.0.0', self.runner)
        self.assertEqual(b'zip', self.output.read_bytes())
        self.assertEqual(3, sum('--verify' in command for command in self.calls))
        self.assertEqual(3, sum(command[0] == '/usr/sbin/spctl' for command in self.calls))

    def test_ad_hoc_or_other_team_never_creates_archive(self):
        for invalid in (self.details.replace('TeamIdentifier=ABCDE12345', 'TeamIdentifier=OTHER12345'),
                        self.details.replace('Signature size=9000', 'Signature=adhoc'),
                        self.details.replace('Developer ID Application:', 'Apple Development:')):
            self.details = invalid
            with self.assertRaises(ValueError):
                package.package(self.bundle, self.output, '1.6.1', '10422', 'ABCDE12345', '11.0.0', self.runner)
            self.assertFalse(self.output.exists())
            self.setUpDetails()

    def setUpDetails(self):
        self.details = ('Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\n'
                        'Signature size=9000\nCDHash=' + 'a' * 40 + '\n'
                        'Authority=Developer ID Application: Example (ABCDE12345)\n')

    def test_changed_bundle_or_incompatible_minimum_preserves_existing_archive(self):
        self.output.write_bytes(b'existing')
        with patch.object(package, 'inspect_archive', return_value=(12, 0, 0)):
            with self.assertRaisesRegex(ValueError, 'Minimum'):
                package.package(self.bundle, self.output, '1.6.1', '10422', 'ABCDE12345', '11.0.0', self.runner)
        self.assertEqual(b'existing', self.output.read_bytes())
        with patch.object(package, 'inspect_archive', return_value=(11, 0, 0)), \
             patch.object(package, 'verify_bundle', side_effect=['a' * 40, 'b' * 40]):
            with self.assertRaisesRegex(ValueError, 'changed'):
                package.package(self.bundle, self.output, '1.6.1', '10422', 'ABCDE12345', '11.0.0', self.runner)
        self.assertEqual(b'existing', self.output.read_bytes())


if __name__ == '__main__':
    unittest.main()
