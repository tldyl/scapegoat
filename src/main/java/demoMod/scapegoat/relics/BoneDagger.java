package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.interfaces.PostIncreaseSinSubscriber;

public class BoneDagger extends CustomRelic implements PostIncreaseSinSubscriber {
    public static final String ID = Scapegoat.makeID("BoneDagger");
    public static final String IMG_PATH = "relics/boneDagger.png";
    public static final String IMG_OUTLINE_PATH = "relics/boneDaggerOutline.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));
    private static final Texture OUTLINE_IMG = new Texture(Scapegoat.getResourcePath(IMG_OUTLINE_PATH));

    public BoneDagger() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onIncreaseSin(int amount) {
        this.flash();
        AbstractDungeon.player.gainGold(amount * 60);
    }
}
