package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Cliff;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TemporarySolution extends BaseCard {
    public static final String ID = makeID("TemporarySolution");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0
    );

    private static final int ENERGY = 2;

    private static final int MATERIAL = 2;
    private static final int UPG_MATERIAL = 1;

    private static final int CLIFFS = 1;

    public TemporarySolution() {
        super(ID, info, BasicMod.imagePath("cards/temporary_solution.png"));
        setMagic(MATERIAL, UPG_MATERIAL);

        setPreviewCycle(
                Material::new,
                Cliff::new
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(ENERGY));
        addToBot(new MakeTempCardInHandAction(new Material(), magicNumber));
        addToBot(new MakeTempCardInDiscardAction(new Cliff(), CLIFFS));
    }
}
