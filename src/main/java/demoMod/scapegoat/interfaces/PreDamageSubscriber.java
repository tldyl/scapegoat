package demoMod.scapegoat.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface PreDamageSubscriber {
    void justAttacked(DamageInfo info);
}
