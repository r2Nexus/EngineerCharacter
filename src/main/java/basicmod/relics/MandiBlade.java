package basicmod.relics;

import basicmod.cardmods.MandiBladeStrikeMod;
import basicmod.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class MandiBlade extends BaseRelic {
    public static final String ID = makeID("MandiBlade");

    private static final int HEAL = 2;

    public MandiBlade() {
        super(ID, "mandiblade", AbstractCardEnum.ENGINEER, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        applyToAllCombatPiles();
    }

    // “Net” to catch strikes created/drawn later (generated cards, etc.)
    @Override
    public void atTurnStartPostDraw() {
        applyToAllCombatPiles();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (isStrike(card)) {
            flash();
            addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL));
        }
    }

    private void applyToAllCombatPiles() {
        if (AbstractDungeon.player == null) return;

        applyGroup(AbstractDungeon.player.hand);
        applyGroup(AbstractDungeon.player.drawPile);
        applyGroup(AbstractDungeon.player.discardPile);
        // optional:
        // applyGroup(AbstractDungeon.player.exhaustPile);
    }

    private void applyGroup(CardGroup group) {
        for (AbstractCard c : group.group) {
            if (isStrike(c)) {
                MandiBladeStrikeMod.apply(c);
            }
        }
    }

    private boolean isStrike(AbstractCard c) {
        return c.hasTag(AbstractCard.CardTags.STRIKE);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MandiBlade();
    }
}
