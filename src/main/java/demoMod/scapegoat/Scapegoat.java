package demoMod.scapegoat;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import demoMod.scapegoat.cards.scapegoat.*;
import demoMod.scapegoat.cards.status.Cost;
import demoMod.scapegoat.characters.ScapegoatCharacter;
import demoMod.scapegoat.dynamicVars.MiscNumber;
import demoMod.scapegoat.dynamicVars.SecondaryM;
import demoMod.scapegoat.enums.AbstractCardEnum;
import demoMod.scapegoat.enums.AbstractPlayerEnum;
import demoMod.scapegoat.potions.BottledRedemption;
import demoMod.scapegoat.potions.DevilInAJar;
import demoMod.scapegoat.potions.LiquidSequin;
import demoMod.scapegoat.relics.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpireInitializer
public class Scapegoat implements EditCardsSubscriber,
                                  EditStringsSubscriber,
                                  EditKeywordsSubscriber,
                                  EditRelicsSubscriber,
                                  EditCharactersSubscriber,
                                  PostInitializeSubscriber,
                                  AddAudioSubscriber,
                                  PostUpdateSubscriber {

    private static final String ATTACK_CARD = "512/bg_attack_scapegoat.png";
    private static final String SKILL_CARD = "512/bg_skill_scapegoat.png";
    private static final String POWER_CARD = "512/bg_power_scapegoat.png";
    private static final String ENERGY_ORB = "512/card_scapegoat_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";
    private static final String ATTACK_CARD_PORTRAIT = "1024/bg_attack_scapegoat.png";
    private static final String SKILL_CARD_PORTRAIT = "1024/bg_skill_scapegoat.png";
    private static final String POWER_CARD_PORTRAIT = "1024/bg_power_scapegoat.png";
    private static final String ENERGY_ORB_PORTRAIT = "1024/card_scapegoat_orb.png";
    public static Color mainGoatColor = new Color(0.066F, 0.631F, 0.714F, 1.0F);

    private static final List<AbstractGameAction> actionQueue = new ArrayList<>();

    public static void initialize() {
        new Scapegoat();
        BaseMod.addColor(AbstractCardEnum.SCAPEGOAT,
                mainGoatColor, mainGoatColor, mainGoatColor, mainGoatColor, mainGoatColor, mainGoatColor, mainGoatColor,
                getResourcePath(ATTACK_CARD), getResourcePath(SKILL_CARD),
                getResourcePath(POWER_CARD), getResourcePath(ENERGY_ORB),
                getResourcePath(ATTACK_CARD_PORTRAIT), getResourcePath(SKILL_CARD_PORTRAIT),
                getResourcePath(POWER_CARD_PORTRAIT), getResourcePath(ENERGY_ORB_PORTRAIT), getResourcePath(CARD_ENERGY_ORB));
    }

    public Scapegoat() {
        BaseMod.subscribe(this);
    }

    public static String makeID(String name) {
        return "Scapegoat:" + name;
    }

    public static String getResourcePath(String resource) {
        return "scapegoatImages/" + resource;
    }

    public static String getLanguageString() {
        String language;
        switch (Settings.language) {
            case ZHS:
                language = "zhs";
                break;
                /*
            case KOR:
                language = "kor";
                break;
            case JPN:
                language = "jpn";
                break;
                */
            default:
                language = "eng";
        }
        return language;
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new MiscNumber());
        BaseMod.addDynamicVariable(new SecondaryM());

        BaseMod.addCard(new Strike_Scapegoat());
        BaseMod.addCard(new Defend_Scapegoat());
        BaseMod.addCard(new Preparation());
        BaseMod.addCard(new Ritual());
        BaseMod.addCard(new FetterDagger());
        BaseMod.addCard(new BladeImpact());
        BaseMod.addCard(new ProbeStrike());
        BaseMod.addCard(new CutThroat());
        BaseMod.addCard(new TailWhip());
        BaseMod.addCard(new Punish());
        BaseMod.addCard(new EquivalentSubstitution());
        BaseMod.addCard(new BloodyThorns());
        BaseMod.addCard(new Reloaded());
        BaseMod.addCard(new AvoidCombat());
        BaseMod.addCard(new Barrier());
        BaseMod.addCard(new Convene());
        BaseMod.addCard(new Redemption());
        BaseMod.addCard(new GunSlinger());
        BaseMod.addCard(new DualMark());
        BaseMod.addCard(new Peeper());
        BaseMod.addCard(new Sacrifice());
        BaseMod.addCard(new ForTheSin());
        BaseMod.addCard(new AggravatedSin());
        BaseMod.addCard(new BloodForBlade());
        BaseMod.addCard(new Liquidation());
        BaseMod.addCard(new Convergence());
        BaseMod.addCard(new FalseAccusation());
        BaseMod.addCard(new Weave());
        BaseMod.addCard(new ReturnedBlade());
        BaseMod.addCard(new AllOutParry());
        BaseMod.addCard(new Malice());
        BaseMod.addCard(new BurnTheBoat());
        BaseMod.addCard(new PryIntoTruth());
        BaseMod.addCard(new InstinctJump());
        BaseMod.addCard(new SkilledChopping());
        BaseMod.addCard(new MaliciousErosion());
        BaseMod.addCard(new RunningFireOfTheDead());
        BaseMod.addCard(new DealWithDevil());
        BaseMod.addCard(new BrandishingNeedle());
        BaseMod.addCard(new CrossDominate());
        BaseMod.addCard(new MasteryOfFirearms());
        BaseMod.addCard(new DelayedFlame());
        BaseMod.addCard(new ReturnToSilence());
        BaseMod.addCard(new EmergencyCall());
        BaseMod.addCard(new BloodVibration());
        BaseMod.addCard(new Vibration());
        BaseMod.addCard(new Adaptability());
        BaseMod.addCard(new RitualLunge());
        BaseMod.addCard(new Splash());
        BaseMod.addCard(new HeadStart());
        BaseMod.addCard(new BlazeAway());
        BaseMod.addCard(new Gnaw());
        BaseMod.addCard(new SacrificeGarrotte());
        BaseMod.addCard(new ExtraordinaryFood());
        BaseMod.addCard(new AimingShot());
        BaseMod.addCard(new Binding());
        BaseMod.addCard(new SacrificeReplacement());
        BaseMod.addCard(new DrinkPoisonToEndThirst());
        BaseMod.addCard(new MarkerCapture());
        BaseMod.addCard(new TurnBackForSin());
        BaseMod.addCard(new CurtainCall());
        BaseMod.addCard(new PriorRepair());
        BaseMod.addCard(new SequinShard());
        BaseMod.addCard(new StockUp());
        BaseMod.addCard(new BaresTheSoul());
        BaseMod.addCard(new GunSwordDance());
        BaseMod.addCard(new EmergencyMeasurement());
        BaseMod.addCard(new AbyssMemory());
        BaseMod.addCard(new BloodyCrown());
        BaseMod.addCard(new PunishmentForm());
        BaseMod.addCard(new Awe());
        BaseMod.addCard(new Deflagration());
        BaseMod.addCard(new CounterAttack());
        BaseMod.addCard(new Asterism());
        BaseMod.addCard(new TemporaryCollaboration());
        BaseMod.addCard(new WashAway());


        //Special cards
        BaseMod.addCard(new SoulFlame());
        BaseMod.addCard(new SerialBullet());
        BaseMod.addCard(new Cost());
        BaseMod.addCard(new Shard());
        BaseMod.addCard(new SelfSacrifice());

        UnlockTracker.unlockCard(Strike_Scapegoat.ID);
        UnlockTracker.unlockCard(Defend_Scapegoat.ID);
        UnlockTracker.unlockCard(Preparation.ID);
        UnlockTracker.unlockCard(Ritual.ID);
    }

    @Override
    public void receiveEditKeywords() {
        final Gson gson = new Gson();
        String language;
        language = getLanguageString();
        final String json = Gdx.files.internal("localization/" + language + "/Scapegoat-KeywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditStrings() {
        String language;
        language = getLanguageString();

        String cardStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-PowerStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-RelicStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String uiStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-UIStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String charStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-CharacterStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, charStrings);
        String eventStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-EventStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-MonsterStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String potionStrings = Gdx.files.internal("localization/" + language + "/Scapegoat-PotionStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("GUN_FIRE_POLARIS", "scapegoatAudio/sfx/gun_fire_polaris.wav");
        BaseMod.addAudio("ICE_BLAST_PROJECTILE_SPELL_01", "scapegoatAudio/sfx/ice_blast_projectile_spell_01.wav");
        BaseMod.addAudio("SFX_GLASS_BROKEN", "scapegoatAudio/sfx/sfx_glass_broken.ogg");
        BaseMod.addAudio("SFX_MESS", "scapegoatAudio/sfx/sfx_mess.ogg");
        BaseMod.addAudio("SFX_EXPLOSION_STRONG", "scapegoatAudio/sfx/sfx_explosion_strong.wav");
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new ScapegoatCharacter("Scapegoat", AbstractPlayerEnum.SCAPEGOAT), getResourcePath("charSelect/button.png"), getResourcePath("charSelect/portrait.png"), AbstractPlayerEnum.SCAPEGOAT);
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new BronzePipe(), AbstractCardEnum.SCAPEGOAT);
        BaseMod.addRelicToCustomPool(new BurningPipe(), AbstractCardEnum.SCAPEGOAT);
        BaseMod.addRelicToCustomPool(new OldShell(), AbstractCardEnum.SCAPEGOAT);
        BaseMod.addRelicToCustomPool(new MarkOfThePayback(), AbstractCardEnum.SCAPEGOAT);
        BaseMod.addRelicToCustomPool(new DarkBullet(), AbstractCardEnum.SCAPEGOAT);
        BaseMod.addRelicToCustomPool(new BurningEye(), AbstractCardEnum.SCAPEGOAT);
        BaseMod.addRelic(new AppleCandy(), RelicType.SHARED);
        BaseMod.addRelic(new ShinyShard(), RelicType.SHARED);
        BaseMod.addRelic(new AzureFlame(), RelicType.SHARED);
        BaseMod.addRelic(new RuneEctoplasm(), RelicType.SHARED);
        BaseMod.addRelic(new BloodVial(), RelicType.SHARED);
        BaseMod.addRelic(new TechInSpire(), RelicType.SHARED);
        BaseMod.addRelic(new GhostMask(), RelicType.SHARED);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.addPotion(LiquidSequin.class, Color.BLUE, Color.CYAN, Color.WHITE, LiquidSequin.ID, AbstractPlayerEnum.SCAPEGOAT);
        BaseMod.addPotion(BottledRedemption.class, Color.WHITE, Color.WHITE, null, BottledRedemption.ID, AbstractPlayerEnum.SCAPEGOAT);
        BaseMod.addPotion(DevilInAJar.class, Color.BLUE, Color.BLACK, Color.BLUE, DevilInAJar.ID, AbstractPlayerEnum.SCAPEGOAT);
    }

    @Override
    public void receivePostUpdate() {
        if (actionQueue.size() > 0) {
            actionQueue.get(0).update();
            if (actionQueue.get(0).isDone) {
                actionQueue.remove(0);
            }
        }
    }

    public static void addToBot(AbstractGameAction action) {
        actionQueue.add(action);
    }
}
