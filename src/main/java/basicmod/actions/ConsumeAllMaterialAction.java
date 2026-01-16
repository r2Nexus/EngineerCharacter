package basicmod.actions;

import basicmod.BasicMod;
import basicmod.cards.Material;
import basicmod.util.ChargeSystem;
import basicmod.util.ConsumeCardEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConsumeAllMaterialAction extends AbstractGameAction {
    private final Consumer<Integer> onConsumed; // gets total number consumed
    private final boolean includeHand;
    private final boolean includeDraw;
    private final boolean includeDiscard;

    public ConsumeAllMaterialAction(Consumer<Integer> onConsumed) {
        this(onConsumed, true, true, true);
    }

    public ConsumeAllMaterialAction(Consumer<Integer> onConsumed,
                                    boolean includeHand,
                                    boolean includeDraw,
                                    boolean includeDiscard) {
        this.onConsumed = onConsumed;
        this.includeHand = includeHand;
        this.includeDraw = includeDraw;
        this.includeDiscard = includeDiscard;
    }

    @Override
    public void update() {
        int consumed = 0;

        if (includeHand) {
            consumed += consumeFromGroup(AbstractDungeon.player.hand);
        }
        if (includeDraw) {
            consumed += consumeFromGroup(AbstractDungeon.player.drawPile);
        }
        if (includeDiscard) {
            consumed += consumeFromGroup(AbstractDungeon.player.discardPile);
        }

        if (consumed > 0) BasicMod.materialConsumedThisTurn += consumed;

        // Charge System
        ChargeSystem.onMaterialConsumed(consumed);

        if (onConsumed != null) {
            onConsumed.accept(consumed);
        }
        isDone = true;
    }

    private int consumeFromGroup(CardGroup group) {
        // 1) collect first (avoid concurrent modification)
        List<AbstractCard> mats = new ArrayList<>();
        for (AbstractCard c : group.group) {
            if (c instanceof Material) {
                mats.add(c);
            }
        }

        // 2) remove with VFX (no exhaust)
        for (AbstractCard c : mats) {
            removeWithConsumeEffect(group, c);
        }

        return mats.size();
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

        // remove mechanically (no exhaust triggers/counters)
        from.group.remove(card);

        if (from == AbstractDungeon.player.hand) {
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }
    }
}
