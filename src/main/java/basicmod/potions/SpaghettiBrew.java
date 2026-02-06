package basicmod.potions;

import basicmod.orbs.LandMineOrb;
import basicmod.orbs.MinerOrb;
import basicmod.orbs.TurretOrb;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static basicmod.BasicMod.makeID;

public class SpaghettiBrew extends BasePotion {
    public static final String ID = makeID("SpaghettiBrew");

    private static final int BASE_SLOTS = 2;

    public static final Color LIQUID_COLOR = new Color(0.95f, 0.88f, 0.55f, 1.00f);
    public static final Color HYBRID_COLOR = new Color(0.80f, 0.20f, 0.12f, 0.95f);
    public static final Color SPOTS_COLOR  = new Color(0.32f, 0.10f, 0.06f, 1.00f);

    public SpaghettiBrew() {
        super(ID, BASE_SLOTS, PotionRarity.RARE, PotionSize.JAR, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);

        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = new Color(0.72f, 0.42f, 0.16f, 1f);
    }

    @Override
    public boolean canUse() {
        if (AbstractDungeon.player == null) return false;
        if (AbstractDungeon.getCurrRoom() == null) return false;

        // In combat: keep normal restriction
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            return AbstractDungeon.actionManager != null && !AbstractDungeon.actionManager.turnHasEnded;
        }

        return true;
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.player == null) return;

        // We want: +potency orb slots, then channel 1 random orb per orb slot you will have
        int slotsGained = this.potency;
        int slotsAfter = AbstractDungeon.player.maxOrbs + slotsGained;

        // First increase slots (queued first)
        addToBot(new IncreaseMaxOrbAction(slotsGained));

        // Then channel "slotsAfter" random orbs
        for (int i = 0; i < slotsAfter; i++) {
            addToBot(new ChannelAction(randomOrbFromPool()));
        }
    }

    private static final List<Supplier<AbstractOrb>> ORB_POOL = Arrays.asList(
            TurretOrb::new,
            MinerOrb::new,
            LandMineOrb::new
    );

    private AbstractOrb randomOrbFromPool() {
        int i = AbstractDungeon.cardRandomRng.random(ORB_POOL.size() - 1);
        return ORB_POOL.get(i).get();
    }

    @Override
    public String getDescription() {
        // Uses !P! so Sacred Bark displays correctly
        return DESCRIPTIONS[0].replace("!P!", Integer.toString(this.potency));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new SpaghettiBrew();
    }
}
