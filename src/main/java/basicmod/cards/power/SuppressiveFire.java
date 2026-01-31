package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.SuppressiveFirePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SuppressiveFire extends BaseCard {
    public static final String ID = makeID("SuppressiveFire");

    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 0;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public SuppressiveFire() {
        super(ID, info, BasicMod.imagePath("cards/power/suppressive_fire.png"));
        setCostUpgrade(baseCost - 1);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ApplyPowerAction(p,p, new SuppressiveFirePower(p,magicNumber),magicNumber));
    }
}
