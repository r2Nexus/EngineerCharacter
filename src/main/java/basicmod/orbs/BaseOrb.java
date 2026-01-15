package basicmod.orbs;

import basicmod.BasicMod;
import basicmod.cards.Material;
import basicmod.powers.PurpleSciencePower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

/**
 * Base class to remove orb boilerplate:
 * - loads OrbStrings + sets name
 * - loads texture + default render
 * - standardized focus math (with per-stat toggles)
 * - optional secondary stat (e.g. block amount, charges, etc.)
 * - token-based description formatting (!P!, !E!, and optional secondary token)
 *
 * NOTE: We "poll" for focus changes (and some other relevant state) during updateAnimation.
 * This avoids fragile patches and keeps descriptions synced (e.g. when Focus changes mid-combat).
 */
public abstract class BaseOrb extends AbstractOrb {

    protected static String makeID(String name) {
        return BasicMod.makeID(name);
    }

    protected final OrbStrings orbStrings;
    protected final Texture orbImg;

    // Optional "second number" your orb can use (block amount, charges, etc.)
    protected int baseSecondaryAmount = 0;
    protected int secondaryAmount = 0;

    // Whether Focus affects each stat
    protected boolean focusAffectsPassive = true;
    protected boolean focusAffectsEvoke = true;
    protected boolean focusAffectsSecondary = false;

    // Token for the secondary stat in description (set to "!B!" for your LandMine)
    protected String secondaryToken = "!S!";

    protected static final float ORB_W = 96.0f;
    protected static final float ORB_H = 96.0f;

    // =========================
    // Auto-refresh cache (cheap per-frame sync)
    // =========================
    private int cachedFocus = Integer.MIN_VALUE;
    private int cachedBasePassive = Integer.MIN_VALUE;
    private int cachedBaseEvoke = Integer.MIN_VALUE;
    private int cachedBaseSecondary = Integer.MIN_VALUE;

    protected BaseOrb(String orbID, String texturePath, int basePassive, int baseEvoke) {
        this.ID = orbID;

        this.orbStrings = CardCrawlGame.languagePack.getOrbString(orbID);
        this.name = orbStrings.NAME;

        this.orbImg = ImageMaster.loadImage(texturePath);
        this.img = orbImg;

        this.basePassiveAmount = basePassive;
        this.baseEvokeAmount = baseEvoke;

        // Initialize amounts + description once
        refresh();

        // Prime cache so we don't refresh again on the first frame unless something changed
        snapshotRefreshState();
    }

    /* =========================
       Config helpers (constructor-friendly)
       ========================= */

    /** Call from subclass constructor if you want a secondary stat. */
    protected final void setSecondary(int baseSecondary) {
        this.baseSecondaryAmount = baseSecondary;
        this.secondaryAmount = baseSecondary;
    }

    protected final void setSecondaryToken(String token) {
        this.secondaryToken = token;
    }

    protected final void setFocusAffectsPassive(boolean v) { this.focusAffectsPassive = v; }
    protected final void setFocusAffectsEvoke(boolean v) { this.focusAffectsEvoke = v; }
    protected final void setFocusAffectsSecondary(boolean v) { this.focusAffectsSecondary = v; }

    /* =========================
       Focus + stat calculation
       ========================= */

    protected int getFocus() {
        if (AbstractDungeon.player == null) return 0;
        AbstractPower p = AbstractDungeon.player.getPower(FocusPower.POWER_ID);
        return (p != null) ? p.amount : 0;
    }

    protected int withFocus(int base, boolean affected) {
        int f = affected ? getFocus() : 0;
        return Math.max(0, base + f);
    }

    @Override
    public void applyFocus() {
        this.passiveAmount = withFocus(this.basePassiveAmount, focusAffectsPassive);
        this.evokeAmount   = withFocus(this.baseEvokeAmount,   focusAffectsEvoke);
        this.secondaryAmount = withFocus(this.baseSecondaryAmount, focusAffectsSecondary);
    }

    /* =========================
       Description
       ========================= */

