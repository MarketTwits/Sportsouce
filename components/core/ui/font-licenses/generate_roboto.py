"""Generate bundled static Roboto faces. Requires fonttools; takes the original variable TTF."""
import hashlib
import sys
from pathlib import Path

from fontTools.ttLib import TTFont
from fontTools.varLib.instancer import instantiateVariableFont

source = Path(sys.argv[1])
assert hashlib.sha256(source.read_bytes()).hexdigest() == "d7598e12c5dbef095ff8272cfc55da0250bd07fbdecbac8a530b9b277872a134"
output = Path(__file__).resolve().parent.parent / "src/commonMain/composeResources/font"
names = set()
for weight, name in [(300, "light"), (400, "regular"), (500, "medium"), (600, "semibold"),
                     (700, "bold"), (800, "extrabold"), (900, "black")]:
    face = instantiateVariableFont(TTFont(source), {"wght": weight, "wdth": 100}, updateFontNames=True)
    assert face["OS/2"].usWeightClass == weight
    assert all(ord(letter) in face.getBestCmap() for letter in "SportSauceРегистрацияЯё")
    names.add(face["name"].getDebugName(6))
    face.save(output / f"roboto_{name}.ttf")
assert len(names) == 7, "Static font faces must not share the Regular PostScript name"
print("Generated seven distinct static Roboto faces")
