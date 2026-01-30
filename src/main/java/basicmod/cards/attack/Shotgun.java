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
import com.megacrit.cardcrawl.powers.WeakPower;

public class Shotgun extends BaseCard {
    public static final String ID = makeID("Shotgun");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    private static final int DAMAGE = 6;
    private static final int MAGIC = 2;
    private static final int UPG_DAMAGE = 0;
    private static final int UPG_MAGIC = 0;

    private static final int MAT = 2;
    public static final int UPG_MAT = -1;

    public Shotgun() {
        super(ID, info, BasicMod.imagePath("cards/attack/shotgun.png"));
        tags.add(CardTagEnum.GUN);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        setCustomVar("MAT", VariableType.MAGIC, MAT, UPG_MAT);

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

        addToBot(new ConsumeMaterialAction(customVar("MAT"), () -> {
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mo.isDeadOrEscaped()) {
                    addToTop(new ApplyPowerAction(
                            mo,
                            p,
                            new WeakPower(mo, this.magicNumber, false),
                            this.magicNumber
                    ));
                }
            }
        }));
    }
}
