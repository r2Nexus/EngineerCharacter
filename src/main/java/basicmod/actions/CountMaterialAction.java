package basicmod.actions;

import basicmod.cards.Material;
import basicmod.util.MaterialUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;

public class CountMaterialAction extends AbstractGameAction {

    private final Consumer<Integer> onCounted;
    private final boolean includeHand;
    private final boolean includeDraw;
    private final boolean includeDiscard;

    public CountMaterialAction(Consumer<Integer> onCounted) {
        this(onCounted, true, true, true);
    }

    public CountMaterialAction(Consumer<Integer> onCounted,
                               boolean includeHand,
                               boolean includeDraw,
                               boolean includeDiscard) {
        this.onCounted = onCounted;
        this.includeHand = includeHand;
        this.includeDraw = includeDraw;
        this.includeDiscard = includeDiscard;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int count = 0;

        if (includeHand)    count += MaterialUtils.countHand(p);
        if (includeDraw)    count += MaterialUtils.countDraw(p);
        if (includeDiscard) count += MaterialUtils.countDiscard(p);

        if (onCounted != null) {
            onCounted.accept(count);
        }

        isDone = true;
    }

    private int countPile(java.util.ArrayList<com.megacrit.cardcrawl.cards.AbstractCard> pile) {
        int c = 0;
        for (com.megacrit.cardcrawl.cards.AbstractCard card : pile) {
            if (card instanceof Material) {
                c++;
            }
        }
        return c;
    }
}
