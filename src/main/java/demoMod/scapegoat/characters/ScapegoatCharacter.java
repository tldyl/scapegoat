package demoMod.scapegoat.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.animations.GifAnimation;
import demoMod.scapegoat.cards.scapegoat.Defend_Scapegoat;
import demoMod.scapegoat.cards.scapegoat.Preparation;
import demoMod.scapegoat.cards.scapegoat.Ritual;
import demoMod.scapegoat.cards.scapegoat.Strike_Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.enums.AbstractPlayerEnum;
import demoMod.scapegoat.patches.CharacterSelectScreenPatch;
import demoMod.scapegoat.relics.BronzePipe;

import java.util.ArrayList;

public class ScapegoatCharacter extends CustomPlayer {
    private static final CharacterStrings charStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    private GifAnimation gifAnimation;
    private FrameBuffer frameBuffer;

    private static final String[] orbTextures = {
            "scapegoatImages/char/orb/layer1.png",
            "scapegoatImages/char/orb/layer2.png",
            "scapegoatImages/char/orb/layer3.png",
            /*
            "scapegoatImages/char/orb/layer4.png",
            "scapegoatImages/char/orb/layer5.png",
            "scapegoatImages/char/orb/layer6.png",
            "scapegoatImages/char/orb/layer1d.png",
            "scapegoatImages/char/orb/layer2d.png",
            "scapegoatImages/char/orb/layer3d.png",
            "scapegoatImages/char/orb/layer4d.png",
            "scapegoatImages/char/orb/layer5d.png"
            */
    };

    public ScapegoatCharacter(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, "scapegoatImages/char/orb/vfx.png", null, (String) null);
        this.initializeClass(Scapegoat.getResourcePath("char/character.png"), Scapegoat.getResourcePath("char/shoulder2.png"), Scapegoat.getResourcePath("char/shoulder.png"), Scapegoat.getResourcePath("char/corpse.png"), this.getLoadout(), 0.0F, -10F, 258.0F, 282.0F, new EnergyManager(3));
        if (ModHelper.enabledMods.size() > 0 && (ModHelper.isModEnabled("Diverse") || ModHelper.isModEnabled("Chimera") || ModHelper.isModEnabled("Blue Cards"))) {
            this.masterMaxOrbs = 1;
        }
        this.loadAnimation(Scapegoat.getResourcePath("char/tizui.atlas"), Scapegoat.getResourcePath("char/tizui37.json"), 6.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTimeScale(0.6F);
        if (CharacterSelectScreenPatch.reskinIndex == 1) {
            this.gifAnimation = new GifAnimation(Scapegoat.getResourcePath("char/character_skin.gif"));
        }
        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        this.img = frameBuffer.getColorBufferTexture();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (CharacterSelectScreenPatch.reskinIndex == 0) {
            super.render(sb);
            return;
        }
        this.stance.render(sb);
        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !this.isDead) {
            this.renderHealth(sb);
            for (AbstractOrb o : this.orbs) {
                o.render(sb);
            }
        }
        if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
            if (this.img != this.corpseImg) {
                sb.end();
                frameBuffer.begin();
                sb.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                sb.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                this.gifAnimation.render(sb, this.drawX - this.hb.width * 0.5625F + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY - 24.0F * Settings.scale, this.hb.width * 1.125F, this.hb.height * 1.125F);
                sb.end();
                frameBuffer.end();
                sb.begin();

                sb.setColor(Color.WHITE);
                sb.draw(this.img, this.flipHorizontal ? (this.drawX + this.animX) * 2.0F - this.img.getWidth() : 0, this.flipVertical ? (this.drawY + this.animY + AbstractDungeon.sceneOffsetY - 24.0F * Settings.scale) * 2.0F - this.img.getHeight() : 0,
                        this.img.getWidth(), this.img.getHeight(),
                        0, 0,
                        this.img.getWidth(), this.img.getHeight(),
                        this.flipHorizontal, !this.flipVertical);

                this.hb.render(sb);
                this.healthHb.render(sb);
            } else {
                sb.setColor(Color.WHITE);
                sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
            }
        } else {
            this.renderShoulderImg(sb);
        }
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        int index = -1;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            switch (monster.id) {
                case "ruina:Gunman":
                    index = 3;
                    break;
                case "ruina:NightmareWolf":
                    index = 4;
                    break;
                case "ruina:ParadiseLost":
                    index = 5;
                    break;
                case "ruina:Twilight":
                    index = 6;
                    break;
                case "ruina:JesterOfNihil":
                    index = 7;
                    break;
                case "ruina:QueenBee":
                    index = 8;
                    break;
            }
            if (index > 0) break;
        }
        if (index > 0) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, charStrings.TEXT[index], 2.0F, 2.0F));
        }
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(Strike_Scapegoat.ID);
        ret.add(Strike_Scapegoat.ID);
        ret.add(Strike_Scapegoat.ID);
        ret.add(Strike_Scapegoat.ID);
        ret.add(Defend_Scapegoat.ID);
        ret.add(Defend_Scapegoat.ID);
        ret.add(Defend_Scapegoat.ID);
        ret.add(Defend_Scapegoat.ID);
        ret.add(Preparation.ID);
        ret.add(Ritual.ID);
        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(BronzePipe.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION, 78, 78, 0, 99, 5, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.SCAPEGOAT;
    }

    @Override
    public Color getCardRenderColor() {
        return Scapegoat.mainGoatColor;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Preparation();
    }

    @Override
    public Color getCardTrailColor() {
        return Scapegoat.mainGoatColor.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 7;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.play("ATTACK_HEAVY");
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new ScapegoatCharacter("Scapegoat", AbstractPlayerEnum.SCAPEGOAT);
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.FIREBRICK;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("Scapegoat");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];
    }
}
