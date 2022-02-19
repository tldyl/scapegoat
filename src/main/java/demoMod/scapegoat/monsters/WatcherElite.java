package demoMod.scapegoat.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.cards.purple.Alpha2;
import demoMod.scapegoat.stances.EnemyCalmStance;
import demoMod.scapegoat.stances.EnemyDivinityStance;
import demoMod.scapegoat.stances.EnemyWrathStance;

import java.io.IOException;

public class WatcherElite extends AbstractMonster {
    public static final String ID = Scapegoat.makeID("Watcher");
    public static final String NAME = CardCrawlGame.languagePack.getCharacterString("Watcher").NAMES[0];

    private Bone eyeBone;
    private int turn = 0;
    protected TextureAtlas eyeAtlas = null;
    protected Skeleton eyeSkeleton;
    public AnimationState eyeState;
    protected AnimationStateData eyeStateData;
    public AbstractStance stance = new NeutralStance();

    public WatcherElite() {
        super(NAME, ID, 320, 0.0F, -5.0F, 240.0F, 270.0F, null, 0.0F, Settings.HEIGHT * 0.95F);
        this.loadAnimation("images/characters/watcher/idle/skeleton.atlas", "images/characters/watcher/idle/skeleton.json", 1.0f);
        final AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.flipHorizontal = true;
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.7F);
        type = EnemyType.ELITE;

