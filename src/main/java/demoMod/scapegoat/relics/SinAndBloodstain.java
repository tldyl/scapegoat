package demoMod.scapegoat.relics;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.KeywordStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.cards.status.Cost;
import demoMod.scapegoat.powers.BloodstainPower;
import demoMod.scapegoat.powers.SinPower;

import java.util.HashMap;
import java.util.Map;

public class SinAndBloodstain extends AbstractBlight implements CustomSavable<Map<String, Integer>> {
    public static final String ID = Scapegoat.makeID("SinAndBloodstain");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String KEY_WORD_SIN_NAME = BaseMod.getKeywordTitle("sin");
    public static final String KEY_WORD_BLOODSTAIN_NAME = BaseMod.getKeywordTitle("bloodstain");
    public static final String KEY_WORD_SIN_BODY = BaseMod.getKeywordDescription("sin");
    public static final String KEY_WORD_BLOODSTAIN_BODY = BaseMod.getKeywordDescription("bloodstain");
    public int sinThisGame = 0;
    public int bloodstainThisGame = 0;
    public int sinCounter = 0;
    public int bloodstainCounter = 0;
    private AbstractPlayer p;

    public SinAndBloodstain() {
        super(ID, NAME, DESCRIPTION, "durian.png", true);
        String IMG_PATH = "scapegoatImages/relics/sinAndBloodstain.png";
        this.img = new Texture(IMG_PATH);
        this.outlineImg = new Texture(IMG_PATH);
        this.increment = 0;
        this.p = AbstractDungeon.player;
        BaseMod.addSaveField(ID, this);
        this.tips.add(new PowerTip(KEY_WORD_SIN_NAME, KEY_WORD_SIN_BODY));
        this.tips.add(new PowerTip(KEY_WORD_BLOODSTAIN_NAME, KEY_WORD_BLOODSTAIN_BODY));
    }

    @Override
    public void onVictory() {
        if (this.sinCounter > 0) this.sinCounter--;
        if (this.bloodstainCounter > 0) this.bloodstainCounter--;
    }

    @Override
    public void atBattleStart() {
        if (this.sinCounter > 0 || this.bloodstainCounter > 0) {
            this.flash();
        }
        if (this.sinCounter > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SinPower(p, this.sinCounter)));
            AbstractRoom room = new MonsterRoomElite();
            boolean isFinalActAvailableTmp = Settings.isFinalActAvailable;
            boolean hasEmeraldKey = AbstractDungeon.getCurrMapNode().hasEmeraldKey;
            Settings.isFinalActAvailable = true;
            AbstractDungeon.getCurrMapNode().hasEmeraldKey = true;
            room.monsters = AbstractDungeon.getCurrRoom().monsters;
            room.applyEmeraldEliteBuff();
            Settings.isFinalActAvailable = isFinalActAvailableTmp;
            AbstractDungeon.getCurrMapNode().hasEmeraldKey = hasEmeraldKey;
        }
        if (this.bloodstainCounter > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BloodstainPower(p, this.bloodstainCounter)));
            float roll = AbstractDungeon.cardRandomRng.random(1.0F);
            if (roll < 0.33F) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VulnerablePower(p, 1, true)));
            } else if (roll < 0.67F) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WeakPower(p, 1, true)));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FrailPower(p, 1, true)));
            }
            roll = AbstractDungeon.cardRandomRng.random(1.0F);
            if (roll < 0.33F) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), 2, true, true));
            } else if (roll < 0.67F) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 2, true, true));
            } else {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Cost(), 2, true, true));
            }
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        float offsetX = ReflectionHacks.getPrivateStatic(AbstractBlight.class, "offsetX");
        if (inTopPanel) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.sinCounter), offsetX + this.currentX + 30.0F * Settings.scale, this.currentY + 21.0F * Settings.scale, Color.PURPLE);
        } else {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.sinCounter), this.currentX + 30.0F * Settings.scale, this.currentY + 21.0F * Settings.scale, Color.PURPLE);
        }
        if (inTopPanel) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.bloodstainCounter), offsetX + this.currentX + 30.0F * Settings.scale, this.currentY - 21.0F * Settings.scale, Color.RED);
        } else {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.bloodstainCounter), this.currentX + 30.0F * Settings.scale, this.currentY - 21.0F * Settings.scale, Color.RED);
        }
    }

    @Override
    public Map<String, Integer> onSave() {
        Map<String, Integer> ret = new HashMap<>();
        ret.put("sinCounter", this.sinCounter);
        ret.put("bloodstainCounter", this.bloodstainCounter);
        ret.put("sinThisGame", this.sinThisGame);
        ret.put("bloodstainThisGame", this.bloodstainThisGame);
        if (!p.blights.contains(this)) {
            Scapegoat.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    BaseMod.getSaveFields().remove(ID);
                    isDone = true;
                }
            });
        }
        return ret;
    }

    @Override
    public void onLoad(Map<String, Integer> data) {
        if (data != null) {
            this.sinCounter = data.getOrDefault("sinCounter", 0);
            this.bloodstainCounter = data.getOrDefault("bloodstainCounter", 0);
            this.sinThisGame = data.getOrDefault("sinThisGame", 0);
            this.bloodstainThisGame = data.getOrDefault("bloodstainThisGame", 0);
        }
    }

    static {
        RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
        NAME = relicStrings.NAME;
        DESCRIPTION = relicStrings.DESCRIPTIONS[0];
    }
}
