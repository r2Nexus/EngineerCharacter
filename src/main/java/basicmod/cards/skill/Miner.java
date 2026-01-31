package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.orbs.MinerOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Miner extends BaseCard {
    public static final String ID = makeID("Miner");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public static final int MATERIALS = 1;
    public static final int UPG_MATERIALS = 1;

    public Miner() {
        super(ID, info, BasicMod.imagePath("cards/skill/miner.png"));
        setMagic(MATERIALS, UPG_MATERIALS);

        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ChannelAction(new MinerOrb()));
        addMaterialToHand(magicNumber);
    }
}
