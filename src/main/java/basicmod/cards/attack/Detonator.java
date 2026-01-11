package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.orbs.LandMineOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Detonator extends BaseCard {
    public static final String ID = makeID("Detonator");

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );


    public Detonator() {
        super(ID, info, BasicMod.imagePath("cards/attack/detonator.png"));
        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));
        addToBot(new ChannelAction(new LandMineOrb()));
    }
}
