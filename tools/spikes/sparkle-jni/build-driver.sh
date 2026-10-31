#!/bin/sh
set -eu
if [ "$(uname -s)" != Darwin ] || [ "$#" -ne 1 ]; then
    echo "Usage on macOS: $0 /absolute/path/to/output.dylib" >&2
    exit 2
fi
case "$1" in /*.dylib) ;; *) echo 'Output must be an absolute .dylib path' >&2; exit 2;; esac
[ ! -e "$1" ] || { echo 'Output already exists' >&2; exit 1; }
root=$(CDPATH= cd -- "$(dirname -- "$0")/../../.." && pwd)
java_dir=$(/usr/libexec/java_home -v 21)
xcrun --sdk macosx clang -Wall -Wextra -Werror -Wno-unused-parameter \
    -arch arm64 -mmacosx-version-min=11.0 -dynamiclib -fobjc-arc -fblocks \
    -framework AppKit -framework Foundation -I"$java_dir/include" -I"$java_dir/include/darwin" \
    "$root/components/selfupdater/thirdparty/api/src/macosNative/SparkleJniDriver.m" -o "$1"
