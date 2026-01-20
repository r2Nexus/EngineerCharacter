package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.RoboportPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Roboport extends BaseCard {
    public static final String ID = makeID("Roboport");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3
    );

    private static final int SCRY = 5;
    private static final int UPG_SCRY = 2;

    public Roboport() {
        super(ID, info, BasicMod.imagePath("cards/power/roboport.png"));
        setMagic(SCRY, UPG_SCRY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RoboportPower(p, this.magicNumber), this.magicNumber));
    }
}
