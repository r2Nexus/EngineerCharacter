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
