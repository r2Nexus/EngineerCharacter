package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.actions.ConsumeAllMaterialAction;
import basicmod.actions.ConsumeMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.cards.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Tank extends BaseCard {
    public static final String ID = makeID("Tank");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 4;

    private static final int DAMAGE = 14;
    private static final int UPG_DAMAGE = 0;

    private static final int CONSUME = 2;
    private static final int REPEATS = 4;

    public Tank() {
        super(ID, info, BasicMod.imagePath("cards/skill/tank.png"));
        setBlock(BLOCK, UPG_BLOCK);
        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new GainBlockAction(p, p, block));

        for(int i = 0; i < REPEATS; i++)
        {
            addToBot(new ConsumeMaterialAction(CONSUME, () ->
            {
                addToTop(new DamageAction(
                        m,
                        new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT
                ));
            }, true, true, true));
        }
    }
}
