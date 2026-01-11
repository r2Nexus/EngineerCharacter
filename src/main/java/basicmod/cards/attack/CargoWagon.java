package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CargoWagon extends BaseCard {
    public static final String ID = makeID("CargoWagon");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 3;

    private static final int HAND_MAT = 2;
    private static final int DISC_MAT = 1;
    private static final int UPG_DISC_MAT = 1;

    public CargoWagon() {
        super(ID, info, BasicMod.imagePath("cards/attack/cargo_wagon.png"));
        tags.add(CardTagEnum.WAGON);

        setDamage(DAMAGE, UPG_DAMAGE);

        setCustomVar("HAND", VariableType.MAGIC, HAND_MAT, 0);
        setCustomVar("DISC", VariableType.MAGIC, DISC_MAT, UPG_DISC_MAT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));

        addToBot(new MakeTempCardInHandAction(new Material(), customVar("HAND")));
        addToBot(new MakeTempCardInDiscardAction(new Material(), customVar("DISC")));
    }
}
