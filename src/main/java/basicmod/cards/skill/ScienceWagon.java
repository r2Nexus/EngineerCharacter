package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import basicmod.util.ScienceCardPool;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ScienceWagon extends BaseCard {
    public static final String ID = makeID("ScienceWagon");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int AMOUNT = 1;
    private static final int UPG_AMOUNT = 1;

    public ScienceWagon() {
        super(ID, info, BasicMod.imagePath("cards/skill/science_wagon.png"));
        tags.add(CardTagEnum.WAGON);

        setMagic(AMOUNT, UPG_AMOUNT);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            AbstractCard science = ScienceCardPool.getRandom();
            if (science == null) return;

            // costs 0 this turn
            science.freeToPlayOnce = true;
            science.costForTurn = 0;
            science.isCostModifiedForTurn = true;

            addToBot(new MakeTempCardInHandAction(science, 1));
        }
    }

}
