package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Fish extends BaseCard {
    public static final String ID = makeID("Fish");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int HEAL = 3;
    private static final int UPG_HEAL = 0;

    private static final int B_HEAL = 3;
    private static final int UPG_B_HEAL = 2;

    public Fish() {
        super(ID, info, BasicMod.imagePath("cards/skill/fish.png"));
        setExhaust(true);

        setMagic(HEAL, UPG_HEAL);
        setCustomVar("B_HEAL", VariableType.MAGIC, B_HEAL, UPG_B_HEAL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HealAction(p, p, magicNumber));
        addToBot(new ConsumeMaterialAction(2, () -> addToTop(new HealAction(p, p, customVar("B_HEAL")))));
    }
}
