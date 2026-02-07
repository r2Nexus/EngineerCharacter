package basicmod.cards.power;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.BeaconPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static basicmod.BasicMod.makeID;

public class Beacon extends BaseCard {
    public static final String ID = makeID("Beacon");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private static final int COST = 4;
    private static final int UPG_COST = 3;

    public Beacon() {
        super(ID, info, BasicMod.imagePath("cards/power/beacon.png"));
        setCustomVar("COST", COST, UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // non-stackable: don't apply if already present
        if (!p.hasPower(BeaconPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new BeaconPower(p, customVar("COST")), 1));
        }
    }
}
