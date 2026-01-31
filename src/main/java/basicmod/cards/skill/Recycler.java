package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.AddMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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

    public Recycler() {
        super(ID, info, BasicMod.imagePath("cards/skill/recycler.png"));
        cardsToPreview = new Material();
        setCostUpgrade(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hand.isEmpty()) return;

        addToBot(new ExhaustAction(p, p, 1, false, false, false) {
            private final int before = p.exhaustPile.size();

            @Override
            public void update() {
                super.update();

                if (!this.isDone) return;

                int after = p.exhaustPile.size();
                int exhausted = after - before;
                if (exhausted <= 0) return;

                AbstractCard exhaustedCard = p.exhaustPile.group.get(after - 1);

                int mats = getMaterialFromCost(exhaustedCard);
                if (mats > 0) {
                    addToTop(new AddMaterialAction(mats, AddMaterialAction.Destination.HAND));
                }
            }
        });
    }

    private int getMaterialFromCost(AbstractCard c) {
        int cost = c.costForTurn;

        if (cost == -2) return 0;
        if (cost == -1) return 0;
        return Math.max(0, cost);
    }
}
