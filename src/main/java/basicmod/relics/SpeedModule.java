package basicmod.relics;

import basicmod.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class SpeedModule extends BaseRelic {
    public static final String ID = makeID("SpeedModule");

    public SpeedModule() {
        super(ID, "speed_module", AbstractCardEnum.ENGINEER, RelicTier.SHOP, LandingSound.CLINK);
        this.counter = -1;
    }

    @Override
    public void atPreBattle() {
        // Using counter as "triggers left" this combat
        this.counter = 3;
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player == null) return;
        if (this.counter <= 0) return;

        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new IncreaseMaxOrbAction(1));

        this.counter--;

        if (this.counter <= 0) {
            // Optional: show it's "spent" for the rest of combat
            this.grayscale = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SpeedModule();
    }
}
