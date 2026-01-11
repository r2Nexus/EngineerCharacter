package basicmod.patches;

import basicmod.util.WagonTracker;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnPreDrawCards")
public class WagonResetPatch {
    @SpirePrefixPatch
    public static void prefix(AbstractPlayer __instance) {
        WagonTracker.resetTurn();
    }
}
