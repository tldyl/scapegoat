package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import demoMod.scapegoat.Scapegoat;

public class MarkOfThePayback extends CustomRelic {
    public static final String ID = Scapegoat.makeID("MarkOfThePayback");
    public static final String IMG_PATH = "relics/markOfThePayback.png";
    public static final String IMG_OUTLINE_PATH = "relics/markOfThePaybackOutline.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));
    private static final Texture OUTLINE_IMG = new Texture(Scapegoat.getResourcePath(IMG_OUTLINE_PATH));

    public MarkOfThePayback() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
