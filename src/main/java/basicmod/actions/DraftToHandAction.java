package basicmod.actions;

import basicmod.util.EngineerDraftPool;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class DraftToHandAction extends AbstractGameAction {
    private final CardGroup options = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private final int picks;
    private final String prompt;

    public DraftToHandAction(int poolSize, int picks, String prompt) {
        this.picks = picks;
        this.prompt = prompt;

        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;

        ArrayList<AbstractCard> generated = EngineerDraftPool.randomOptions(poolSize);
        for (AbstractCard c : generated) {
            options.addToTop(c);
        }
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            int actualPicks = Math.min(picks, options.size());
            AbstractDungeon.gridSelectScreen.open(options, actualPicks, prompt, false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard picked : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractCard copy = picked.makeCopy();

                // 0 cost this turn
                copy.setCostForTurn(0);
                copy.isCostModifiedForTurn = true;

                // add to hand (with overflow handling like the base game)
                if (AbstractDungeon.player.hand.size() >= 10) {
                    AbstractDungeon.player.createHandIsFullDialog();
                    AbstractDungeon.player.discardPile.addToTop(copy);
                } else {
                    AbstractDungeon.player.hand.addToTop(copy);
                    copy.triggerWhenDrawn();
                }

                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.glowCheck();
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
            return;
        }

        tickDuration();
    }
}
