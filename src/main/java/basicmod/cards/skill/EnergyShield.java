package basicmod.cards.skill;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EnergyShield extends BaseCard {
    public static final String ID = makeID("EnergyShield");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 0;

    private static final int BONUS = 8;
    private static final int UPG_BONUS = 0;

    private static final int CHARGE = 8;
    private static final int UPG_CHARGE = -2;

    public EnergyShield() {
        super(ID, info, BasicMod.imagePath("cards/skill/energy_shield.png"));

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));

        setCustomVar("BONUS", VariableType.BLOCK, BONUS, UPG_BONUS);
        setBlock(BLOCK, UPG_BLOCK);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new GainBlockAction(p, p, block));
        addToBot(new SpendChargeAction(this, () -> addToTop(new GainBlockAction(p, p, customVar("BONUS")))));
    }
}
