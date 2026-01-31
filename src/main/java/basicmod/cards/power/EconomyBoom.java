package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.orbs.MinerOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.EconomyBoomPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EconomyBoom extends BaseCard {
    public static final String ID = makeID("EconomyBoom");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    private static final int PER_TURN = 1;

    public EconomyBoom() {
        super(ID, info, BasicMod.imagePath("cards/power/economy_boom.png"));
        setMagic(PER_TURN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EconomyBoomPower(p, magicNumber), magicNumber));

        if (upgraded) {
            addToBot(new ChannelAction(new MinerOrb()));
        }
    }
}
