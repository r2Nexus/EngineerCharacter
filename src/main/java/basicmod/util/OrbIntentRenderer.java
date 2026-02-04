package basicmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class OrbIntentRenderer {
    private OrbIntentRenderer() {}

    private static final Texture[] ATK_INTENTS = new Texture[] {
            ImageMaster.INTENT_ATK_1,
            ImageMaster.INTENT_ATK_2,
            ImageMaster.INTENT_ATK_3,
            ImageMaster.INTENT_ATK_4,
            ImageMaster.INTENT_ATK_5,
            ImageMaster.INTENT_ATK_6,
            ImageMaster.INTENT_ATK_7
    };

    private static Texture getAttackIconForDamage(int dmg) {
        if (dmg <= 4)  return ATK_INTENTS[0];
        if (dmg <= 9)  return ATK_INTENTS[1];
        if (dmg <= 14) return ATK_INTENTS[2];
        if (dmg <= 19) return ATK_INTENTS[3];
        if (dmg <= 24) return ATK_INTENTS[4];
        if (dmg <= 29) return ATK_INTENTS[5];
        return ATK_INTENTS[6];
    }

    /**
     * Draw intent icon centered at (cx, cy) using its native pixel size.
     * This prevents "shield too huge" issues caused by forcing everything into 128x128.
     */
    private static void drawCenteredNative(SpriteBatch sb, Texture tex, float cx, float cy,
                                           float scaleMult, float shadowAlpha) {
        if (tex == null) return;

        float scale = Settings.scale * scaleMult;

        float w = tex.getWidth();
        float h = tex.getHeight();

        // shadow
        sb.setColor(0f, 0f, 0f, shadowAlpha);
        sb.draw(tex,
                cx - (w / 2f) + 2f * Settings.scale,
                cy - (h / 2f) - 2f * Settings.scale,
                w / 2f, h / 2f,
                w, h,
                scale, scale,
                0f,
                0, 0,
                (int) w, (int) h,
                false, false);

        // main
        sb.setColor(Color.WHITE);
        sb.draw(tex,
                cx - (w / 2f),
                cy - (h / 2f),
                w / 2f, h / 2f,
                w, h,
                scale, scale,
                0f,
                0, 0,
                (int) w, (int) h,
                false, false);
    }

    private static void renderScaledNumber(SpriteBatch sb, BitmapFont font, String text,
                                           float x, float y, Color color, float scaleMult) {
        // FontHelper fonts are shared; always restore scale
        float oldX = font.getData().scaleX;
        float oldY = font.getData().scaleY;

        font.getData().setScale(oldX * scaleMult, oldY * scaleMult);
        FontHelper.renderFontLeftTopAligned(sb, font, text, x, y, color);

        font.getData().setScale(oldX, oldY);
    }

    /**
     * @param cx,cy are the intent "center" position (like intentHb.cX/cY for monsters).
     * IMPORTANT: if your caller already adds bobEffect.y into cy, do NOT add bob here.
     */
    public static void renderIntent(SpriteBatch sb, OrbIntent intent,
                                    float cx, float cy,
                                    float iconScaleMult, float shadowAlpha,
                                    boolean drawNumbers) {
        if (intent == null) return;

        // =========================
        // ICON(S)
        // =========================
        if (intent.type == OrbIntent.Type.ATTACK_DEFEND) {

            Texture shield = ImageMaster.INTENT_DEFEND;
            Texture weapon = getAttackIconForDamage(intent.amount);


            float shieldScale = iconScaleMult;
            float weaponScale = iconScaleMult;

            drawCenteredNative(sb, shield, cx, cy, shieldScale, shadowAlpha);
            drawCenteredNative(sb, weapon, cx, cy, weaponScale, shadowAlpha);
        } else {
            Texture weapon = getAttackIconForDamage(intent.amount);
            drawCenteredNative(sb, weapon, cx, cy, iconScaleMult, shadowAlpha);
        }

        if (!drawNumbers) return;

        // =========================
        // NUMBERS (monster-style bottom-left)
        // =========================
        float x = cx - 30.0F * Settings.scale;
        float yCenter = cy - 12.0F * Settings.scale; // the "single number" baseline

        BitmapFont font = FontHelper.topPanelInfoFont;

        boolean two = intent.secondaryAmount > 0;

        // Tuning knobs
        float primaryScale = 1.0f;      // attack slightly larger
        float secondaryScale = 0.85f;    // block slightly smaller
        float pairGap = 25.0f * Settings.scale;

        if (!two) {
            renderScaledNumber(sb, font, Integer.toString(intent.amount), x, yCenter, Color.WHITE, primaryScale);
        } else {
            // primary above center, secondary below center
            float yPrimary   = yCenter + (pairGap / 2f);
            float ySecondary = yCenter - (pairGap / 2f);

            renderScaledNumber(sb, font, Integer.toString(intent.amount), x, yPrimary, Color.WHITE, primaryScale);
            renderScaledNumber(sb, font, Integer.toString(intent.secondaryAmount), x, ySecondary, Color.WHITE, secondaryScale);
        }
    }
}
