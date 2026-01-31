package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.attack.ArtilleryWagon;
import basicmod.cards.attack.CargoWagon;
import basicmod.cards.attack.FluidWagon;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.ChainSignalPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChainSignal extends BaseCard {
    public static final String ID = makeID("ChainSignal");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private static final int AMOUNT = 1;
    private static final int UPG_AMOUNT = 1;

    public ChainSignal() {
        super(ID, info, BasicMod.imagePath("cards/skill/chain_signal.png"));
        setMagic(AMOUNT, UPG_AMOUNT);
        setExhaust(true);

        setPreviewCycle(
                CargoWagon::new,
                ArtilleryWagon::new,
                FluidWagon::new,
                ScienceWagon::new
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ApplyPowerAction(p, p, new ChainSignalPower(p, magicNumber), magicNumber));
    }
}
