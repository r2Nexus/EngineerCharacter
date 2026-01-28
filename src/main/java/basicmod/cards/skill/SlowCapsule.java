package basicmod.cards.skill;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.orbs.LandMineOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.DebuffMinesPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SlowCapsule extends BaseCard {
    public static final String ID = makeID("SlowCapsule");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int WEAK = 1;
    private static final int FRAGILE = 1;

    private static final int CHARGE = 7;
    private static final int UPG_CHARGE = 0;

    public SlowCapsule() {
        super(ID, info, BasicMod.imagePath("cards/skill/slow_capsule.png"));

        // store debuff amounts in magic/second var style
        setMagic(WEAK); // We'll treat magicNumber = weak amount
        setCustomVar("VULN", VariableType.MAGIC, FRAGILE, 0);

        // Charge display + gating like LaserTurret
        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChannelAction(new LandMineOrb()));

        addToBot(new SpendChargeAction(this, () -> {
            int weakAmt = magicNumber;
            int fragileAmt = customVar("VULN");

            addToTop(new ApplyPowerAction(
                    p, p,
                    new DebuffMinesPower(p, p, fragileAmt, weakAmt),
                    1
            ));
        }));
    }
}
