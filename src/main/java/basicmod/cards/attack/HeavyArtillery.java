package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.actions.ConsumeMaterialFromPilesAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class HeavyArtillery extends BaseCard {
    public static final String ID = makeID("HeavyArtillery");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2
    );

    private static final int DAMAGE = 10;
    private static final int UPG_DAMAGE = 0;

    private static final int SPLASH = 15;
    private static final int UPG_SPLASH = 5;

    public HeavyArtillery() {
        super(ID, info, BasicMod.imagePath("cards/attack/heavy_artillery.png"));
        setDamage(DAMAGE, UPG_DAMAGE);
        setCustomVar("SPLASH", VariableType.DAMAGE, SPLASH, UPG_SPLASH);
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Base damage (always)
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SMASH
        ));

        addToBot(new ConsumeMaterialFromPilesAction(3, () -> {
            addToBot(new DamageAllEnemiesAction(
                    p,
                    customVarMulti("SPLASH"),
                    damageTypeForTurn,
                    AbstractGameAction.AttackEffect.SMASH
            ));
        },
                true,
                true,
                true
        ));
    }
}
