import copy
import json
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

import publish_update_release as publisher
import update_manifest


class PublishTest(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.directory = Path(self.tmp.name)
        self.assets = []
        for name in ('Sportsauce-1.0.0-1.apk', 'Sportsauce-1.0.0-1.dmg', 'Sportsauce-1.0.0-1.exe', 'update-manifest.json', 'whatsnew-ru-RU.txt', 'whatsnew-en-US.txt'):
            path = self.directory / name
            path.write_bytes(name.encode())
            self.assets.append(path)
        manifest = update_manifest.create_manifest('1.0.0', '1', self.assets[1], 'arm64', self.assets[2], 'x64')
        self.assets[3].write_text(json.dumps(manifest), encoding='utf-8')
        self.release = {'draft': True, 'prerelease': False, 'target_commitish': 'abc', 'assets': []}
        self.uploaded = {}
        self.calls = []

    def gh(self, *args, **kwargs):
        self.calls.append(args)
        if args[:2] == ('release', 'create'):
            return ''
        if args[:2] == ('release', 'upload'):
            for path in args[3:]:
                self.uploaded[Path(path).name] = Path(path).read_bytes()
            self.release['assets'] = [{'name': name, 'state': 'uploaded'} for name in self.uploaded]
            return ''
        if args[:2] == ('release', 'download'):
            target = Path(args[args.index('--dir') + 1])
            for name, data in self.uploaded.items():
                (target / name).write_bytes(data)
            return ''
        if args[:2] == ('release', 'edit'):
            return ''
        raise AssertionError(args)

    def run_publish(self, initial=None):
        values = iter([initial, copy.deepcopy(self.release)] if initial is None else [initial])
        def find(*args):
            try:
                return next(values)
            except StopIteration:
                return copy.deepcopy(self.release)
        with patch.object(publisher, 'gh', side_effect=self.gh), patch.object(publisher, 'find_release', side_effect=find), patch.object(publisher, 'check_tag'):
            publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)

    def test_new_draft_uploads_verifies_and_publishes(self):
        self.run_publish()
        self.assertEqual({p.name for p in self.assets}, set(self.uploaded))
        self.assertEqual(('release', 'edit'), self.calls[-1][:2])

    def test_android_only_release_is_verified_and_published(self):
        self.assets = [self.assets[0], *self.assets[-2:]]
        self.run_publish()
        self.assertEqual({p.name for p in self.assets}, set(self.uploaded))
        self.assertEqual(('release', 'edit'), self.calls[-1][:2])

    def test_retry_of_android_only_release_does_not_change_published_assets(self):
        self.assets = [self.assets[0], *self.assets[-2:]]
        self.uploaded = {path.name: path.read_bytes() for path in self.assets}
        release = {**self.release, 'draft': False,
                   'assets': [{'name': name, 'state': 'uploaded'} for name in self.uploaded]}
        with patch.object(publisher, 'gh', side_effect=self.gh), \
             patch.object(publisher, 'find_release', return_value=release), \
             patch.object(publisher, 'check_tag', return_value=True):
            publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)
        self.assertFalse(any(call[:2] in (('release', 'create'), ('release', 'upload'), ('release', 'edit')) for call in self.calls))

    def test_incomplete_desktop_set_is_rejected_before_network(self):
        with patch.object(publisher, 'gh') as gh:
            with self.assertRaises(ValueError):
                publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes',
                                  [self.assets[0], self.assets[1], *self.assets[-2:]])
            gh.assert_not_called()

    def test_retries_a_partial_matching_draft_without_reuploading_existing_files(self):
        self.uploaded[self.assets[0].name] = self.assets[0].read_bytes()
        self.release['assets'] = [{'name': self.assets[0].name, 'state': 'uploaded'}]
        self.run_publish(copy.deepcopy(self.release))
        upload = next(call for call in self.calls if call[:2] == ('release', 'upload'))
        self.assertNotIn(str(self.assets[0]), upload)
        self.assertEqual(('release', 'edit'), self.calls[-1][:2])

    def test_conflicting_draft_asset_or_source_never_publishes(self):
        self.uploaded[self.assets[0].name] = b'different'
        self.release['assets'] = [{'name': self.assets[0].name, 'state': 'uploaded'}]
        with self.assertRaisesRegex(RuntimeError, 'differs'):
            self.run_publish(copy.deepcopy(self.release))
        self.assertFalse(any(call[:2] == ('release', 'edit') for call in self.calls))
        for change in ({'target_commitish': 'other'}, {'prerelease': True}):
            draft = {**self.release, **change}
            with self.assertRaises(RuntimeError):
                self.run_publish(draft)

    def test_retry_of_identical_published_release_is_read_only(self):
        self.uploaded = {path.name: path.read_bytes() for path in self.assets}
        release = {**self.release, 'draft': False,
                   'assets': [{'name': name, 'state': 'uploaded'} for name in self.uploaded]}
        with patch.object(publisher, 'gh', side_effect=self.gh), \
             patch.object(publisher, 'find_release', return_value=release), \
             patch.object(publisher, 'check_tag', return_value=True):
            publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)
        self.assertFalse(any(call[:2] in (('release', 'create'), ('release', 'upload'), ('release', 'edit')) for call in self.calls))

    def test_retry_of_different_or_incomplete_published_release_fails_closed(self):
        self.uploaded = {path.name: path.read_bytes() for path in self.assets}
        release = {**self.release, 'draft': False,
                   'assets': [{'name': name, 'state': 'uploaded'} for name in self.uploaded]}
        with patch.object(publisher, 'gh', side_effect=self.gh), \
             patch.object(publisher, 'find_release', return_value=release), \
             patch.object(publisher, 'check_tag', return_value=True):
            self.uploaded[self.assets[0].name] = b'changed'
            with self.assertRaisesRegex(RuntimeError, 'Published asset differs'):
                publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)
            release['assets'].pop()
            with self.assertRaisesRegex(RuntimeError, 'asset set'):
                publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)
        self.assertFalse(any(call[:2] in (('release', 'create'), ('release', 'upload'), ('release', 'edit')) for call in self.calls))

    def test_published_release_requires_existing_tag_and_stable_channel(self):
        release = {**self.release, 'draft': False}
        with patch.object(publisher, 'gh') as gh, \
             patch.object(publisher, 'find_release', return_value=release), \
             patch.object(publisher, 'check_tag', return_value=False):
            with self.assertRaisesRegex(RuntimeError, 'unexpected tag or channel'):
                publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)
            gh.assert_not_called()
        release['prerelease'] = True
        with patch.object(publisher, 'gh') as gh, \
             patch.object(publisher, 'find_release', return_value=release), \
             patch.object(publisher, 'check_tag', return_value=True):
            with self.assertRaisesRegex(RuntimeError, 'unexpected tag or channel'):
                publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)
            gh.assert_not_called()

    def test_tag_mismatch_fails_closed(self):
        with patch.object(publisher, 'gh', side_effect=['{}', '{"sha":"wrong"}']):
            with self.assertRaisesRegex(RuntimeError, 'different commit'):
                publisher.check_tag('owner/repo', '1.0.0.1', 'abc')

    def test_missing_required_asset_fails_before_network(self):
        self.assets[0].unlink()
        with patch.object(publisher, 'gh') as gh:
            with self.assertRaises(ValueError):
                publisher.publish('owner/repo', 'tag', 'abc', 'title', 'notes', self.assets)
            gh.assert_not_called()

    def test_wrong_apk_name_or_stale_manifest_fails_before_network(self):
        with patch.object(publisher, 'gh') as gh:
            apk = self.assets[0].with_name('wrong.apk')
            apk.write_bytes(self.assets[0].read_bytes())
            with self.assertRaisesRegex(ValueError, 'APK name'):
                publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', [apk, *self.assets[1:]])
            self.assets[1].write_bytes(b'changed after manifest generation')
            with self.assertRaisesRegex(ValueError, 'does not match'):
                publisher.publish('owner/repo', '1.0.0.1', 'abc', 'title', 'notes', self.assets)
            gh.assert_not_called()


if __name__ == '__main__':
    unittest.main()
