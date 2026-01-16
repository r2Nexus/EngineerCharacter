package basicmod.util;

import basemod.helpers.CardModifierManager;
import basicmod.cardmods.ChargeMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;
import java.util.List;

public class ChargeSystem {
    private ChargeSystem() {}

    public static void onMaterialConsumed(int amount) {
        if (amount <= 0) return;
        if (AbstractDungeon.player == null) return;

        // 1) Always charge master deck so it persists between combats
        chargeGroup(AbstractDungeon.player.masterDeck, amount);

        // 2) If we're in a combat, also charge combat instances so UI stays consistent
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT) {
            List<CardGroup> groups = Arrays.asList(
                    AbstractDungeon.player.hand,
                    AbstractDungeon.player.drawPile,
                    AbstractDungeon.player.discardPile,
                    AbstractDungeon.player.exhaustPile,
                    AbstractDungeon.player.limbo
            );

            for (CardGroup g : groups) {
                if (g != null) chargeGroup(g, amount);
            }
        }
    }

    private static void chargeGroup(CardGroup group, int amount) {
        for (AbstractCard c : group.group) {
            for (basemod.abstracts.AbstractCardModifier mod : CardModifierManager.modifiers(c)) {
                if (mod instanceof ChargeMod) {
                    ((ChargeMod) mod).addCharge(c, amount); // change to 1 if you want per-consume-event
                }
            }
        }
    }
}
