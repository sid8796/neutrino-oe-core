# Validation — 2026-11-03

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
