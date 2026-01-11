package basicmod.actions;

import basicmod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ExhaustWagonCardsInHandAction extends AbstractGameAction {
    private final Consumer<Integer> onExhausted;
    private final ArrayList<AbstractCard> toExhaust = new ArrayList<>();

    public ExhaustWagonCardsInHandAction(Consumer<Integer> onExhausted) {
        this.onExhausted = onExhausted;
    }

    @Override
    public void update() {
        toExhaust.clear();
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.hasTag(CardTagEnum.WAGON)) {
                toExhaust.add(c);
            }
        }

        int count = toExhaust.size();

        // exhaust them
        for (AbstractCard c : toExhaust) {
            addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
        }

        // callback
        if (onExhausted != null) {
            onExhausted.accept(count);
        }

        isDone = true;
    }
}
