package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.scapegoat.Scapegoat;

public class BloodVial extends CustomRelic {
    public static final String ID = Scapegoat.makeID("BloodVial");
    public static final String IMG_PATH = "images/relics/blood_vial.png";
    private static final Texture IMG = new Texture(IMG_PATH);

    public BloodVial() {
        super(ID, IMG, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onVictory() {
        this.flash();
        AbstractDungeon.player.heal(2);
    }
}
