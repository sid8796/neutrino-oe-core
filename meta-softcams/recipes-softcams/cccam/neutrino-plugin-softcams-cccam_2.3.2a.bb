SUMMARY = "Optional proprietary CCcam binary for Neutrino"
LICENSE = "CLOSED"
CCCAM_VERSION = "2.3.2a"
PV = "${CCCAM_VERSION}+git${SRCPV}"

SRC_URI = "http://downloads.openpli.org/softcams/CCcam-${CCCAM_VERSION}.zip \
           git://github.com/MOHAMED19OS/SoftCam_Emu.git;protocol=https;branch=main;name=softcam;destsuffix=softcam-emu \
           file://softcam.cccam file://CCcam.cfg"
SRCREV_softcam = "${AUTOREV}"
SRCREV_FORMAT = "softcam"
SRC_URI[sha256sum] = "1c130bb76b06f836a4ed8936cd205f01d90c2645cd13814e4c487a7296f04cdb"
S = "${WORKDIR}"
COMPATIBLE_MACHINE = "^(formuler1|formuler3|formuler4|formuler4turbo)$"
RDEPENDS:${PN} = "libxcrypt-compat"
INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP:${PN} = "already-stripped ldflags file-rdeps"
do_install() {
    install -d ${D}${bindir} ${D}${sysconfdir}/init.d ${D}/var/keys
    install -m 0755 ${WORKDIR}/CCcam.${TARGET_ARCH} ${D}${bindir}/CCcam
    install -m 0755 ${WORKDIR}/softcam.cccam ${D}${sysconfdir}/init.d/softcam.cccam
    install -m 0600 ${WORKDIR}/CCcam.cfg ${D}/var/keys/CCcam.cfg
    install -m 0600 ${WORKDIR}/softcam-emu/SoftCam.Key ${D}/var/keys/SoftCam.Key
}
FILES:${PN} += "/var/keys"
CONFFILES:${PN} += "/var/keys/CCcam.cfg /var/keys/SoftCam.Key"
