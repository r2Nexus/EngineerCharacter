package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.orbs.TurretOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.powers.FlameTurretPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FlameTurret extends BaseCard {
    public static final String ID = makeID("FlameTurret");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int AOE = 3;
    private static final int UPG_AOE = 1;

    public FlameTurret() {
        super(ID, info, BasicMod.imagePath("cards/skill/flame_turret.png"));
        setMagic(AOE, UPG_AOE); // AoE damage
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChannelAction(new TurretOrb()));
        addToBot(new ApplyPowerAction(p, p, new FlameTurretPower(p, this.magicNumber), this.magicNumber));
    }
}
