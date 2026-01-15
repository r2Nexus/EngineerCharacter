package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Railgun extends BaseCard {
    public static final String ID = makeID("Railgun");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            3
    );

    private static final int MAGIC = 6;      // constant per science played
    private static final int UPG_MAGIC = 2;  // upgrade bonus (optional)

    private final String BASE_DESCRIPTION;

    public Railgun() {
        super(ID, info, BasicMod.imagePath("cards/attack/railgun.png"));
        setCostUpgrade(2);

        setMagic(MAGIC, UPG_MAGIC);
        setDamage(0); // dynamic

        BASE_DESCRIPTION = this.rawDescription;
        initializeDescription();
    }

    private int computeDynamicBaseDamage() {
        int sciences = Math.max(0, BasicMod.sciencePlayedThisCombat);
        return magicNumber * sciences;
    }

    private void updateDynamicDescription() {
        this.rawDescription = BASE_DESCRIPTION + " NL (Currently: " + this.damage + ")";
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int sciences = Math.max(1, BasicMod.sciencePlayedThisCombat);
        int dmg = magicNumber * sciences;

        addToBot(new DamageAction(
                m,
                new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL)
        ));
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;

        baseDamage = computeDynamicBaseDamage();
        super.applyPowers();
        baseDamage = realBase;

        updateDynamicDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;

        baseDamage = computeDynamicBaseDamage();
        super.calculateCardDamage(mo);
        baseDamage = realBase;

        updateDynamicDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = BASE_DESCRIPTION;
        initializeDescription();
    }
}
