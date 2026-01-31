package basicmod.relics;

import basicmod.BasicMod;
import basicmod.actions.AddMaterialAction;
import basicmod.cards.other.Material;
import basicmod.orbs.TurretOrb;
import basicmod.patches.PlayerClassEnum;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.actions.AddMaterialAction.Destination.HAND;

public class BuiltTurret extends BaseRelic {

    public static final String ID = BasicMod.makeID("BuiltTurret");

    public BuiltTurret() {
        super(ID, "turret_relic", RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new ChannelAction(new TurretOrb()));
        AbstractDungeon.actionManager.addToBottom(new AddMaterialAction(1, HAND));
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
