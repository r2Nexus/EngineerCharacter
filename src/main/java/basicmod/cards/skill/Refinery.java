package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeAllMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.cards.Material;
import basicmod.orbs.TurretOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Refinery extends BaseCard {
    public static final String ID = makeID("Refinery");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int GAIN = 2;
    private static final int UPG_GAIN = 1;

    public Refinery() {
        super(ID, info, BasicMod.imagePath("cards/skill/refinery.png"));
        setMagic(GAIN, UPG_GAIN);

        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConsumeAllMaterialAction(consumed ->{
            if(consumed >= 1) addToTop(new MakeTempCardInDiscardAction(new Material(), consumed * magicNumber));
        }, true, false, false));
    }
}
