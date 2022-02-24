package demoMod.scapegoat.cards.tempCards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.relics.AbstractAbnormalityPages;

public class AbnormalityTemplateCard extends CustomCard {
    private AbstractAbnormalityPages relicRef;
    public Runnable followupAction = null;

    public AbnormalityTemplateCard(String id, String name, String description, AbstractAbnormalityPages relicRef) {
        super(id, name, Scapegoat.abnormalityCardImage(id), -2, description, AbstractCard.CardType.SKILL, AbstractCardEnum.SCAPEGOAT, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE);
        this.relicRef = relicRef;
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void onChoseThisOption() {
        relicRef.chooseCard[0] = relicRef.cardsToPreview.indexOf(this);
        ReflectionHacks.setPrivateFinal(relicRef, AbstractRelic.class, "name", relicRef.DESCRIPTIONS[relicRef.chooseCard[0] * relicRef.cardsToPreview.size() + 1]);
        relicRef.description = relicRef.DESCRIPTIONS[relicRef.chooseCard[0] * relicRef.cardsToPreview.size() + 2];
        relicRef.flavorText = relicRef.DESCRIPTIONS[relicRef.chooseCard[0] * relicRef.cardsToPreview.size() + 3];

        relicRef.tips.clear();
        relicRef.tips.add(new PowerTip(relicRef.DESCRIPTIONS[relicRef.chooseCard[0] * relicRef.cardsToPreview.size() + 1], relicRef.description));
        ReflectionHacks.privateMethod(AbstractRelic.class, "initializeTips").invoke(relicRef);
        if (followupAction != null) {
            followupAction.run();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        AbnormalityTemplateCard copied = new AbnormalityTemplateCard(this.cardID, this.name, this.rawDescription, this.relicRef);
        copied.followupAction = this.followupAction;
        return copied;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbnormalityTemplateCard) {
            AbnormalityTemplateCard card = (AbnormalityTemplateCard) obj;
            return this.uuid.equals(card.uuid);
        }
        return false;
    }
}
