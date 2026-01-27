package basicmod.potions;

import basicmod.cards.Material;
import basicmod.patches.PlayerClassEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static basicmod.BasicMod.makeID;

public class MaterialPotion extends BasePotion {
    public static final String ID = makeID("MaterialPotion");
    private static final int POTENCY = 3;

    public MaterialPotion() {
        // Uses a basegame potion color+shape (easy mode).
        super(ID, POTENCY, PotionRarity.COMMON, PotionSize.BOTTLE, PotionColor.SMOKE);

        this.isThrown = false;
        this.targetRequired = false;

        this.playerClass = PlayerClassEnum.ENGINEER;

        Color tungsten = new Color(0.72f, 0.42f, 0.16f, 1f);
        this.labOutlineColor = tungsten.cpy();
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.player == null) return;
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(new Material(), this.potency)
        );
    }

    @Override
    public String getDescription() {

        return DESCRIPTIONS[0].replace("!P!", Integer.toString(this.potency));
    }

    @Override
    public void addAdditionalTips() {

        CardStrings mat = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getCardStrings(Material.ID);
        this.tips.add(new PowerTip(mat.NAME, mat.DESCRIPTION));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new MaterialPotion();
    }
}
