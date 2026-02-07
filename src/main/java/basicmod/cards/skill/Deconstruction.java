package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Deconstruction extends BaseCard {
    public static final String ID = makeID("Deconstruction");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private static final int EVOKE = 1;
    private static final int MAGIC = 1;

    public Deconstruction() {
        super(ID, info, BasicMod.imagePath("cards/skill/deconstruction.png"));
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(upgraded) addToBot(new EvokeWithoutRemovingOrbAction(EVOKE));
        addToBot(new EvokeOrbAction(EVOKE));
        addToBot(new IncreaseMaxOrbAction(magicNumber));
    }
}