        loadEyeAnimation();
        this.eyeBone = this.skeleton.findBone("eye_anchor");
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.damage.add(new DamageInfo(this, 6));
            this.damage.add(new DamageInfo(this, 40));
            this.damage.add(new DamageInfo(this, 20));
        } else {
            this.damage.add(new DamageInfo(this, 5));
            this.damage.add(new DamageInfo(this, 30));
            this.damage.add(new DamageInfo(this, 18));
        }
    }

    private void loadEyeAnimation() {
        this.eyeAtlas = new TextureAtlas(Gdx.files.internal("images/characters/watcher/eye_anim/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.eyeAtlas);
        json.setScale(Settings.scale / 1.0F);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/characters/watcher/eye_anim/skeleton.json"));
        this.eyeSkeleton = new Skeleton(skeletonData);
        this.eyeSkeleton.setColor(Color.WHITE);
        this.eyeStateData = new AnimationStateData(skeletonData);
        this.eyeState = new AnimationState(this.eyeStateData);
        this.eyeStateData.setDefaultMix(0.2F);
        this.eyeState.setAnimation(0, "None", true);
    }

    public void onStanceChange(String newStance) {
        this.stance.onExitStance();
        switch (newStance) {
            case "Calm":
                this.eyeState.setAnimation(0, "Calm", true);
                this.stance = new EnemyCalmStance(this);
                break;
            case "Wrath":
                this.eyeState.setAnimation(0, "Wrath", true);
                this.stance = new EnemyWrathStance(this);
                break;
            case "Divinity":
                this.eyeState.setAnimation(0, "Divinity", true);
                this.stance = new EnemyDivinityStance(this);
                break;
            case "Neutral":
                this.eyeState.setAnimation(0, "None", true);
                this.stance = new NeutralStance();
                break;
            default:
                this.eyeState.setAnimation(0, "None", true);
                break;
        }
        this.stance.onEnterStance();
    }

    @Override
    public void update() {
        super.update();
        this.stance.update();
        this.eyeSkeleton.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render(SpriteBatch sb) {
        this.stance.render(sb);
        super.render(sb);
        this.eyeState.update(Gdx.graphics.getDeltaTime());
        this.eyeState.apply(this.eyeSkeleton);
        this.eyeSkeleton.updateWorldTransform();
        this.eyeSkeleton.setPosition(this.skeleton.getX() + this.eyeBone.getWorldX(), this.skeleton.getY() + this.eyeBone.getWorldY());
        this.eyeSkeleton.setColor(this.tint.color);
        this.eyeSkeleton.setFlip(this.flipHorizontal, this.flipVertical);

        sb.end();
        CardCrawlGame.psb.begin();
        sr.draw(CardCrawlGame.psb, this.eyeSkeleton);
        CardCrawlGame.psb.end();
        sb.begin();
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                onStanceChange("Divinity");
                int multi = 5;
                if (AbstractDungeon.ascensionLevel >= 18) multi++;
                addToBot(new AnimateFastAttackAction(this));
                for (int i=0;i<multi;i++) {
                    addToBot(new DamageAction(p, this.damage.get(0)));
                    addToBot(new VFXAction(new LightningEffect(p.drawX, p.drawY), 0.0F));
                    addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
                }
                addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, 2, true)));
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 2, true)));
                if (p.orbs.size() > 0) {
                    if (AbstractDungeon.miscRng.randomBoolean()) {
                        addToBot(new ApplyPowerAction(p, this, new FocusPower(p, -1)));
                    } else {
                        addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -1)));
                    }
                } else {
                    addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -1)));
                }
                boolean shuffleInto = AbstractDungeon.ascensionLevel < 18;
                addToBot(new MakeTempCardInDrawPileAction(new Alpha2(), 2, shuffleInto, true));
                break;
            case 2:
                onStanceChange("Wrath");
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new GainBlockAction(this, p.lastDamageTaken));
                addToTop(new VFXAction(new WallopEffect(p.lastDamageTaken, p.hb.cX, p.hb.cY)));
                addToTop(new AnimateFastAttackAction(this));
                break;
            case 3:
                onStanceChange("Calm");
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    addToBot(new GainBlockAction(this, 30));
                } else {
                    addToBot(new GainBlockAction(this, 99));
                }
                addToBot(new ApplyPowerAction(p, this, new FrailPower(p, 1, true)));
                break;
            case 4:
                onStanceChange("Wrath");
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new GainBlockAction(this, p.lastDamageTaken));
                addToTop(new VFXAction(new WallopEffect(p.lastDamageTaken, p.hb.cX, p.hb.cY)));
                addToTop(new AnimateFastAttackAction(this));
                addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, 1, true)));
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true)));
                break;
            case 5:
                onStanceChange("Calm");
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    addToBot(new GainBlockAction(this, 30));
                } else {
                    addToBot(new GainBlockAction(this, 99));
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 4)));
                break;
            case 6:
                onStanceChange("Divinity");
                multi = 5;
                if (AbstractDungeon.ascensionLevel >= 18) multi++;
                addToBot(new AnimateFastAttackAction(this));
                for (int i=0;i<multi;i++) {
                    addToBot(new DamageAction(p, this.damage.get(0)));
                    addToBot(new VFXAction(new LightningEffect(p.drawX, p.drawY), 0.0F));
                    addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
                }
                if (p.orbs.size() > 0) {
                    if (AbstractDungeon.miscRng.randomBoolean()) {
                        addToBot(new ApplyPowerAction(p, this, new FocusPower(p, -1)));
                    } else {
                        addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -1)));
                    }
                } else {
                    addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -1)));
                }
                shuffleInto = AbstractDungeon.ascensionLevel < 18;
                addToBot(new MakeTempCardInDrawPileAction(new Alpha2(), 2, shuffleInto, true));
                break;
        }
        getMove(0);
    }

    @Override
    protected void getMove(int aiRng) {
        turn++;
        switch (turn) {
            case 1:
                if (AbstractDungeon.ascensionLevel >= 18) {
                    setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 6, true);
                } else {
                    setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 5, true);
                }
                break;
            case 2:
                setMove((byte) 2, Intent.ATTACK_DEFEND, this.damage.get(1).base);
                break;
            case 3:
                setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(2).base);
                break;
            case 4:
                setMove((byte) 4, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                break;
            case 5:
                setMove((byte) 5, Intent.ATTACK_BUFF, this.damage.get(2).base);
                break;
            case 6:
                if (AbstractDungeon.ascensionLevel >= 18) {
                    setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 6, true);
                } else {
                    setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 5, true);
                }
                turn = 2;
                break;
        }
    }

    public void movePosition(float x, float y) {
        this.drawX = x;
        this.drawY = y;
        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 170.0F * Settings.scale;
        this.animX = 0.0F;
        this.animY = 0.0F;
        this.refreshHitboxLocation();
    }

    @Override
    public void die(boolean triggerRelics) {
        this.state.setTimeScale(0.1F);
        this.stance.onExitStance();
        this.useShakeAnimation(5.0F);
        Scapegoat.isReskinUnlocked = true;
        Scapegoat.reskinSaveData.setBool("isReskinUnlocked", Scapegoat.isReskinUnlocked);
        try {
            Scapegoat.reskinSaveData.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.die(triggerRelics);
    }
}
