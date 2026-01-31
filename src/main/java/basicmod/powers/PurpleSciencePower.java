package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class PurpleSciencePower extends BasePower {
    public static final String POWER_ID = makeID("PurpleSciencePower");

    // Once-per-turn gate
    private boolean usedThisTurn = false;

    public PurpleSciencePower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/purple_science.png"),
                BasicMod.imagePath("powers/large/purple_science.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
    }

    public int trySpendVirtualForConsume(int requested) {
        if (usedThisTurn) return 0;
        if (requested <= 0) return 0;

        usedThisTurn = true;
        flash();

        return Math.min(this.amount, requested);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0]
                .replace("!M!", Integer.toString(this.amount));
    }
}
