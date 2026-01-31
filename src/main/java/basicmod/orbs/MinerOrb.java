package basicmod.orbs;

import basicmod.BasicMod;
import basicmod.actions.AddMaterialAction;
import basicmod.cards.other.Material;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import static basicmod.actions.AddMaterialAction.Destination.HAND;

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
                new AddMaterialAction(passiveAmount, HAND)
        );
    }

    @Override
    public void onEvoke() {
        refresh();
        AbstractDungeon.actionManager.addToTop(
                new AddMaterialAction(evokeAmount, HAND)
        );
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
