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

public class SubmachineGun extends BaseCard {
    public static final String ID = makeID("SubmachineGun");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 6;
    private static final int BONUS_DAMAGE = 6;
    private static final int UPG_DAMAGE = 2;
    private static final int UPG_BONUS = 2;

    public SubmachineGun() {
        super(ID, info, BasicMod.imagePath("cards/attack/submachinegun.png"));
        tags.add(CardTagEnum.GUN);

        setDamage(DAMAGE, UPG_DAMAGE);
        setCustomVar("BONUS", VariableType.DAMAGE, BONUS_DAMAGE, UPG_BONUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Base damage (always)
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));

        addToBot(new ConsumeMaterialAction(1, () -> {
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, customVar("BONUS"), damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY
            ));
        },
                true,
                upgraded,
                upgraded
        ));
    }
}
