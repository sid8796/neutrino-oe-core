SUMMARY = "GNU libmicrohttpd required by the pinned NI GUI"
HOMEPAGE = "https://www.gnu.org/software/libmicrohttpd/"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=57d09285feac8a64efa878e692b14f36"
SRC_URI = "https://ftp.gnu.org/gnu/libmicrohttpd/libmicrohttpd-${PV}.tar.gz"
# From NI buildsystem 2999bab12e77e493956635c754703dd022012280.
SRC_URI[sha256sum] = "04bfe8ef75db7d629a33de767599765cecadc56274a39822d5d081030d577685"
DEPENDS = "file"
inherit autotools pkgconfig gettext
EXTRA_OECONF = "--enable-shared --disable-static --disable-examples --disable-doc --disable-curl --disable-https"
CFLAGS:append = " -pthread -D_REENTRANT"
do_compile:append() {
    sed -i 's|-L${STAGING_LIBDIR}||g' ${B}/libmicrohttpd.pc
}
