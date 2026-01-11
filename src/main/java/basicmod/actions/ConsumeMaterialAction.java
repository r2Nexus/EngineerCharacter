package basicmod.actions;

import basicmod.BasicMod;
import basicmod.cards.Material;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ConsumeMaterialAction extends AbstractGameAction {
    private final int amount;
    private final Runnable onSuccess;

    public ConsumeMaterialAction(int amount, Runnable onSuccess) {
        this.amount = amount;
        this.onSuccess = onSuccess;
    }

    @Override
    public void update() {
        // Collect Material cards in hand
        ArrayList<AbstractCard> mats = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof Material) {
                mats.add(c);
            }
        }

        if (mats.size() < amount) {
            isDone = true;
            return;
        }

        // Consume immediately so later actions see the updated hand state
        for (int i = 0; i < amount; i++) {
            AbstractCard c = mats.get(i);
            AbstractDungeon.player.hand.moveToExhaustPile(c);

            // Counter for other cards
            BasicMod.materialConsumedThisTurn++;
        }

        if (onSuccess != null) {
            onSuccess.run();
        }

        isDone = true;
    }
}
