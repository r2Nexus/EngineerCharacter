package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class YellowBelt extends BaseCard {
    public static final String ID = makeID("YellowBelt");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int DRAW = 1;
    private static final int UPG_DRAW = 1;
    private static final int EVOKE = 1;

    public YellowBelt() {
        super(ID, info, BasicMod.imagePath("cards/skill/yellow_belt.png"));
        setMagic(DRAW, UPG_DRAW);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EvokeOrbAction(EVOKE));
        addToBot(new DrawCardAction(p, magicNumber));
    }
}
