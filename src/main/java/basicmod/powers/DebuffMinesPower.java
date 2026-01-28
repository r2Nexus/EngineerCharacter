package basicmod.powers;

import basicmod.BasicMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class DebuffMinesPower extends BasePower {
    public static final String POWER_ID = BasicMod.makeID("DebuffMines");

    public DebuffMinesPower(AbstractCreature owner, AbstractCreature source, int vulnAmt, int fragileAmt) {
        super(POWER_ID, PowerType.BUFF, true, owner, source, 1);
        this.amount = vulnAmt;      // amount = vulnerable
        this.amount2 = fragileAmt;  // amount2 = fragile
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    public void onMineTriggered() {
        for (AbstractCreature mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo == null || mo.isDeadOrEscaped()) continue;

            if (amount > 0) {
                addToBot(new ApplyPowerAction(mo, owner, new VulnerablePower(mo, amount, false), amount));
            }
            if (amount2 > 0) {
                addToBot(new ApplyPowerAction(mo, owner, new WeakPower(mo, amount2, false), amount2));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = "This turn, your Mines apply " + amount + " Vulnerable and " + amount2 + " Weak to ALL enemies.";
    }
}
