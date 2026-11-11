SUMMARY = "Optional OSCam-Emu for Neutrino"
HOMEPAGE = "https://github.com/oscam-mirror/oscam-emu"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
PV = "1.0+git"
SRC_URI = "git://github.com/oscam-mirror/oscam-emu.git;protocol=https;branch=master file://softcam.oscam-emu file://oscam.conf"
SRCREV = "ad7588c6dd83f3780812580c9a2e63b603680106"
S = "${WORKDIR}/git"
inherit cmake
RDEPENDS:${PN} += "ni-camd-control"
DEPENDS = "openssl libusb libdvbcsa pcsc-lite"
EXTRA_OECMAKE = "-DOSCAM_SYSTEM_NAME=Tuxbox -DWEBIF=1 -DWITH_SSL=1 -DHAVE_LIBUSB=1 -DHAVE_PCSC=1 -DCARDREADER_PCSC=1 -DHAVE_LIBDVBCSA=1 -DUSE_COMPRESS=0 -DWITH_STAPI=0"
do_configure:prepend() {
    # Let Yocto split debug symbols and preserve the toolchain's linker flags.
    sed -i '/^[[:space:]]*set (CMAKE_EXE_LINKER_FLAGS "-s")$/d' "${S}/CMakeLists.txt"
}
do_install() {
    install -d ${D}${bindir} ${D}${sysconfdir}/init.d ${D}/var/tuxbox/config/oscam-emu

    install -m 0644 ${WORKDIR}/oscam.conf ${D}/var/tuxbox/config/oscam-emu/oscam.conf
    install -m 0755 ${B}/oscam ${D}${bindir}/oscam-emu
    install -d ${D}${localstatedir}/bin
    ln -s ${bindir}/oscam-emu ${D}${localstatedir}/bin/oscam
    install -m 0755 ${WORKDIR}/softcam.oscam-emu ${D}${sysconfdir}/init.d/softcam.oscam-emu
}
FILES:${PN} += "/var/tuxbox/config/oscam-emu ${localstatedir}/bin/oscam"
CONFFILES:${PN} += "/var/tuxbox/config/oscam-emu/oscam.conf"
