package demoMod.scapegoat.dynamicVars;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class MiscNumber extends DynamicVariable {
    @Override
    public String key() {
        return "Misc";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        return card.misc;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card.misc;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
