SUMMARY = "Optional proprietary CCcam binary for Neutrino"
LICENSE = "CLOSED"
SRC_URI = "http://downloads.openpli.org/softcams/CCcam-${PV}.zip file://softcam.cccam"
SRC_URI[sha256sum] = "1c130bb76b06f836a4ed8936cd205f01d90c2645cd13814e4c487a7296f04cdb"
S = "${WORKDIR}"
COMPATIBLE_MACHINE = "^formuler3$"
RDEPENDS:${PN} = "libxcrypt-compat"
INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP:${PN} = "already-stripped ldflags file-rdeps"
do_install() {
    install -d ${D}${bindir} ${D}${sysconfdir}/init.d ${D}/var/keys
    install -m 0755 ${WORKDIR}/CCcam.${TARGET_ARCH} ${D}${bindir}/CCcam
    install -m 0755 ${WORKDIR}/softcam.cccam ${D}${sysconfdir}/init.d/softcam.cccam
}
FILES:${PN} += "/var/keys"
