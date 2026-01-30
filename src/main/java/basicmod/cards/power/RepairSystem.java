package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.RepairSystemPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RepairSystem extends BaseCard {
    public static final String ID = makeID("RepairSystem");

    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 0;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public RepairSystem() {
        super(ID, info, BasicMod.imagePath("cards/power/repair_system.png"));
        setMagic(BLOCK, UPG_BLOCK);
        setCostUpgrade(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new RepairSystemPower(p, this.magicNumber),
                this.magicNumber
        ));
    }
}
