package basicmod.potions;

import basicmod.util.ChargeSystem;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static basicmod.BasicMod.makeID;

public class OverclockPotion extends BasePotion {
    public static final String ID = makeID("OverclockPotion");

    private static final int BASE_DRAW = 2;     // Sacred Bark will typically double potency -> draw 4
    private static final int FULL_CHARGE = 999; // "fully charge" everything with ChargeMod

    public static final Color LIQUID_COLOR  = new Color(0.05f, 0.08f, 0.20f, 1.00f);
    public static final Color HYBRID_COLOR  = new Color(0.20f, 0.55f, 1.00f, 0.95f);
    public static final Color SPOTS_COLOR   = new Color(0.72f, 0.93f, 1.00f, 1.00f);

    public OverclockPotion() {
        super(ID, BASE_DRAW, PotionRarity.UNCOMMON, PotionSize.BOLT, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);

        this.isThrown = false;
        this.targetRequired = false;

        Color outline = new Color(0.72f, 0.42f, 0.16f, 1f);
        this.labOutlineColor = outline.cpy();
    }

    @Override
    public boolean canUse() {
        if (AbstractDungeon.player == null) return false;
        if (AbstractDungeon.getCurrRoom() == null) return false;

        // In combat: keep normal restriction
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            return AbstractDungeon.actionManager != null && !AbstractDungeon.actionManager.turnHasEnded;
        }

        return true;
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.player == null) return;

        boolean inCombat = AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;

        // Draw only makes sense in combat
        if (inCombat) {
            addToBot(new DrawCardAction(AbstractDungeon.player, this.potency));
        }

        // Fully charge everything
        ChargeSystem.onMaterialConsumed(FULL_CHARGE);
    }

    @Override
    public String getDescription() {
        // Uses !P! so Sacred Bark displays correctly
        return DESCRIPTIONS[0].replace("!P!", Integer.toString(this.potency));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new OverclockPotion();
    }
}
