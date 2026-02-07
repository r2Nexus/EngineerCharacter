package basicmod.powers;

import basemod.BaseMod;
import basicmod.BasicMod;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class ExpandedPocketsPower extends BasePower {
    public static final String POWER_ID = makeID("ExpandedPocketsPower");

    private static final int TOTAL_HAND_SIZE_CAP = 20;

    private int appliedBonus = 0;

    public ExpandedPocketsPower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                Math.max(0, amount),
                BasicMod.imagePath("powers/expanded_pockets_power.png"),
                BasicMod.imagePath("powers/large/expanded_pockets_power.png")
        );
    }

    @Override
    public void onInitialApplication() {
        recalcAndApply();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = Math.max(0, this.amount + stackAmount);
        recalcAndApply();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount = Math.max(0, this.amount - reduceAmount);
        recalcAndApply();
    }

    @Override
    public void onRemove() {
        // Revert only what we applied.
        BaseMod.MAX_HAND_SIZE -= appliedBonus;
        appliedBonus = 0;
    }

    @Override
    public void onVictory() {
        // Extra safety: powers usually get cleared anyway.
        BaseMod.MAX_HAND_SIZE -= appliedBonus;
        appliedBonus = 0;
    }

    private void recalcAndApply() {
        // Remove previous contribution first (idempotent)
        BaseMod.MAX_HAND_SIZE -= appliedBonus;
        appliedBonus = 0;

        // Baseline without THIS power applied
        int baseline = BaseMod.MAX_HAND_SIZE;

        // Enforce total cap
        int roomForMore = Math.max(0, TOTAL_HAND_SIZE_CAP - baseline);

        int desired = Math.max(0, this.amount);
        appliedBonus = Math.min(desired, roomForMore);

        BaseMod.MAX_HAND_SIZE += appliedBonus;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description =
                DESCRIPTIONS[0] + this.amount +
                        DESCRIPTIONS[1] + TOTAL_HAND_SIZE_CAP +
                        DESCRIPTIONS[2];
    }
}
