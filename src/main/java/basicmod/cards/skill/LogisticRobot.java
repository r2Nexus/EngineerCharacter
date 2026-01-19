package basicmod.cards.skill;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LogisticRobot extends BaseCard {
    public static final String ID = makeID("LogisticRobot");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private static final int SCRY = 4;
    private static final int UPG_SCRY = 2;   // optional

    private static final int DRAW = 2;

    private static final int CHARGE = 10;
    private static final int UPG_CHARGE = -2;

    public LogisticRobot() {
        super(ID, info, BasicMod.imagePath("cards/skill/logistic_robot.png"));

        setMagic(SCRY, UPG_SCRY);
        setCustomVar("DRAW", VariableType.MAGIC, DRAW, 0);

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ScryAction(magicNumber));
        addToBot(new SpendChargeAction(this, () -> addToTop(new SeekAction(customVar("DRAW")))));
    }
}
