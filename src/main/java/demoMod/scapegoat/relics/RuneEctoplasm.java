package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import demoMod.scapegoat.Scapegoat;

public class RuneEctoplasm extends CustomRelic {
    public static final String ID = Scapegoat.makeID("RuneEctoplasm");
    public static final String IMG_PATH = "relics/runeEctoplasm.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));

    public RuneEctoplasm() {
        super(ID, IMG, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new RelicAboveCreatureAction(p, this));
        addToBot(new ApplyPowerAction(p, p, new InvinciblePower(p, 10)));
    }
}
