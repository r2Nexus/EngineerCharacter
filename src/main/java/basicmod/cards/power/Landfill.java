package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Landfill extends BaseCard {
    public static final String ID = makeID("Landfill");

    private static final int SLOTS = 2;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public Landfill() {
        super(ID, info, BasicMod.imagePath("cards/power/landfill.png"));
        setMagic(SLOTS);
        setCostUpgrade(-1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new IncreaseMaxOrbAction(magicNumber));
    }
}
