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

public class ConsumeMaterialAction extends AbstractGameAction {
    private final int amount;
    private final Runnable onSuccess;
    private final Runnable onFail;

    private final boolean useHand;
    private final boolean useDraw;
    private final boolean useDiscard;

    private final boolean endOfTurnContext;

    public ConsumeMaterialAction(int amount, Runnable onSuccess) {
        this(amount, onSuccess, null, true, false, false, false);
    }

    public ConsumeMaterialAction(int amount, Runnable onSuccess,
                                 boolean useHand, boolean useDraw, boolean useDiscard) {
        this(amount, onSuccess, null, useHand, useDraw, useDiscard, false);
    }

    public ConsumeMaterialAction(int amount, Runnable onSuccess,
                                 boolean useHand, boolean useDraw, boolean useDiscard,
                                 boolean endOfTurnContext) {
        this(amount, onSuccess, null, useHand, useDraw, useDiscard, endOfTurnContext);
    }

    public ConsumeMaterialAction(int amount, Runnable onSuccess, Runnable onFail) {
        this(amount, onSuccess, onFail, true, false, false, false);
    }

    public ConsumeMaterialAction(int amount, Runnable onSuccess, Runnable onFail,
                                 boolean useHand, boolean useDraw, boolean useDiscard) {
        this(amount, onSuccess, onFail, useHand, useDraw, useDiscard, false);
    }

    public ConsumeMaterialAction(int amount, Runnable onSuccess, Runnable onFail,
                                 boolean useHand, boolean useDraw, boolean useDiscard,
                                 boolean endOfTurnContext) {
        this.amount = amount;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
        this.useHand = useHand;
        this.useDraw = useDraw;
        this.useDiscard = useDiscard;
        this.endOfTurnContext = endOfTurnContext;
    }

    private static class MatRef {
        AbstractCard card;
        CardGroup from;

        MatRef(AbstractCard card, CardGroup from) {
            this.card = card;
            this.from = from;
        }
    }

    @Override
    public void update() {
        if (amount <= 0) {
            if (onSuccess != null) onSuccess.run();
            isDone = true;
            return;
        }

        // --- PurpleScience
        int virtualPaid = 0;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(PurpleSciencePower.POWER_ID)) {
            PurpleSciencePower p = (PurpleSciencePower) AbstractDungeon.player.getPower(PurpleSciencePower.POWER_ID);
            virtualPaid = p.trySpendVirtualForConsume(amount);
        }
        int realNeeded = amount - virtualPaid;

        if (realNeeded <= 0) {
            int totalConsumed = amount;

            BasicMod.materialConsumedThisTurn += totalConsumed;
            ChargeSystem.onMaterialConsumed(totalConsumed);

            boolean inferredEot = AbstractDungeon.actionManager != null && AbstractDungeon.actionManager.turnHasEnded;
            ConsumeEvents.fireConsume(
                    AbstractDungeon.player,
                    totalConsumed,
                    this.endOfTurnContext || inferredEot
            );

            if (onSuccess != null) onSuccess.run();
            isDone = true;
            return;
        }

        List<MatRef> materials = new ArrayList<>();

        // Priority order: Hand → Draw → Discard
        if (useHand) collect(materials, AbstractDungeon.player.hand);
        if (useDraw) collect(materials, AbstractDungeon.player.drawPile);
        if (useDiscard) collect(materials, AbstractDungeon.player.discardPile);

        if (materials.size() < realNeeded) {
            if (onFail != null) onFail.run();
            isDone = true;
            return;
        }

        for (int i = 0; i < realNeeded; i++) {
            MatRef ref = materials.get(i);
            removeWithConsumeEffect(ref.from, ref.card);
            BasicMod.materialConsumedThisTurn++;
        }

        int totalConsumed = realNeeded + virtualPaid;

        ChargeSystem.onMaterialConsumed(totalConsumed);

        if (onSuccess != null) onSuccess.run();

        boolean inferredEot = AbstractDungeon.actionManager != null && AbstractDungeon.actionManager.turnHasEnded;
        ConsumeEvents.fireConsume(
                AbstractDungeon.player,
                totalConsumed,
                this.endOfTurnContext || inferredEot
        );

        isDone = true;
    }

    private void collect(List<MatRef> out, CardGroup group) {
        for (AbstractCard c : group.group) {
            if (c instanceof Material) {
                out.add(new MatRef(c, group));
            }
        }
    }

    private void removeWithConsumeEffect(CardGroup from, AbstractCard card) {
        card.unhover();
        card.stopGlowing();
        card.isGlowing = false;

        AbstractCard vfxCard = card.makeStatEquivalentCopy();
        vfxCard.current_x = card.current_x;
        vfxCard.current_y = card.current_y;
        vfxCard.target_x = card.current_x;
        vfxCard.target_y = card.current_y;
        vfxCard.drawScale = card.drawScale;
        vfxCard.angle = card.angle;
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
