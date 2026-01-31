package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Scrap extends BaseCard {
    public static final String ID = makeID("Scrap");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0
    );

    private static final int BLOCK_PER_MATERIAL = 5;
    private static final int UPG_BLOCK_PER_MATERIAL = -1;

    public Scrap() {
        super(ID, info, BasicMod.imagePath("cards/skill/scrap.png"));
        setMagic(BLOCK_PER_MATERIAL, UPG_BLOCK_PER_MATERIAL);
        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int block = Math.max(0, p.currentBlock);
        int mats = block / magicNumber;

        if (block > 0) {
            addToBot(new LoseBlockAction(p, p, block));
        }
        if (mats > 0) {
            addMaterialToHand(mats);
        }
    }
}
