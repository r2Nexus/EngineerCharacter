package basicmod.patches;

import basicmod.util.WagonTracker;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
public class WagonPlayedPatch {
    @SpirePrefixPatch
    public static void prefix(AbstractPlayer __instance, AbstractCard card, AbstractMonster monster, int energyOnUse) {
        if (card != null && card.hasTag(CardTagEnum.WAGON)) {
            WagonTracker.playedWagonThisTurn = true;
        }
    }
}
