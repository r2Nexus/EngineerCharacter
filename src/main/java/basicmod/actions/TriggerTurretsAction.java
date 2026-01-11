package basicmod.actions;

import basicmod.orbs.TurretOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class TriggerTurretsAction extends AbstractGameAction {
    @Override
    public void update() {
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (orb instanceof TurretOrb) {
                ((TurretOrb) orb).fireAtRandomEnemy();
                orb.triggerEvokeAnimation();
            }
        }
        isDone = true;
    }
}
