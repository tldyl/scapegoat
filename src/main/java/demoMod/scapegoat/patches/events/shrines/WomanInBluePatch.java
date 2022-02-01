package demoMod.scapegoat.patches.events.shrines;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.WomanInBlue;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.utils.SinAndBloodstainManager;

public class WomanInBluePatch {
    public static final String ID = Scapegoat.makeID("The Woman in Blue");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = WomanInBlue.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(WomanInBlue event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.optionList.add(3, new LargeDialogOptionButton(0, String.format(OPTIONS[7], AbstractDungeon.ascensionLevel >= 15 ? 2 : 1)));
                event.imageEventText.optionList.remove(0);
                event.imageEventText.optionList.remove(0);
                event.imageEventText.optionList.remove(0);
                event.imageEventText.optionList.get(1).slot = 1;
                for (LargeDialogOptionButton button : event.imageEventText.optionList) {
                    button.calculateY(event.imageEventText.optionList.size());
                }
            }
        }
    }

    @SpirePatch(
            clz = WomanInBlue.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(WomanInBlue event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                Enum screen = ReflectionHacks.getPrivate(event, WomanInBlue.class, "screen");
                switch (screen.name()) {
                    case "INTRO":
                        try {
                            ReflectionHacks.setPrivate(event, WomanInBlue.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.WomanInBlue$CurScreen"), "RESULT"));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        switch (buttonPressed) {
                            case 0:
                                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);
                                Scapegoat.addToBot(new AbstractGameAction() {
                                    private float duration = 0.5F;
                                    @Override
                                    public void update() {
                                        duration -= Gdx.graphics.getDeltaTime();
                                        if (duration <= 0.0F) {
                                            CardCrawlGame.screenShake.rumble(5.0F);
                                            isDone = true;
                                        }
                                    }
                                });
                                CardCrawlGame.sound.play("SFX_GLASS_BROKEN");
                                CardCrawlGame.sound.play("SFX_MESS");
                                event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                                AbstractDungeon.getCurrRoom().rewards.clear();
                                AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(PotionHelper.getRandomPotion()));
                                AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(PotionHelper.getRandomPotion()));
                                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                                AbstractDungeon.combatRewardScreen.open();
                                SinAndBloodstainManager.increaseBloodstain(1);
                                AbstractEvent.logMetric("The Woman in Blue", "Bought 2 Potions");
                                event.imageEventText.clearAllDialogs();
                                event.imageEventText.setDialogOption(OPTIONS[4]);
                                return SpireReturn.Return(null);
                            case 1:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                                CardCrawlGame.sound.play("BLUNT_FAST");
                                if (AbstractDungeon.ascensionLevel >= 15) {
                                    AbstractDungeon.player.damage(new DamageInfo(null, MathUtils.ceil((float)AbstractDungeon.player.maxHealth * 0.05F), DamageInfo.DamageType.HP_LOSS));
                                }
                                event.imageEventText.clearAllDialogs();
                                event.imageEventText.setDialogOption(OPTIONS[4]);
                                event.logMetric("Bought 0 Potions");
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
