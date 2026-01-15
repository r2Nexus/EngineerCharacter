package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class SuppressiveFirePower extends BasePower {
    public static final String POWER_ID = makeID("SuppressiveFirePower");

    public SuppressiveFirePower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                owner,
                amount,
                BasicMod.imagePath("powers/example.png"),
                BasicMod.imagePath("powers/large/suppressive_fire_power.png")
        );
        updateDescription();
    }
}
