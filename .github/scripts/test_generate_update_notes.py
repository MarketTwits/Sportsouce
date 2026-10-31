from pathlib import Path
import tempfile
import unittest
from generate_update_notes import generate


class GenerateNotesTest(unittest.TestCase):
    def test_snapshots_are_localized_and_versioned(self):
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory) / 'source'
            source.mkdir()
            (source / 'whatsnew-ru-RU').write_text('Версия $APP_VERSION', encoding='utf-8')
            (source / 'whatsnew-en-US').write_text('Version $APP_VERSION', encoding='utf-8')
            output = Path(directory) / 'output'
            generate(source, output, '1.2.3')
            self.assertEqual('Версия 1.2.3', (output / 'whatsnew-ru-RU.txt').read_text(encoding='utf-8'))
            self.assertEqual('Version 1.2.3', (output / 'whatsnew-en-US.txt').read_text(encoding='utf-8'))
            with self.assertRaises(ValueError):
                generate(source, output, '../bad')
            (source / 'whatsnew-en-US').write_text(' ' * 10, encoding='utf-8')
            with self.assertRaises(ValueError):
                generate(source, output, '1.2.3')


if __name__ == '__main__':
    unittest.main()
