package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class RoboportPower extends BasePower {
    public static final String POWER_ID = makeID("RoboportPower");

    public RoboportPower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/example.png"),
                BasicMod.imagePath("powers/large/example.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToTop(new ScryAction(amount));
    }

    @Override
    public void updateDescription() {
        // "At the start of your turn, Scry !M!."
        this.description = DESCRIPTIONS[0].replace("!M!", Integer.toString(this.amount));
    }
}
