package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PerimeterWall extends BaseCard {
    public static final String ID = makeID("PerimeterWall");

    private static final int BLOCK = 2;
    private static final int UPG_BLOCK = 1;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public PerimeterWall() {
        super(ID, info, BasicMod.imagePath("cards/power/perimeter_wall.png"));
        setMagic(BLOCK, UPG_BLOCK);
        setCostUpgrade(-1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new basicmod.powers.PerimeterWall(p, this.magicNumber),
                this.magicNumber
        ));
    }
}
