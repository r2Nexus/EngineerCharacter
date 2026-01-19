package basicmod.actions;

import basicmod.cardmods.ChargeMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SpendChargeAction extends AbstractGameAction {
    private final AbstractCard card;
    private final Runnable onSuccess;
    private final Runnable onFail;

    public SpendChargeAction(AbstractCard card, Runnable onSuccess) {
        this(card, onSuccess, null);
    }

    public SpendChargeAction(AbstractCard card, Runnable onSuccess, Runnable onFail) {
        this.card = card;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }

    @Override
    public void update() {
        ChargeMod cm = ChargeMod.get(card);
        if (cm != null && cm.trySpendFull(card)) {
            if (onSuccess != null) onSuccess.run();
        } else {
            if (onFail != null) onFail.run();
        }
        isDone = true;
    }
}
