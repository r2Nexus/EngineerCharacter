package basicmod.cards.skill;

import basicmod.BasicMod;
import basicmod.cards.BaseCard;
import basicmod.patches.AbstractCardEnum;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

public class RedBelt extends BaseCard {
    public static final String ID = makeID("RedBelt");

    private static final CardStats info = new CardStats(
            AbstractCardEnum.ENGINEER,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int EVOKE = 1;

    public RedBelt() {
        super(ID, info, BasicMod.imagePath("cards/skill/red_belt.png"));
        setExhaust(true,false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        int filled = (int) AbstractDungeon.player.orbs.stream()
                .filter(o -> !(o instanceof EmptyOrbSlot))
                .count();
        for (int i = 0; i < filled; i++) {
            addToBot(new EvokeOrbAction(EVOKE));
        }
    }
}
