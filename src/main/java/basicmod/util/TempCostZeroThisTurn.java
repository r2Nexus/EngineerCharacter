package basicmod.util;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TempCostZeroThisTurn extends AbstractCardModifier {
    @Override
    public void onInitialApplication(AbstractCard card) {
        card.setCostForTurn(0);
        card.freeToPlayOnce = true;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        // when removed, card will go back to normal cost calculations
        card.resetAttributes();
        card.applyPowers();
        return true;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempCostZeroThisTurn();
    }
}
