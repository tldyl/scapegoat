package demoMod.scapegoat.relics;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.PostPowerApplySubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import demoMod.scapegoat.Scapegoat;

public class AppleCandy extends CustomRelic implements PostPowerApplySubscriber {
    public static final String ID = Scapegoat.makeID("AppleCandy");
    public static final String IMG_PATH = "relics/appleCandy.png";
    private static final Texture IMG = new Texture(Scapegoat.getResourcePath(IMG_PATH));

    public AppleCandy() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.FLAT);
        BaseMod.subscribe(this);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        AbstractPlayer p = AbstractDungeon.player;
        if (!p.relics.contains(this)) {
            Scapegoat.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    BaseMod.unsubscribe(AppleCandy.this);
                    isDone = true;
                }
            });
        } else {
            Scapegoat.addToBot(new AbstractGameAction() {
                private float duration = 0.3F;

                @Override
                public void update() {
                    duration -= Gdx.graphics.getDeltaTime();
                    if (duration <= 0 && !isDone) {
                        if (power.ID.equals(VulnerablePower.POWER_ID) && power.owner instanceof AbstractPlayer && power.owner.hasPower(VulnerablePower.POWER_ID)) {
                            AppleCandy.this.flash();
                            VulnerablePower vulnerablePower = (VulnerablePower) power;
                            boolean justApplied = ReflectionHacks.getPrivate(vulnerablePower, VulnerablePower.class, "justApplied");
                            if (justApplied) {
                                Scapegoat.addToTop(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, 5)));
                            } else {
                                Scapegoat.addToTop(new GainBlockAction(p, 5));
                            }
                            Scapegoat.addToTop(new RelicAboveCreatureAction(power.owner, AppleCandy.this));
                        }
                        isDone = true;
                    }
                }
            });
        }
    }
}
