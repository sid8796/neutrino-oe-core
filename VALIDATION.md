# Validation

## First complete standalone image — 2026-11-04

Built from core commit de688495317833b37e2ee162bcd93c080f7d9b08 in
the Linux filesystem, using build directory /home/sid8796/neutrino-build.
The final incremental invocation completed all 3977 tasks successfully;
3940 were already complete from the preceding build invocations.

- Kernel patch series refreshed without disabling patch-fuzz QA. Replaying
  all 16 patches produced identical SHA256 values for all 26 touched source files.
- Kernel do_prepare_recipe_sysroot passed after recreating its stale workdir.
- Kernel do_package_qa passed with the empty-dirs exception scoped to this
  vendor recipe's kernel-image/kernel-dev staging layout only.
- Image do_rootfs, required GUI resource/locale validation, do_image_qa,
  do_image_ubifs, do_image_ubi and do_image_complete all passed.
- check_deploy.py passed for Neutrino-0.1-formuler3_usb.zip:
  kernel.bin 5,337,094 / 6,291,456 bytes; rootfs.bin 44,171,264 / 256,901,120 bytes.
- USB ZIP SHA256:
  9866e28db2731ae93c8f88f8530b55a5306e0a07ec5f3453e14d3160d69996de

This validates compilation and image packaging. Booting this new standalone
image, kernel/vendor-module runtime compatibility and optional softcams have
not been tested on the receiver. The earlier GUI packaging warning about an
embedded TMPDIR reference remains; it did not fail package QA. Personal Wi-Fi
credentials and channel lists are not part of this base image.

## Initial preparation — 2026-11-03

Standalone checkout: neutrino-oe-core, Yocto Scarthgap, machine formuler3.

- BitBake parse passed: 2830 .bb recipes, 4757 targets, 706 skipped,
  0 masked, 0 errors. No meta-openpli, meta-local, Qt/Clang/LTS-mixin or
  other receiver-vendor layer is configured.
- Complete dependency graph for ni-formuler3-image passed: 249 recipes;
  the exact PN list is in BUILD-RECIPES.txt.
- Dependency graphs for neutrino-plugin-softcams-oscam-emu and
  neutrino-plugin-softcams-cccam both passed.
- NI GUI/HAL patch application checks and locked source revisions passed.
- Local file resources and LF line endings in owned layers passed checks.
- Python syntax and shell syntax passed for setup/env and softcam scripts.
- Three gitlink revisions match sources.lock.json and checked-out submodules.
  Their object stores were repacked and no longer borrow the old checkout.
- Only the formuler3 machine config, its driver recipe and Linux 4.10.6
  are retained in meta-formuler. Metadata source is recorded in PROVENANCE.md.
- No supplied Wi-Fi PSK is present in tracked project files.

Limits: a complete firmware compile from this relocated standalone tree has
not been run. The existing successful receiver GUI boot belongs to the earlier
OpenPLi-based workspace. Softcam compilation/runtime and redistributed CCcam
binary compatibility have not been tested. A verbose `bitbake -n` was stopped
after graph validation; it is not reported as a passed compile or completed dry run.
The optional softcams are not part of the default image and do not auto-start.

Initial standalone preparation used local automation commits. Public publication
uses the connected sid8796 account; local draft history remains available in bundles.


## Fork integration — 2026-11-03

The first F3 GUI/HAL commits are published in the user forks on formuler3.
Core source URLs and SHA pins now refer to those commits. Two external model
patches were removed so the same changes cannot be applied twice. Seven generic
GUI compatibility patches remain in the layer; HAL has no external patch.

Byte comparison passed for all 2982 tracked GUI files and all 239 tracked HAL
files against the exact patched sources used for the receiver build. This
checks the composite sources, not only patch application. Hardware switches
and feature restrictions remain as before.

After integration BitBake parsed 2830 recipes (2828 cached, 2 parsed), 4757 targets,
706 skipped, 0 masked, 0 errors. The image dependency graph passed again.
Source-check script validates actual checkout HEAD against the locked revision.
The full standalone image was not recompiled in this step.
