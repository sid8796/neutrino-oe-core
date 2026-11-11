SUMMARY = "SysV service dispatcher for Neutrino Camd control"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://camd"
S = "${WORKDIR}"

inherit allarch update-rc.d

INITSCRIPT_NAME = "camd"
INITSCRIPT_PARAMS = "defaults 92 8"

do_install() {
    install -d ${D}${sysconfdir}/init.d ${D}${localstatedir}/etc
    install -m 0755 ${WORKDIR}/camd ${D}${sysconfdir}/init.d/camd
}

FILES:${PN} += "${localstatedir}/etc"
