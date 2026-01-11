package basicmod.cards;

import basicmod.BasicMod;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
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

    public Material() {
        super(ID, info, BasicMod.imagePath("cards/material/material.png"));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        this.cantUseMessage = "Material cannot be played.";
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
}
