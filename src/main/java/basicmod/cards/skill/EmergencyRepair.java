package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EmergencyRepair extends BaseCard {
    public static final String ID = makeID("EmergencyRepair");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0
    );

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 4;

    public EmergencyRepair() {
        super(ID, info, BasicMod.imagePath("cards/skill/emergency_repair.png"));
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConsumeMaterialAction(1, () -> {
            addToTop(new GainBlockAction(p, p, this.block));
        }));
    }
}
