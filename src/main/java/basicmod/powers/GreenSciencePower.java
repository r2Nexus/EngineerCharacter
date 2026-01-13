package basicmod.powers;

import basicmod.BasicMod;
import basicmod.cards.Material;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static basicmod.BasicMod.makeID;

public class GreenSciencePower extends BasePower {
    public static final String POWER_ID = makeID("GreenSciencePower");

    public GreenSciencePower(AbstractCreature owner, int amount) {
        super(
                POWER_ID,
                PowerType.BUFF,
                false,
                owner,
                null,
                amount,
                BasicMod.imagePath("powers/green_science.png"),
                BasicMod.imagePath("powers/large/green_science.png")
        );
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        for (int i = 0; i < amount; i++) {
            addToBot(new DrawNonMaterialWithReshuffleAction());
        }
    }

    private static class DrawNonMaterialWithReshuffleAction extends AbstractGameAction {
        private boolean reshuffled = false;

        @Override
        public void update() {
            AbstractCard toDraw = null;

            // Try find a non-material in draw pile
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (!(c instanceof Material)) {
                    toDraw = c;
                    break;
                }
            }

            if (toDraw != null) {
                AbstractDungeon.player.drawPile.moveToHand(toDraw);
                isDone = true;
                return;
            }

            // None found -> reshuffle once if discard pile has cards
            if (!reshuffled && !AbstractDungeon.player.discardPile.isEmpty()) {
                reshuffled = true;

                // After shuffle completes, this same action will run again next update
                addToTop(this);
                addToTop(new EmptyDeckShuffleAction());

                isDone = true;
                return;
            }

            // Still none (or nothing to shuffle)
            isDone = true;
        }
    }

    @Override
    public void updateDescription() {
        // "At the start of your turn, draw !M! non-Material card."
        this.description = DESCRIPTIONS[0].replace("!M!", Integer.toString(this.amount));
    }
}
