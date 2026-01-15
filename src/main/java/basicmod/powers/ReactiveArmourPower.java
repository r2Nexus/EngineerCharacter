package basicmod.powers;

import basicmod.BasicMod;
import basicmod.orbs.LandMineOrb;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class ReactiveArmourPower extends BasePower {
    public static final String POWER_ID = makeID("ReactiveArmourPower");

    private boolean triggeredThisTurn = false;

    public ReactiveArmourPower(AbstractCreature owner) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                owner,
                -1, // hide number (and we don't use amount)
                BasicMod.imagePath("powers/reactive_armour_power.png"),
                BasicMod.imagePath("powers/large/reactive_armour_power.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        triggeredThisTurn = false;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        // "unblocked damage" = actual HP loss
        if (damageAmount > 0 && !triggeredThisTurn) {
            triggeredThisTurn = true;
            flash();
            addToBot(new ChannelAction(new LandMineOrb()));
        }
        return damageAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0]
                .replace("!M!", Integer.toString(this.amount));
    }
}
