package demoMod.scapegoat.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ProceedButtonPatch {
    @SpirePatch(
            clz = ProceedButton.class,
            method = "update"
    )
    public static class PatchUpdate {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(ProceedButton button) {
            System.out.println(AbstractDungeon.getCurrRoom().event.combatTime);
            if (AbstractDungeon.getCurrRoom().event instanceof Vampires && AbstractDungeon.getCurrRoom().event.combatTime) {
                button.show();
                AbstractDungeon.dungeonMapScreen.open(false);
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ProceedButton.class, "hide");
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
