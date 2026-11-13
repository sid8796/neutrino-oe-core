SUMMARY = "NI hardware abstraction library for Formuler receivers"
HOMEPAGE = "https://github.com/neutrino-images/ni-libstb-hal"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
PV = "2026.11+git"
PR = "r1"
SRC_URI = "git://github.com/sid8796/ni-libstb-hal.git;protocol=https;branch=formuler3"
SRCREV = "3e06251c1756c448962dfc411aab4f149ec9b8b7"
S = "${WORKDIR}/git"
COMPATIBLE_MACHINE = "^(formuler1|formuler3|formuler4|formuler4turbo)$"
PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS = "ffmpeg ni-openthreads libass libpng zlib"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-target=cdk --with-targetprefix=/usr \
                --with-boxtype=mipsbox --with-boxmodel=${MACHINE} \
                --disable-shared --enable-static --disable-gstreamer --without-debug"
# Match NI's static HAL linkage. The GUI resolves its FFmpeg/PNG/ASS dependencies.
do_install:append() {
    # HAL wrappers include ../libmipsbox/*.h; keep their relative layout intact.
    for tree in include libmipsbox libdvbci libeplayer3 common; do
        # MIPS headers are symlinks to shared ARM headers; stage their contents.
        (cd ${S}; find -L "$tree" -type f -name '*.h') | while IFS= read -r relative; do
            install -D -m 0644 "${S}/$relative" "${D}${includedir}/ni-hal/$relative"
        done
    done
    # Explicit public MIPS headers: install dereferences each source symlink.
    for header in video_lib.h audio_lib.h hdmi_cec.h hdmi_cec_types.h linux-uapi-cec.h playback_libeplayer3.h record_lib.h; do
        install -D -m 0644 "${S}/libmipsbox/$header" "${D}${includedir}/ni-hal/libmipsbox/$header"
    done
    test -f ${D}${includedir}/ni-hal/libmipsbox/video_lib.h
    test -f ${D}${includedir}/ni-hal/libmipsbox/audio_lib.h
    test -f ${D}${includedir}/ni-hal/libmipsbox/hdmi_cec.h
}
FILES:${PN}-dev:append = " ${includedir}/ni-hal"

# Fail at the producer, rather than much later while compiling the GUI.
SYSROOT_PREPROCESS_FUNCS += "ni_check_hal_headers"
ni_check_hal_headers() {
    for header in video_lib.h audio_lib.h hdmi_cec.h; do
        if [ ! -r "${SYSROOT_DESTDIR}${includedir}/ni-hal/libmipsbox/$header" ]; then
            bbfatal "HAL public header missing from staged sysroot: libmipsbox/$header"
        fi
    done
}
