package basicmod.cards.attack;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class CombatStim extends BaseCard {
    public static final String ID = makeID("CombatStim");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            SelfOrEnemyTargeting.SELF_OR_ENEMY, // <-- IMPORTANT
            0
    );

    private static final int CHARGE = 6;
    private static final int UPG_CHARGE = 0;

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 0;

    private static final int STR = 2;
    private static final int UPG_STR = 1;

    public CombatStim() {
        super(ID, info, BasicMod.imagePath("cards/attack/combat_stim.png"));

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(STR, UPG_STR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null) return;

        addToBot(new DamageAction(
                target,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        ));

        // charged: target gains Strength
        addToBot(new SpendChargeAction(this, () -> {
            addToTop(new ApplyPowerAction(
                    target, p,
                    new StrengthPower(target, this.magicNumber),
                    this.magicNumber
            ));
        }));
    }
}
