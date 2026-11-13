# Metadata provenance

Vendor metadata was selected from OpenPLi/openpli-oe-core at
47e156fabcc3555dde753a111ab06740824f2e2d, based on the verified Formuler F3
image dependency graph. Upstream recipe/file copyright and license notices
are retained. Generic recipes/classes now reside in meta-neutrino; the only
vendor BSP is meta-formuler for Formuler F1, F3, F4 and F4 Turbo with Linux
4.10.6. The machine configurations and driver recipes for the additional models
come from the same OpenPLi revision.
NI GUI/HAL remain pinned external source repositories, suitable for later forks.
The submodules retain the exact revisions of the successful original build.


## Formuler F3 fork commits

GUI: sid8796/ni-neutrino, formuler3, 5c6dc790522e24e729d46e9a1d91f7468f6e0748;
parent 0b86f12c6673a84040a12bf96f5bc5ae9d822a54.
HAL: sid8796/ni-libstb-hal, formuler3, ad730ed072cd0b836a2a9efcadb4c52ff6dd7aa0;
parent 03296a3a88db7a890669d493db5571f690acd4e6.
These are paired model-registration/profile commits analogous in scope to the
initial model entries of other receivers. They retain the upstream history.
All tracked GUI/HAL files, after remaining layer patches, byte-match the sources
used for the receiver image (2982 GUI files and 239 HAL files).
