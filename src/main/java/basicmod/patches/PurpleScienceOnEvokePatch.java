package basicmod.patches;

import basicmod.cards.Material;
import basicmod.powers.PurpleSciencePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

@SpirePatch(
        clz = AbstractOrb.class,
        method = "onEvoke"
)
public class PurpleScienceOnEvokePatch {

    @SpirePostfixPatch
    public static void afterEvoke(AbstractOrb __instance) {
        if (AbstractDungeon.player == null) return;

        PurpleSciencePower p =
                (PurpleSciencePower) AbstractDungeon.player.getPower(PurpleSciencePower.POWER_ID);

        if (p == null) return;

        p.flash();
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDiscardAction(new Material(), p.amount)
        );
    }
}
