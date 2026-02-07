package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.powers.RedSciencePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RedScience extends BaseCard {
    public static final String ID = makeID("RedScience");

    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 0;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    public RedScience() {
        super(ID, info, BasicMod.imagePath("cards/power/red_science.png"));
        tags.add(CardTagEnum.SCIENCE);

        setMagic(MAGIC, UPG_MAGIC);
        cardsToPreview = new Material();
        setInnate(false, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p, p,
                new RedSciencePower(p, magicNumber),
                magicNumber
        ));
    }
}
