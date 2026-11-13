SUMMARY = "NI Neutrino GUI for Formuler F3 (experimental bring-up)"
HOMEPAGE = "https://github.com/neutrino-images/ni-neutrino"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
PV = "2026.11+git"
SRC_URI = "git://github.com/sid8796/ni-neutrino.git;protocol=https;branch=formuler3 \
           file://0002-skip-disabled-mips-accel-probe.patch \
           file://0003-menu-header-without-lua.patch \
           file://0004-honor-disabled-optional-features.patch \
           file://0005-movieplayer-header-without-lua.patch \
           file://0006-movieplayer-without-lua-backend.patch \
           file://0007-plugins-without-lua-backend.patch \
           file://0008-luaserver-without-lua-backend.patch"
SRCREV = "519f05cbd7ec4a58c2868cef9b489462fd480798"
S = "${WORKDIR}/git"
COMPATIBLE_MACHINE = "^(formuler1|formuler3|formuler4|formuler4turbo)$"
PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS = "ni-libstb-hal ni-openthreads libdvbsi++ libsigc++-2.0 \
           curl freetype libpng jpeg giflib zlib fribidi pugixml ffmpeg \
           libass openssl libmicrohttpd lua gettext-native"

inherit autotools pkgconfig gettext

# Core API validates transitive includes using Automake dependency files.
EXTRA_OECONF = "--enable-dependency-tracking --with-target=cdk --with-targetprefix=/usr \
    --with-boxtype=mipsbox --with-boxmodel=${MACHINE} \
    --with-stb-hal-includes=${STAGING_INCDIR}/ni-hal/include \
    --with-stb-hal-build=${STAGING_LIBDIR} \
    --disable-mips-acc --disable-change-osd-resolution \
    --disable-pip --enable-lua --with-lua-provider=lua --disable-graphlcd --disable-lcd4linux \
    --enable-ni-web --disable-api-doc --disable-legacy-api --disable-mcp \
    --enable-fribidi --enable-pugixml --enable-giflib --enable-ffmpegdec \
    --with-network-backend=ifupdown --without-debug"

# Lua is enabled for plugins, the Lua server and WebTV stream scripts.
RDEPENDS:${PN}:append = " lua luaposix ni-web-vendor"
# OE-Core Lua is static; expose its API to loadable modules such as luaposix.
LDFLAGS:append = " -Wl,--export-dynamic"
# Static HAL dependencies must follow libstb-hal.a with --as-needed.
export LIBS = "-lcrypto -ldl -lz -lswscale"
FILES:${PN}:append = " /var/tuxbox ${datadir}/tuxbox ${datadir}/fonts /var/httpd"
