package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.actions.TriggerTurretsAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TargetPractice extends BaseCard {
    public static final String ID = makeID("TargetPractice");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;

    public TargetPractice() {
        super(ID, info, BasicMod.imagePath("cards/attack/target_practice.png"));
        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Base damage (always)
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));

        addToBot(new ConsumeMaterialAction(
                1,
                () -> addToTop(new TriggerTurretsAction()),
                true,
                upgraded,
                upgraded));
    }
}
