package basicmod.patches;

import basemod.helpers.CardModifierManager;
import basicmod.cardmods.ChargeMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(clz = AbstractCard.class, method = "canUse")
public class ChargeCanUsePatch {
    @SpirePostfixPatch
    public static boolean postfix(boolean __result, AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
        if (!__result) return false;

        for (basemod.abstracts.AbstractCardModifier mod : CardModifierManager.modifiers(__instance)) {
            if (mod instanceof ChargeMod) {
                ChargeMod cm = (ChargeMod) mod;
                if (!cm.isFullyCharged()) {
                    __instance.cantUseMessage = "Needs Charge (" + cm.charge + "/" + cm.maxCharge + ").";
                    return false;
                }
            }
        }
        return true;
    }
}
