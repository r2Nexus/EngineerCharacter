package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NuclearWarhead extends BaseCard {
    public static final String ID = makeID("NuclearWarhead");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    private static final int DAMAGE = 20;
    private static final int UPG_DAMAGE = 0;

    private static final int BONUS_DAMAGE = 30;
    private static final int UPG_BONUS = 15;

    private static final int CONSUME = 6;

    public NuclearWarhead() {
        super(ID, info, BasicMod.imagePath("cards/attack/nuclear_warhead.png"));

        setDamage(DAMAGE, UPG_DAMAGE);
        setCustomVar("BONUS", VariableType.DAMAGE, BONUS_DAMAGE, UPG_BONUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConsumeMaterialAction(
                CONSUME,
                () -> addToTop(new DamageAction(
                        m,
                        new DamageInfo(p, damage + customVar("BONUS"), damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY
                )),
                () -> addToTop(new DamageAction(
                        m,
                        new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY
                )),
                true, true, true
        ));
    }
}
