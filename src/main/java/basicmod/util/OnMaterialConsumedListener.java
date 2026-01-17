package basicmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnMaterialConsumedListener {
    /**
     * Called once per consumed Material card (only when consumption actually happens).
     */
    void onMaterialConsumed(AbstractCard materialCard);
}
