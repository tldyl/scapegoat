package demoMod.scapegoat.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import demoMod.scapegoat.Scapegoat;

public class ScapegoatBirthrightRelic extends CustomRelic {
    public static final String ID = Scapegoat.makeID("ScapegoatBirthrightRelic");

    public ScapegoatBirthrightRelic() {
        super(ID, "", RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onTrigger() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasRelic("IsaacExt:Birthright")) {
            AbstractRelic relic = p.getRelic("IsaacExt:Birthright");
            relic.flash();
            addToBot(new RelicAboveCreatureAction(p, relic));
        }
    }
}
