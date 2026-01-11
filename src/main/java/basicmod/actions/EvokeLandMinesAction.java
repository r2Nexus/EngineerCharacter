package basicmod.actions;

import basicmod.orbs.LandMineOrb;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.ArrayList;

public class EvokeLandMinesAction extends AbstractGameAction {
    @Override
    public void update() {
        // Collect first, because evoking shifts orb slots
        ArrayList<AbstractOrb> mines = new ArrayList<>();
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (orb instanceof LandMineOrb) {
                mines.add(orb);
            }
        }

        // Then evoke them
        for (AbstractOrb mine : mines) {
            AbstractDungeon.actionManager.addToTop(new EvokeSpecificOrbAction(mine));
        }

        isDone = true;
    }
}
