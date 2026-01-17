package basicmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ConsumeEvents {
    private ConsumeEvents() {}

    public static void fireMaterialConsumed(AbstractCreature owner, AbstractCard materialCard) {
        if (owner == null) return;
        for (AbstractPower p : owner.powers) {
            if (p instanceof OnMaterialConsumedListener) {
                ((OnMaterialConsumedListener) p).onMaterialConsumed(materialCard);
            }
        }
    }
}
