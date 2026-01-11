package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class PiercingRounds extends BaseCard {
    public static final String ID = makeID("PiercingRounds");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            AbstractCard.CardType.ATTACK,
            AbstractCard.CardRarity.COMMON,
            AbstractCard.CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 7;
    private static final int MAGIC = 2;
    private static final int UPG_DAMAGE = 0;
    private static final int UPG_MAGIC = 1;

    public PiercingRounds() {
        super(ID, info, BasicMod.imagePath("cards/attack/piercing_rounds.png"));
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
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
            addToBot(new ApplyPowerAction(
                    m,
                    p,
                    new VulnerablePower(m, magicNumber, false),
                    magicNumber
            ));
        }));
    }
}
