package demoMod.scapegoat.patches.events.beyond;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;

public class SensoryStonePatch {
    public static final String ID = Scapegoat.makeID("SensoryStone");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = SensoryStone.class,
            method = "getRandomMemory"
    )
    public static class PatchGetRandomMemory {
        public static SpireReturn<Void> Prefix(SensoryStone event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                return SpireReturn.Return(null);
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
