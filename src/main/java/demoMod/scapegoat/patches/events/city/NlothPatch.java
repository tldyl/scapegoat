package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.NlothsGift;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

import java.util.List;
import java.util.stream.Collectors;

public class NlothPatch {
    public static final String ID = Scapegoat.makeID("N'loth");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static AbstractCard card = null;

    @SpirePatch(
            clz = Nloth.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Nloth event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                List<AbstractCard> cards = AbstractDungeon.player.masterDeck.group.stream().filter(card -> card.rarity == AbstractCard.CardRarity.COMMON).collect(Collectors.toList());
                if (!cards.isEmpty()) {
                    card = cards.get(AbstractDungeon.miscRng.random(cards.size() - 1));
                    event.imageEventText.optionList.add(2, new LargeDialogOptionButton(2, OPTIONS[0] + card.name + String.format(OPTIONS[1], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1)));
                    event.imageEventText.optionList.get(3).slot = 3;
                    for (LargeDialogOptionButton button : event.imageEventText.optionList) {
                        button.calculateY(event.imageEventText.optionList.size());
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = Nloth.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Nloth event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, Nloth.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        if (card != null) {
                            switch (buttonPressed) {
                                case 2:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                                    AbstractRelic gift;
                                    if (AbstractDungeon.player.hasRelic("Nloth's Gift")) {
                                        gift = new Circlet();
                                    } else {
                                        gift = new NlothsGift();
                                    }
                                    AbstractEvent.logMetricObtainRelic("N'loth", "Traded Relic", gift);
                                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), gift);
                                    AbstractDungeon.effectList.add(new PurgeCardEffect(card));
                                    AbstractDungeon.player.masterDeck.removeCard(card);
                                    card = null;
                                    SinAndBloodstainManager.increaseBloodstain(1);
                                    ReflectionHacks.setPrivate(event, Nloth.class, "screenNum", 1);
                                    event.imageEventText.updateDialogOption(0, OPTIONS[2]);
                                    event.imageEventText.clearRemainingOptions();
                                    return SpireReturn.Return(null);
                                case 3:
                                    AbstractEvent.logMetricIgnored("N'loth");
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                                    ReflectionHacks.setPrivate(event, Nloth.class, "screenNum", 1);
                                    event.imageEventText.updateDialogOption(0, OPTIONS[2]);
                                    event.imageEventText.clearRemainingOptions();
                                    return SpireReturn.Return(null);
                            }
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
