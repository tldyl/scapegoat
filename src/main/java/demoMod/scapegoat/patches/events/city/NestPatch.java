package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.Nest;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class NestPatch {
    public static final String ID = Scapegoat.makeID("Nest");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = Nest.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Nest event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, Nest.class, "screenNum");
                int goldGain = ReflectionHacks.getPrivate(event, Nest.class, "goldGain");
                switch (screenNum) {
                    case 0:
                        event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        event.imageEventText.updateDialogOption(0, OPTIONS[2] + goldGain + OPTIONS[3]);
                        event.imageEventText.setDialogOption(OPTIONS[0] + 6 + OPTIONS[1], new RitualDagger());
                        int amount = 1;
                        if (AbstractDungeon.ascensionLevel >= 15) amount++;
                        event.imageEventText.setDialogOption(String.format(OPTIONS[6], amount, amount));
                        UnlockTracker.markCardAsSeen("RitualDagger");
                        ReflectionHacks.setPrivate(event, Nest.class, "screenNum", 1);
                        return SpireReturn.Return(null);
                    case 1:
                        switch (buttonPressed) {
                            case 2:
                                AbstractEvent.logMetricGainGold("Nest", "Stole From Cult", 200);
                                event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                ReflectionHacks.setPrivate(event, Nest.class, "screenNum", 2);
                                AbstractDungeon.effectList.add(new RainingGoldEffect(200));
                                AbstractDungeon.player.gainGold(200);
                                SinAndBloodstainManager.increaseSin(1);
                                SinAndBloodstainManager.increaseBloodstain(1);
                                event.imageEventText.updateDialogOption(0, OPTIONS[4]);
                                event.imageEventText.clearRemainingOptions();
                                return SpireReturn.Return(null);
                        }
                }
            }
            return SpireReturn.Continue();
        }
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
