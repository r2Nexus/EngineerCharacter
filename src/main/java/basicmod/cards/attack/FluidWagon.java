package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.CountMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FluidWagon extends BaseCard {
    public static final String ID = makeID("FluidWagon");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 3;

    private static final int BLOCK_PER = 1;
    private static final int UPG_BLOCK_PER = 1;

    public FluidWagon() {
        super(ID, info, BasicMod.imagePath("cards/attack/fluid_wagon.png"));
        tags.add(CardTagEnum.WAGON);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(BLOCK_PER, UPG_BLOCK_PER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));

        addToBot(new CountMaterialAction(materials -> {
            if (materials > 0) {
                addToBot(new GainBlockAction(p, p, materials * magicNumber));
            }
        }));
    }
}
