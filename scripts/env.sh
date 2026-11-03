#!/bin/bash
if [ "${BASH_SOURCE[0]}" = "$0" ]; then echo 'Use: source scripts/env.sh [build-directory]' >&2; exit 1; fi
neutrino_root=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
export OEROOT="$neutrino_root/openembedded-core"
export BBPATH="${1:-$neutrino_root/build}"
export PATH="$neutrino_root/scripts:$neutrino_root/bitbake/bin:$neutrino_root/openembedded-core/scripts:$PATH"
cd "$BBPATH" || return
