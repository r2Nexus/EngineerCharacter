package basicmod.powers;

import basicmod.BasicMod;
import basicmod.actions.AddMaterialAction;
import basicmod.cards.other.Material;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;
import static basicmod.actions.AddMaterialAction.Destination.HAND;

public class RedSciencePower extends BasePower {
    public static final String POWER_ID = makeID("RedSciencePower");

    public RedSciencePower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/red_science.png"),
                BasicMod.imagePath("powers/large/red_science.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new AddMaterialAction(this.amount, HAND));
    }

    @Override
    public void updateDescription() {
        // "At start of turn, gain !M! Material."
        this.description = DESCRIPTIONS[0].replace("!M!", Integer.toString(this.amount));
    }
}
