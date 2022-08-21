package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.BetaArtCard;
import demoMod.scapegoat.powers.BulletPower;

import java.util.function.Supplier;

public class GunSwordDance extends CustomCard implements BetaArtCard {
    public static final String ID = Scapegoat.makeID("GunSwordDance");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/gunSwordDance.png";
    private static final String BETA_ART_PATH = Scapegoat.getResourcePath("cards/betaArt/gunSwordDance.png");

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = -1;

    public GunSwordDance() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.exhaust = true;
        this.cardsToPreview = new SerialBullet();
        this.baseMagicNumber = this.magicNumber = 2;
        Scapegoat.loadJokeCardImage(this, BETA_ART_PATH);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        if (this.upgraded) {
            ++effect;
        }

        if (effect > 0) {
            addToBot(new MakeTempCardInHandAction(new SerialBullet(), effect));
            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new BulletPower(monster, this.magicNumber)));
            }
        }
    }

    @Override
    public String getCardID() {
        return cardID;
    }

    @Override
    public String getTextureImg() {
        return textureImg;
    }

    @Override
    public String getBetaArtPath() {
        return BETA_ART_PATH;
    }

    @Override
    public Supplier<Texture> getDefaultTexture() {
        return () -> super.getPortraitImage();
    }

    @Override
    public Texture getPortraitImage() {
        return BetaArtCard.super.getPortraitImage();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
