package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.orbs.TurretOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Turret extends BaseCard {
    public static final String ID = makeID("Turret");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    public Turret() {
        super(ID, info, BasicMod.imagePath("cards/skill/turret.png"));
        setCostUpgrade(0);
        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ChannelAction(new TurretOrb()));
        addToBot(new MakeTempCardInHandAction(new Material()));
    }
}
