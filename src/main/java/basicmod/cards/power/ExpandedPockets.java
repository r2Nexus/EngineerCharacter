package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.ExpandedPocketsPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static basicmod.BasicMod.makeID;

public class ExpandedPockets extends BaseCard {
    public static final String ID = makeID("ExpandedPockets");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int BONUS = 3;
    private static final int UPG_BONUS = 0;

    public ExpandedPockets() {
        super(ID, info, BasicMod.imagePath("cards/skill/expanded_pockets.png"));
        setMagic(BONUS, UPG_BONUS);
        setCostUpgrade(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ExpandedPocketsPower(p, this.magicNumber), this.magicNumber));
    }
}
