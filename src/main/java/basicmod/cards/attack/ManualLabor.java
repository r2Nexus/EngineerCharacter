package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ManualLabor extends BaseCard {
    public static final String ID = makeID("ManualLabor");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 4;

    private static final int DRAW = 2;

    public ManualLabor() {
        super(ID, info, BasicMod.imagePath("cards/attack/manual_labor.png"));
        setMagic(DRAW);
        setDamage(DAMAGE, UPG_DAMAGE);

        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));

        addToBot(new MakeTempCardInHandAction(new Material(), magicNumber));
    }
}
