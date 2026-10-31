#!/bin/sh
set -eu
if [ "$#" -lt 1 ] || [ "$#" -gt 2 ]; then
    echo "Usage: ANDROID_HOME=/path/to/sdk $0 emulator-serial [installer-ui|permission-ui]" >&2
    exit 2
fi
serial=$1
mode=${2:-adb}
case "$mode" in adb|installer-ui|permission-ui) ;; *) echo 'Unknown test mode' >&2; exit 2;; esac
sdk=${ANDROID_HOME:?Set ANDROID_HOME to the Android SDK directory}
adb="$sdk/platform-tools/adb"
build_tools=${SPORTSAUCE_TEST_BUILD_TOOLS:-36.1.0}
aapt="$sdk/build-tools/$build_tools/aapt"
zipalign="$sdk/build-tools/$build_tools/zipalign"
apksigner="$sdk/build-tools/$build_tools/apksigner"
android_jar="$sdk/platforms/android-36/android.jar"
root=$(CDPATH= cd -- "$(dirname -- "$0")/../../.." && pwd)
test_package=com.markettwits.selfupdater.thirdparty.api.test
fixture_package=com.markettwits.updater.fixture
for package in "$test_package" "$fixture_package"; do
    if [ -n "$("$adb" -s "$serial" shell pm path "$package" 2>/dev/null | tr -d '\r')" ]; then
        echo "Package $package already exists on $serial; use an isolated emulator" >&2
        exit 1
    fi
done
tmp=$(mktemp -d "${TMPDIR:-/tmp}/sportsauce-apk-upgrade.XXXXXX")
fixture_installed=false
test_installed=false
instrument_pid=''
cleanup() {
    if [ -n "$instrument_pid" ]; then kill "$instrument_pid" 2>/dev/null || true; wait "$instrument_pid" 2>/dev/null || true; fi
    if $fixture_installed; then "$adb" -s "$serial" uninstall "$fixture_package" >/dev/null 2>&1 || true; fi
    if $test_installed; then "$adb" -s "$serial" uninstall "$test_package" >/dev/null 2>&1 || true; fi
    "$adb" -s "$serial" shell rm -f /sdcard/sportsauce-installer.xml >/dev/null 2>&1 || true
    rm -rf "$tmp"
}
trap cleanup EXIT
trap 'exit 130' HUP INT TERM
for name in original other; do
    keytool -genkeypair -keystore "$tmp/$name.jks" -storepass android -keypass android \
        -alias test -dname 'CN=Sportsauce Test Fixture' -keyalg RSA -keysize 2048 -validity 1 >/dev/null 2>&1
done
make_apk() {
    version=$1
    key=$2
    file=$3
    cat > "$tmp/AndroidManifest.xml" <<MANIFEST
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="$fixture_package" android:versionCode="$version" android:versionName="1.0.$version">
    <uses-sdk android:minSdkVersion="26" android:targetSdkVersion="28" />
    <application android:hasCode="false" android:debuggable="true" android:label="Updater test fixture" />
</manifest>
MANIFEST
    "$aapt" package -f -M "$tmp/AndroidManifest.xml" -I "$android_jar" -F "$tmp/unsigned.apk"
    "$zipalign" -f 4 "$tmp/unsigned.apk" "$tmp/aligned.apk"
    "$apksigner" sign --ks "$tmp/$key.jks" --ks-key-alias test --ks-pass pass:android --key-pass pass:android --out "$tmp/$file.apk" "$tmp/aligned.apk"
}
make_apk 100 original installed
make_apk 101 original good
make_apk 102 other wrong
make_apk 99 original old
"$adb" -s "$serial" install "$tmp/installed.apk" >/dev/null
fixture_installed=true
(cd "$root" && ./gradlew :components:selfupdater:thirdparty:api:assembleAndroidDeviceTest >/dev/null)
"$adb" -s "$serial" install "$root/components/selfupdater/thirdparty/api/build/outputs/apk/androidTest/api-androidTest.apk" >/dev/null
test_installed=true
"$adb" -s "$serial" shell run-as "$fixture_package" mkdir -p files
"$adb" -s "$serial" shell run-as "$fixture_package" touch files/upgrade-marker
launch_installer=false
permission_flow=false
if [ "$mode" = installer-ui ]; then
    "$adb" -s "$serial" shell appops set "$test_package" REQUEST_INSTALL_PACKAGES allow
    launch_installer=true
elif [ "$mode" = permission-ui ]; then
    "$adb" -s "$serial" shell appops set "$test_package" REQUEST_INSTALL_PACKAGES deny
    launch_installer=true
    permission_flow=true
fi
if "$adb" -s "$serial" install -r "$tmp/wrong.apk" >/dev/null 2>&1; then
    echo 'Android accepted the wrong signing key' >&2
    exit 1
