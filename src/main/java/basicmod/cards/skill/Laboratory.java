package basicmod.cards.skill;

import basemod.helpers.CardModifierManager;
import basicmod.BasicMod;
import basicmod.actions.SpendChargeAction;
import basicmod.cardmods.ChargeMod;
import basicmod.cards.BaseCard;
import basicmod.cards.power.*;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import basicmod.util.ScienceCardPool;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Laboratory extends BaseCard {
    public static final String ID = makeID("Laboratory");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int DRAW = 2;
    private static final int UPG_DRAW = 1;

    private static final int CHARGE = 8;
    private static final int UPG_CHARGE = 0;

    public Laboratory() {
        super(ID, info, BasicMod.imagePath("cards/skill/science_lab.png"));

        setMagic(DRAW, UPG_DRAW);

        setCustomVar("CHARGE", VariableType.MAGIC, CHARGE, UPG_CHARGE);
        int max = customVar("CHARGE");
        CardModifierManager.addModifier(this, new ChargeMod("CHARGE", max));

        setPreviewCycle(
                RedScience::new,
                GreenScience::new,
                BlackScience::new,
                BlueScience::new,
                PurpleScience::new
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));

        addToBot(new SpendChargeAction(this, () -> {
            AbstractCard proto = ScienceCardPool.getRandom();
            if (proto == null) return;

            AbstractCard science = proto.makeStatEquivalentCopy();
            science.setCostForTurn(0);

            addToBot(new MakeTempCardInHandAction(science, 1));
        }));
    }
}
