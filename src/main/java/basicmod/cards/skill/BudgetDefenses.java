package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BudgetDefenses extends BaseCard {
    public static final String ID = makeID("BudgetDefenses");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 0;

    private static final int DRAW = 1;
    private static final int UPG_DRAW = 1;

    public BudgetDefenses() {
        super(ID, info, BasicMod.imagePath("cards/skill/budget_defenses.png"));
        setBlock(BLOCK, UPG_BLOCK);
        setCustomVar("DRAW", VariableType.BLOCK, DRAW, UPG_DRAW);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new MakeTempCardInHandAction(new Material(), customVar("DRAW")));
    }
}
