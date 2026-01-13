package basicmod.cards.skill;

import basemod.BaseMod;
import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class LightArmour extends BaseCard {
    public static final String ID = makeID("LightArmour");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 3;

    private static final int BONUS = 5;
    private static final int UPG_BONUS = 0;

    public LightArmour() {
        super(ID, info, BasicMod.imagePath("cards/skill/light_armour.png"));
        setBlock(BLOCK, UPG_BLOCK);
        setCustomVar("BONUS", VariableType.BLOCK, BONUS, UPG_BONUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new GainBlockAction(p, p, this.block));
        if(BasicMod.materialConsumedThisTurn >= 1) addToBot(
                new GainBlockAction(p,customVar("BONUS")
                ));
    }
}
