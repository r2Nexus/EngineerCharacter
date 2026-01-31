package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.actions.AddMaterialAction;
import basicmod.cards.BaseCard;
import basicmod.cards.other.Material;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static basicmod.actions.AddMaterialAction.Destination.HAND;

public class ExpansionPolicy extends BaseCard {
    public static final String ID = makeID("ExpansionPolicy"); // <-- fix this

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 3;

    public ExpansionPolicy() {
        super(ID, info, BasicMod.imagePath("cards/attack/expansion_policy.png"));
        setDamage(DAMAGE, UPG_DAMAGE);

        this.isMultiDamage = true;
        cardsToPreview = new Material();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int aliveEnemies = 0;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) aliveEnemies++;
        }

        addToBot(new DamageAllEnemiesAction(
                p,
                this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));

        addMaterialToHand(aliveEnemies);
    }
}
