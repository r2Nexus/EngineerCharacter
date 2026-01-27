package basicmod.cards.attack;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Railgun extends BaseCard {
    public static final String ID = makeID("Railgun");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1
    );

    private static final int CHARGE_MAX = 8;
    private static final int UPG_CHARGE_MAX = 4;
    private static final int DAMAGE_PER_CHARGE = 3;

    public Railgun() {
        super(ID, info, BasicMod.imagePath("cards/attack/railgun.png"));

        // Match your existing charge-card convention exactly:
        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE_MAX, UPG_CHARGE_MAX);

        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));
        setDamage(0);
    }

    private int computeDamageFromCharge() {
        ChargeMod mod = ChargeMod.get(this);
        if (mod == null) return 0;
        return DAMAGE_PER_CHARGE * mod.getCharge(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ChargeMod mod = ChargeMod.get(this);
        int spent = (mod == null) ? 0 : mod.spendAll(this);
        int dmg = DAMAGE_PER_CHARGE * spent;

        addToBot(new DamageAction(
                m,
                new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL)
        ));
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;

        baseDamage = computeDamageFromCharge();
        super.applyPowers();
        baseDamage = realBase;

        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;

        baseDamage = computeDamageFromCharge();
        super.calculateCardDamage(mo);
        baseDamage = realBase;

        initializeDescription();
    }
}
