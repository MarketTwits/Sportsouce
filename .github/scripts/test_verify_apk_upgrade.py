import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

import verify_apk_upgrade as upgrade


class VerifyApkUpgradeTest(unittest.TestCase):
    def test_signer_formats_from_old_and_new_build_tools(self):
        digest = 'a' * 64
        examples = [
            f'Signer #1 certificate SHA-256 digest: {digest}\n',
            f'V2 Signer: certificate SHA-256 digest: {digest}\n',
            f'V2 Signer: certificate SHA-256 digest: {digest}\nV3 Signer: certificate SHA-256 digest: {digest}\n',
        ]
        for output in examples:
            with self.subTest(output=output), patch.object(upgrade, 'command', return_value=output):
                self.assertEqual({digest}, upgrade.signers('apksigner', Path('app.apk')))
        with patch.object(upgrade, 'command', return_value=f'V2 Signer: certificate SHA-256 digest: {digest}\nV3 Signer: certificate SHA-256 digest: {"b" * 64}\n'):
            with self.assertRaises(ValueError):
                upgrade.signers('apksigner', Path('app.apk'))
        with patch.object(upgrade, 'command', return_value=f'V2 Signer: certificate SHA-256 digest: {digest}\nV3 Signer: certificate SHA-256 digest: invalid\n'):
            with self.assertRaises(ValueError):
                upgrade.signers('apksigner', Path('app.apk'))

    def test_versions_package_and_signer(self):
        with tempfile.TemporaryDirectory() as directory:
            old, new = Path(directory) / 'old.apk', Path(directory) / 'new.apk'
            old.write_bytes(b'old')
            new.write_bytes(b'new')
            versions = {old: ('com.markettwits.sportsouce', 10421), new: ('com.markettwits.sportsouce', 10422)}
            signatures = {old: {'a' * 64}, new: {'a' * 64}}
            with patch.object(upgrade, 'identity', side_effect=lambda _, p: versions[p]), \
                 patch.object(upgrade, 'signers', side_effect=lambda _, p: signatures[p]):
                upgrade.verify('aapt', 'apksigner', old, new)
                versions[new] = ('com.markettwits.sportsouce', 10421)
                with self.assertRaisesRegex(ValueError, 'higher versionCode'):
                    upgrade.verify('aapt', 'apksigner', old, new)
                new.write_bytes(b'old')
                upgrade.verify('aapt', 'apksigner', old, new)
                versions[new] = ('com.markettwits.sportsouce', 10420)
                with self.assertRaisesRegex(ValueError, 'higher versionCode'):
                    upgrade.verify('aapt', 'apksigner', old, new)
                versions[new] = ('other.package', 10422)
                with self.assertRaisesRegex(ValueError, 'package'):
                    upgrade.verify('aapt', 'apksigner', old, new)
                versions[new] = ('com.markettwits.sportsouce', 10422)
                signatures[new] = {'b' * 64}
                with self.assertRaisesRegex(ValueError, 'certificate'):
                    upgrade.verify('aapt', 'apksigner', old, new)

    def test_unparseable_or_ambiguous_tools_output_is_rejected(self):
        with patch.object(upgrade, 'command', return_value="package: name='com.markettwits.sportsouce' versionCode='1' versionCode='2'"):
            with self.assertRaises(ValueError):
                upgrade.identity('aapt', Path('app.apk'))
        with patch.object(upgrade, 'command', return_value='no certificate'):
            with self.assertRaises(ValueError):
                upgrade.signers('apksigner', Path('app.apk'))


if __name__ == '__main__':
    unittest.main()
