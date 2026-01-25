package basicmod.powers;

import basicmod.BasicMod;
import basicmod.orbs.MinerOrb; // <- adjust to your actual Miner orb class/package
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class EconomyBoomPower extends BasePower {
    public static final String POWER_ID = makeID("EconomyBoomPower");

    public EconomyBoomPower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/economy_boom_power.png"),
                BasicMod.imagePath("powers/large/economy_boom_power.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        for (int i = 0; i < amount; i++) {
            addToBot(new ChannelAction(new MinerOrb()));
        }
    }

    @Override
    public void updateDescription() {
        // "At the start of your turn, Channel !M! Miner."
        this.description = DESCRIPTIONS[0].replace("!M!", Integer.toString(this.amount));
    }
}
