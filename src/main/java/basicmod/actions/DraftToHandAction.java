package basicmod.actions;

import basicmod.util.EngineerDraftPool;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
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

        // IMPORTANT: initialize duration so the action doesn't finish instantly
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;

        ArrayList<AbstractCard> generated = EngineerDraftPool.randomOptions(poolSize);
        for (AbstractCard c : generated) {
            options.addToTop(c);
        }
    }

    @Override
    public void update() {
        // First update tick: open the selection screen
        if (this.duration == this.startDuration) {
            int actualPicks = Math.min(picks, options.size());
            AbstractDungeon.gridSelectScreen.open(options, actualPicks, prompt, false, false, false, false);
            tickDuration();
            return;
        }

        // After player picks cards, they'll appear here
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard picked : AbstractDungeon.gridSelectScreen.selectedCards) {
                addToTop(new MakeTempCardInHandAction(picked.makeCopy(), 1));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
            return;
        }

        tickDuration();
    }
}
