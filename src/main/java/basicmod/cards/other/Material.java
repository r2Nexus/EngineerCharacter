package basicmod.cards.other;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Material extends BaseCard {
    public static final String ID = makeID("Material");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    private static final int GEN = 2;

    public Material() {
        super(ID, info, BasicMod.imagePath("cards/material/material.png"));
        // leave it unplayable by default
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();

            // Make it playable
            upgradeBaseCost(0);

            // Make it a normal self-target skill when upgraded
            this.target = CardTarget.SELF;

            // Exhaust on use
            this.exhaust = true;

            // Swap to upgrade description
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            this.cantUseMessage = "Material cannot be played.";
            return false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) return;

        // Add 2 normal (unupgraded) Materials to hand
        addMaterialToHand(2);
    }
}
