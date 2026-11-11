SUMMARY = "Experimental NI Neutrino image for Formuler F3"
LICENSE = "MIT"
COMPATIBLE_MACHINE = "^formuler3$"
inherit core-image
IMAGE_FEATURES += "ssh-server-dropbear debug-tweaks"
IMAGE_INSTALL += "ni-neutrino ni-formuler3-init ca-certificates kmod init-ifupdown iproute2 ethtool"
# Use the OpenPLi kernel-module recipe, compiled against this image's F3 kernel.
IMAGE_INSTALL += "kernel-module-88x2bu wpa-supplicant iw"
IMAGE_LINGUAS = "en-us ru-ru"
IMAGE_INSTALL += "locale-base-ru-ru"
IMAGE_ROOTFS_EXTRA_SPACE = "8192"
# KiB; final .ubi and compressed kernel require a separate deploy-size check.
IMAGE_ROOTFS_MAXSIZE = "250880"

# These assets are required before Neutrino can show its first setup screen.
# Validate the final rootfs, not only do_install's staging directory.
ROOTFS_POSTPROCESS_COMMAND:append = " ni_validate_gui_resources; "

python ni_validate_gui_resources() {
    import os
    import subprocess

    root = d.getVar('IMAGE_ROOTFS')
    share = d.getVar('datadir')
    required = [
        d.getVar('bindir') + '/neutrino',
        '/usr/sbin/ni-wifi-ifupdown',
        '/etc/network/pre-wlan0.sh',
        '/etc/network/post-wlan0.sh',
        '/etc/wpa_supplicant/ifupdown.sh',
        share + '/fonts/neutrino.ttf',
        share + '/fonts/tuxtxt.ttf',
        share + '/fonts/fa-solid-900.ttf',
        share + '/tuxbox/neutrino/locale/english.locale',
        share + '/tuxbox/neutrino/locale/russian.locale',
        '/usr/lib/locale/locale-archive',
    ]
    for name in required:
        path = os.path.join(root, name.lstrip('/'))
        if not os.path.isfile(path) or os.path.getsize(path) == 0:
            bb.fatal('Missing required Neutrino resource in rootfs: ' + name)
        if name.endswith('.ttf'):
            with open(path, 'rb') as stream:
                if stream.read(4) not in (b'\x00\x01\x00\x00', b'OTTO', b'true', b'ttcf'):
                    bb.fatal('Invalid Neutrino font payload in rootfs: ' + name)

    localedef = bb.utils.which(d.getVar('PATH'), 'cross-localedef')
    if not localedef:
        bb.fatal('Native localedef is required to inspect the system locale archive')
    env = dict(os.environ, PATH=d.getVar('PATH'))
    env['LOCALEARCHIVE'] = os.path.join(root, 'usr/lib/locale/locale-archive')
    locales = subprocess.check_output([localedef, '--list-archive'], env=env, text=True).splitlines()
    if 'ru_RU.utf8' not in locales:
        bb.fatal('Russian UTF-8 locale is absent from the rootfs locale archive')

    def catalog(name):
        path = os.path.join(root, (share + '/tuxbox/neutrino/locale/' + name + '.locale').lstrip('/'))
        with open(path, encoding='utf-8') as stream:
            lines = [line.rstrip('\n').split(' ', 1) for line in stream if line.strip() and not line.startswith('#')]
        if any(len(line) != 2 for line in lines) or len(dict(lines)) != len(lines):
            bb.fatal('Invalid or duplicate entries in Neutrino locale: ' + name)
        return dict(lines)
    english, russian = catalog('english'), catalog('russian')
    if english.keys() != russian.keys():
        bb.fatal('Russian Neutrino locale key set does not match the installed English locale')
}
