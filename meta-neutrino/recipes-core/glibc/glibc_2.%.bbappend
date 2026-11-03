FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-ptrace-protect-ptrace_peeksiginfo_args-from-redefint.patch \
                   file://0006-PATCH-remove-arc4random-implementation.patch \
                   file://0005-Revert-Linux-Use-32-bit-vDSO-for-clock_gettime-getti.patch \
                   "
SRC_URI:append:arm = " file://tls-libwidevinecdm.so-since-4.10.2252.0-has-TLS-with.patch"
