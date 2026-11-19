#!/bin/sh
set -eu
if [ "$(uname -s)" != Darwin ] || [ "$#" -lt 2 ] || [ "$#" -gt 3 ]; then
    echo "Usage on macOS: $0 /absolute/path/to/SportSauce.app /absolute/path/to/Sparkle.framework [error|none|available|incompatible|signed|tampered|signed-none|signed-incompatible]" >&2
    exit 2
fi
app=$1
framework=$2
probe_case=${3:-error}
native_driver=${SPORTSAUCE_TEST_NATIVE_DRIVER:-0}
interactive=${SPORTSAUCE_TEST_INTERACTIVE:-0}
if [ "$interactive" != 0 ] && { [ "$interactive" != 1 ] || [ "$native_driver" != 1 ] || [ "$probe_case" != signed ]; }; then
    echo 'Interactive probe requires SPORTSAUCE_TEST_NATIVE_DRIVER=1 and signed feed' >&2; exit 2
fi
case "$probe_case" in error|none|available|incompatible|signed|tampered|signed-none|signed-incompatible) ;; *) echo 'Unknown probe case' >&2; exit 2;; esac
if [ "$native_driver" != 0 ] && [ "$probe_case" != signed ] && [ "$probe_case" != tampered ] && [ "$probe_case" != signed-none ] && [ "$probe_case" != signed-incompatible ]; then
    echo 'Native driver test requires signed or tampered feed' >&2
    exit 2
