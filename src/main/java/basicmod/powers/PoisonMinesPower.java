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
        super(
                POWER_ID,
                PowerType.BUFF,
                true,
                owner,
                source,
                amount,
                BasicMod.imagePath("powers/poison_mines_power.png"),
                BasicMod.imagePath("powers/large/poison_mines_power.png"));
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
                addToTop(new ApplyPowerAction(mo, owner, new PoisonPower(mo, owner, amount), amount));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
