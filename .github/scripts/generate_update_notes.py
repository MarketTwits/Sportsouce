#!/usr/bin/env python3
"""Create immutable, localized release notes from the checked-out source revision."""
import argparse
from pathlib import Path
import re


def generate(source: Path, output: Path, version: str) -> None:
    if not re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+", version):
        raise ValueError("Invalid release version")
    rendered = {}
    for locale in ("ru-RU", "en-US"):
        notes = (source / f"whatsnew-{locale}").read_text(encoding="utf-8").replace("$APP_VERSION", version)
        if not notes.strip() or len(notes) > 32_768 or len(notes.encode("utf-8")) > 131_072:
            raise ValueError(f"Invalid release notes for {locale}")
        rendered[locale] = notes
    output.mkdir(parents=True, exist_ok=True)
    for locale, notes in rendered.items():
        (output / f"whatsnew-{locale}.txt").write_text(notes, encoding="utf-8")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--version", required=True)
    parser.add_argument("--source", type=Path, default=Path(".github/whatsnew"))
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    generate(args.source, args.output, args.version)


if __name__ == "__main__":
    main()
