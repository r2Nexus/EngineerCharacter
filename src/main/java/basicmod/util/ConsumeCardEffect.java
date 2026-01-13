package basicmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ConsumeCardEffect extends AbstractGameEffect {
    private static final float MIN_SCALE = 0.01f;

    private final AbstractCard card;
    private final float startScale;
    private final float startAngle;

    public ConsumeCardEffect(AbstractCard card) {
        this.card = card;

        card.unhover();
        card.stopGlowing();
        card.isGlowing = false;

        startScale = Math.max(card.drawScale, MIN_SCALE);
        startAngle = card.angle;

        duration = 0.35f;
        startingDuration = duration;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();

        float t = 1f - (duration / startingDuration);
        t = Math.min(1f, Math.max(0f, t));

        // shrink (but never hit 0)
        float newScale = Interpolation.fade.apply(startScale, MIN_SCALE, t);
        card.drawScale = Math.max(newScale, MIN_SCALE);

        // fade out
        card.transparency = Interpolation.fade.apply(1f, 0f, t);

        // spin
        card.angle = startAngle + 180f * t;

        if (duration <= 0f) {
            // stop rendering next frame
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (card.drawScale <= MIN_SCALE || card.transparency <= 0.001f) return;
        card.render(sb);
    }

    @Override
    public void dispose() {}
}
