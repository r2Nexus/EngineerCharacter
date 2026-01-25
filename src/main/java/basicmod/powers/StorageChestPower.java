package basicmod.powers;

import basicmod.BasicMod;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.List;

import static basicmod.BasicMod.makeID;

public class StorageChestPower extends BasePower {
    public static final String POWER_ID = makeID("StorageChestPower");
    private static final UIStrings UI = CardCrawlGame.languagePack.getUIString(makeID("StorageChestUI"));

    public StorageChestPower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false, // persistent
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/storage_chest_power.png"),
                BasicMod.imagePath("powers/large/storage_chest_power.png")
        );
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        if (amount <= 0) return;
        if (AbstractDungeon.player.hand.isEmpty()) return;

        flash();

        String prompt = UI.TEXT[0].replace("!M!", Integer.toString(this.amount));
        addToBot(new SelectCardsInHandAction(
                this.amount,
                prompt,
                true,
                true,   // canPickZero
                c -> true,
                (List<AbstractCard> chosen) -> {
                    for (AbstractCard c : chosen) {
                        c.retain = true;
                        c.flash();
                    }
                }
        ));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("!M!", Integer.toString(this.amount));
    }
}
