package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class BeltFedPower extends BasePower {
    public static final String POWER_ID = makeID("BeltFedPower");

    public BeltFedPower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                owner,
                amount,
                BasicMod.imagePath("powers/example.png"),
                BasicMod.imagePath("powers/large/example.png")
        );
        updateDescription();
    }
}
