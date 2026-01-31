package basicmod.actions;

import basicmod.BasicMod;
import basicmod.cards.other.Material;
import basicmod.powers.PurpleSciencePower;
import basicmod.util.ChargeSystem;
import basicmod.util.ConsumeCardEffect;
import basicmod.util.ConsumeEvents;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConsumeAllMaterialAction extends AbstractGameAction {
    private final Consumer<Integer> onConsumed;
    private final boolean includeHand;
    private final boolean includeDraw;
    private final boolean includeDiscard;

    private final boolean endOfTurnContext;

    public ConsumeAllMaterialAction(Consumer<Integer> onConsumed) {
        this(onConsumed, true, true, true, false);
    }

    public ConsumeAllMaterialAction(Consumer<Integer> onConsumed,
                                    boolean includeHand,
                                    boolean includeDraw,
                                    boolean includeDiscard) {
        this(onConsumed, includeHand, includeDraw, includeDiscard, false);
    }

    public ConsumeAllMaterialAction(Consumer<Integer> onConsumed,
                                    boolean includeHand,
                                    boolean includeDraw,
                                    boolean includeDiscard,
                                    boolean endOfTurnContext) {
        this.onConsumed = onConsumed;
        this.includeHand = includeHand;
        this.includeDraw = includeDraw;
        this.includeDiscard = includeDiscard;
        this.endOfTurnContext = endOfTurnContext;
    }

    @Override
    public void update() {
        int realConsumed = 0;

        if (includeHand) {
            realConsumed += consumeFromGroup(AbstractDungeon.player.hand);
        }
        if (includeDraw) {
            realConsumed += consumeFromGroup(AbstractDungeon.player.drawPile);
        }
        if (includeDiscard) {
            realConsumed += consumeFromGroup(AbstractDungeon.player.discardPile);
        }

        // --- PurpleScience
        int virtualConsumed = 0;
        if (includeHand && AbstractDungeon.player != null && AbstractDungeon.player.hasPower(PurpleSciencePower.POWER_ID)) {
            PurpleSciencePower p = (PurpleSciencePower) AbstractDungeon.player.getPower(PurpleSciencePower.POWER_ID);

            virtualConsumed = p.trySpendVirtualForConsume(p.amount);
        }
        // ---------------------------------------------------------------------------

        int totalConsumed = realConsumed + virtualConsumed;

        if (totalConsumed > 0) {
            BasicMod.materialConsumedThisTurn += totalConsumed;
        }

        ChargeSystem.onMaterialConsumed(totalConsumed);

        if (onConsumed != null) {
            onConsumed.accept(totalConsumed);
        }

        boolean inferredEot = AbstractDungeon.actionManager != null && AbstractDungeon.actionManager.turnHasEnded;
        ConsumeEvents.fireConsume(
                AbstractDungeon.player,
                totalConsumed,
                this.endOfTurnContext || inferredEot
        );

        isDone = true;
    }

    private int consumeFromGroup(CardGroup group) {
        List<AbstractCard> mats = new ArrayList<>();
        for (AbstractCard c : group.group) {
            if (c instanceof Material) {
                mats.add(c);
            }
        }

        for (AbstractCard c : mats) {
            removeWithConsumeEffect(group, c);
        }

        return mats.size();
    }

    private void removeWithConsumeEffect(CardGroup from, AbstractCard card) {
        card.unhover();
        card.stopGlowing();
        card.isGlowing = false;

        AbstractCard vfxCard = card.makeStatEquivalentCopy();
        vfxCard.current_x = card.current_x;
        vfxCard.current_y = card.current_y;
        vfxCard.target_x  = card.current_x;
        vfxCard.target_y  = card.current_y;
        vfxCard.drawScale = card.drawScale;
        vfxCard.angle     = card.angle;
        vfxCard.transparency = card.transparency;

        AbstractDungeon.effectList.add(new ConsumeCardEffect(vfxCard));

        from.group.remove(card);

        if (from == AbstractDungeon.player.hand) {
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }

        boolean inferredEot = AbstractDungeon.actionManager != null && AbstractDungeon.actionManager.turnHasEnded;
        ConsumeEvents.fireMaterialConsumed(
                AbstractDungeon.player,
                card,
                this.endOfTurnContext || inferredEot
        );
    }
}
