#!/usr/bin/env python3
"""Check F3 USB ZIP structure and partition limits; does not certify ABI or flash."""
import argparse
import gzip
import hashlib
from pathlib import Path
import sys
import zipfile

KERNEL_LIMIT = 6 * 1024**2
ROOTFS_LIMIT = 245 * 1024**2


def inspect(path):
    with zipfile.ZipFile(path) as archive:
        names = archive.namelist()
        required = ('formuler3/kernel.bin', 'formuler3/rootfs.bin', 'formuler3/imageversion', 'formuler3/noforce')
        for name in required:
            if names.count(name) != 1:
                raise ValueError('Missing or duplicate ZIP entry: ' + name)
        for name in names:
            parts = Path(name).parts
            if name.startswith(('/', '\\')) or '..' in parts or '\\' in name:
                raise ValueError('Unexpected ZIP entry: ' + name)
            if not name.startswith('formuler3/'):
                raise ValueError('Unexpected device directory: ' + name)
        if 'formuler3/force' in names:
            raise ValueError('This development bundle expects noforce, not force.')
        for name, limit in ((required[0], KERNEL_LIMIT), (required[1], ROOTFS_LIMIT)):
            size = archive.getinfo(name).file_size
            if not 0 < size <= limit:
                raise ValueError(f'{name}: {size} bytes exceeds partition limit {limit}, or is empty')
            print(f'{name}: {size} / {limit} bytes')
        with archive.open(required[0]) as kernel:
            if kernel.read(2) != b'\x1f\x8b':
                raise ValueError('kernel.bin must be gzip-compressed for this BSP')
        # Read the entire compressed stream to verify gzip CRC and truncation.
        with archive.open(required[0]) as kernel, gzip.GzipFile(fileobj=kernel) as stream:
            while stream.read(1024 * 1024):
                pass
        with archive.open(required[1]) as rootfs:
            if rootfs.read(4) != b'UBI#':
                raise ValueError('rootfs.bin is not a UBI image')
        if not archive.read(required[2]).strip():
            raise ValueError('Empty imageversion')
    h = hashlib.sha256()
    with path.open('rb') as stream:
        for block in iter(lambda: stream.read(1024 * 1024), b''):
            h.update(block)
    print('ZIP SHA256:', h.hexdigest())
    print('Structure and size checks passed. Kernel/module ABI and boot are NOT checked.')


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('usb_zip', type=Path)
    args = ap.parse_args()
    try:
        inspect(args.usb_zip)
    except (OSError, ValueError, zipfile.BadZipFile, EOFError) as exc:
        ap.exit(1, str(exc) + '\n')


if __name__ == '__main__':
    main()
