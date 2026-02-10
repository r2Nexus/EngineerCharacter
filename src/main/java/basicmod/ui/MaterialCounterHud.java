package basicmod.ui;

import basemod.BaseMod;
import basemod.interfaces.PostRenderSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import basicmod.BasicMod;
import basicmod.cards.other.Material;
import basicmod.patches.EnergyOrbPositionPatch;
import basicmod.patches.PlayerClassEnum;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MaterialCounterHud implements PostUpdateSubscriber, RenderSubscriber, PostRenderSubscriber {

    private static final String UI_ID = BasicMod.makeID("MaterialCounterUI");
    private static final UIStrings UI = CardCrawlGame.languagePack.getUIString(UI_ID);

    private final Texture[] wedges = new Texture[] {
            ImageMaster.loadImage(BasicMod.imagePath("ui/material_wedge_draw.png")),
            ImageMaster.loadImage(BasicMod.imagePath("ui/material_wedge_hand.png")),
            ImageMaster.loadImage(BasicMod.imagePath("ui/material_wedge_discard.png"))
    };

    private final Hitbox[] hbs = new Hitbox[] {
            new Hitbox(1, 1),
            new Hitbox(1, 1),
            new Hitbox(1, 1)
    };

    private int drawCount, handCount, discardCount;

    private float segW, segH, radius;
    private float lastHbSize = -1f;

    // ---- layout tuning ----
    private static final float CENTER_ANGLE = 125f;
    private static final float ANGLE_STEP   = 35f;

    private static final float SEG_SCALE    = 0.50f;
    private static final float RADIUS_SCALE = 0.57f;
    private static final float RADIUS_PAD   = 12f;

    private static final float ARC_OFF_X = 0f;
    private static final float ARC_OFF_Y = 0f;

    private static final float ART_ANGLE_OFFSET = 0f;

    // ---- sprite anchoring ----
    private static final float WEDGE_ANCHOR_X = 0.50f;
    private static final float WEDGE_ANCHOR_Y = 0.56f;

    // ---- text placement per wedge (0..1 in segment space) ----
    // Order: 0=bottom-left, 1=middle, 2=top
    private static final float[] TEXT_ANCHOR_X = { 0.50f, 0.50f, 0.50f };
    private static final float[] TEXT_ANCHOR_Y = { 0.52f, 0.52f, 0.52f };

    // ---- per-wedge pixel nudges (pre-rotation) ----
    private static final float[] TEXT_NUDGE_X_PX = { -1.0f, 2.0f, 0.0f };
    private static final float[] TEXT_NUDGE_Y_PX = { -3.0f, -1.0f, -2.0f };

    public MaterialCounterHud() {
        BaseMod.subscribe(this);
    }

    private boolean shouldShow() {
        if (CardCrawlGame.dungeon == null) return false;
        if (AbstractDungeon.player == null) return false;
        if (AbstractDungeon.currMapNode == null || AbstractDungeon.currMapNode.room == null) return false;

        AbstractRoom room = AbstractDungeon.currMapNode.room;
        if (room.phase != AbstractRoom.RoomPhase.COMBAT) return false;

        return AbstractDungeon.player.chosenClass == PlayerClassEnum.ENGINEER;
    }

    private static float angForIndex(int i) {
        return CENTER_ANGLE + (1 - i) * ANGLE_STEP;
    }

    private void ensureSizes() {
        float orb = 128f * 1.15f * Settings.scale;
        segW = orb * SEG_SCALE;
        segH = orb * SEG_SCALE;
        radius = orb * RADIUS_SCALE + RADIUS_PAD * Settings.scale;
    }

    @Override
    public void receivePostUpdate() {
        if (!shouldShow()) return;

        ensureSizes();

        drawCount = countMaterial(AbstractDungeon.player.drawPile);
        handCount = countMaterial(AbstractDungeon.player.hand);
        discardCount = countMaterial(AbstractDungeon.player.discardPile);

        float cx = energyOrbCenterX() + ARC_OFF_X * Settings.scale;
        float cy = energyOrbCenterY() + ARC_OFF_Y * Settings.scale;

        float hbSize = Math.max(segW, segH) * 0.65f;

        if (lastHbSize < 0f || Math.abs(hbSize - lastHbSize) > 0.5f) {
            lastHbSize = hbSize;
            for (int i = 0; i < 3; i++) {
                hbs[i] = new Hitbox(hbSize, hbSize);
            }
        }

        for (int i = 0; i < 3; i++) {
            float ang = angForIndex(i);
            float px = cx + MathUtils.cosDeg(ang) * radius;
            float py = cy + MathUtils.sinDeg(ang) * radius;

            hbs[i].move(px, py);
            hbs[i].update();
        }
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (!shouldShow()) return;

        if (segW <= 0f || radius <= 0f) ensureSizes();

        float cx = energyOrbCenterX() + ARC_OFF_X * Settings.scale;
        float cy = energyOrbCenterY() + ARC_OFF_Y * Settings.scale;

        sb.setColor(Color.WHITE);

        for (int i = 0; i < 3; i++) {
            float ang = angForIndex(i);

            float centerX = cx + MathUtils.cosDeg(ang) * radius;
            float centerY = cy + MathUtils.sinDeg(ang) * radius;

            float rotation = (ang - 90f) + ART_ANGLE_OFFSET;

            float originX = segW * WEDGE_ANCHOR_X;
            float originY = segH * WEDGE_ANCHOR_Y;

            float drawX = centerX - originX;
            float drawY = centerY - originY;

            float a = hbs[i].hovered ? 1f : 0.92f;
            sb.setColor(1f, 1f, 1f, a);

            Texture tex = wedges[i];
            sb.draw(
                    tex,
                    drawX, drawY,
                    originX, originY,
                    segW, segH,
                    1f, 1f,
                    rotation,
                    0, 0,
                    tex.getWidth(), tex.getHeight(),
                    false, false
            );

            // ---- TEXT: per-wedge anchor + per-wedge pixel nudge, then rotate ----
            float localX = (TEXT_ANCHOR_X[i] - WEDGE_ANCHOR_X) * segW;
            float localY = (TEXT_ANCHOR_Y[i] - WEDGE_ANCHOR_Y) * segH;

            localX += TEXT_NUDGE_X_PX[i] * Settings.scale;
            localY += TEXT_NUDGE_Y_PX[i] * Settings.scale;

            float textX = centerX + MathUtils.cosDeg(rotation) * localX - MathUtils.sinDeg(rotation) * localY;
            float textY = centerY + MathUtils.sinDeg(rotation) * localX + MathUtils.cosDeg(rotation) * localY;

            sb.setColor(Color.WHITE);

            String text = (i == 0) ? Integer.toString(drawCount)
                    : (i == 1) ? Integer.toString(handCount)
                    : Integer.toString(discardCount);

            FontHelper.renderFontCentered(
                    sb,
                    FontHelper.topPanelAmountFont,
                    text,
                    textX,
                    textY,
                    Color.WHITE
            );
        }
    }

    @Override
    public void receivePostRender(SpriteBatch sb) {
        if (!shouldShow()) return;
        if (!(hbs[0].hovered || hbs[1].hovered || hbs[2].hovered)) return;

        String[] t = UI.TEXT; // convenience

        String header = t[0];
        String body =
                t[1] + " NL NL " +
                        t[2] + "#b" + drawCount + " NL " +
                        t[3] + "#b" + handCount + " NL " +
                        t[4] + "#b" + discardCount;

        TipHelper.renderGenericTip(
                InputHelper.mX + 20f * Settings.scale,
                InputHelper.mY + 20f * Settings.scale,
                header,
                body
        );
    }

    private static int countMaterial(com.megacrit.cardcrawl.cards.CardGroup group) {
        int c = 0;
        for (com.megacrit.cardcrawl.cards.AbstractCard card : group.group) {
            if (Material.ID.equals(card.cardID)) c++;
        }
        return c;
    }

    private static float energyOrbCenterX() {
        return EnergyOrbPositionPatch.valid ? EnergyOrbPositionPatch.x : 190f * Settings.scale;
    }

    private static float energyOrbCenterY() {
        return EnergyOrbPositionPatch.valid ? EnergyOrbPositionPatch.y : 190f * Settings.scale;
    }
}
