#!/usr/bin/env python3
"""Prevent an older workflow run or app build from overwriting the published web identity."""
import json
from pathlib import Path
import sys


def read_identity(path: Path):
    value = json.loads(path.read_text(encoding="utf-8"))
    if (value.get("schemaVersion") != 1 or type(value.get("sequence")) is not int
            or type(value.get("buildNumber")) is not int
            or value["sequence"] <= 0 or value["buildNumber"] <= 0):
        raise ValueError(f"Invalid web deployment identity: {path}")
    return value["sequence"], value["buildNumber"]


def validate(candidate: Path, published: Path | None):
    raw = candidate.read_text(encoding="utf-8")
    if len(raw.encode("utf-16-le")) // 2 > 131_072:
        raise ValueError("Web deployment metadata exceeds the browser limit")
    payload = json.loads(raw)
    if (not isinstance(payload, dict) or not isinstance(payload.get("versionName"), str)
            or not payload["versionName"].strip() or not isinstance(payload.get("changes", {}), dict)
            or any(not isinstance(key, str) or not isinstance(value, str)
                   for key, value in payload.get("changes", {}).items())):
        raise ValueError("Invalid web deployment metadata")
    sequence, build = read_identity(candidate)
    if published is not None:
        old_sequence, old_build = read_identity(published)
        if sequence <= old_sequence or build < old_build:
            raise ValueError("The published web deployment is newer than this candidate")


if __name__ == "__main__":
    if len(sys.argv) not in (2, 3):
        raise SystemExit("Usage: check_web_deployment_order.py candidate.json [published.json]")
    validate(Path(sys.argv[1]), Path(sys.argv[2]) if len(sys.argv) == 3 else None)
