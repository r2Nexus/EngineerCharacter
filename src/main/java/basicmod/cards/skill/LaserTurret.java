package basicmod.cards.skill;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.orbs.TurretOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.FreeTurretFirePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LaserTurret extends BaseCard {
    public static final String ID = makeID("LaserTurret");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int CHARGE = 8;
    private static final int UPG_CHARGE = 0;

    public LaserTurret() {
        super(ID, info, BasicMod.imagePath("cards/skill/laser_turret.png"));

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));

        setCostUpgrade(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChannelAction(new TurretOrb()));

        addToBot(new SpendChargeAction(this, () -> {
            addToTop(new ApplyPowerAction(p, p, new FreeTurretFirePower(p), 1));
        }));
    }
}
