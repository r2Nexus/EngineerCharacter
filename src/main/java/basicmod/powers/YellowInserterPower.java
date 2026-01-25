package basicmod.powers;

import basicmod.BasicMod;
import basicmod.cards.Material;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static basicmod.BasicMod.makeID;

public class YellowInserterPower extends BasePower {
    public static final String POWER_ID = makeID("YellowInserterPower");

    private final int draw;
    private final int mat;

    public YellowInserterPower(AbstractCreature owner, int draw, int mat) {
        super(
                POWER_ID,
                PowerType.BUFF,
                true,           // turnBased (one-shot)
                owner,
                null,
                1,
                BasicMod.imagePath("powers/yellow_inserter_power.png"),
                BasicMod.imagePath("powers/large/yellow_inserter_power.png")
        );
        this.draw = draw;
        this.mat = mat;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new DrawCardAction(draw));
        addToBot(new MakeTempCardInHandAction(new Material(), mat));
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        // put this in your power strings:
        // "At the start of your next turn, draw !D! and add !M! Material to your hand."
        this.description = DESCRIPTIONS[0]
                .replace("!D!", Integer.toString(draw))
                .replace("!M!", Integer.toString(mat));
    }
}
