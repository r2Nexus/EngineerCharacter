package basicmod.cards.attack;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PersonalLaser extends BaseCard {
    public static final String ID = makeID("PersonalLaser");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int CHARGE = 5;
    private static final int UPG_CHARGE = 0;

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 2;

    public PersonalLaser() {
        super(ID, info, BasicMod.imagePath("cards/attack/personal_laser.png"));

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        for (int i = 0; i < 4; i++) {
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }
    }
}