    /**
     * Token formatter:
     * - !P! => passiveAmount
     * - !E! => evokeAmount
     * - secondaryToken (default !S!) => secondaryAmount
     */
    protected String format(String template) {
        if (template == null) return "";

        String out = template
                .replace("!P!", Integer.toString(passiveAmount))
                .replace("!E!", Integer.toString(evokeAmount));

        String token = (secondaryToken != null && !secondaryToken.isEmpty()) ? secondaryToken : "!S!";
        out = out.replace(token, Integer.toString(secondaryAmount));

        // If user forgot to change secondaryToken but used !S! in json, still support it.
        // (This is harmless if token == "!S!" because replace is idempotent here.)
        out = out.replace("!S!", Integer.toString(secondaryAmount));

        return out;
    }

    /** Default: format DESCRIPTION[0]. Override if you want multi-part descriptions. */
    protected String makeDescription() {
        if (orbStrings.DESCRIPTION == null || orbStrings.DESCRIPTION.length == 0) return "";
        return format(orbStrings.DESCRIPTION[0]);
    }

    /**
     * IMPORTANT: updateDescription should ONLY rebuild text from current numbers.
     * Don't call applyFocus() here, use refresh() when you need recalculation.
     */
    @Override
    public void updateDescription() {
        this.description = makeDescription();
    }

    /**
     * Recalculate numbers + rebuild description.
     * Call this from:
     * - your orb constructor (BaseOrb does this already)
     * - onEvoke / onStartOfTurn / onEndOfTurn if you want to be explicit
     */
    public void refresh() {
        applyFocus();
        updateDescription();
    }

    /* =========================
       Auto-refresh logic
       ========================= */

    private void snapshotRefreshState() {
        cachedFocus = getFocus();
        cachedBasePassive = basePassiveAmount;
        cachedBaseEvoke = baseEvokeAmount;
        cachedBaseSecondary = baseSecondaryAmount;
    }

    private boolean refreshStateChanged() {
        if (AbstractDungeon.player == null) return false;

        int f = getFocus();
        return f != cachedFocus
                || basePassiveAmount != cachedBasePassive
                || baseEvokeAmount != cachedBaseEvoke
                || baseSecondaryAmount != cachedBaseSecondary;
    }

    /** Cheap per-frame check; only refreshes when something relevant actually changes. */
    protected void refreshIfNeeded() {
        if (!inCombat()) return;

        if (refreshStateChanged()) {
            refresh();
            snapshotRefreshState();
        }
    }

    /* =========================
       Animation + render
       ========================= */

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        this.bobEffect.update();

        // ✅ Keep description synced (e.g. Focus changes mid-combat)
        refreshIfNeeded();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(orbImg, cX - 48.0F, cY - 48.0F + bobEffect.y, ORB_W, ORB_H);

        // Keep these when overriding render in subclasses
        renderText(sb);
        hb.render(sb);
    }

    /* =========================
       Small combat helpers
       ========================= */

    protected static boolean inCombat() {
        return AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    protected static boolean anyMonsterIntendsAttack() {
        if (!inCombat() || AbstractDungeon.getCurrRoom().monsters == null) return false;

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m == null || m.isDeadOrEscaped()) continue;

            switch (m.intent) {
                case ATTACK:
                case ATTACK_BUFF:
                case ATTACK_DEBUFF:
                case ATTACK_DEFEND:
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    protected static int filledOrbSlots() {
        if (AbstractDungeon.player == null) return 0;
        int filled = 0;
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (!(o instanceof EmptyOrbSlot)) filled++;
        }
        return filled;
    }

    protected static int totalOrbSlots() {
        if (AbstractDungeon.player == null) return 0;
        return AbstractDungeon.player.maxOrbs;
    }

    protected static int emptyOrbSlots() {
        return Math.max(0, totalOrbSlots() - filledOrbSlots());
    }

    protected void passiveVfx(OrbFlareEffect.OrbFlareColor color) {
        AbstractDungeon.actionManager.addToBottom(
                new VFXAction(new OrbFlareEffect(this, color), 0.1f)
        );
    }

    protected void evokeVfx(OrbFlareEffect.OrbFlareColor color) {
        AbstractDungeon.actionManager.addToBottom(
                new VFXAction(new OrbFlareEffect(this, color), 0.1f)
        );
    }
}