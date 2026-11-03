#!/usr/bin/env python3
"""Validate locked source files/patches against upstream snapshots (read-only)."""
import argparse
import json
from pathlib import Path
import re
import shutil
import subprocess
import sys

BUNDLE = Path(__file__).resolve().parents[1]


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--hal', required=True, type=Path)
    ap.add_argument('--gui', required=True, type=Path)
    args = ap.parse_args()
    git = shutil.which('git')
    if not git:
        ap.error('git is required')
    mapping = {'ni-libstb-hal': args.hal, 'ni-neutrino': args.gui}
    for package, source in mapping.items():
        patches = sorted((BUNDLE / 'meta-neutrino/recipes-multimedia' / package / 'files').glob('*.patch'))
        # The GUI patches touch different files; checking both at once detects overlap too.
        if patches:
            subprocess.run([git, 'apply', '--check', *map(str, patches)], cwd=source.resolve(), check=True)
            print(package + ': git apply --check passed')
        else:
            print(package + ': no external patches; model changes are in the fork')
    lock = json.loads((BUNDLE / 'sources.lock.json').read_text())
    for package, source in mapping.items():
        revision = lock[package]['rev']
        actual = subprocess.check_output([git, 'rev-parse', 'HEAD'], cwd=source.resolve(), text=True).strip()
        assert actual == revision, package + ': checkout does not match source lock'
        recipe = (BUNDLE / 'meta-neutrino/recipes-multimedia' / package / (package + '_git.bb')).read_text()
        assert f'SRCREV = "{revision}"' in recipe, package
        print(package + ': recipe revision matches source lock')
    for path in (p for layer in ('meta-neutrino', 'meta-formuler', 'meta-softcams') for p in (BUNDLE / layer).rglob('*')):
        if not path.is_file() or '__pycache__' in path.parts:
            continue
        if path.suffix in ('.sh', '.py', '.bb', '.bbappend', '.conf', '.patch') or path.name.startswith('ni-neutrino'):
            assert b'\r\n' not in path.read_bytes(), str(path) + ': CRLF found'
        if path.suffix in ('.patch', '.diff'):
            assert re.search(r'^Upstream-Status:\s*(Pending|Submitted|Backport|Denied|Inappropriate)(?:\s|$)', path.read_text(), re.M), str(path) + ': missing or invalid Upstream-Status'
        if path.suffix in ('.bb', '.bbappend'):
            data = path.read_text()
            if path.suffix == '.bb' and path.name.startswith(('ni-', 'neutrino-plugin-')):
                assert re.search(r'^LICENSE\s*=', data, re.M), path
            uri_match = re.search(r'^SRC_URI\s*=\s*"(.*?)"', data, re.M | re.S)
            uris = uri_match.group(1) if uri_match else ''
            for name in re.findall(r"""file://([^\s"'();,]+)""", uris):
                if '${' not in name:
                    assert (path.parent / 'files' / name).exists() or (path.parent / name).exists() or any(path.parent.rglob(name)), (path, name)
    print('Source reference, patch status and line ending checks passed; this is not a BitBake parse or compile.')


if __name__ == '__main__':
    main()

