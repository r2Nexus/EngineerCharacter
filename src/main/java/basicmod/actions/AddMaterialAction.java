package basicmod.actions;

import basicmod.cards.other.Material;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class AddMaterialAction extends AbstractGameAction {

    public enum Destination {
        HAND,
        DRAW_PILE,
        DISCARD_PILE
    }

    private final int amount;
    private final Destination destination;

    public AddMaterialAction(int amount, Destination destination) {
        this.amount = Math.max(0, amount);
        this.destination = destination;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (amount <= 0 || AbstractDungeon.player == null) {
            isDone = true;
            return;
        }

        switch (destination) {
            case HAND:
                addToHandLeft(amount);
                break;

            case DRAW_PILE:
                addToDrawPileTop(amount);
                break;

            case DISCARD_PILE:
                addToDiscardPile(amount);
                break;
        }

        isDone = true;
    }

    private void addToHandLeft(int amt) {
        int handSpace = 10 - AbstractDungeon.player.hand.size();
        int toHand = Math.max(0, Math.min(amt, handSpace));
        int toDiscard = amt - toHand;

        if (toDiscard > 0) {
            AbstractDungeon.player.createHandIsFullDialog();
        }

        // Add to LEFT: addToTop
        for (int i = 0; i < toHand; i++) {
            AbstractCard m = makeMaterial();
            AbstractDungeon.player.hand.addToBottom(m);

            m.flash();
            m.triggerWhenDrawn();
        }

        // Overflow goes to discard
        for (int i = 0; i < toDiscard; i++) {
            AbstractDungeon.player.discardPile.addToTop(makeMaterial());
        }

        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
    }

    private void addToDrawPileTop(int amt) {
        for (int i = 0; i < amt; i++) {
            AbstractDungeon.player.drawPile.addToTop(makeMaterial());
        }
    }

    private void addToDiscardPile(int amt) {
        for (int i = 0; i < amt; i++) {
            AbstractDungeon.player.discardPile.addToTop(makeMaterial());
        }
    }

    private AbstractCard makeMaterial() {
        AbstractCard c = new Material();

        // Mirrors MakeTempCardInHandAction behavior
        UnlockTracker.markCardAsSeen(c.cardID);
        if (c.type != CardType.CURSE && c.type != CardType.STATUS
                && AbstractDungeon.player.hasPower("MasterRealityPower")) {
            c.upgrade();
        }

        return c;
    }
}
