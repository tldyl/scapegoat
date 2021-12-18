package demoMod.scapegoat.monsters;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.effects.BlackFogEffect;

public class CityVampire extends AbstractMonster {
    public static final String ID = Scapegoat.makeID("CityVampire");
    public static final String NAME;
    private static final String[] MOVES;
    private static final String[] DIALOG;
    private boolean talky;
    private boolean firstTurn = true;

    public CityVampire(float offsetX, float offsetY, boolean talk) {
        super(NAME, ID, 50, -8.0F, 10.0F, 180.0F, 343.0F, Scapegoat.getResourcePath("monsters/cityVampire.png"), offsetX, offsetY);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(50, 56);
        } else {
            this.setHp(48, 54);
        }
        this.dialogX = -50.0F * Settings.scale;
        this.dialogY = 50.0F * Settings.scale;
        if (AbstractDungeon.ascensionLevel < 17) {
            this.damage.add(new DamageInfo(this, 6));
        } else {
            this.damage.add(new DamageInfo(this, 8));
        }
        this.damage.add(new DamageInfo(this, 7));
        this.talky = talk;
    }

    @Override
    public void usePreBattleAction() {
        if (this.talky) {
            Scapegoat.addToBot(new TalkAction(this, DIALOG[0]));
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 0:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new VFXAction(new BiteEffect(p.drawX, p.drawY, Color.WHITE.cpy())));
                addToBot(new DamageAction(p, this.damage.get(0)));
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        if (p.lastDamageTaken > 0) {
                            addToTop(new HealAction(CityVampire.this, CityVampire.this, p.lastDamageTaken));
                        }
                        isDone = true;
                    }
                });
                break;
            case 1:
                if (AbstractDungeon.ascensionLevel < 17) {
                    addToBot(new ApplyPowerAction(p, this, new FrailPower(p, 1, true)));
                    addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
                } else {
                    addToBot(new ApplyPowerAction(p, this, new FrailPower(p, 2, true)));
                    addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 2)));
                }
                break;
            case 2:
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new VFXAction(new ClawEffect(p.hb.cX, p.hb.cY, Color.CYAN, Color.WHITE), 0.1F));
                addToBot(new DamageAction(p, this.damage.get(1)));
                addToBot(new VFXAction(new ClawEffect(p.hb.cX, p.hb.cY, Color.CYAN, Color.WHITE), 0.1F));
                addToBot(new DamageAction(p, this.damage.get(1)));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int aiRng) {
        if (firstTurn) {
            setMove(MOVES[0], (byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base);
            firstTurn = false;
            return;
        }
        if (aiRng < 33) {
            setMove((byte) 1, Intent.DEBUFF);
        } else if (aiRng < 66) {
            setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base, 2, true);
        } else {
            setMove(MOVES[0], (byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base);
        }
    }

    @Override
    public void die() {
        super.die();
        this.useShakeAnimation(5.0F);
        Scapegoat.addToBot(new VFXAction(new BlackFogEffect(this.hb.cX, this.hb.cY)));
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
