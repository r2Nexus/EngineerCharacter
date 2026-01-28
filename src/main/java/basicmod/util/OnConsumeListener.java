package basicmod.util;

public interface OnConsumeListener {
    /** Called once per consume action, with the total amount consumed. */
    void onConsume(int amount);
}
