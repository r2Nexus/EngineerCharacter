package basicmod.orbs;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class TurretOrb extends BaseOrb {
    public static final String ORB_ID = makeID("Turret");

    private static final int BASE_PASSIVE = 8;
    private static final int BASE_EVOKE = 8;

    public TurretOrb() {
        super(ORB_ID, BasicMod.imagePath("orbs/turret.png"), BASE_PASSIVE, BASE_EVOKE);
    }

    @Override
    public void onEndOfTurn() {
        refresh();

        AbstractDungeon.actionManager.addToTop(
                new ConsumeMaterialAction(1, this::fireAtRandomEnemy)
        );
    }

    public void fireAtRandomEnemy() {
        AbstractDungeon.actionManager.addToTop(new DamageRandomEnemyAction(
                new DamageInfo(AbstractDungeon.player, passiveAmount, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        ));
    }

    @Override
    public void onEvoke() {
        refresh();

        AbstractDungeon.actionManager.addToTop(new DamageRandomEnemyAction(
                new DamageInfo(AbstractDungeon.player, evokeAmount, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.FIRE
        ));
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
