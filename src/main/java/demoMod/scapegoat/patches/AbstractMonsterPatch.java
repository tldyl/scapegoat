package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.actions.WatcherComeOnStageAction;
import demoMod.scapegoat.monsters.WatcherElite;
import demoMod.scapegoat.powers.SpiritLinkPower;
import demoMod.scapegoat.relics.SinAndBloodstain;

public class AbstractMonsterPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class PatchDamage {
        public static void Prefix(AbstractMonster m, DamageInfo info) {
            if (info.output > 0 && m.hasPower(IntangiblePower.POWER_ID)) {
                info.output = 1;
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = {
                    boolean.class
            }
    )
    public static class PatchDie {
        public static void Prefix(AbstractMonster m, boolean triggerRelics) {
            if (!m.isDying && m.currentHealth <= 0) {
                if (m.hasPower(SpiritLinkPower.POWER_ID)) {
                    AbstractPower power = m.getPower(SpiritLinkPower.POWER_ID);
                    power.onDeath();
                }
            }
        }
    }

    @SpirePatch(
            clz = SpireShield.class,
            method = "usePreBattleAction"
    )
    public static class PatchUsePreBattleActionForSpireShield {
        public static SpireReturn<Void> Prefix(SpireShield shield) {
            if (AbstractDungeon.player.hasBlight(SinAndBloodstain.ID)) {
                SinAndBloodstain sinAndBloodstain = (SinAndBloodstain) AbstractDungeon.player.getBlight(SinAndBloodstain.ID);
                if (sinAndBloodstain.sinThisGame + sinAndBloodstain.bloodstainThisGame >= 3) {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            WatcherElite watcher = new WatcherElite();
                            Scapegoat.addToTop(new WatcherComeOnStageAction(watcher));
                            Scapegoat.addToTop(new SpawnMonsterAction(watcher, false));
                            addToTop(new AbstractGameAction() {
                                @Override
                                public void update() {
                                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                                    isDone = true;
                                }
                            });
                            isDone = true;
                        }
                    });

                    return SpireReturn.Return(null);
                }
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, shield, new SurroundedPower(AbstractDungeon.player)));

            if (AbstractDungeon.ascensionLevel >= 18) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(shield, shield, new ArtifactPower(shield, 2)));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(shield, shield, new ArtifactPower(shield, 1)));
            }
            return SpireReturn.Return(null);
        }
    }
}
