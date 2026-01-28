package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.orbs.LandMineOrb; // <-- rename if your mine orb is different
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.PoisonMinesPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PoisonCapsule extends BaseCard {
    public static final String ID = makeID("PoisonCapsule");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int POISON = 2;
    private static final int UPG_POISON = 1;

    public PoisonCapsule() {
        super(ID, info, BasicMod.imagePath("cards/skill/poison_capsule.png"));
        setMagic(POISON, UPG_POISON);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChannelAction(new LandMineOrb()));

        addToBot(new ConsumeMaterialAction(1, () ->
                addToTop(new ApplyPowerAction(p, p, new PoisonMinesPower(p, p, magicNumber), magicNumber))
        ));
    }
}
