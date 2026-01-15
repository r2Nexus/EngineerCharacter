package basicmod.cards.attack;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.patches.CardTagEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.UUID;

public class Locomotive extends BaseCard {
    public static final String ID = makeID("Locomotive");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2
    );

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 5;

    public Locomotive() {
        super(ID, info, BasicMod.imagePath("cards/attack/locomotive.png"));
        setDamage(DAMAGE, UPG_DAMAGE);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ));

        // Snapshot and replay wagons played earlier this combat
        replayEarlierWagons(m, this.uuid);
    }

    private void replayEarlierWagons(AbstractMonster initialTarget, UUID thisUUID) {
        ArrayList<AbstractCard> played = new ArrayList<>(AbstractDungeon.actionManager.cardsPlayedThisCombat);

        for (AbstractCard c : played) {
            if (c.uuid.equals(thisUUID)) break;
            if (!c.hasTag(CardTagEnum.WAGON)) continue;

            queueReplay(c, pickTargetForReplay(c, initialTarget));
        }
    }

    private AbstractMonster pickTargetForReplay(AbstractCard card, AbstractMonster preferred) {
        // Cards that don't need a monster target
        if (card.target == AbstractCard.CardTarget.SELF
                || card.target == AbstractCard.CardTarget.ALL
                || card.target == AbstractCard.CardTarget.ALL_ENEMY
                || card.target == AbstractCard.CardTarget.NONE) {
            return null;
        }

        // For ENEMY / SELF_AND_ENEMY etc.
        if (preferred != null && !preferred.isDeadOrEscaped()) {
            return preferred;
        }

        // Pick a living monster if the preferred is dead
        return AbstractDungeon.getRandomMonster();
    }

    private void queueReplay(AbstractCard original, AbstractMonster target) {
        AbstractCard copy = original.makeSameInstanceOf();
        copy.purgeOnUse = true;          // don't go to discard/exhaust
        copy.freeToPlayOnce = true;      // don't spend energy
        copy.exhaustOnUseOnce = true;    // safety
        copy.current_x = this.current_x;
        copy.current_y = this.current_y;
        copy.target_x = Settings.WIDTH / 2f;
        copy.target_y = Settings.HEIGHT / 2f;

        copy.applyPowers();

        AbstractDungeon.player.limbo.addToBottom(copy);
        AbstractDungeon.actionManager.addCardQueueItem(
                new CardQueueItem(copy, target, copy.energyOnUse, true, true),
                true
        );
    }
}
