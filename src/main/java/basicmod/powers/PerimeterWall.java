package basicmod.powers;

import basicmod.BasicMod;
import basicmod.cards.Material;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class PerimeterWall extends BasePower {
    public static final String POWER_ID = makeID("PerimeterWall");

    public PerimeterWall(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/perimeter_wall_power.png"),
                BasicMod.imagePath("powers/large/perimeter_wall_power.png")
        );
        updateDescription();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (card instanceof Material) {
            flash();
            addToBot(new GainBlockAction(owner, owner, this.amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("!B!", Integer.toString(this.amount));
    }
}
