package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.scapegoat.Scapegoat;

public class GhostMask extends CustomRelic {
    public static final String ID = Scapegoat.makeID("GhostMask");
    public static final String IMG_PATH = "relics/ghostMask.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));

    public GhostMask() {
        super(ID, IMG, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        AbstractPlayer p = AbstractDungeon.player;
        if (damageAmount > 0 && info.owner == p) {
            this.flash();
            addToTop(new HealAction(p, p, 1));
            addToTop(new RelicAboveCreatureAction(p, this));
        }
        return damageAmount;
    }
}
