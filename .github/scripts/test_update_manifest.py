import hashlib
import pathlib
import tempfile
import unittest
import struct

from update_manifest import create_manifest, executable_architecture


class ManifestTest(unittest.TestCase):
    def test_inspects_executable_architecture_and_rejects_unknown_formats(self):
        with tempfile.TemporaryDirectory() as directory:
            path = pathlib.Path(directory) / "launcher"
            path.write_bytes(b"\xcf\xfa\xed\xfe" + struct.pack("<I", 0x0100000C))
            self.assertEqual("arm64", executable_architecture(path, "macos"))
            path.write_bytes(b"\xfe\xed\xfa\xcf" + struct.pack(">I", 0x01000007))
            self.assertEqual("x64", executable_architecture(path, "macos"))
            pe = bytearray(128)
            pe[:2] = b"MZ"
            struct.pack_into("<I", pe, 60, 80)
            pe[80:84] = b"PE\x00\x00"
            struct.pack_into("<H", pe, 84, 0x8664)
            path.write_bytes(pe)
            self.assertEqual("x64", executable_architecture(path, "windows"))
            struct.pack_into("<H", pe, 84, 0xAA64)
            path.write_bytes(pe)
            self.assertEqual("arm64", executable_architecture(path, "windows"))
            with self.assertRaises(ValueError):
                executable_architecture(path, "macos")
            struct.pack_into("<H", pe, 84, 0x14C)
            path.write_bytes(pe)
            with self.assertRaises(ValueError):
                executable_architecture(path, "windows")

    def test_manifest_matches_actual_files(self):
        with tempfile.TemporaryDirectory() as directory:
            root = pathlib.Path(directory)
            mac = root / "Sportsauce-1.6.1-10422.dmg"
            windows = root / "Sportsauce-1.6.1-10422.exe"
            mac.write_bytes(b"mac payload")
            windows.write_bytes(b"windows payload")
            result = create_manifest("1.6.1", "10422", mac, "arm64", windows, "x64")
            self.assertEqual("1.6.1.10422", result["releaseId"])
            self.assertEqual(10422, result["buildNumber"])
            self.assertEqual(["arm64", "x64"], [entry["architecture"] for entry in result["assets"]])
            self.assertEqual(hashlib.sha256(b"mac payload").hexdigest(), result["assets"][0]["sha256"])
            self.assertEqual(len(b"windows payload"), result["assets"][1]["size"])

    def test_rejects_wrong_names_empty_files_unknown_architecture_and_bad_version(self):
        with tempfile.TemporaryDirectory() as directory:
            root = pathlib.Path(directory)
            mac = root / "Sportsauce-1.6.1-10422.dmg"
            windows = root / "Sportsauce-1.6.1-10422.exe"
            mac.write_bytes(b"a")
            windows.write_bytes(b"b")
            for args in [
                ("1.6.1", "10422", mac, "unknown", windows, "x64"),
                ("1.6.1-rc1", "10422", mac, "arm64", windows, "x64"),
                ("1.6.1", "0", mac, "arm64", windows, "x64"),
                ("1.6.1", "10422", windows, "arm64", windows, "x64"),
            ]:
                with self.assertRaises(ValueError):
                    create_manifest(*args)
            windows.write_bytes(b"")
            with self.assertRaises(ValueError):
                create_manifest("1.6.1", "10422", mac, "arm64", windows, "x64")


if __name__ == "__main__":
    unittest.main()
