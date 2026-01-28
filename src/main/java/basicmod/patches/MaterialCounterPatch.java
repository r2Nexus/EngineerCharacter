package basicmod.patches;

import basicmod.cards.other.Material;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class MaterialCounterPatch {

    @SpirePatch(clz = EnergyPanel.class, method = "render")
    public static class OverrideEnergyTooltip {

        /**
         * Replace TipHelper.renderGenericTip(...) calls *inside EnergyPanel.render*.
         * This is effectively "override the vanilla energy tooltip".
         */
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(TipHelper.class.getName())
                            && m.getMethodName().equals("renderGenericTip")) {
                        // signature: renderGenericTip(float x, float y, String header, String body)
                        m.replace("{ " + MaterialCounterPatch.class.getName()
                                + ".OverrideEnergyTooltip.renderEnergyTipOverride($1, $2, $3, $4); }");
                    }
                }
            };
        }

        /**
         * Called instead of TipHelper.renderGenericTip from EnergyPanel.render.
         * Decide whether to override or fall back to vanilla args.
         */
        public static void renderEnergyTipOverride(float x, float y, String header, String body) {
            // Only override in combat (your request). Otherwise preserve vanilla.
            if (AbstractDungeon.player == null
                    || AbstractDungeon.getCurrRoom() == null
                    || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                TipHelper.renderGenericTip(x, y, header, body);
                return;
            }

            int hand = countMaterial(AbstractDungeon.player.hand.group);
            int draw = countMaterial(AbstractDungeon.player.drawPile.group);
            int disc = countMaterial(AbstractDungeon.player.discardPile.group);
            int total = hand + draw + disc;

            String newHeader = "Materials";
            String newBody =
                    "Hand: " + hand + " NL " +
                            "Draw: " + draw + " NL " +
                            "Discard: " + disc + " NL " +
                            "Total: " + total;

            // Keep the exact tooltip position that vanilla was using.
            TipHelper.renderGenericTip(x, y, newHeader, newBody);
        }

        private static int countMaterial(ArrayList<AbstractCard> cards) {
            int c = 0;
            for (AbstractCard card : cards) {
                if (Material.ID.equals(card.cardID)) c++;
            }
            return c;
        }
    }
}
