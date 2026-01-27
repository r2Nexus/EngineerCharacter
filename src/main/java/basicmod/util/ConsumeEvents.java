package basicmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ConsumeEvents {
    private ConsumeEvents() {}

    // Readable by listeners during the callback (synchronous)
    private static boolean endOfTurnContext = false;

    public static boolean isEndOfTurnContext() {
        return endOfTurnContext;
    }

    public static void fireMaterialConsumed(AbstractCreature owner, AbstractCard materialCard) {
        fireMaterialConsumed(owner, materialCard, false);
    }

    public static void fireMaterialConsumed(AbstractCreature owner, AbstractCard materialCard, boolean isEndOfTurnContext) {
        if (owner == null) return;

        boolean prev = endOfTurnContext;
        endOfTurnContext = isEndOfTurnContext;
        try {
            // Powers on owner
            for (AbstractPower p : owner.powers) {
                if (p instanceof OnMaterialConsumedListener) {
                    ((OnMaterialConsumedListener) p).onMaterialConsumed(materialCard);
                }
            }

            // Relics (player only)
            if (AbstractDungeon.player != null && owner == AbstractDungeon.player) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof OnMaterialConsumedListener) {
                        ((OnMaterialConsumedListener) r).onMaterialConsumed(materialCard);
                    }
                }
            }
        } finally {
            endOfTurnContext = prev;
        }
    }
}
