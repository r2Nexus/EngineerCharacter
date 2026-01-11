package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
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
            1
    );

    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 0;
    private static final int MAT = 2;
    private static final int UPG_MAT = 1;

    public LogisticRobot() {
        super(ID, info, BasicMod.imagePath("cards/skill/logistic_robot.png"));
        setExhaust(true);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAT, UPG_MAT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));

        addToBot(new ConsumeMaterialAction(magicNumber, () ->{
            addToBot(new SeekAction(1));
        }));
    }
}
