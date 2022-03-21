package demoMod.scapegoat.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.MiracleEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.effects.WatcherEliteSanctityEffect;
import demoMod.scapegoat.monsters.WatcherElite;

import java.util.ArrayList;
import java.util.List;

public class WatcherComeOnStageAction extends AbstractGameAction {
    private WatcherElite watcherElite;
    private AbstractPlayer p;
    private float pStartX;
    private float watcherStartY;
    private boolean killShieldAndSpear = false;
    private boolean playerTalk = false;
    private WatcherEliteSanctityEffect watcherEliteSanctityEffect;

    public WatcherComeOnStageAction(WatcherElite watcherElite) {
        this.watcherElite = watcherElite;
        this.watcherEliteSanctityEffect = new WatcherEliteSanctityEffect(watcherElite);
        this.p = AbstractDungeon.player;
        this.pStartX = p.drawX;
        this.watcherStartY = watcherElite.drawY;
        this.duration = this.startDuration = 6.0F;
    }

    @Override
    public void update() {
        if (!Scapegoat.showWatcherComeOnStageAnimation) {
            List<AbstractMonster> toRemove = new ArrayList<>();
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster instanceof SpireSpear || monster instanceof SpireShield) {
                    toRemove.add(monster);
                    monster.dispose();
                }
            }
            AbstractDungeon.getMonsters().monsters.removeAll(toRemove);
            watcherElite.movePosition(watcherElite.drawX, Settings.HEIGHT * 0.33F);
            p.movePosition(Settings.WIDTH * 0.25F, p.drawY);
            isDone = true;
            return;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster instanceof SpireSpear || monster instanceof SpireShield) {
                monster.update();
            }
        }
        if (this.duration == this.startDuration) AbstractDungeon.actionManager.clearPostCombatActions();
        if (this.duration < 4.0F) {
            if (this.duration >= 3.0F) {
                if (!killShieldAndSpear) {
                    AbstractGameAction killAllMonsterAction = new AbstractGameAction() {
                        @Override
                        public void update() {
                            if (isDone) return;
                            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                                if (monster instanceof SpireSpear || monster instanceof SpireShield) {
                                    Scapegoat.addToTop(new InstantKillAction(monster));
                                }
                            }
                            isDone = true;
                        }
                    };
                    Scapegoat.addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            AbstractDungeon.actionManager.actions.clear();
                            isDone = true;
                        }
                    });
                    Scapegoat.addToTop(killAllMonsterAction);
                    for (int i=0;i<6;i++) {
                        Scapegoat.addToTop(new MyAttackDamageRandomEnemyAction(AttackEffect.LIGHTNING, 60));
                    }
                    killShieldAndSpear = true;
                }
            } else if (this.duration >= 2.5F) {
                p.movePosition(Interpolation.exp10Out.apply(this.pStartX, Settings.WIDTH * 0.25F, (3.0F - this.duration) / 0.5F), p.drawY);
            } else if (this.duration >= 0.5F) {
                if (!playerTalk) {
                    AbstractDungeon.effectList.add(new SpeechBubble(p.dialogX, p.dialogY, 2.0F, "~...~ ~???~ NL #r@!!!@", true));
                    AbstractDungeon.effectList.add(this.watcherEliteSanctityEffect);
                    playerTalk = true;
                }
                watcherElite.movePosition(watcherElite.drawX, Interpolation.linear.apply(watcherStartY, Settings.HEIGHT * 0.33F, (2.5F - this.duration) / 2.0F));
            }
        }
        tickDuration();
        if (this.isDone) {
            this.watcherEliteSanctityEffect.stop();
            MiracleEffect effect = new MiracleEffect();
            ReflectionHacks.setPrivate(effect, MiracleEffect.class, "x", watcherElite.hb.cX - (float)ImageMaster.CRYSTAL_IMPACT.packedWidth / 2.0F);
            ReflectionHacks.setPrivate(effect, MiracleEffect.class, "y", watcherElite.hb.cY - (float)ImageMaster.CRYSTAL_IMPACT.packedHeight / 2.0F);
            AbstractDungeon.effectList.add(effect);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
        }
    }
}
