package basicmod.relics;

import basicmod.BasicMod;
import basicmod.cards.Material;
import basicmod.orbs.TurretOrb;
import basicmod.patches.PlayerClassEnum;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BuiltTurret extends BaseRelic {

    public static final String ID = BasicMod.makeID("BuiltTurret");

    public BuiltTurret() {
        super(ID, "turret_relic", RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new ChannelAction(new TurretOrb()));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Material(), 1));
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null
                && AbstractDungeon.player.chosenClass == PlayerClassEnum.ENGINEER;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BuiltTurret();
    }
}
