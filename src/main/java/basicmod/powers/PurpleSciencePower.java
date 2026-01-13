package basicmod.powers;

import basicmod.BasicMod;
import basicmod.cards.Material;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class PurpleSciencePower extends BasePower {
    public static final String POWER_ID = makeID("PurpleSciencePower");

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
    public void updateDescription() {
        this.description = DESCRIPTIONS[0]
                .replace("!M!", Integer.toString(this.amount));
    }
}
