package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeAllMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RocketBarrage extends BaseCard {
    public static final String ID = makeID("RocketBarrage");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            2
    );

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;

    public RocketBarrage() {
        super(ID, info, BasicMod.imagePath("cards/attack/rocket_barrage.png"));
        setDamage(DAMAGE, UPG_DAMAGE);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConsumeAllMaterialAction(consumed -> {
            if (consumed <= 0) return;
            for (int i = 0; i < consumed; i++) {
                addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(damage, true),
                        DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }));
    }
}
