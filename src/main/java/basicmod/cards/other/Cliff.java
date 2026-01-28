package basicmod.cards.other;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.attack.ArtilleryWagon;
import basicmod.cards.attack.CargoWagon;
import basicmod.cards.attack.FluidWagon;
import basicmod.cards.skill.ScienceWagon;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Cliff extends BaseCard {
    public static final String ID = makeID("Cliff");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.STATUS,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            1
    );

    public Cliff() {
        super(ID, info, BasicMod.imagePath("cards/material/cliff.png"));
        setExhaust(true);
        setSelfRetain(true);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
