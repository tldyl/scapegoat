package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import demoMod.scapegoat.Scapegoat;

public class BurningEye extends CustomRelic {
    public static final String ID = Scapegoat.makeID("BurningEye");
    public static final String IMG_PATH = "relics/burningEye.png";
    public static final String IMG_OUTLINE_PATH = "relics/burningEyeOutline.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));
    private static final Texture OUTLINE_IMG = new Texture(Scapegoat.getResourcePath(IMG_OUTLINE_PATH));

    public BurningEye() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
