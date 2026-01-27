package basicmod.patches;

import basemod.ReflectionHacks;
import basicmod.cards.BaseCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

@SpirePatch(clz = SingleCardViewPopup.class, method = "update")
public class PopupPreviewCyclePatch {
    @SpirePostfixPatch
    public static void postfix(SingleCardViewPopup __instance) {
        try {
            // private fields in most builds, so use ReflectionHacks
            Boolean isOpen = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "isOpen");
            if (isOpen == null || !isOpen) return;

            AbstractCard c = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (c instanceof BaseCard) {
                ((BaseCard) c).tickPreviewCycleInPopup();
            }
        } catch (Exception ignored) {
            // if this ever breaks due to field renames, remove ignore and log once
        }
    }
}
