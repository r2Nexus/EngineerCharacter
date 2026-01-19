package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Recycler extends BaseCard {
    public static final String ID = makeID("Recycler");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int MAX_EXHAUST = 2;
    private static final int UPG_MAX_EXHAUST = 2;

    public Recycler() {
        super(ID, info, BasicMod.imagePath("cards/skill/recycler.png"));
        setMagic(MAX_EXHAUST, UPG_MAX_EXHAUST);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cap = Math.min(magicNumber, p.hand.size());

        addToBot(new ExhaustAction(p, p, cap, false, true, true) {
            private final int before = p.exhaustPile.size();

            @Override
            public void update() {
                super.update();
                if (this.isDone) {
                    int exhausted = p.exhaustPile.size() - before;
                    if (exhausted > 0) {
                        addToTop(new MakeTempCardInHandAction(new Material(), exhausted));
                    }
                }
            }
        });
    }
}
