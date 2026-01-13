package basicmod.orbs;

import basicmod.BasicMod;
import basicmod.cards.Material;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class MinerOrb extends BaseOrb {
    public static final String ORB_ID = makeID("Miner");

    private static final int BASE_PASSIVE = 1;
    private static final int BASE_EVOKE = 2;

    public MinerOrb() {
        super(ORB_ID, BasicMod.imagePath("orbs/miner.png"), BASE_PASSIVE, BASE_EVOKE);
        setFocusAffectsPassive(false);
        setFocusAffectsEvoke(true);

        refresh();
    }

    @Override
    public void onStartOfTurn() {
        refresh();

        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(new Material(), passiveAmount)
        );
    }

    @Override
    public void onEvoke() {
        refresh();
        AbstractDungeon.actionManager.addToTop(
                new MakeTempCardInHandAction(new Material(), evokeAmount)
        );
        triggerPurpleScience();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new MinerOrb();
    }

    @Override
    public void playChannelSFX() {
        // optional
    }
}
