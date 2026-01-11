package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import basicmod.util.WagonTracker;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RailSignal extends BaseCard {
    public static final String ID = makeID("RailSignal");

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
    private static final int UPG_BONUS = 2;

    public RailSignal() {
        super(ID, info, BasicMod.imagePath("cards/skill/rail_signal.png"));
        setBlock(BLOCK, UPG_BLOCK);
        setCustomVar("BONUS", VariableType.BLOCK, BONUS, UPG_BONUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));

        if (WagonTracker.playedWagonThisTurn) {
            addToBot(new GainBlockAction(p, p, customVar("BONUS")));
        }
    }
}
