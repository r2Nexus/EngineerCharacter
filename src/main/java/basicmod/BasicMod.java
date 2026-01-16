package basicmod;

import basemod.BaseMod;
import basemod.interfaces.*;
import basicmod.cards.attack.*;
import basicmod.cards.power.*;
import basicmod.cards.skill.*;
import basicmod.characters.Engineer;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.PlayerClassEnum;
import basicmod.relics.BuiltMiner;
import basicmod.relics.BuiltTurret;
import basicmod.util.*;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class BasicMod implements
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditCharactersSubscriber,
        OnStartBattleSubscriber,
        EditRelicsSubscriber,
        PostDrawSubscriber{
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    public static final boolean DEV_MODE =
            Loader.DEBUG
                    || "true".equalsIgnoreCase(System.getProperty("basicmod.dev", "false"));

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new BasicMod();
    }

    public BasicMod() {
        BaseMod.subscribe(this);
        logger.info(modID + " subscribed to BaseMod.");

        BaseMod.addColor(
                AbstractCardEnum.ENGINEER,
                ENGINEER_COLOR, ENGINEER_COLOR, ENGINEER_COLOR, ENGINEER_COLOR,
                ENGINEER_COLOR, ENGINEER_COLOR, ENGINEER_COLOR,

                imagePath("512/bg_attack_engineer.png"),
                imagePath("512/bg_skill_engineer.png"),
                imagePath("512/bg_power_engineer.png"),
                imagePath("512/card_orb_engineer.png"),

                imagePath("1024/bg_attack_engineer.png"),
                imagePath("1024/bg_skill_engineer.png"),
                imagePath("1024/bg_power_engineer.png"),
                imagePath("1024/card_orb_engineer.png")
        );
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.

        //If you want to set up a config panel, that will be done here.
        //You can find information about this on the BaseMod wiki page "Mod Config and Panel".
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);



        if (DEV_MODE) {
            logger.info("[DEV] Revealing all " + modID + " cards in compendium.");
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c.cardID != null && c.cardID.startsWith(modID + ":")) {
                    UnlockTracker.markCardAsSeen(c.cardID);
                }
            }
        }
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void debugExists(String rel) {
        String p = imagePath(rel);
        BasicMod.logger.info("[ORB CHECK] " + p + " exists=" + Gdx.files.internal(p).exists());
    }

    @Override
    public void receiveEditCards() {

        debugExists("512/card_orb_engineer.png");
        debugExists("1024/card_orb_engineer.png");

        // --- Attacks ---
        BaseMod.addCard(new Pistol());
        BaseMod.addCard(new SubmachineGun());
        BaseMod.addCard(new RainingBullets());
        BaseMod.addCard(new Flamethrower());
        BaseMod.addCard(new Shotgun());
        BaseMod.addCard(new PiercingRounds());
        BaseMod.addCard(new TurretPush());
        BaseMod.addCard(new RocketBarrage());
        BaseMod.addCard(new Detonator());
        BaseMod.addCard(new TargetPractice());
        BaseMod.addCard(new PoisonCapsule());
        BaseMod.addCard(new CliffExplosive());
        BaseMod.addCard(new HeavyArtillery());
        BaseMod.addCard(new Locomotive());
        BaseMod.addCard(new CargoWagon());
        BaseMod.addCard(new ArtilleryWagon());
        BaseMod.addCard(new FluidWagon());
        BaseMod.addCard(new ExpansionPolicy());
        BaseMod.addCard(new Railgun());
        BaseMod.addCard(new PersonalLaser());
        BaseMod.addCard(new DischargeDefense());

        // --- Skills ---
        BaseMod.addCard(new Wall());
        BaseMod.addCard(new Miner());
        BaseMod.addCard(new Turret());
        BaseMod.addCard(new ManualLabor());
        BaseMod.addCard(new SteamBattery());
        BaseMod.addCard(new LandMine());
        BaseMod.addCard(new EmergencyRepair());
        BaseMod.addCard(new HeavyArmour());
        BaseMod.addCard(new YellowBelt());
        BaseMod.addCard(new RedBelt());
        BaseMod.addCard(new YellowInserter());
        BaseMod.addCard(new FastInserter());
        BaseMod.addCard(new Deconstruction());
        BaseMod.addCard(new LogisticRobot());
        BaseMod.addCard(new Blueprint());
        BaseMod.addCard(new RailSignal());
        BaseMod.addCard(new ChainSignal());
        BaseMod.addCard(new ScienceWagon());
        BaseMod.addCard(new Derailment());
        BaseMod.addCard(new BudgetDefenses());
        BaseMod.addCard(new LightArmour());
        BaseMod.addCard(new MineField());
        BaseMod.addCard(new RapidDeployment());
        BaseMod.addCard(new SteamEngine());
        BaseMod.addCard(new Recycler());
        BaseMod.addCard(new EnergyShield());
        BaseMod.addCard(new PersonalBattery());

        // --- Powers ---
        BaseMod.addCard(new Landfill());
        BaseMod.addCard(new RepairSystem());
        BaseMod.addCard(new BlackScience());    ScienceCardPool.add(BlackScience.ID);
        BaseMod.addCard(new BlueScience());     ScienceCardPool.add(BlueScience.ID);
        BaseMod.addCard(new RedScience());      ScienceCardPool.add(RedScience.ID);
        BaseMod.addCard(new GreenScience());    ScienceCardPool.add(GreenScience.ID);
        BaseMod.addCard(new PurpleScience());   ScienceCardPool.add(PurpleScience.ID);
        BaseMod.addCard(new PerimeterWall());
        BaseMod.addCard(new SuppressiveFire());
        BaseMod.addCard(new BeltFed());
        BaseMod.addCard(new ReactiveArmour());

        // --- Other ---
        BaseMod.addCard(new basicmod.cards.Material());
    }

    @Override
    public void receiveEditRelics() {
        // --- Relics ---
        BaseMod.addRelicToCustomPool(new BuiltMiner(), AbstractCardEnum.ENGINEER);
        BaseMod.addRelicToCustomPool(new BuiltTurret(), AbstractCardEnum.ENGINEER);
    }

    public static final Color ENGINEER_COLOR = CardHelper.getColor(140f, 140f, 140f);
    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(
                new Engineer("Engineer"),
                imagePath("charSelect/EngineerButton.png"),
                imagePath("charSelect/EngineerPortrait.jpg"),
                PlayerClassEnum.ENGINEER
        );
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        // 1) Always load English first
        try {
            String path = localizationPath(defaultLanguage, "Keywords.json");
            String json = Gdx.files.internal(path)
                    .readString(String.valueOf(StandardCharsets.UTF_8));

            KeywordInfo[] arr = gson.fromJson(json, KeywordInfo[].class);

            logger.info("[KEYWORDS] Loading " + arr.length + " keywords from: " + path);
            for (KeywordInfo k : arr) {
                k.prep();
                registerKeyword(k);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load default (eng) Keywords.json", e);
        }

        // 2) Overlay current language if different
        if (!defaultLanguage.equals(getLangString())) {
            try {
                String path2 = localizationPath(getLangString(), "Keywords.json");
                String json2 = Gdx.files.internal(path2)
                        .readString(String.valueOf(StandardCharsets.UTF_8));

                KeywordInfo[] arr2 = gson.fromJson(json2, KeywordInfo[].class);

                logger.info("[KEYWORDS] Loading " + arr2.length + " keywords from: " + path2);
                for (KeywordInfo k : arr2) {
                    k.prep();
                    registerKeyword(k);
                }
            } catch (Exception e) {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION, info.COLOR);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    @Override
    public void receiveAddAudio() {
        loadAudio(Sounds.class);
    }

    private static final String[] AUDIO_EXTENSIONS = { ".ogg", ".wav", ".mp3" }; //There are more valid types, but not really worth checking them all here
    private void loadAudio(Class<?> cls) {
        try {
            Field[] fields = cls.getDeclaredFields();
            outer:
            for (Field f : fields) {
                int modifiers = f.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && f.getType().equals(String.class)) {
                    String s = (String) f.get(null);
                    if (s == null) { //If no defined value, determine path using field name
                        s = audioPath(f.getName());

                        for (String ext : AUDIO_EXTENSIONS) {
                            String testPath = s + ext;
                            if (Gdx.files.internal(testPath).exists()) {
                                s = testPath;
                                BaseMod.addAudio(s, s);
                                f.set(null, s);
                                continue outer;
                            }
                        }
                        throw new Exception("Failed to find an audio file \"" + f.getName() + "\" in " + resourcesFolder + "/audio; check to ensure the capitalization and filename are correct.");
                    }
                    else { //Otherwise, load defined path
                        if (Gdx.files.internal(s).exists()) {
                            BaseMod.addAudio(s, s);
                        }
                        else {
                            throw new Exception("Failed to find audio file \"" + s + "\"; check to ensure this is the correct filepath.");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Exception occurred in loadAudio: ", e);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String audioPath(String file) {
        return resourcesFolder + "/audio/" + file;
    }
    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }
    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    /**
     * Checks the expected resources path based on the package name.
     */
    private static String checkResourcesPath() {
        String name = BasicMod.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        if (separator > 0)
            name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + BasicMod.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     */
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(BasicMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    // in BasicMod.java
    public static int sciencePlayedThisCombat = 0;
    public static int materialConsumedThisTurn = 0;
    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        materialConsumedThisTurn = 0;
        sciencePlayedThisCombat = 0;
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        materialConsumedThisTurn = 0;
    }
}
