package basicmod.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import basicmod.BasicMod;
import basicmod.cards.attack.ManualLabor;
import basicmod.cards.attack.Pistol;
import basicmod.cards.skill.Turret;
import basicmod.cards.skill.Wall;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.PlayerClassEnum;

import basicmod.relics.BuiltTurret;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;

public class Engineer extends CustomPlayer {
    public static final String ID = BasicMod.makeID("Engineer");

    private static final CharacterStrings strings =
            CardCrawlGame.languagePack.getCharacterString(ID);

    //TODO: do it correctly
    private static final String[] ORB_TEXTURES = {
            BasicMod.imagePath("char/engineer/orb/layer1.png"),
            BasicMod.imagePath("char/engineer/orb/layer2.png"),
            BasicMod.imagePath("char/engineer/orb/layer3.png"),
            BasicMod.imagePath("char/engineer/orb/layer4.png"),
            BasicMod.imagePath("char/engineer/orb/layer5.png"),
            BasicMod.imagePath("char/engineer/orb/layer6.png"),
            BasicMod.imagePath("char/engineer/orb/layer1d.png"),
            BasicMod.imagePath("char/engineer/orb/layer2d.png"),
            BasicMod.imagePath("char/engineer/orb/layer3d.png"),
            BasicMod.imagePath("char/engineer/orb/layer4d.png"),
            BasicMod.imagePath("char/engineer/orb/layer5d.png"),
    };

    private static final String ORB_VFX = BasicMod.imagePath("char/engineer/orb/vfx.png");
    private static final float[] ORB_LAYER_SPEEDS = {
            // Bright
            -40f,  40f,  -25f,  25f,  -15f,  0f,

            // Dark
            -40f,  40f,  -25f,  25f,

            // Base/frame should be static
            0f
    };
    public Engineer(String name) {
        super(
                name,
                PlayerClassEnum.ENGINEER,
                ORB_TEXTURES,
                ORB_VFX,
                ORB_LAYER_SPEEDS,
                new SpriterAnimation(BasicMod.imagePath("char/engineer/static.scml"))
        );

        initializeClass(
                BasicMod.imagePath("char/engineer/engineer.png"),
                BasicMod.imagePath("char/engineer/shoulder2.png"),
                BasicMod.imagePath("char/engineer/shoulder1.png"),
                BasicMod.imagePath("char/engineer/corpse.png"),
                getLoadout(),
                20.0F, -10.0F, 220.0F, 290.0F,
                new EnergyManager(3)
        );

        this.masterMaxOrbs = 3;
        this.maxOrbs = this.masterMaxOrbs;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                strings.NAMES[0],
                strings.TEXT[0],
                70, 70, 0, 99, 5,
                this,
                getStartingRelics(),
                getStartingDeck(),
                false
        );
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();

        // Ensure the orb list always matches maxOrbs
        if (this.orbs == null) return;

        // Grow/shrink to match maxOrbs
        while (this.orbs.size() < this.maxOrbs) {
            this.orbs.add(new EmptyOrbSlot());
        }
        while (this.orbs.size() > this.maxOrbs) {
            this.orbs.remove(this.orbs.size() - 1);
        }

        // Re-slot so positions/shuffling behave correctly
        for (int i = 0; i < this.orbs.size(); i++) {
            this.orbs.get(i).setSlot(i, this.maxOrbs);
        }
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> deck = new ArrayList<>();
        deck.add(Pistol.ID); deck.add(Pistol.ID); deck.add(Pistol.ID); deck.add(Pistol.ID);
        deck.add(Wall.ID);   deck.add(Wall.ID);   deck.add(Wall.ID);   deck.add(Wall.ID);
        deck.add(ManualLabor.ID); deck.add(Turret.ID);
        return deck;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> relics = new ArrayList<>();
        relics.add(BuiltTurret.ID);
        return relics;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Engineer(this.name);
    }

    // ---- REQUIRED AbstractPlayer overrides ----

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return strings.NAMES[0];
    }

    @Override
    public Color getCardRenderColor() {
        return Color.LIGHT_GRAY.cpy();
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.ENGINEER;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.LIGHT_GRAY.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        };
    }


    @Override
    public Color getCardTrailColor() {
        return Color.LIGHT_GRAY.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.play("ATTACK_HEAVY");
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    @Override
    public String getLocalizedCharacterName() {
        return strings.NAMES[0];
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard getStartCardForEvent() {
        return new Pistol();
    }

    @Override
    public String getSpireHeartText() {
        return strings.TEXT.length > 1 ? strings.TEXT[1] : "The factory hums louder...";
    }

    @Override
    public String getVampireText() {
        return strings.TEXT.length > 2 ? strings.TEXT[2] : "Offer blood? No thanks.";
    }
}
