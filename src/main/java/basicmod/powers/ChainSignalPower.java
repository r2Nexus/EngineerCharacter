package basicmod.powers;

import basicmod.BasicMod;
import basicmod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static basicmod.BasicMod.makeID;

public class ChainSignalPower extends BasePower {
    public static final String POWER_ID = makeID("ChainSignalPower");

    public ChainSignalPower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                owner,
                amount,
                BasicMod.imagePath("powers/chain_signal_power.png"),
                BasicMod.imagePath("powers/large/chain_signal_power.png")
        );
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!isWagon(card)) return;

        // Prevent the duplicated card from retriggering this power
        if (card.isInAutoplay) return;

        flash();

        AbstractCard copy = card.makeSameInstanceOf();
        copy.purgeOnUse = true;       // do not create a real extra card
        copy.freeToPlayOnce = true;   // do not pay energy again
        copy.isInAutoplay = true;     // loop protection
        copy.energyOnUse = card.energyOnUse;

        // keep visuals sane
        copy.current_x = card.current_x;
        copy.current_y = card.current_y;
        copy.target_x = card.target_x;
        copy.target_y = card.target_y;

        AbstractDungeon.player.limbo.addToBottom(copy);
        addToTop(new NewQueueCardAction(copy, true, true, true));

        amount--;
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        } else {
            updateDescription();
        }
    }

    private boolean isWagon(AbstractCard card) {

        return card.cardID != null && card.hasTag(CardTagEnum.WAGON);
    }

    @Override
    public void updateDescription() {
        // DESCRIPTIONS[0] = "The next #b";
        // DESCRIPTIONS[1] = " Wagon(s) you play are played twice."
        if (DESCRIPTIONS != null && DESCRIPTIONS.length >= 2) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = "The next " + amount + " Wagon(s) you play are played twice.";
        }
    }
}
