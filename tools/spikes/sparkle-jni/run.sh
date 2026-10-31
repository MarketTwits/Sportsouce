#!/bin/sh
set -eu
if [ "$(uname -s)" != Darwin ] || [ "$#" -ne 1 ]; then
    echo "Usage on macOS: $0 /absolute/path/to/Sparkle.framework" >&2
    exit 2
fi
framework=$1
case "$framework" in /*) ;; *) echo 'Framework path must be absolute' >&2; exit 2;; esac
[ -f "$framework/Versions/B/Sparkle" ] || { echo 'Sparkle framework binary not found' >&2; exit 2; }
source_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
tmp_dir=$(mktemp -d "${TMPDIR:-/tmp}/sportsauce-sparkle-probe.XXXXXX")
trap 'rm -rf "$tmp_dir"' EXIT HUP INT TERM
java_dir=$(/usr/libexec/java_home -v 21)
clang -dynamiclib -fblocks -framework AppKit -framework Foundation \
    -I"$java_dir/include" -I"$java_dir/include/darwin" \
    "$source_dir/probe.m" -o "$tmp_dir/libprobe.dylib"
"$java_dir/bin/javac" -d "$tmp_dir" "$source_dir/Probe.java"
output=$("$java_dir/bin/java" -Dsportsauce.probe.library="$tmp_dir/libprobe.dylib" -cp "$tmp_dir" Probe "$framework/Versions/B/Sparkle")
printf '%s\n' "$output"
[ "$output" = 'Sparkle controller initialized on AppKit main thread' ]
