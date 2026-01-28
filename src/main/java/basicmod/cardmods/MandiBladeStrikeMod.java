package basicmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class MandiBladeStrikeMod extends AbstractCardModifier {
    public static final String ID = "${modID}:MandiBladeStrikeMod";

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        // Make it exhaust
        card.exhaust = true;

        // Reduce cost by 1, but don’t go below 0
        if (card.cost >= 0) {
            card.updateCost(-1);
            // updateCost already clamps and updates costForTurn as needed
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MandiBladeStrikeMod();
    }

    public static boolean has(AbstractCard c) {
        return CardModifierManager.hasModifier(c, ID);
    }

    public static void apply(AbstractCard c) {
        if (!has(c)) {
            CardModifierManager.addModifier(c, new MandiBladeStrikeMod());
        }
    }
}
