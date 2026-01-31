package basicmod.orbs;

import basicmod.BasicMod;
import basicmod.powers.DebuffMinesPower;
import basicmod.powers.PoisonMinesPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class LandMineOrb extends BaseOrb {

    public static final String ORB_ID = makeID("LandMine");

    private static final int BASE_EVOKE_DAMAGE = 3;
    private static final int BASE_BLOCK = 4;

    private boolean triggeredThisTurn = false;

    public LandMineOrb() {
        // passive = 0 (does nothing), evoke = damage
        super(ORB_ID, BasicMod.imagePath("orbs/land_mine.png"), 0, BASE_EVOKE_DAMAGE);

        // Secondary stat = block
        setSecondary(BASE_BLOCK);
        setSecondaryToken("!B!");

        // Scaling rules
        setFocusAffectsPassive(false);
        setFocusAffectsSecondary(true);
    }

    @Override
    public void onStartOfTurn() {
        triggeredThisTurn = false;
    }

    @Override
    public void onEndOfTurn() {
        if (triggeredThisTurn) return;
        if (!inCombat()) return;
        if (!anyMonsterIntendsAttack()) return;

        triggeredThisTurn = true;
        AbstractDungeon.actionManager.addToTop(new EvokeSpecificOrbAction(this));
    }

    @Override
    public void onEvoke() {
        refresh(); // Focus BS

        AbstractDungeon.actionManager.addToTop(
                new DamageAllEnemiesAction(
                        AbstractDungeon.player,
                        DamageInfo.createDamageMatrix(evokeAmount, true),
                        DamageInfo.DamageType.THORNS,
                        AbstractGameAction.AttackEffect.FIRE
                )
        );

        PoisonMinesPower pmp = (PoisonMinesPower) AbstractDungeon.player.getPower(PoisonMinesPower.POWER_ID);
        if (pmp != null) {
            pmp.onMineTriggered();
        }

        DebuffMinesPower dmp = (DebuffMinesPower) AbstractDungeon.player.getPower(DebuffMinesPower.POWER_ID);
        if (dmp != null) dmp.onMineTriggered();

        AbstractDungeon.actionManager.addToTop(
                new GainBlockAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        secondaryAmount
                )
        );
    }

    @Override
    public void renderText(SpriteBatch sb) {
        float yOffset = 16.0f * Settings.scale;

        // Top: damage
        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardEnergyFont_L,
                Integer.toString(evokeAmount),
                cX + NUM_X_OFFSET,
                cY + NUM_Y_OFFSET + bobEffect.y / 2.0f + yOffset,
                Color.WHITE.cpy(),
                fontScale
        );

        // Bottom: block
        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardEnergyFont_L,
                Integer.toString(secondaryAmount),
                cX + NUM_X_OFFSET,
                cY + NUM_Y_OFFSET + bobEffect.y / 2.0f - yOffset,
                Color.WHITE.cpy(),
                fontScale * 0.80f
        );
    }

    @Override
    public AbstractOrb makeCopy() {
        return new LandMineOrb();
    }

    @Override
    public void playChannelSFX() {
        // optional
    }
}
