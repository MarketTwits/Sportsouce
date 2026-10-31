import json
from pathlib import Path
import tempfile
import unittest
from check_web_deployment_order import validate


class DeploymentOrderTest(unittest.TestCase):
    def test_rejects_stale_or_invalid_identity(self):
        with tempfile.TemporaryDirectory() as tmp:
            candidate = Path(tmp) / 'candidate.json'
            published = Path(tmp) / 'published.json'
            def write(path, sequence, build):
                path.write_text(json.dumps({'schemaVersion': 1, 'sequence': sequence, 'buildNumber': build, 'versionName': '1.0.0'}))
            write(published, 20, 100)
            write(candidate, 21, 100)
            validate(candidate, published)
            validate(candidate, None)
            for sequence, build in [(20, 100), (19, 101), (21, 99), (True, 100), (21, 0)]:
                write(candidate, sequence, build)
                with self.assertRaises(ValueError):
                    validate(candidate, published)
            published.write_text('not json')
            write(candidate, 21, 100)
            with self.assertRaises(ValueError):
                validate(candidate, published)

    def test_rejects_unreadable_or_oversized_browser_metadata(self):
        with tempfile.TemporaryDirectory() as tmp:
            candidate = Path(tmp) / 'candidate.json'
            base = {'schemaVersion': 1, 'sequence': 21, 'buildNumber': 100, 'versionName': '1.0.0'}
            for change in ({'versionName': ''}, {'versionName': None}, {'changes': {'ru': 42}},
                           {'changes': {'en': 'a' * 131_072}}, {'changes': {'en': '😀' * 65_536}}):
                candidate.write_text(json.dumps({**base, **change}, ensure_ascii=False))
                with self.assertRaises(ValueError):
                    validate(candidate, None)
            candidate.write_text(json.dumps({**base, 'changes': {'en': 'Fixes'}}))
            validate(candidate, None)


if __name__ == '__main__':
    unittest.main()
