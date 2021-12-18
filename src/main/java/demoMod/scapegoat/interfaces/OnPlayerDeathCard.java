package demoMod.scapegoat.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public interface OnPlayerDeathCard {
    boolean onPlayerDeath(AbstractPlayer p, DamageInfo info);
}
