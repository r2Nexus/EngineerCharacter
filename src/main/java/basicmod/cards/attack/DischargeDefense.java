package basicmod.cards.attack;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DischargeDefense extends BaseCard {
    public static final String ID = makeID("DischargeDefense");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 0;

    private static final int CHARGE = 8;
    private static final int UPG_CHARGE = -2;

    public DischargeDefense() {
        super(ID, info, BasicMod.imagePath("cards/attack/discharge_defense.png"));

        setBlock(BLOCK, UPG_BLOCK);

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new GainBlockAction(p, p, block));

        int damage = p.currentBlock;
        addToBot(new SpendChargeAction(this, () ->
                addToTop(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.LIGHTNING
        ))));
    }
}
