SUMMARY = "Browser modules for NI WebUI"
HOMEPAGE = "https://github.com/neutrino-images/ni-buildsystem"
LICENSE = "MIT & Apache-2.0"
LIC_FILES_CHKSUM = "file://preact/package/LICENSE;md5=11497b3621048be9724baca2ec0d85ee \
    file://htm/package/LICENSE;md5=de72828030685f7dd2005bd74de4650c \
    file://preact-router/package/LICENSE;md5=8318b79a651efc0165e52d30e14babde \
    file://hls/package/LICENSE;md5=8c22b1b3074f826155dbc85e6a0d4d1f"

# Match the module versions and browser import paths in ni-buildsystem.
SRC_URI = "https://registry.npmjs.org/preact/-/preact-10.24.3.tgz;name=preact;subdir=web-vendor/preact \
    https://registry.npmjs.org/htm/-/htm-3.1.1.tgz;name=htm;subdir=web-vendor/htm \
    https://registry.npmjs.org/preact-router/-/preact-router-4.1.2.tgz;name=router;subdir=web-vendor/preact-router \
    https://registry.npmjs.org/hls.js/-/hls.js-1.7.3.tgz;name=hls;subdir=web-vendor/hls"
SRC_URI[preact.sha256sum] = "d8f5d1d6bb058659079b26044ff532a77eb7060b9ae001faef80257891151880"
SRC_URI[htm.sha256sum] = "2425b9bee11409177bcabc7f32e319926fc6690c1701c0b257c88bdff2d5ba90"
SRC_URI[router.sha256sum] = "c5aec9082f57c2efa5ed7268b1bfb177e74e707741e2a107e755aacc9a8bf5d9"
SRC_URI[hls.sha256sum] = "a7290f1645f7c605baec75638acda6c6f7b08dde8a615c841a19d0e38df88729"

S = "${WORKDIR}/web-vendor"

inherit allarch

NI_WEB_VENDOR_DIR = "${datadir}/tuxbox/neutrino/httpd/vendor"

ni_web_install_module() {
    sed -e '/^\/\/# sourceMappingURL=/d' \
        -e 's|from"preact/hooks"|from"/vendor/hooks.module.js"|g' \
        -e 's|from"preact"|from"/vendor/preact.module.js"|g' \
        "$1" > "${T}/$2"
    gzip -9 -n -c "${T}/$2" > "${D}${NI_WEB_VENDOR_DIR}/$2.gz"
}

do_install() {
    install -d "${D}${NI_WEB_VENDOR_DIR}"
    ni_web_install_module "${S}/preact/package/dist/preact.module.js" preact.module.js
    ni_web_install_module "${S}/preact/package/hooks/dist/hooks.module.js" hooks.module.js
    ni_web_install_module "${S}/htm/package/dist/htm.module.js" htm.module.js
    ni_web_install_module "${S}/preact-router/package/dist/preact-router.module.js" preact-router.module.js
    ni_web_install_module "${S}/hls/package/dist/hls.light.min.mjs" hls.module.js
}

FILES:${PN} = "${NI_WEB_VENDOR_DIR}"
