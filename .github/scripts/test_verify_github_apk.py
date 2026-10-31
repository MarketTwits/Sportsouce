import subprocess
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

import verify_github_apk


class VerifyGithubApkTest(unittest.TestCase):
    def test_rejects_wrong_package_version_and_ambiguous_output(self):
        good = "package: name='com.markettwits.sportsouce' versionCode='10422' versionName='1.6.1'\n"
        with tempfile.TemporaryDirectory() as directory:
            apk = Path(directory) / 'Sportsauce-1.6.1-10422.apk'
            apk.write_bytes(b'test')
            with patch.object(verify_github_apk.subprocess, 'run') as run:
                run.return_value = subprocess.CompletedProcess([], 0, stdout=good)
                verify_github_apk.verify('aapt', apk, '1.6.1', '10422')
                for output in (good.replace('10422', '10423'), good.replace('1.6.1', '1.6.2'),
                               good.replace('com.markettwits.sportsouce', 'other.app'), good + good,
                               good.rstrip() + " versionCode='10422'\n"):
                    run.return_value = subprocess.CompletedProcess([], 0, stdout=output)
                    with self.assertRaises(ValueError):
                        verify_github_apk.verify('aapt', apk, '1.6.1', '10422')
            with self.assertRaises(ValueError):
                verify_github_apk.verify('aapt', apk, '1.6.1', '10423')


if __name__ == '__main__':
    unittest.main()
