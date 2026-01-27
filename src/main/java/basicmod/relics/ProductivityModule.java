package basicmod.relics;

import basicmod.patches.AbstractCardEnum;
import basicmod.util.ConsumeEvents;
import basicmod.util.OnMaterialConsumedListener;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class ProductivityModule extends BaseRelic implements OnMaterialConsumedListener {
    public static final String ID = makeID("ProductivityModule");

    private int pendingDraw = 0;

    public ProductivityModule() {
        super(ID, "productivity_module", AbstractCardEnum.ENGINEER, RelicTier.RARE, LandingSound.CLINK);
        this.counter = -1;
    }

    @Override
    public void atPreBattle() {
        this.counter = 0;
        this.pendingDraw = 0;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.pendingDraw = 0;
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player != null && pendingDraw > 0) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DrawCardAction(AbstractDungeon.player, pendingDraw));
            pendingDraw = 0;
        }
    }

    @Override
    public void onMaterialConsumed(AbstractCard materialCard) {
        if (AbstractDungeon.player == null) return;

        this.counter++;

        if (this.counter >= 3) {
            int draws = this.counter / 3;
            this.counter = this.counter % 3;

            boolean shouldDefer =
                    ConsumeEvents.isEndOfTurnContext()
                            || (AbstractDungeon.actionManager != null && AbstractDungeon.actionManager.turnHasEnded);

            if (shouldDefer) {
                pendingDraw += draws;
                // Optional: show feedback now too
                // flash();
                // addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            } else {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new DrawCardAction(AbstractDungeon.player, draws));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ProductivityModule();
    }
}
