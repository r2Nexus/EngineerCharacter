package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.YellowInserterPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class YellowInserter extends BaseCard {
    public static final String ID = makeID("YellowInserter");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int DRAW = 1;
    private static final int UPG_DRAW = 0;

    private static final int MAT = 1;
    private static final int UPG_MAT = 1;

    public YellowInserter() {
        super(ID, info, BasicMod.imagePath("cards/skill/yellow_inserter.png"));
        setMagic(DRAW, UPG_DRAW);
        setCustomVar("MAT", VariableType.MAGIC, MAT, UPG_MAT);

        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // now
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new MakeTempCardInHandAction(new Material(), customVar("MAT")));

        // next turn
        addToBot(new ApplyPowerAction(p, p,
                new YellowInserterPower(p, magicNumber, customVar("MAT")),
                1
        ));
    }
}
