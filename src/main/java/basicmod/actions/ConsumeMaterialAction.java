package basicmod.actions;

import basicmod.BasicMod;
import basicmod.cards.Material;
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

    private final boolean useHand;
    private final boolean useDraw;
    private final boolean useDiscard;

    public ConsumeMaterialAction(int amount, Runnable onSuccess) {
        this(amount, onSuccess, true, false, false);
    }

    public ConsumeMaterialAction(int amount, Runnable onSuccess,
                                 boolean useHand, boolean useDraw, boolean useDiscard) {
        this.amount = amount;
        this.onSuccess = onSuccess;
        this.useHand = useHand;
        this.useDraw = useDraw;
        this.useDiscard = useDiscard;
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

        List<MatRef> materials = new ArrayList<>();

        // Priority order: Hand → Draw → Discard
        if (useHand) {
            collect(materials, AbstractDungeon.player.hand);
        }
        if (useDraw) {
            collect(materials, AbstractDungeon.player.drawPile);
        }
        if (useDiscard) {
            collect(materials, AbstractDungeon.player.discardPile);
        }

        if (materials.size() < amount) {
            isDone = true;
            return;
        }

        for (int i = 0; i < amount; i++) {
            MatRef ref = materials.get(i);
            removeWithConsumeEffect(ref.from, ref.card);
            BasicMod.materialConsumedThisTurn++;
        }

        // Charge system
        ChargeSystem.onMaterialConsumed(amount);

        if (onSuccess != null) {
            onSuccess.run();
        }

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
        // prevent hover jank
        card.unhover();
        card.stopGlowing();
        card.isGlowing = false;

        // spawn VFX using a copy (safer)
        AbstractCard vfxCard = card.makeStatEquivalentCopy();
        vfxCard.current_x = card.current_x;
        vfxCard.current_y = card.current_y;
        vfxCard.target_x  = card.current_x;
        vfxCard.target_y  = card.current_y;
        vfxCard.drawScale = card.drawScale;
        vfxCard.angle     = card.angle;
        vfxCard.transparency = card.transparency;

        AbstractDungeon.effectList.add(new ConsumeCardEffect(vfxCard));

        // remove mechanically (no exhaust)
        from.group.remove(card);

        if (from == AbstractDungeon.player.hand) {
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }

        ConsumeEvents.fireMaterialConsumed(AbstractDungeon.player, card);
    }

}
