package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.orbs.LandMineOrb;
import basicmod.orbs.MinerOrb;
import basicmod.orbs.TurretOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Blueprint extends BaseCard {
    public static final String ID = makeID("Blueprint");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public Blueprint() {
        super(ID, info, BasicMod.imagePath("cards/skill/blueprint.png"));
        setExhaust(true, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ChannelAction(new LandMineOrb()));
        addToBot(new ChannelAction(new TurretOrb()));
        addToBot(new ChannelAction(new MinerOrb()));
    }
}
