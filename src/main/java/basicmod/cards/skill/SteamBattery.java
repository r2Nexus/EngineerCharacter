package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;

public class SteamBattery extends BaseCard {
    public static final String ID = makeID("SteamBattery");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            AbstractCard.CardType.SKILL,
            CardRarity.UNCOMMON,
            AbstractCard.CardTarget.SELF,
            1
    );

    private static final int ENERGY_NEXT_TURN = 1;
    private static final int UPG_ENERGY = 0;
    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 3;

    public SteamBattery() {
        super(ID, info, BasicMod.imagePath("cards/skill/steam_battery.png"));
        setMagic(ENERGY_NEXT_TURN, UPG_ENERGY);
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, magicNumber), magicNumber));
    }
}
