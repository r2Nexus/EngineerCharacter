package basicmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.regex.Pattern;

public class ChargeMod extends AbstractCardModifier {
    public static final String ID = "${modID}:ChargeMod";

    public int charge;
    public int maxCharge;
    private String baseDesc;

    public ChargeMod(int maxCharge, int startingCharge) {
        this.maxCharge = Math.max(1, maxCharge);
        this.charge = Math.max(0, Math.min(startingCharge, this.maxCharge));
    }

    public ChargeMod(int maxCharge) {
        this(maxCharge, 0);
    }

    public boolean isFullyCharged() {
        return charge >= maxCharge;
    }

    public void addCharge(AbstractCard card, int amount) {
        if (amount <= 0 || isFullyCharged()) return;
        charge = Math.min(maxCharge, charge + amount);
        card.superFlash();
        updateDesc(card);
    }

    private static final String PROGRESS_TOKEN = "<CHARGE>";
    private static final Pattern PROGRESS_LINE =
            Pattern.compile("^\\d+\\s*/\\s*\\d+(\\s*\\(Ready\\))?\\s*$");

    private void updateDesc(AbstractCard card) {
        // Build a stable base that always contains the token
        String raw = card.rawDescription;

        if (baseDesc == null) {
            baseDesc = ensureTokenized(raw);
        } else {
            // Keep baseDesc tokenized even if something mutated it earlier
            baseDesc = ensureTokenized(baseDesc);
        }

        // Replace token with *plain* progress text for best centering
        String progress = charge + "/" + maxCharge + (isFullyCharged() ? " (Ready)" : "");
        card.rawDescription = baseDesc.replace(PROGRESS_TOKEN, progress);
        card.initializeDescription();
    }

    private String ensureTokenized(String raw) {
        if (raw.contains(PROGRESS_TOKEN)) return raw;

        // If an old progress line is already there at the end, replace it with the token
        int lastNl = raw.lastIndexOf(" NL ");
        if (lastNl != -1) {
            String tail = raw.substring(lastNl + 4);
            if (PROGRESS_LINE.matcher(tail).matches()) {
                return raw.substring(0, lastNl + 4) + PROGRESS_TOKEN;
            }
        }

        // Fallback: append token on a new line
        return raw + " NL " + PROGRESS_TOKEN;
    }


    @Override
    public void onInitialApplication(AbstractCard card) {
        updateDesc(card);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        charge = 0; // reset when fired (your rule)
        updateDesc(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ChargeMod(maxCharge, charge);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
