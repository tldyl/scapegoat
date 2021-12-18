package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.powers.BulletPower;

public class OldShell extends CustomRelic {
    public static final String ID = Scapegoat.makeID("OldShell");
    public static final String IMG_PATH = "relics/oldShell.png";
    public static final String IMG_OUTLINE_PATH = "relics/oldShellOutline.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));
    private static final Texture OUTLINE_IMG = new Texture(Scapegoat.getResourcePath(IMG_OUTLINE_PATH));

    public OldShell() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            addToBot(new RelicAboveCreatureAction(monster, this));
            addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new BulletPower(monster, 1)));
        }
    }
}
