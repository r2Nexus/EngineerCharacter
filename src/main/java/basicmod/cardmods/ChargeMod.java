package basicmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basicmod.cards.BaseCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.regex.Pattern;

public class ChargeMod extends AbstractCardModifier {
    public static final String ID = "${modID}:ChargeMod";

    public int charge;
    private final String varKey; // e.g. "CHARGE"

    private static final String PROGRESS_TOKEN = "<CHARGE>";
    private static final Pattern PROGRESS_LINE =
            Pattern.compile("^\\d+\\s*/\\s*\\d+(\\s*\\(Ready\\))?\\s*$");

    public ChargeMod(String varKey, int startingCharge) {
        this.varKey = varKey;
        this.charge = Math.max(0, startingCharge);
    }

    public ChargeMod(String varKey) {
        this(varKey, 0);
    }

    public int getMaxCharge(AbstractCard card) {
        if (card instanceof BaseCard) {
            int max = ((BaseCard) card).customVar(varKey); // <-- always current (base/upgrade/etc.)
            return Math.max(1, max);
        }
        return 1; // fallback so nothing crashes if applied to non-BaseCard
    }

    private void clamp(AbstractCard card) {
        int max = getMaxCharge(card);
        if (charge > max) charge = max;
        if (charge < 0) charge = 0;
    }

    public boolean isFullyCharged(AbstractCard card) {
        return charge >= getMaxCharge(card);
    }

    public void addCharge(AbstractCard card, int amount) {
        if (amount <= 0) return;

        clamp(card);
        int max = getMaxCharge(card);
        if (charge >= max) return;

        charge = Math.min(max, charge + amount);
        card.superFlash();
        card.initializeDescription(); // triggers modifyDescription()
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        int max = getMaxCharge(card);
        clamp(card);

        String base = ensureTokenized(rawDescription);
        String progress = charge + "/" + max + (charge >= max ? " (Ready)" : "");
        return base.replace(PROGRESS_TOKEN, progress);
    }

    private String ensureTokenized(String raw) {
        if (raw.contains(PROGRESS_TOKEN)) return raw;

        int lastNl = raw.lastIndexOf(" NL ");
        if (lastNl != -1) {
            String tail = raw.substring(lastNl + 4);
            if (PROGRESS_LINE.matcher(tail).matches()) {
                return raw.substring(0, lastNl + 4) + PROGRESS_TOKEN;
            }
        }
        return raw + " NL " + PROGRESS_TOKEN;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        clamp(card);
        card.initializeDescription();
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        charge = 0; // your rule
        card.initializeDescription();
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ChargeMod(varKey, charge);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
