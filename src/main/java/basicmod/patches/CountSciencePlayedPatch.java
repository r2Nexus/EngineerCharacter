package basicmod.patches;

import basicmod.BasicMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
public class CountSciencePlayedPatch {
    @SpirePostfixPatch
    public static void count(AbstractPlayer __instance, AbstractCard card,
                             AbstractMonster m, int energyOnUse) {
        if (card != null && card.hasTag(CardTagEnum.SCIENCE)) {
            BasicMod.sciencePlayedThisCombat++;
        }
    }
}
