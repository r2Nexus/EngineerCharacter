package basicmod.relics;

import basicmod.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class BurnerMiningDrill extends BaseRelic {
    public static final String ID = makeID("BurnerMiningDrill");

    private boolean usedThisCombat = false;

    public BurnerMiningDrill() {
        super(ID, "burner_mining_drill", AbstractCardEnum.ENGINEER, RelicTier.UNCOMMON, LandingSound.CLINK);
        this.counter = -1;
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false;
        this.counter = 1;      // armed
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        usedThisCombat = false;
        this.counter = -1;     // hide out of combat
        this.grayscale = false;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (usedThisCombat) return;
        if (AbstractDungeon.player == null) return;
        if (c == null) return;

        usedThisCombat = true;

        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        c.exhaust = true;

        if (c.costForTurn >= 0) {
            c.setCostForTurn(0);
        }

        this.counter = 0;      // spent
        this.grayscale = true; // optional visual feedback
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BurnerMiningDrill();
    }
}
