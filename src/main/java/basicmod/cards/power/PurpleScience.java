package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.powers.PurpleSciencePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PurpleScience extends BaseCard {
    public static final String ID = makeID("PurpleScience");

    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 0;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public PurpleScience() {
        super(ID, info, BasicMod.imagePath("cards/power/purple_science.png"));
        tags.add(CardTagEnum.SCIENCE);
        setCostUpgrade(-1);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new PurpleSciencePower(p, magicNumber),
                magicNumber
        ));
    }
}
