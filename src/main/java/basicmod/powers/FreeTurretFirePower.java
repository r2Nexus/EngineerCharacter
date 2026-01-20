package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class FreeTurretFirePower extends BasePower {
    public static final String POWER_ID = makeID("FreeTurretFirePower");

    public FreeTurretFirePower(AbstractCreature owner) {
        super(
                POWER_ID,
                PowerType.BUFF,
                true,
                owner,
                null,
                1,
                BasicMod.imagePath("powers/example.png"),
                BasicMod.imagePath("powers/large/example.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