fi
if "$adb" -s "$serial" install -r "$tmp/old.apk" >/dev/null 2>&1; then
    echo 'Android accepted a downgrade' >&2
    exit 1
fi
encode() { base64 < "$tmp/$1.apk" | tr -d '\n\r'; }
"$adb" -s "$serial" shell am instrument -w \
    -e class com.markettwits.selfupdater.thirdparty.api.SignedUpgradeFixtureDeviceTest \
    -e launch_installer "$launch_installer" \
    -e permission_flow "$permission_flow" \
    -e good_apk "$(encode good)" -e wrong_signer_apk "$(encode wrong)" -e old_apk "$(encode old)" \
    "$test_package/androidx.test.runner.AndroidJUnitRunner" > "$tmp/instrumentation.log" 2>&1 &
instrument_pid=$!
if [ "$mode" = permission-ui ]; then
    found=false
    attempts=0
    while [ "$attempts" -lt 20 ]; do
        attempts=$((attempts + 1))
        "$adb" -s "$serial" shell uiautomator dump /sdcard/sportsauce-installer.xml >/dev/null 2>&1 || true
        "$adb" -s "$serial" exec-out cat /sdcard/sportsauce-installer.xml > "$tmp/ui.xml" 2>/dev/null || true
        coordinates=$(python3 - "$tmp/ui.xml" <<'PY'
import re, sys, xml.etree.ElementTree as ET
try:
    root = ET.parse(sys.argv[1]).getroot()
    switches = [n for n in root.iter('node') if n.get('package') == 'com.android.settings' and n.get('checkable') == 'true' and n.get('checked') == 'false' and n.get('enabled') == 'true']
    if len(switches) == 1:
        bounds = re.fullmatch(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', switches[0].get('bounds', ''))
        if bounds:
            x1, y1, x2, y2 = map(int, bounds.groups())
            if x2 > x1 and y2 > y1:
                print((x1 + x2) // 2, (y1 + y2) // 2)
except (ET.ParseError, OSError):
    pass
PY
)
        if [ -n "$coordinates" ]; then
            "$adb" -s "$serial" shell input tap $coordinates
            "$adb" -s "$serial" shell input keyevent 4
            found=true
            break
        fi
        sleep 1
    done
    $found || { echo 'Unknown-sources permission switch not found' >&2; exit 1; }
fi
if [ "$mode" = installer-ui ] || [ "$mode" = permission-ui ]; then
    found=false
    attempts=0
    while [ "$attempts" -lt 20 ]; do
        attempts=$((attempts + 1))
        "$adb" -s "$serial" shell uiautomator dump /sdcard/sportsauce-installer.xml >/dev/null 2>&1 || true
        "$adb" -s "$serial" exec-out cat /sdcard/sportsauce-installer.xml > "$tmp/ui.xml" 2>/dev/null || true
        coordinates=$(python3 - "$tmp/ui.xml" <<'PY'
import re, sys, xml.etree.ElementTree as ET
try:
    root = ET.parse(sys.argv[1]).getroot()
    for node in root.iter('node'):
        if node.get('text', '').strip().casefold() in {'update', 'install', 'обновить', 'установить'}:
            bounds = re.fullmatch(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', node.get('bounds', ''))
            if bounds:
                x1, y1, x2, y2 = map(int, bounds.groups())
                if x2 > x1 and y2 > y1:
                    print((x1 + x2) // 2, (y1 + y2) // 2)
                    break
except (ET.ParseError, OSError):
    pass
PY
)
        if [ -n "$coordinates" ]; then
            # Coordinates are taken from the current UI hierarchy.
            "$adb" -s "$serial" shell input tap $coordinates
            found=true
            break
        fi
        sleep 1
    done
    $found || { echo 'Package Installer confirmation button not found' >&2; exit 1; }
    attempts=0
    while ! "$adb" -s "$serial" shell dumpsys package "$fixture_package" | tr -d '\r' | grep -E 'versionCode=101([[:space:]]|$)' >/dev/null; do
        attempts=$((attempts + 1))
        [ "$attempts" -lt 20 ] || { echo 'Package Installer did not complete the upgrade' >&2; exit 1; }
        sleep 1
    done
fi
wait "$instrument_pid" || { cat "$tmp/instrumentation.log" >&2; exit 1; }
instrument_pid=''
cat "$tmp/instrumentation.log"
grep -F 'OK (1 test)' "$tmp/instrumentation.log" >/dev/null
if [ "$mode" = adb ]; then
    "$adb" -s "$serial" install -r "$tmp/good.apk" >/dev/null
fi
"$adb" -s "$serial" shell run-as "$fixture_package" test -f files/upgrade-marker || { echo 'Upgrade lost application data' >&2; exit 1; }
"$adb" -s "$serial" shell dumpsys package "$fixture_package" | tr -d '\r' | grep -E 'versionCode=101([[:space:]]|$)' >/dev/null
echo 'Signed fixture upgraded to 101 with application data preserved'
