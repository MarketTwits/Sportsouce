import tempfile
import unittest
from pathlib import Path

from verify_android_manifest import verify


class VerifyManifestTest(unittest.TestCase):
    def test_channel_and_permission_are_checked_independently(self):
        with tempfile.TemporaryDirectory() as directory:
            manifest = Path(directory) / "AndroidManifest.xml"

            def write(channel, permission, provider):
                manifest.write_text(
                    '<manifest xmlns:android="http://schemas.android.com/apk/res/android">'
                    + ('<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>' if permission else '')
                    + f'<application><meta-data android:name="com.markettwits.selfupdater.CHANNEL" android:value="{channel}"/>'
                    + ('<provider android:name="com.markettwits.selfupdater.thirdparty.api.SelfUpdaterFileProvider"/>' if provider else '')
                    + '</application></manifest>',
                    encoding="utf-8",
                )

            for variant, channel, permission in [("githubRelease", "github", True), ("googlePlayRelease", "googlePlay", False)]:
                write(channel, permission, permission)
                verify(manifest, variant)
                write(channel, not permission, permission)
                with self.assertRaises(ValueError):
                    verify(manifest, variant)
                write("wrong", permission, permission)
                with self.assertRaises(ValueError):
                    verify(manifest, variant)
                write(channel, permission, not permission)
                with self.assertRaises(ValueError):
                    verify(manifest, variant)


if __name__ == "__main__":
    unittest.main()
