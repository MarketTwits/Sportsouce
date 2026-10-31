"""Reject APK installer implementation retained or inlined into Play's R8 output."""
from pathlib import Path
import re
import sys


def verify(path: Path) -> None:
    mapping = path.read_text(encoding="utf-8")
    forbidden = re.compile(r"com\.markettwits\.selfupdater\.thirdparty\.api\."
                           r"(?:SelfUpdaterThirdParty(?:\$|\.|\s| ->)|AndroidApkVerifier(?:\$|\.|\s| ->)|SelfUpdaterFileProvider(?:\$|\.|\s| ->))")
    if forbidden.search(mapping):
        raise ValueError("Google Play release contains APK installer implementation")


if __name__ == "__main__":
    verify(Path(sys.argv[1]))
