package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CliffExplosive extends BaseCard {
    public static final String ID = makeID("CliffExplosive");

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 4;

    private static final int EXHAUST = 1;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );


    public CliffExplosive() {
        super(ID, info, BasicMod.imagePath("cards/attack/cliff_explosive.png"));
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(EXHAUST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));
        addToBot(new ExhaustAction(magicNumber, false, false, false));
    }
}
