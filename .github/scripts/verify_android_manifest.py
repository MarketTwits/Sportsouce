"""Check the merged Android manifest for the chosen distribution channel."""
import argparse
from pathlib import Path
import xml.etree.ElementTree as ET

ANDROID = "{http://schemas.android.com/apk/res/android}"
CHANNELS = {
    "githubRelease": ("github", True),
    "googlePlayRelease": ("googlePlay", False),
}


def verify(manifest_path: Path, variant: str) -> None:
    channel, expects_install_permission = CHANNELS[variant]
    manifest = ET.parse(manifest_path).getroot()
    permissions = {entry.get(ANDROID + "name") for entry in manifest.findall("uses-permission")}
    channels = [entry.get(ANDROID + "value") for entry in manifest.findall("./application/meta-data")
                if entry.get(ANDROID + "name") == "com.markettwits.selfupdater.CHANNEL"]
    has_install_permission = "android.permission.REQUEST_INSTALL_PACKAGES" in permissions
    providers = [entry for entry in manifest.findall("./application/provider")
                 if entry.get(ANDROID + "name") == "com.markettwits.selfupdater.thirdparty.api.SelfUpdaterFileProvider"]
    if (has_install_permission != expects_install_permission or channels != [channel]
            or len(providers) != int(expects_install_permission)):
        raise ValueError(f"Invalid distribution manifest: {variant}")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("variant", choices=CHANNELS)
    args = parser.parse_args()
    task = "process" + args.variant[0].upper() + args.variant[1:] + "MainManifest"
    path = Path("instances/app-android/build/intermediates/merged_manifest") / args.variant / task / "AndroidManifest.xml"
    verify(path, args.variant)


if __name__ == "__main__":
    main()
