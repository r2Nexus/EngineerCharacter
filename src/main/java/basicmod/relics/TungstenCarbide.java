package basicmod.relics;

import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class TungstenCarbide extends BaseRelic {
    public static final String ID = makeID("TungstenCarbide");

    private static final int BLOCK_PER_MATERIAL = 3;

    public TungstenCarbide() {
        super(ID, "tungsten_carbide_relic", AbstractCardEnum.ENGINEER, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public void onPlayerEndTurn() {
        if (AbstractDungeon.player == null) return;

        int mats = 0;
        for (com.megacrit.cardcrawl.cards.AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof Material) mats++;
        }

        if (mats > 0) {
            int block = mats * BLOCK_PER_MATERIAL;
            flash();
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TungstenCarbide();
    }
}
