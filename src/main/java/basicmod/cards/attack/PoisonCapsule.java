package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class PoisonCapsule extends BaseCard {
    public static final String ID = makeID("PoisonCapsule");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 6;

    private static final int POISON = 4;
    private static final int UPG_POISON = 3;

    private static final int DEBUFF = 2;      // weak and vulnerable
    private static final int UPG_DEBUFF = 1;

    public PoisonCapsule() {
        super(ID, info, BasicMod.imagePath("cards/attack/poison_capsule.png"));
        setDamage(DAMAGE);
        setMagic(POISON, UPG_POISON);

        setCustomVar("DEBUFF", VariableType.MAGIC, DEBUFF, UPG_DEBUFF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));

        addToBot(new ApplyPowerAction(
                m,
                p,
                new PoisonPower(m, p, magicNumber),
                magicNumber
        ));

        addToBot(new ConsumeMaterialAction(2, () -> {
            int debuffAmt = customVar("DEBUFF");

            addToBot(new ApplyPowerAction(
                    m,
                    p,
                    new VulnerablePower(m, debuffAmt, false),
                    debuffAmt
            ));
            addToBot(new ApplyPowerAction(
                    m,
                    p,
                    new WeakPower(m, debuffAmt, false),
                    debuffAmt
            ));
        }));
    }
}
