package basicmod.util;

public class WagonTracker {
    private WagonTracker() {}

    // true if a Wagon has been played this turn
    public static boolean playedWagonThisTurn = false;

    public static void resetTurn() {
        playedWagonThisTurn = false;
    }
}
