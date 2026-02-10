package basicmod.patches;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;

@SpirePatch(clz = CustomEnergyOrb.class, method = "renderOrb")
public class EnergyOrbPositionPatch {
    public static float x = 0f;
    public static float y = 0f;
    public static boolean valid = false;

    @SpirePrefixPatch
    public static void Prefix(CustomEnergyOrb __instance, SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        x = current_x;
        y = current_y;
        valid = true;
    }
}
