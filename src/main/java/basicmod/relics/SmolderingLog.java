package basicmod.relics;

import basicmod.patches.AbstractCardEnum;
import basicmod.util.ConsumeEvents;
import basicmod.util.OnConsumeListener;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class SmolderingLog extends BaseRelic implements OnConsumeListener {
    public static final String ID = makeID("SmolderingLog");

    public SmolderingLog() {
        super(ID, "smoldering_log_relic", AbstractCardEnum.ENGINEER, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onConsume(int amount) {
        if (amount <= 0) return;
        if (AbstractDungeon.player == null) return;

        // ignore end-of-turn auto-consume
        // if (ConsumeEvents.isEndOfTurnContext()) return;

        flash();
        AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
        addToBot(new MakeTempCardInHandAction(c, 1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SmolderingLog();
    }
}
