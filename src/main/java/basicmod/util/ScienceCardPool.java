package basicmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

public class ScienceCardPool {
    private ScienceCardPool() {}

    // Store IDs, not card objects
    public static final ArrayList<String> IDS = new ArrayList<>();

    public static void add(String cardId) {
        if (!IDS.contains(cardId)) IDS.add(cardId);
    }

    public static AbstractCard getRandom() {
        if (IDS.isEmpty()) return null;

        String id = IDS.get(com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRandomRng.random(IDS.size() - 1));
        AbstractCard template = CardLibrary.getCard(id);
        return template == null ? null : template.makeCopy();
    }
}
