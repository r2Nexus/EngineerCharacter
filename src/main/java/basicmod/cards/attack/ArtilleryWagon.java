package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ArtilleryWagon extends BaseCard {
    public static final String ID = makeID("ArtilleryWagon");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 10;
    private static final int UPG_DAMAGE = 3;

    private static final int STOCK_COST = 4;
    private static final int UPG_STOCK_COST = -1;

    private static final int AOE = 12;

    public ArtilleryWagon() {
        super(ID, info, BasicMod.imagePath("cards/attack/artillery_wagon.png"));
        tags.add(CardTagEnum.WAGON);

        setDamage(DAMAGE, UPG_DAMAGE);
        setCustomVar("COST", VariableType.MAGIC, STOCK_COST, UPG_STOCK_COST);
        setCustomVar("AOE", VariableType.DAMAGE, AOE, 0);
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ));

        addToBot(new ConsumeMaterialAction(customVar("COST"), () -> {
            addToBot(new DamageAllEnemiesAction(
                    p,
                    customVarMulti("AOE"),
                    damageTypeForTurn,
                    AbstractGameAction.AttackEffect.FIRE
            ));
        }));
    }
}
