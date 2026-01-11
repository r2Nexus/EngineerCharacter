package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ExhaustWagonCardsInHandAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Derailment extends BaseCard {
    public static final String ID = makeID("Derailment");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    // per exhausted
    private static final int BLOCK_PER = 7;
    private static final int UPG_BLOCK_PER = 2;

    public Derailment() {
        super(ID, info, BasicMod.imagePath("cards/skill/derailment.png"));
        setBlock(BLOCK_PER, UPG_BLOCK_PER);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExhaustWagonCardsInHandAction(exhausted -> {
            if (exhausted <= 0) return;
            addToBot(new GainBlockAction(p, p, block * exhausted));
        }));
    }
}
