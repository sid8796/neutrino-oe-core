SUMMARY = "NI OpenThreads library"
HOMEPAGE = "https://github.com/neutrino-images/ni-openthreads"
LICENSE = "OSG-0.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2c38926f611bfbd5a3be0f817c8d2dad"
NO_GENERIC_LICENSE[OSG-0.0] = "LICENSE.txt"
PV = "3.3.1+git"
PROVIDES += "openthreads"
SRC_URI = "git://github.com/neutrino-images/ni-openthreads.git;protocol=https;branch=master"
SRCREV = "0636bebc470f3626830e7de4335f40c22c8fda51"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DBUILD_SAMPLES=OFF -DDYNAMIC_OPENTHREADS=ON \
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
    -D_OPENTHREADS_ATOMIC_USE_GCC_BUILTINS_EXITCODE=0 \
    -D_OPENTHREADS_ATOMIC_USE_GCC_BUILTINS_EXITCODE__TRYRUN_OUTPUT=1"
