package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class PoisonMinesPower extends BasePower {
    public static final String POWER_ID = BasicMod.makeID("PoisonMines");

    public PoisonMinesPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, PowerType.BUFF, true, owner, source, amount);
        // BasePower auto-loads textures via ID (removePrefix) and pulls PowerStrings
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    /** Call this when a Mine triggers/explodes. */
    public void onMineTriggered() {
        if (amount <= 0) return;

        for (AbstractCreature mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo != null && !mo.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mo, owner, new PoisonPower(mo, owner, amount), amount));
            }
        }
    }

    @Override
    public void updateDescription() {
        // Prefer localization if you want; but this is fine and clear.
        // If you want full localization, tell me your PowerStrings format and I'll adapt.
        this.description = "This turn, your Mines apply " + amount + " Poison to ALL enemies.";
    }
}
