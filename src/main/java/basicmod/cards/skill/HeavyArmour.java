package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class HeavyArmour extends BaseCard {
    public static final String ID = makeID("HeavyArmour");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 9;
    private static final int UPG_BLOCK = 3;

    public HeavyArmour() {
        super(ID, info, BasicMod.imagePath("cards/skill/heavy_armour.png"));
        setBlock(BLOCK, UPG_BLOCK);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));

        addToBot(new ConsumeMaterialAction(1, () -> {
            addToTop(new ApplyPowerAction(
                    p, p,
                    new PlatedArmorPower(p, 3),
                    3
            ));
        }));
    }
}
