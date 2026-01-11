package basicmod.patches;

import basicmod.orbs.BaseOrb;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class FocusRefreshOrbsPatch {

    private static boolean inCombat() {
        return AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    private static void queueRefresh() {
        if (!inCombat()) return;
        if (AbstractDungeon.player == null || AbstractDungeon.actionManager == null) return;

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player == null || AbstractDungeon.player.orbs == null) {
                    isDone = true;
                    return;
                }

                for (AbstractOrb o : AbstractDungeon.player.orbs) {
                    if (o instanceof BaseOrb) {
                        ((BaseOrb) o).refresh();
                    } else if (o != null) {
                        o.updateDescription();
                    }
                }
                isDone = true;
            }
        });
    }

    private static void refreshIfPlayerFocus(AbstractPower p) {
        if (!(p instanceof FocusPower)) return;
        if (AbstractDungeon.player == null) return;
        if (p.owner != AbstractDungeon.player) return;
        queueRefresh();
    }

    // Focus applied
    @SpirePatch2(clz = AbstractPower.class, method = "onInitialApplication")
    public static class OnInitial {
        @SpirePostfixPatch
        public static void post(AbstractPower __instance) {
            refreshIfPlayerFocus(__instance);
        }
    }

    // Focus increased
    @SpirePatch2(clz = AbstractPower.class, method = "stackPower", paramtypez = { int.class })
    public static class OnStack {
        @SpirePostfixPatch
        public static void post(AbstractPower __instance, int stackAmount) {
            refreshIfPlayerFocus(__instance);
        }
    }

    // Focus reduced
    @SpirePatch2(clz = AbstractPower.class, method = "reducePower", paramtypez = { int.class })
    public static class OnReduce {
        @SpirePostfixPatch
        public static void post(AbstractPower __instance, int reduceAmount) {
            refreshIfPlayerFocus(__instance);
        }
    }
}