fi
case "$app:$framework" in /*:/*) ;; *) echo 'Paths must be absolute' >&2; exit 2;; esac
[ -f "$app/Contents/app/SportSauce.cfg" ] || { echo 'Packaged SportSauce.cfg not found' >&2; exit 2; }
[ -f "$framework/Versions/B/Sparkle" ] || { echo 'Sparkle framework binary not found' >&2; exit 2; }
source_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
tmp_dir=$(mktemp -d "${TMPDIR:-/tmp}/sportsauce-sparkle-package.XXXXXX")
server_pid=''
cleanup() {
    if [ -n "$server_pid" ]; then kill "$server_pid" 2>/dev/null || true; wait "$server_pid" 2>/dev/null || true; fi
    rm -rf "$tmp_dir"
}
trap cleanup EXIT HUP INT TERM
app_copy="$tmp_dir/SportSauce.app"
ditto "$app" "$app_copy"
mkdir -p "$app_copy/Contents/Frameworks"
ditto "$framework" "$app_copy/Contents/Frameworks/Sparkle.framework"
plist="$app_copy/Contents/Info.plist"
public_key='AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA='
# Disposable test identity. Local feeds only exercise update discovery, not artifact trust.
case "$probe_case" in
    error) feed_url='https://127.0.0.1:1/appcast.xml'; expected='Sparkle informational check reported an error to JVM' ;;
    none|available|incompatible|signed|tampered|signed-none|signed-incompatible)
        feed_file="$tmp_dir/appcast.xml"
        if [ "$probe_case" = available ] || [ "$probe_case" = incompatible ] || [ "$probe_case" = signed ] || [ "$probe_case" = tampered ] || [ "$probe_case" = signed-incompatible ]; then
            item='<item><title>Test update</title><sparkle:version>999999</sparkle:version><sparkle:shortVersionString>99.0.0</sparkle:shortVersionString><enclosure url="https://example.invalid/update.zip" length="1" type="application/octet-stream" sparkle:edSignature="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==" /></item>'
            expected='Sparkle informational check found an update'
            if [ "$probe_case" = incompatible ] || [ "$probe_case" = signed-incompatible ]; then
                item=$(printf '%s' "$item" | sed 's|</item>|<sparkle:minimumSystemVersion>99.0</sparkle:minimumSystemVersion></item>|')
                expected='Sparkle informational check found an incompatible update'
            fi
        else
            item='<item><title>Old update</title><sparkle:version>1</sparkle:version><sparkle:shortVersionString>1.0.0</sparkle:shortVersionString><enclosure url="https://example.invalid/update.zip" length="1" type="application/octet-stream" sparkle:edSignature="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==" /></item>'
            expected='Sparkle informational check found no update'
        fi
        printf '%s\n' '<?xml version="1.0" encoding="UTF-8"?><rss version="2.0" xmlns:sparkle="http://www.andymatuschak.org/xml-namespaces/sparkle"><channel><title>Test</title>' "$item" '</channel></rss>' > "$feed_file"
        if [ "$probe_case" = signed ] || [ "$probe_case" = tampered ] || [ "$probe_case" = signed-none ] || [ "$probe_case" = signed-incompatible ]; then
            signer="$(dirname "$framework")/bin/sign_update"
            [ -x "$signer" ] || { echo 'Sparkle sign_update tool not found beside framework' >&2; exit 2; }
            umask 077
            openssl genpkey -algorithm Ed25519 -outform DER -out "$tmp_dir/key.der"
            tail -c 32 "$tmp_dir/key.der" | base64 | tr -d '\n' > "$tmp_dir/key.seed"
            public_key=$(openssl pkey -inform DER -in "$tmp_dir/key.der" -pubout -outform DER | tail -c 32 | base64 | tr -d '\n')
            "$signer" --ed-key-file "$tmp_dir/key.seed" "$feed_file" >/dev/null
            /usr/libexec/PlistBuddy -c 'Add :SURequireSignedFeed bool true' "$plist"
            /usr/libexec/PlistBuddy -c 'Add :SUVerifyUpdateBeforeExtraction bool true' "$plist"
            if [ "$probe_case" = tampered ]; then
                sed 's/<title>Test update<\/title>/<title>Modified update<\/title>/' "$feed_file" > "$tmp_dir/modified.xml"
                mv "$tmp_dir/modified.xml" "$feed_file"
                expected='Sparkle informational check reported an error to JVM'
            fi
        fi
        python3 -u - "$tmp_dir" > "$tmp_dir/server-port" 2> "$tmp_dir/server.log" <<'PY' &
import functools, http.server, sys
handler = functools.partial(http.server.SimpleHTTPRequestHandler, directory=sys.argv[1])
server = http.server.HTTPServer(('127.0.0.1', 0), handler)
print(server.server_port, flush=True)
server.serve_forever()
PY
        server_pid=$!
        attempts=0
        while [ ! -s "$tmp_dir/server-port" ]; do
            attempts=$((attempts + 1))
            [ "$attempts" -le 50 ] && kill -0 "$server_pid" 2>/dev/null || { echo 'Local feed server did not start' >&2; exit 1; }
            sleep 0.1
        done
        port=$(cat "$tmp_dir/server-port")
        feed_url="http://127.0.0.1:$port/appcast.xml"
        /usr/libexec/PlistBuddy -c 'Add :NSAppTransportSecurity dict' "$plist"
        /usr/libexec/PlistBuddy -c 'Add :NSAppTransportSecurity:NSAllowsLocalNetworking bool true' "$plist"
        ;;
esac
/usr/libexec/PlistBuddy -c "Add :SUFeedURL string $feed_url" "$plist"
/usr/libexec/PlistBuddy -c "Add :SUPublicEDKey string $public_key" "$plist"
/usr/libexec/PlistBuddy -c 'Add :SUEnableAutomaticChecks bool false' "$plist"
java_dir=$(/usr/libexec/java_home -v 21)
if [ "$native_driver" = 1 ] || [ "$native_driver" = 2 ] || [ "$native_driver" = 3 ]; then
    root=$(CDPATH= cd -- "$source_dir/../../.." && pwd)
    policy=${SPORTSAUCE_NATIVE_TEST_POLICY:-loopback}
    case "$policy" in loopback) test_define=-DSPORTSAUCE_SPARKLE_TESTING=1 ;; production) test_define='' ;; *) echo 'Unknown native test policy' >&2; exit 2;; esac
    clang -dynamiclib -fobjc-arc -fblocks $test_define -framework AppKit -framework Foundation \
        -I"$java_dir/include" -I"$java_dir/include/darwin" \
        "$root/components/selfupdater/thirdparty/api/src/macosNative/SparkleJniDriver.m" -o "$app_copy/Contents/app/libprobe.dylib"
    if [ "$native_driver" = 1 ]; then
        "$java_dir/bin/javac" -d "$tmp_dir/classes" "$source_dir/driver/com/markettwits/selfupdater/thirdparty/api/SparkleJniDriver.java"
        main_class=com.markettwits.selfupdater.thirdparty.api.SparkleJniDriver
    elif [ "$native_driver" = 2 ]; then
        (cd "$root" && ./gradlew :components:selfupdater:thirdparty:api:compileTestKotlinJvm >/dev/null)
        mkdir -p "$tmp_dir/classes"
        ditto "$root/components/selfupdater/api/build/classes/kotlin/jvm/main" "$tmp_dir/classes"
        ditto "$root/components/selfupdater/thirdparty/api/build/classes/kotlin/jvm/main" "$tmp_dir/classes"
        ditto "$root/components/selfupdater/thirdparty/api/build/classes/kotlin/jvm/test" "$tmp_dir/classes"
        # The release runtime was shrunk before the bridge existed. Use the exact
        # unshrunk Gradle dependencies only in this isolated integration probe.
        python3 - "$root/gradle/libs.versions.toml" "$app_copy/Contents/app" <<'PY'
import os, pathlib, shutil, sys, tomllib
versions = tomllib.loads(pathlib.Path(sys.argv[1]).read_text())['versions']
cache = pathlib.Path(os.environ.get('GRADLE_USER_HOME', pathlib.Path.home() / '.gradle')) / 'caches/modules-2/files-2.1'
for group, name, version, destination in [
    ('org.jetbrains.kotlin', 'kotlin-stdlib', versions['kotlin'], 'probe-stdlib.jar'),
    ('org.jetbrains.kotlinx', 'kotlinx-coroutines-core-jvm', versions['kotlinx-coroutines'], 'probe-coroutines.jar'),
]:
    matches = list((cache / group / name / version).glob(f'*/{name}-{version}.jar'))
    if len(matches) != 1:
        raise SystemExit(f'Expected exactly one cached runtime jar for {name}:{version}')
    shutil.copyfile(matches[0], pathlib.Path(sys.argv[2]) / destination)
