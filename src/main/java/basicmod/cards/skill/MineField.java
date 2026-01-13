package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.orbs.LandMineOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class MineField extends BaseCard {
    public static final String ID = makeID("MineField");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -1 // X-cost
    );

    public static final int MAGIC = 1;
    public static final int UPG_MAGIC = 1;

    public MineField() {
        super(ID, info, BasicMod.imagePath("cards/attack/mine_field.png"));
        setMagic(MAGIC, UPG_MAGIC);          // base: X + 1 mines, upgrade: X + 2 mines
        setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energy = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) energy = this.energyOnUse;

        int chemicalXBonus = 0;
        if (p.hasRelic(ChemicalX.ID)) {
            chemicalXBonus = 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        int minesToChannel = energy + chemicalXBonus + this.magicNumber;

        // Spend the energy actually used (NOT the Chemical X bonus)
        if (!this.freeToPlayOnce) {
            p.energy.use(energy);
        }

        for (int i = 0; i < minesToChannel; i++) {
            addToBot(new ChannelAction(new LandMineOrb()));
        }
    }
}
