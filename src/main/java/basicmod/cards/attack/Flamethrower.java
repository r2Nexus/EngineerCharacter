package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Flamethrower extends BaseCard {
    public static final String ID = makeID("Flamethrower");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    private static final int DAMAGE = 6;
    private static final int MAGIC = 4;
    private static final int UPG_DAMAGE = 0;
    private static final int UPG_MAGIC = 2;
    private static final int TIMES = 2;

    public Flamethrower() {
        super(ID, info, BasicMod.imagePath("cards/attack/flame_thrower.png"));
        tags.add(CardTagEnum.GUN);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(
                p,
                multiDamage,
                damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE
        ));

        for (int i = 0; i < TIMES; i++) {
            addToBot(new ConsumeMaterialAction(1, () -> {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!mo.isDeadOrEscaped()) {
                        addToBot(new ApplyPowerAction(
                                mo,
                                p,
                                new PoisonPower(mo, p, this.magicNumber),
                                this.magicNumber
                        ));
                    }
                }
            }));
        }
    }
}
