package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.actions.TriggerTurretsAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RainingBullets extends BaseCard {
    public static final String ID = makeID("RainingBullets");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            2
    );

    private static final int DAMAGE = 5;

    public RainingBullets() {
        super(ID, info, BasicMod.imagePath("cards/attack/raining_bullets.png"));
        setDamage(DAMAGE);
        setCostUpgrade(baseCost - 1);
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Base damage (always)
        addToBot(new DamageAllEnemiesAction(
                p,
                multiDamage,
                damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));

        addToBot(new ConsumeMaterialAction(1, () -> addToBot(new TriggerTurretsAction())));
        addToBot(new ConsumeMaterialAction(1, () -> addToBot(new TriggerTurretsAction())));

        if (upgraded) {
            addToBot(new ConsumeMaterialAction(1, () -> addToBot(new TriggerTurretsAction())));
        }
    }
}
