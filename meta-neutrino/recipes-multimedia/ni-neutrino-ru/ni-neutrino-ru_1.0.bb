SUMMARY = "Initial Russian language setting for Neutrino"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"
SRC_URI = "file://neutrino.conf file://russian-locale-source.json"
S = "${WORKDIR}"
inherit allarch
RDEPENDS:${PN} = "ni-neutrino"

do_install() {
    install -d ${D}/var/tuxbox/config
    install -m 0644 ${WORKDIR}/neutrino.conf ${D}/var/tuxbox/config/neutrino.conf
    install -d ${D}${datadir}/doc/${BPN}
    install -m 0644 ${WORKDIR}/russian-locale-source.json ${D}${datadir}/doc/${BPN}/
}
FILES:${PN} += "/var/tuxbox/config/neutrino.conf"
CONFFILES:${PN} = "/var/tuxbox/config/neutrino.conf"
