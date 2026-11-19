from pathlib import Path
import tempfile
import unittest
from verify_play_updater_mapping import verify


class MappingTest(unittest.TestCase):
    def test_finds_direct_and_inlined_installer_but_allows_module(self):
        with tempfile.TemporaryDirectory() as directory:
            mapping = Path(directory) / 'mapping.txt'
            mapping.write_text('com.markettwits.selfupdater.thirdparty.api.SelfUpdaterThirdPartyModuleKt -> a:\n')
            verify(mapping)
            for entry in [
                'com.markettwits.selfupdater.thirdparty.api.SelfUpdaterThirdParty -> a:',
                '  1:2:void com.markettwits.selfupdater.thirdparty.api.SelfUpdaterThirdParty.install():1 -> a',
                'com.markettwits.selfupdater.thirdparty.api.AndroidApkVerifier$1 -> b:',
                'com.markettwits.selfupdater.thirdparty.api.SelfUpdaterFileProvider -> c:',
            ]:
                mapping.write_text(entry)
                with self.assertRaises(ValueError):
                    verify(mapping)


if __name__ == '__main__':
    unittest.main()
