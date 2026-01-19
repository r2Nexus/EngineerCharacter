package basicmod.cards.skill;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LogisticRobot extends BaseCard {
    public static final String ID = makeID("LogisticRobot");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            0
    );

    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 4;

    private static final int CHARGE = 10;
    private static final int UPG_CHARGE = -2;

    public LogisticRobot() {
        super(ID, info, BasicMod.imagePath("cards/skill/logistic_robot.png"));
        setExhaust(true);
        setBlock(BLOCK, UPG_BLOCK);
        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new SeekAction(2));
    }
}
