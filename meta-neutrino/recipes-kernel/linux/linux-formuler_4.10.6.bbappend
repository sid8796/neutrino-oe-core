# USB-image deployment is a separate manual step during bring-up.
# The vendor recipe normally writes /dev/mtd1 when kernel-image is installed
# on a running receiver. Keep package installation from silently flashing it.
pkg_postinst:kernel-image () {
    :
}
