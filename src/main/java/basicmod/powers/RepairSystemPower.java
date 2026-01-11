package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class RepairSystemPower extends BasePower {
    public static final String POWER_ID = makeID("RepairSystemPower");
    private static final int THRESHOLD = 2;

    public RepairSystemPower(AbstractCreature owner, int blockAmount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                owner,
                blockAmount,
                BasicMod.imagePath("powers/repair_system_power.png"),
                BasicMod.imagePath("powers/large/repair_system_power.png")
        );
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;

        if (BasicMod.materialConsumedThisTurn >= THRESHOLD) {
            flash();
            addToBot(new GainBlockAction(owner, owner, this.amount));
        }

        BasicMod.materialConsumedThisTurn = 0;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("!B!", Integer.toString(this.amount));
    }
}
