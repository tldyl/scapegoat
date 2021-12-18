package demoMod.scapegoat.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rewards.RewardItem;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.monsters.CityVampire;

public class VampiresPatch {
    public static final String ID = Scapegoat.makeID("Vampires");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = Vampires.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Vampires event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.optionList.clear();
                event.imageEventText.setDialogOption(OPTIONS[0]);
                if (AbstractDungeon.player.hasRelic(BloodVial.ID)) {
                    event.imageEventText.setDialogOption(OPTIONS[2] + new BloodVial().name + OPTIONS[3]);
                }
                event.imageEventText.setDialogOption(OPTIONS[1]);
            }
        }
    }

    @SpirePatch(
            clz = Vampires.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Vampires event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, Vampires.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 0:
                                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[]{
                                        new CityVampire(-465.0F, -20.0F, false),
                                        new CityVampire(-130.0F, 15.0F, false),
                                        new CityVampire(200.0F, -5.0F, true)
                                });
                                AbstractDungeon.getCurrRoom().rewards.clear();
                                if (!AbstractDungeon.player.hasRelic(BloodVial.ID)) {
                                    AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new BloodVial()));
                                    AbstractDungeon.commonRelicPool.remove(BloodVial.ID);
                                }
                                AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON)));
                                ReflectionHacks.setPrivate(event, Vampires.class, "screenNum", 1);
                                event.enterCombatFromImage();
                                return SpireReturn.Return(null);
                            case 1:
                                if (AbstractDungeon.player.hasRelic(BloodVial.ID)) {
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                    AbstractDungeon.player.loseRelic(BloodVial.ID);
                                    new demoMod.scapegoat.relics.BloodVial().instantObtain();
                                    AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON).instantObtain();
                                    ReflectionHacks.setPrivate(event, Vampires.class, "screenNum", 1);
                                    event.imageEventText.updateDialogOption(0, OPTIONS[4]);
                                    event.imageEventText.clearRemainingOptions();
                                    return SpireReturn.Return(null);
                                }
                        }
                        Scapegoat.addToBot(new SFXAction("EVENT_VAMP_BITE"));
                        Scapegoat.addToBot(new WaitAction(0.2F));
                        Scapegoat.addToBot(new SFXAction("EVENT_VAMP_BITE"));
                        Scapegoat.addToBot(new WaitAction(0.2F));
                        Scapegoat.addToBot(new SFXAction("EVENT_VAMP_BITE"));
                        AbstractDungeon.player.damage(new DamageInfo(null, 6));
                        AbstractEvent.logMetricIgnored("Vampires");
                        event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        ReflectionHacks.setPrivate(event, Vampires.class, "screenNum", 2);
                        event.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        event.imageEventText.clearRemainingOptions();
                        return SpireReturn.Return(null);
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
