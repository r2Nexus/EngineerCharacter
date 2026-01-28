package basicmod.util;

import basicmod.cards.other.Material;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class MaterialUtils {

    public static int countHand(AbstractPlayer p) {
        return countPile(p.hand.group);
    }

    public static int countDraw(AbstractPlayer p) {
        return countPile(p.drawPile.group);
    }

    public static int countDiscard(AbstractPlayer p) {
        return countPile(p.discardPile.group);
    }

    public static int countAll(AbstractPlayer p) {
        return countHand(p) + countDraw(p) + countDiscard(p);
    }

    private static int countPile(java.util.ArrayList<AbstractCard> pile) {
        int c = 0;
        for (AbstractCard card : pile) {
            if (card instanceof Material) c++;
        }
        return c;
    }
}
