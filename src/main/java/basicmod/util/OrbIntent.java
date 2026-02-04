package basicmod.util;

public class OrbIntent {
    public enum Type {
        ATTACK,
        ATTACK_DEFEND
        // later: ATTACK_MULTI, DEFEND, BUFF, DEBUFF, SPECIAL...
    }

    public final Type type;

    // Primary number (damage for attacks)
    public final int amount;

    // Optional secondary (block for ATTACK_DEFEND)
    public final int secondaryAmount;

    public OrbIntent(Type type, int amount) {
        this(type, amount, 0);
    }

    public OrbIntent(Type type, int amount, int secondaryAmount) {
        this.type = type;
        this.amount = amount;
        this.secondaryAmount = secondaryAmount;
    }

    public boolean hasSecondary() {
        return secondaryAmount > 0;
    }
}
