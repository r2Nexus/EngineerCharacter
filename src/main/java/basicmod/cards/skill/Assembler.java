package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.actions.DraftToHandAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Assembler extends BaseCard {
    public static final String ID = makeID("Assembler");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );

    public static final int PICK = 1;
    public static final int UPG_PICK = 1;

    public static final int POOL = 3;
    public static final int UPG_POOL = 0;

    public static final int CONSUME = 3;

    public Assembler() {
        super(ID, info, BasicMod.imagePath("cards/skill/assembler.png"));
        setMagic(PICK,UPG_PICK);
        setCustomVar("POOL", VariableType.MAGIC, POOL, UPG_POOL);
        setCustomVar("CONSUME", VariableType.MAGIC, CONSUME, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ConsumeMaterialAction(customVar("CONSUME"), () ->
                addToTop(new DraftToHandAction(customVar("POOL"), magicNumber, "Choose a card"))));
    }
}
