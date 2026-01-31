package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeAllMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RedBelt extends BaseCard {
    public static final String ID = makeID("RedBelt");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public RedBelt() {
        super(ID, info, BasicMod.imagePath("cards/skill/red_belt.png"));
        setCostUpgrade(0);

        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConsumeAllMaterialAction(consumed -> {
            int gain = consumed + 1; // that much + 1
            addMaterialToHand(gain);
        }, true, true, true));
    }
}
