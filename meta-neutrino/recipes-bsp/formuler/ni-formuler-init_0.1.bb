SUMMARY = "Neutrino boot integration for Formuler receivers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://ni-neutrino file://ni-neutrino-run \
           file://ni-wifi-ifupdown file://pre-wlan0.sh file://post-wlan0.sh"
S = "${WORKDIR}"
COMPATIBLE_MACHINE = "^(formuler1|formuler3|formuler4|formuler4turbo)$"
PACKAGE_ARCH = "${MACHINE_ARCH}"
RPROVIDES:${PN} = "ni-formuler3-init"
RREPLACES:${PN} = "ni-formuler3-init"
RCONFLICTS:${PN} = "ni-formuler3-init"
inherit update-rc.d
INITSCRIPT_NAME = "ni-neutrino"
INITSCRIPT_PARAMS = "defaults 95 05"
RDEPENDS:${PN} = "ni-neutrino kmod wpa-supplicant"
do_install() {
    install -d ${D}${sysconfdir}/init.d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/ni-neutrino ${D}${sysconfdir}/init.d/ni-neutrino
    install -m 0755 ${WORKDIR}/ni-neutrino-run ${D}${sbindir}/ni-neutrino-run
    install -d ${D}${sysconfdir}/network
    install -m 0755 ${WORKDIR}/ni-wifi-ifupdown ${D}${sbindir}/ni-wifi-ifupdown
    install -m 0755 ${WORKDIR}/pre-wlan0.sh ${D}${sysconfdir}/network/pre-wlan0.sh
    install -m 0755 ${WORKDIR}/post-wlan0.sh ${D}${sysconfdir}/network/post-wlan0.sh
}
