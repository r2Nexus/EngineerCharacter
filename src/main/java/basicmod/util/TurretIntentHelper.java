package basicmod.util;

import basicmod.orbs.TurretOrb;
import basicmod.powers.BeltFedPower;
import basicmod.powers.FreeTurretFirePower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class TurretIntentHelper {
    private TurretIntentHelper() {}

    public static void updateTurretIntents() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) return;

        // If outside combat, skip
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getMonsters() == null) return;

        // Free fire: all turrets will fire.
        if (p.hasPower(FreeTurretFirePower.POWER_ID)) {
            setAllTurrets(p, true);
            return;
        }

        boolean beltFed = p.hasPower(BeltFedPower.POWER_ID);

        int ammo = 0;
        ammo += MaterialUtils.countHand(p);
        if (beltFed) {
            ammo += MaterialUtils.countDraw(p);
            ammo += MaterialUtils.countDiscard(p);
        }

        // Default everything to false first
        setAllTurrets(p, false);

        // Allocate from RIGHT -> LEFT
        for (int i = p.orbs.size() - 1; i >= 0; i--) {
            AbstractOrb o = p.orbs.get(i);
            if (o instanceof TurretOrb) {
                TurretOrb turret = (TurretOrb) o;

                if (ammo > 0) {
                    turret.setWillFire(true);
                    ammo--;
                } else {
                    turret.setWillFire(false);
                }
            }
        }
    }

    private static void setAllTurrets(AbstractPlayer p, boolean value) {
        for (AbstractOrb o : p.orbs) {
            if (o instanceof TurretOrb) {
                ((TurretOrb) o).setWillFire(value);
            }
        }
    }
}
