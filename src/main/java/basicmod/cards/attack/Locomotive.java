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

        // IMPORTANT: do the replay AFTER the damage actually resolves,
        // so we can retarget if the original target died.
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                replayEarlierWagons(m, Locomotive.this.uuid);
                isDone = true;
            }
        });
    }

    private void replayEarlierWagons(AbstractMonster preferredTarget, UUID thisUUID) {
        ArrayList<AbstractCard> played = new ArrayList<>(AbstractDungeon.actionManager.cardsPlayedThisCombat);

        for (AbstractCard c : played) {
            if (c == null) continue;

            // Your original logic: stop once we reach this Locomotive in the played list.
            // (Locomotive is played "now", so it should appear at/near the end.)
            if (c.uuid.equals(thisUUID)) break;

            if (!c.hasTag(CardTagEnum.WAGON)) continue;

            AbstractMonster t = pickTargetForReplay(c, preferredTarget);

            // If the replay NEEDS a monster but none exist, skip to avoid "card stuck on screen"
            if (needsMonsterTarget(c) && t == null) continue;

            queueReplay(c, t);
        }
    }

    private boolean needsMonsterTarget(AbstractCard card) {
        return !(card.target == AbstractCard.CardTarget.SELF
                || card.target == AbstractCard.CardTarget.ALL
                || card.target == AbstractCard.CardTarget.ALL_ENEMY
                || card.target == AbstractCard.CardTarget.NONE);
    }

    private AbstractMonster pickTargetForReplay(AbstractCard card, AbstractMonster preferred) {
        // Cards that don't need a monster target
        if (!needsMonsterTarget(card)) return null;

        // If preferred is alive, use it
        if (preferred != null && !preferred.isDeadOrEscaped()) return preferred;

        // Otherwise pick a living monster (null if none alive)
        return AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
    }

    private void queueReplay(AbstractCard original, AbstractMonster target) {
        if (original == null) return;

        AbstractCard copy = original.makeSameInstanceOf();
        copy.purgeOnUse = true;          // don't go to discard/exhaust
        copy.freeToPlayOnce = true;      // don't spend energy
        copy.exhaustOnUseOnce = true;    // safety

        copy.current_x = this.current_x;
        copy.current_y = this.current_y;
        copy.target_x = Settings.WIDTH / 2f;
        copy.target_y = Settings.HEIGHT / 2f;

        // Apply powers and (if needed) recalc damage vs chosen target
        copy.applyPowers();
        if (target != null) {
            copy.calculateCardDamage(target);
        }

        AbstractDungeon.player.limbo.addToBottom(copy);

        // NOTE: Only pass a non-null target for cards that need one (we ensured that above)
        AbstractDungeon.actionManager.addCardQueueItem(
                new CardQueueItem(copy, target, copy.energyOnUse, true, true),
                true
        );
    }
}
