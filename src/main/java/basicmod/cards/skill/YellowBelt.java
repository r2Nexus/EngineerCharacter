package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class YellowBelt extends BaseCard {
    public static final String ID = makeID("YellowBelt");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int DRAW = 2;
    private static final int UPG_DRAW = 0;

    private static final int BONUS = 1;
    private static final int UPG_BONUS = 1;

    public YellowBelt() {
        super(ID, info, BasicMod.imagePath("cards/skill/yellow_belt.png"));
        setMagic(DRAW, UPG_DRAW);
        setCustomVar("BONUS", VariableType.MAGIC, BONUS, UPG_BONUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new MakeTempCardInHandAction(new Material(), customVar("BONUS")));
    }
}
