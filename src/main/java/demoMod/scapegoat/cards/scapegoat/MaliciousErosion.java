package demoMod.scapegoat.cards.scapegoat;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.actions.MaliciousErosionAction;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.interfaces.BetaArtCard;

import java.util.function.Supplier;

public class MaliciousErosion extends CustomCard implements BetaArtCard {
    public static final String ID = Scapegoat.makeID("MaliciousErosion");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/maliciousErosion.png";
    private static final String BETA_ART_PATH = Scapegoat.getResourcePath("cards/betaArt/maliciousErosion.png");

    private static final CardStrings cardStrings;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public MaliciousErosion() {
        super(ID, NAME, Scapegoat.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.SCAPEGOAT, RARITY, TARGET);
        this.exhaust = true;
        Scapegoat.loadJokeCardImage(this, BETA_ART_PATH);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.exhaust = false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MaliciousErosionAction());
    }

    @Override
    public String getCardID() {
        return cardID;
    }

    @Override
    public String getTextureImg() {
        return this.textureImg;
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
