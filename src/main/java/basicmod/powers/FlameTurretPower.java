package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class FlameTurretPower extends BasePower {
    public static final String POWER_ID = makeID("FlameTurretPower");

    public FlameTurretPower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                true, // one-turn effect (we remove next turn)
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/flame_turret_power.png"),
                BasicMod.imagePath("powers/large/flame_turret_power.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        // "This turn, whenever a Turret fires, deal !M! damage to ALL enemies."
        this.description = DESCRIPTIONS[0].replace("!M!", Integer.toString(this.amount));
    }
}
