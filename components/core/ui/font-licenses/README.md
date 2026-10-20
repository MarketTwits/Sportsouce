# Roboto

Bundled static TrueType faces include Cyrillic, weights 300/400/500/600/700/800/900,
normal width (100), upright. They replace the unused Nunito resources.

Source: [Google Fonts Roboto variable font](https://github.com/google/fonts/blob/main/ofl/roboto/Roboto%5Bwdth%2Cwght%5D.ttf), retrieved 2026-10-15.
Source variable-font SHA-256: `d7598e12c5dbef095ff8272cfc55da0250bd07fbdecbac8a530b9b277872a134`.

Generated with fontTools `varLib.instancer.instantiateVariableFont(font, {"wght": weight, "wdth": 100}, updateFontNames=True)`.
Regenerate with `python generate_roboto.py /path/to/Roboto-Variable.ttf` (requires fonttools).
The script verifies the source hash, weights, Latin/Cyrillic coverage and distinct PostScript names.
Do not omit `updateFontNames`: otherwise every static face retains `Roboto-Regular`, including bold.
The original copyright notices are retained in the font metadata. Licensed under
SIL Open Font License 1.1; see [roboto_ofl.txt](../src/commonMain/composeResources/files/licenses/roboto_ofl.txt).
The license is a Compose resource, so it is also included in distributed applications.
