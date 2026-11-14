FORMULER_BACKGROUND_NETWORK = "${@'1' if d.getVar('MACHINE') in ('formuler1', 'formuler3', 'formuler4', 'formuler4turbo') else '0'}"

do_install:append() {
    if [ "${FORMULER_BACKGROUND_NETWORK}" = "1" ]; then
        init="${D}${sysconfdir}/init.d/networking"
        sed -i '/^start)/,/^stop)/ s|^[[:space:]]*ifup -a[[:space:]]*$|\t${sbindir}/ni-wifi-boot start|' "$init"
        grep -Fq '${sbindir}/ni-wifi-boot start' "$init" || bbfatal "Cannot find the networking start command"
    fi
}
