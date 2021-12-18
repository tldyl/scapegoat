package demoMod.scapegoat.dynamicVars;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import demoMod.scapegoat.interfaces.AbstractSecondaryMCard;

public class SecondaryM extends DynamicVariable {
    @Override
    public String key() {
        return "2M";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractSecondaryMCard) {
            return ((AbstractSecondaryMCard) card).isSecondaryMModified();
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractSecondaryMCard) {
            return ((AbstractSecondaryMCard) card).getValue();
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractSecondaryMCard) {
            return ((AbstractSecondaryMCard) card).getBaseValue();
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractSecondaryMCard) {
            return ((AbstractSecondaryMCard) card).isSecondaryMUpgraded();
        }
        return false;
    }
}
