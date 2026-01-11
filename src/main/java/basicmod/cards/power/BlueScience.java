package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;

public class BlueScience extends BaseCard {
    public static final String ID = makeID("BlueScience");

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public BlueScience() {
        super(ID, info, BasicMod.imagePath("cards/power/blue_science.png"));
        tags.add(CardTagEnum.SCIENCE);

        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new FocusPower(p, magicNumber),
                magicNumber
        ));
    }
}
