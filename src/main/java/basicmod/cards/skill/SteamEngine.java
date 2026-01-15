package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.cards.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class SteamEngine extends BaseCard {
    public static final String ID = makeID("SteamEngine");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int MAT = 2;
    private static final int CONS = 3;

    public SteamEngine() {
        super(ID, info, BasicMod.imagePath("cards/skill/steam_engine.png"));
        setMagic(MAT);
        setCustomVar("CONS", VariableType.MAGIC, CONS, 0);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new MakeTempCardInHandAction(new Material(),magicNumber));
        addToBot(new ConsumeMaterialAction(customVar("CONS"), () -> {
            addToBot(new GainEnergyAction(2));
        },
                true,
                upgraded,
                upgraded));
    }
}

