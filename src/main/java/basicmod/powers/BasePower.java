package basicmod.powers;

import basicmod.BasicMod;
import basicmod.util.GeneralUtils;
import basicmod.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class BasePower extends AbstractPower {
    private static PowerStrings getPowerStrings(String ID) {
        return CardCrawlGame.languagePack.getPowerStrings(ID);
    }

    protected AbstractCreature source;
    protected String[] DESCRIPTIONS;

    public int amount2 = 0;
    protected Color redColor2 = Color.RED.cpy();
    protected Color greenColor2 = Color.GREEN.cpy();

    public BasePower(String id, PowerType powerType, boolean isTurnBased,
                     AbstractCreature owner, AbstractCreature source, int amount,
                     String texture48Path, String texture128Path) {
        this(id, powerType, isTurnBased, owner, source, amount, true, false); // loadImage=false
        loadPowerImages(texture48Path, texture128Path);
        updateDescription();
    }

    public BasePower(String id, PowerType powerType, boolean isTurnBased,
                     AbstractCreature owner, int amount,
                     String texture48Path, String texture128Path) {
        this(id, powerType, isTurnBased, owner, null, amount, texture48Path, texture128Path);
    }

    public BasePower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        this(id, powerType, isTurnBased, owner, null, amount);
    }

    public BasePower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractCreature source, int amount) {
        this(id, powerType, isTurnBased, owner, source, amount, true);
    }

    public BasePower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractCreature source, int amount, boolean initDescription) {
        this(id, powerType, isTurnBased, owner, source, amount, initDescription, true);
    }

    public BasePower(String id, PowerType powerType, boolean isTurnBased,
                     AbstractCreature owner, AbstractCreature source, int amount,
                     boolean initDescription, boolean loadImage) {

        this.ID = id;
        this.isTurnBased = isTurnBased;

        PowerStrings strings = getPowerStrings(this.ID);
        if (strings != null && strings.NAME != null) {
            this.name = strings.NAME;
            this.DESCRIPTIONS = strings.DESCRIPTIONS;
        } else {
            this.name = id;
            this.DESCRIPTIONS = new String[]{"MISSING PowerStrings for: " + id};
        }

        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.type = powerType;

        if (loadImage) {
            String unPrefixed = GeneralUtils.removePrefix(id);
            Texture normalTexture = TextureLoader.getPowerTexture(unPrefixed);
            Texture hiDefImage = TextureLoader.getHiDefPowerTexture(unPrefixed);

            if (hiDefImage != null) {
                region128 = new TextureAtlas.AtlasRegion(hiDefImage, 0, 0, hiDefImage.getWidth(), hiDefImage.getHeight());
                if (normalTexture != null) {
                    region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
                }
            } else if (normalTexture != null) {
                this.img = normalTexture;
                region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
            }
        }

        if (initDescription) {
            this.updateDescription();
        }
    }

    protected final void loadPowerImages(String texture48Path, String texture128Path) {
        if (texture48Path == null || texture48Path.isEmpty()) {
            BasicMod.logger.warn("[BasePower] No 48px texture path provided for " + ID);
            return;
        }

        Texture t48 = TextureLoader.getTexture(texture48Path);
        if (t48 == null) {
            BasicMod.logger.error("[BasePower] Missing 48px texture for " + ID + " at: " + texture48Path);
            return;
        }

        this.region48 = new TextureAtlas.AtlasRegion(t48, 0, 0, t48.getWidth(), t48.getHeight());
        this.img = t48; // fallback

        if (texture128Path != null && !texture128Path.isEmpty()) {
            Texture t128 = TextureLoader.getTexture(texture128Path);
            if (t128 != null) {
                this.region128 = new TextureAtlas.AtlasRegion(t128, 0, 0, t128.getWidth(), t128.getHeight());
            } else {
                BasicMod.logger.warn("[BasePower] Missing 128px texture for " + ID + " at: " + texture128Path);
            }
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);

        if (this.amount2 != 0) {
            if (!this.isTurnBased) {
                float alpha = c.a;
                c = this.amount2 > 0 ? this.greenColor2 : this.redColor2;
                c.a = alpha;
            }

            FontHelper.renderFontRightTopAligned(
                    sb,
                    FontHelper.powerAmountFont,
                    Integer.toString(this.amount2),
                    x,
                    y + 15.0F * Settings.scale,
                    this.fontScale,
                    c
            );
        }
    }

    @Override
    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 0 && DESCRIPTIONS[0] != null) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = "";
        }
    }

}