PY
        main_class=com.markettwits.selfupdater.thirdparty.api.SparkleJniIntegrationProbe
    else
        main_class=com.markettwits.sportsouce.app.desktop.MainKt
    fi
    if [ "$native_driver" != 3 ]; then
        "$java_dir/bin/jar" cf "$app_copy/Contents/app/probe.jar" -C "$tmp_dir/classes" .
    fi
    case "$probe_case" in
        signed) expected_arg=1; expected='JNI result: 1 version=99.0.0 build=999999' ;;
        tampered) expected_arg=3; expected='JNI result: 3 version=null build=null' ;;
        signed-none) expected_arg=0; expected='JNI result: 0 version=null build=null' ;;
        signed-incompatible) expected_arg=2; expected='JNI result: 2 version=null build=null' ;;
    esac
    if [ "$policy" = production ]; then expected_arg=3; expected='JNI result: 3 version=null build=null'; fi
    if [ "$native_driver" = 2 ]; then expected="Kotlin updater result: $expected_arg"; fi
    if [ "$native_driver" = 3 ]; then
        expected_code=$expected_arg
        expected="SELF_UPDATER_NATIVE_TEST_OK=$expected_code"
        expected_arg=--verify-packaged-updater
    fi
else
    clang -dynamiclib -fblocks -framework AppKit -framework Foundation \
        -I"$java_dir/include" -I"$java_dir/include/darwin" \
        "$source_dir/packaged.m" -o "$app_copy/Contents/app/libprobe.dylib"
    "$java_dir/bin/javac" -d "$tmp_dir" "$source_dir/PackagedProbe.java"
    "$java_dir/bin/jar" cf "$app_copy/Contents/app/probe.jar" -C "$tmp_dir" PackagedProbe.class
    main_class=PackagedProbe
    expected_arg=$expected
fi
python3 - "$app_copy/Contents/app/SportSauce.cfg" "$main_class" "$native_driver" "${expected_code:-}" <<'PY'
import pathlib, sys
p = pathlib.Path(sys.argv[1])
s = p.read_text()
main = 'app.mainclass=com.markettwits.sportsouce.app.desktop.MainKt'
assert s.count(main) == 1 and s.count('[JavaOptions]') == 1
if sys.argv[3] != '3':
    s = s.replace(main, f'app.mainclass={sys.argv[2]}\napp.classpath=$APPDIR/probe.jar')
if sys.argv[3] == '2':
    s = s.replace('[Application]', '[Application]\napp.classpath=$APPDIR/probe.jar\napp.classpath=$APPDIR/probe-stdlib.jar\napp.classpath=$APPDIR/probe-coroutines.jar')
s = s.replace('[JavaOptions]', '[JavaOptions]\njava-options=-Dsportsauce.probe.library=$APPDIR/libprobe.dylib' +
              (f'\njava-options=-Dsportsauce.probe.expected={sys.argv[4]}' if sys.argv[3] == '3' else ''))
p.write_text(s)
PY
/usr/bin/codesign --force --deep --sign - "$app_copy" >/dev/null 2>&1
if [ "$interactive" = 1 ]; then set -- "$expected_arg" interactive; else set -- "$expected_arg"; fi
if output=$("$app_copy/Contents/MacOS/SportSauce" "$@"); then
    :
else
    printf '%s\n' "$output" >&2
    exit 1
fi
printf '%s\n' "$output"
printf '%s\n' "$output" | grep -Fx "$expected" >/dev/null
if [ "$interactive" = 1 ]; then printf '%s\n' "$output" | grep -Fx 'JNI interactive session active' >/dev/null; fi
