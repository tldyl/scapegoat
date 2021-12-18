package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.powers.BulletPower;

public class DarkBullet extends CustomRelic {
    public static final String ID = Scapegoat.makeID("DarkBullet");
    public static final String IMG_PATH = "relics/darkBullet.png";
    public static final String IMG_OUTLINE_PATH = "relics/darkBulletOutline.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));
    private static final Texture OUTLINE_IMG = new Texture(Scapegoat.getResourcePath(IMG_OUTLINE_PATH));

    public DarkBullet() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.hasPower(BulletPower.POWER_ID)) {
            AbstractMonster target = AbstractDungeon.getRandomMonster();
            if (target == null) return;
            this.flash();
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDeadOrEscaped() && monster.currentHealth < target.currentHealth) {
                    target = monster;
                }
            }
            addToBot(new RelicAboveCreatureAction(target, this));
            addToBot(new ApplyPowerAction(target, m, new BulletPower(target, m.getPower(BulletPower.POWER_ID).amount)));
        }
    }
}
