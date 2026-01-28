package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class NuclearReactor extends BaseCard {
    public static final String ID = makeID("NuclearReactor");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            3
    );

    private static final int START = 4;

    public NuclearReactor() {
        super(ID, info, BasicMod.imagePath("cards/skill/nuclear_reactor.png"));
        setCostUpgrade(2);

        this.misc = START;
        syncFromMisc();
    }

    @Override
    public void applyPowers() {
        syncFromMisc();
        super.applyPowers();
    }

    @Override
    public void use(com.megacrit.cardcrawl.characters.AbstractPlayer p, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        syncFromMisc();
        int x = this.magicNumber;
        addToBot(new GainEnergyAction(x));
        addToBot(new ConsumeMaterialAction(x, this::permanentlyIncrease, true, true, true));
    }

    private void permanentlyIncrease() {
        int newVal = this.misc + 1;

        this.misc = newVal;
        syncFromMisc();

        if (AbstractDungeon.player == null) return;

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid)) {
                c.misc = newVal;

                // Keep its displayed numbers in sync too
                if (c instanceof NuclearReactor) {
                    ((NuclearReactor) c).syncFromMisc();
                } else {
                    c.baseMagicNumber = newVal;
                    c.magicNumber = newVal;
                    c.initializeDescription();
                }
                break;
            }
        }
    }

    private void syncFromMisc() {
        int val = Math.max(0, this.misc);
        this.baseMagicNumber = val;
        this.magicNumber = val;
        this.isMagicNumberModified = false;
        this.initializeDescription();
    }
}
