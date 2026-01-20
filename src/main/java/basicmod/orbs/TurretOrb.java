package basicmod.orbs;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.powers.BeltFedPower;
import basicmod.powers.FlameTurretPower;
import basicmod.powers.FreeTurretFirePower;
import basicmod.powers.SuppressiveFirePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class TurretOrb extends BaseOrb {
    public static final String ORB_ID = makeID("Turret");

    private static final int BASE_PASSIVE = 6;
    private static final int BASE_EVOKE = 8;

    public TurretOrb() {
        super(ORB_ID, BasicMod.imagePath("orbs/turret.png"), BASE_PASSIVE, BASE_EVOKE);
    }

    @Override
    public void onEndOfTurn() {
        refresh();

        // Fire for Free
        if (AbstractDungeon.player.hasPower(FreeTurretFirePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
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
                new ConsumeMaterialAction(1, this::fireAtRandomEnemy,
                        true,
                        hasBeltFed,
                        hasBeltFed
                ));
    }

    public void fireAtRandomEnemy() {
        fireAtRandomEnemy(passiveAmount, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    private void fireAtRandomEnemy(int dmg, AbstractGameAction.AttackEffect effect) {

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
}
