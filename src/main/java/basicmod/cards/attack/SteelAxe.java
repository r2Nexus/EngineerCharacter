package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.cards.skill.Miner;
import basicmod.orbs.MinerOrb;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SteelAxe extends BaseCard {
    public static final String ID = makeID("SteelAxe");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 3;

    public SteelAxe() {
        super(ID, info, BasicMod.imagePath("cards/attack/steel_axe.png"));

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageCallbackAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                unblockedDamageDealt -> {
                    // check if did damage
                    if (unblockedDamageDealt <= 0) return;

                    // check if dead
                    if ((m.isDying || m.currentHealth <= 0) && !m.halfDead) {
                        // channel
                        addToTop(new ChannelAction(new MinerOrb())); // <- swap Miner() for your orb class
                    }
                }
        ));
    }
}
