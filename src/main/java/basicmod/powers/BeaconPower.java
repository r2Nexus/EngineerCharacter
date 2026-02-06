package basicmod.powers;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;

import static basicmod.BasicMod.makeID;

public class BeaconPower extends BasePower {
    public static final String POWER_ID = makeID("BeaconPower");

    private static final int FOCUS_ON_SUCCESS = 1;
    private static final int FOCUS_ON_FAIL = 1;

    private final int cost;

    public BeaconPower(AbstractCreature owner, int cost) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                1,
                BasicMod.imagePath("powers/beacon_power.png"),
                BasicMod.imagePath("powers/large/beacon_power.png")
        );
        this.cost = Math.max(0, cost);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {}

    @Override
    public void atStartOfTurn() {
        if (owner == null || AbstractDungeon.actionManager == null) return;

        addToBot(new ConsumeMaterialAction(
                cost,
                // onSuccess
                () -> {
                    flash();
                    addToTop(new ApplyPowerAction(owner, owner, new FocusPower(owner, FOCUS_ON_SUCCESS), FOCUS_ON_SUCCESS));
                },
                // onFail
                () -> {
                    flash();
                    addToTop(new ApplyPowerAction(owner, owner, new FocusPower(owner, -FOCUS_ON_FAIL), -FOCUS_ON_FAIL));
                },
                // useHand/useDraw/useDiscard
                true, true, true
        ));
    }

    @Override
    public void updateDescription() {
        this.description =
                DESCRIPTIONS[0] + cost +
                        DESCRIPTIONS[1] + FOCUS_ON_SUCCESS +
                        DESCRIPTIONS[2] + FOCUS_ON_FAIL +
                        DESCRIPTIONS[3];
    }
}
