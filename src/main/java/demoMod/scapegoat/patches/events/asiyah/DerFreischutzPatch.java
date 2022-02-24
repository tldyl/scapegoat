package demoMod.scapegoat.patches.events.asiyah;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.scapegoat.Scapegoat;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.relics.DarkBullet;
import demoMod.scapegoat.relics.PageOfFreischutz;

public class DerFreischutzPatch {
    public static final String ID = Scapegoat.makeID("DerFreischutz");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            cls = "ruina.events.act1.DerFreischutz",
            method = SpirePatch.CONSTRUCTOR,
            optional = true
    )
    public static class PatchConstructor {
        public static void Postfix(AbstractImageEvent event) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                event.imageEventText.setDialogOption(OPTIONS[8], new PageOfFreischutz());
            }
        }
    }

    @SpirePatch(
            cls = "ruina.events.act1.DerFreischutz",
            method = "buttonEffect",
            optional = true
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(AbstractImageEvent event, int buttonPressed) {
            if (AbstractDungeon.player instanceof ScapegoatCharacter) {
                try {
                    int screenNum = ReflectionHacks.getPrivate(event, Class.forName("ruina.events.act1.DerFreischutz"), "screenNum");
                    switch (screenNum) {
                        case 0:
                            switch (buttonPressed) {
                                case 2:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                                    ReflectionHacks.setPrivate(event, Class.forName("ruina.events.act1.DerFreischutz"), "screenNum", 1);
                                    event.imageEventText.updateDialogOption(0, OPTIONS[7]);
                                    event.imageEventText.clearRemainingOptions();
                                    CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.LONG, false);
                                    CardCrawlGame.sound.play("BLUNT_HEAVY");
                                    AbstractDungeon.player.damage(new DamageInfo(null, 4));
                                    if (!AbstractDungeon.player.hasRelic(DarkBullet.ID)) {
                                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new DarkBullet());
                                        AbstractDungeon.uncommonRelicPool.remove(DarkBullet.ID);
                                    } else {
                                        AbstractDungeon.player.gainGold(50);
                                    }
                                    if (!AbstractDungeon.player.hasRelic(PageOfFreischutz.ID) && !AbstractDungeon.player.hasRelic("PageOfFreischutz")) {
                                        if (!Loader.isModLoaded("Library of Ruina")) {
                                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new PageOfFreischutz());
                                        } else {
                                            try {
                                                AbstractRelic relic = (AbstractRelic) Class.forName("lor.relic.abnormality.PageOfFreischutz").newInstance();
                                                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
                                                AbstractDungeon.rareRelicPool.remove(relic.relicId);
                                            } catch (InstantiationException | IllegalAccessException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new Circlet());
                                    }
                                    (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
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
