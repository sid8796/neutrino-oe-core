# OpenPLi's inittab recipe hard-codes Enigma2 and omits a serial getty.
# Neutrino is started once through ni-formuler-init's SysV service.
do_install:append:formuler() {
    sed -i '/^gui:.*enigma2\.sh/d' ${D}${sysconfdir}/inittab
    if ! grep -q '^[^#].*getty.*ttyS0' ${D}${sysconfdir}/inittab; then
        printf '\nS0:2345:respawn:${base_sbindir}/getty -L 115200 ttyS0 vt102\n' >> ${D}${sysconfdir}/inittab
    fi
    if grep -q '^[^#].*enigma2\.sh' ${D}${sysconfdir}/inittab; then
        bbfatal "Enigma2 startup remains in the Neutrino image inittab"
    fi
}

RDEPENDS:${PN}:append:formuler = " busybox"

PACKAGE_ARCH:formuler = "${MACHINE_ARCH}"
