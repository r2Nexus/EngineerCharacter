package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.BeltFedPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BeltFed extends BaseCard {
    public static final String ID = makeID("BeltFed");

    private static final int SLOTS = 2;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public BeltFed() {
        super(ID, info, BasicMod.imagePath("cards/power/belt_fed.png"));
        setCostUpgrade(baseCost - 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ApplyPowerAction(p,p, new BeltFedPower(p,-1),0));
    }
}
