package basicmod.cards.skill;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class Exoskeleton extends BaseCard {
    public static final String ID = makeID("Exoskeleton");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    private static final int CHARGE = 8;
    private static final int UPG_CHARGE = 0;

    private static final int BLOCK = 12;
    private static final int UPG_BLOCK = 4;

    private static final int DEX = 2;
    private static final int UPG_DEX = 0;

    public Exoskeleton() {
        super(ID, info, BasicMod.imagePath("cards/skill/exoskeleton.png"));

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(DEX, UPG_DEX); // Dex amount on charge
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));

        addToBot(new SpendChargeAction(this, () -> {
            addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        }));
    }
}
