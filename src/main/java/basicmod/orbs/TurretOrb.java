package basicmod.orbs;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.powers.BeltFedPower;
import basicmod.powers.FlameTurretPower;
import basicmod.powers.FreeTurretFirePower;
import basicmod.powers.SuppressiveFirePower;
import basicmod.util.HasOrbIntent;
import basicmod.util.OrbIntent;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class TurretOrb extends BaseOrb implements HasOrbIntent {
    public static final String ORB_ID = makeID("Turret");

    private static final int BASE_PASSIVE = 6;
    private static final int BASE_EVOKE = 8;

    // --- Intent UI ---
    private boolean willFire = false;

    // --- Recoil animation (render offset only) ---
    private float recoilOffsetX = 0f;
    private float recoilVelocityX = 0f;

    // Tune these
    private static final float RECOIL_KICK = 14f;        // kick left amount
    private static final float SPRING_STRENGTH = 120f;   // return speed
    private static final float DAMPING = 18f;            // wobble control
    private static final float MAX_RECOIL = 28f;         // clamp

    public TurretOrb() {
        super(ORB_ID, BasicMod.imagePath("orbs/turret.png"), BASE_PASSIVE, BASE_EVOKE);
    }

    @Override
    public void onEndOfTurn() {
        refresh();

        // Fire for Free
        if (AbstractDungeon.player.hasPower(FreeTurretFirePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    fireAtRandomEnemy();
                    isDone = true;
                }
            });
            return;
        }

        boolean hasBeltFed = AbstractDungeon.player.hasPower(BeltFedPower.POWER_ID);

        AbstractDungeon.actionManager.addToTop(
                new ConsumeMaterialAction(
                        1,
                        this::fireAtRandomEnemy,
                        true,
                        hasBeltFed,
                        hasBeltFed,
                        true
                )
        );
    }

    public void fireAtRandomEnemy() {
        fireAtRandomEnemy(passiveAmount, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    private void fireAtRandomEnemy(int dmg, AbstractGameAction.AttackEffect effect) {
        // Recoil kick when the shot actually happens
        doRecoil();

        // Flame Turret
        com.megacrit.cardcrawl.powers.AbstractPower flame =
                AbstractDungeon.player.getPower(FlameTurretPower.POWER_ID);
        int aoe = (flame != null) ? flame.amount : 0;

        if (aoe > 0) {
            AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(
                    AbstractDungeon.player,
                    DamageInfo.createDamageMatrix(aoe, true),
                    DamageInfo.DamageType.THORNS,
                    AbstractGameAction.AttackEffect.FIRE
            ));
        }

        // Suppressive Fire
        com.megacrit.cardcrawl.powers.AbstractPower supp =
                AbstractDungeon.player.getPower(SuppressiveFirePower.POWER_ID);
        int suppAmountNow = (supp != null) ? supp.amount : 0;

        if (suppAmountNow > 0) {
            AbstractDungeon.actionManager.addToTop(
                    new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, suppAmountNow)
            );
        }

        // Actual turret shot
        AbstractDungeon.actionManager.addToTop(new DamageRandomEnemyAction(
                new DamageInfo(AbstractDungeon.player, dmg, DamageInfo.DamageType.THORNS),
                effect
        ));
    }

    @Override
    public void onEvoke() {
        refresh();
        fireAtRandomEnemy(evokeAmount, AbstractGameAction.AttackEffect.FIRE);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new TurretOrb();
    }

    @Override
    public void playChannelSFX() {
        // optional
    }

    // Called TurretIntentHelper
    public void setWillFire(boolean value) {
        willFire = value;
    }

    // --- Recoil update hook ---
    @Override
    public void updateAnimation() {
        super.updateAnimation();
        updateRecoil();
    }

    private void doRecoil() {
        recoilOffsetX -= RECOIL_KICK * Settings.scale;

        float min = -MAX_RECOIL * Settings.scale;
        if (recoilOffsetX < min) recoilOffsetX = min;

        recoilVelocityX -= 40f * Settings.scale;
    }

    private void updateRecoil() {
        float dt = Gdx.graphics.getDeltaTime();

        // Spring-damper toward 0
        float accel = (-SPRING_STRENGTH * recoilOffsetX) - (DAMPING * recoilVelocityX);

        recoilVelocityX += accel * dt;
        recoilOffsetX += recoilVelocityX * dt;

        // Snap to rest to avoid tiny jitter
        if (Math.abs(recoilOffsetX) < 0.05f * Settings.scale
                && Math.abs(recoilVelocityX) < 0.05f * Settings.scale) {
            recoilOffsetX = 0f;
            recoilVelocityX = 0f;
        }
    }

    @Override
    protected float getRenderXOffset() {
        return recoilOffsetX;
    }

    // =========================
    // HasOrbIntent
    // =========================

    @Override
    public boolean shouldShowIntent() {
        return inCombat() && willFire;
    }

    @Override
    public OrbIntent getOrbIntent() {
        // What it intends to do at end of turn
        return new OrbIntent(OrbIntent.Type.ATTACK, this.passiveAmount);
    }

    @Override
    public void renderText(com.badlogic.gdx.graphics.g2d.SpriteBatch sb) {
        // Intentionally blank: we only show the number on the intent icon
    }
}
