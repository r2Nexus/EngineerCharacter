package basicmod.util;

import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

public class EngineerDraftPool {
    private static ArrayList<String> cachedIDs = null;

    public static void invalidate() {
        cachedIDs = null;
    }

    private static void ensureCache() {
        if (cachedIDs != null) return;

        cachedIDs = new ArrayList<>();
        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (c.color == AbstractCardEnum.ENGINEER
                    && !c.cardID.equals(Material.ID)
                    && c.type != AbstractCard.CardType.STATUS
                    && c.type != AbstractCard.CardType.CURSE) {
                cachedIDs.add(c.cardID);
            }
        }
    }

    public static ArrayList<AbstractCard> randomOptions(int count) {
        ensureCache();

        ArrayList<AbstractCard> result = new ArrayList<>();
        if (cachedIDs.isEmpty()) return result;

        ArrayList<String> ids = new ArrayList<>(cachedIDs);

        int rolls = Math.min(count, ids.size());
        for (int i = 0; i < rolls; i++) {
            int idx = AbstractDungeon.cardRandomRng.random(ids.size() - 1);
            String id = ids.remove(idx);

            AbstractCard proto = CardLibrary.getCard(id);
            if (proto != null) result.add(proto.makeCopy());
        }
        return result;
    }
}
