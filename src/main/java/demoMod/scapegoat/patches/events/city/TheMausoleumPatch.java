package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.TheMausoleum;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class TheMausoleumPatch {
    public static final String ID = Scapegoat.makeID("The Mausoleum");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = TheMausoleum.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(TheMausoleum event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.optionList.set(0, new LargeDialogOptionButton(0, OPTIONS[0] + (int)(AbstractDungeon.player.maxHealth * 0.15F) + String.format(OPTIONS[1], AbstractDungeon.ascensionLevel > 15 ? 2 : 1)));
                event.imageEventText.optionList.get(0).calculateY(event.imageEventText.optionList.size());
            }
        }
    }

    @SpirePatch(
            clz = TheMausoleum.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(TheMausoleum event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                try {
                    Enum screen = ReflectionHacks.getPrivate(event, TheMausoleum.class, "screen");
                    switch (screen.name()) {
                        case "INTRO":
                            switch (buttonPressed) {
                                case 0:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                    CardCrawlGame.sound.play("SFX_EXPLOSION_STRONG");
                                    CardCrawlGame.screenShake.rumble(2.0F);
                                    AbstractDungeon.player.damage(new DamageInfo(null, (int)(AbstractDungeon.player.maxHealth * 0.15F)));
                                    SinAndBloodstainManager.increaseBloodstain(1);
                                    AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), r);
                                    AbstractEvent.logMetricObtainRelic("The Mausoleum", "Opened", r);
                                    event.imageEventText.clearAllDialogs();
                                    event.imageEventText.setDialogOption(OPTIONS[2]);
                                    ReflectionHacks.setPrivate(event, TheMausoleum.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.city.TheMausoleum$CurScreen"), "RESULT"));
                                    return SpireReturn.Return(null);
                            }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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
