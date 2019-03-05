package garcia.gonzalez.adrian.entidades.particulas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Utils;

public class Particula {
    private final Vector2 position;
    private final long startTime;
    private Vector2 offset;
    private float scale;
    private float alpha;

    private Animation animacion;
    //TODO: Hacer solo abstracto si algún efecto necesita efectos adicionales.

    public Particula(Vector2 position, Animation animacion, Vector2 offset) {
        this.position = position;
        this.animacion = animacion;
        startTime = TimeUtils.nanoTime();
        this.offset = offset;
        this.scale=1;
    }

    public Particula(Vector2 position, Animation animacion, Vector2 offset, float scale) {
        this.position = position;
        this.animacion = animacion;
        startTime = TimeUtils.nanoTime();
        this.offset = offset;
        this.scale = scale;
    }

    public Particula(Vector2 position, Animation animacion, Vector2 offset, float scale, float alpha) {
        this.position = position;
        this.animacion = animacion;
        startTime = TimeUtils.nanoTime();
        this.offset = offset;
        this.scale = scale;
        this.alpha = alpha;
    }

    public void render(SpriteBatch batch) {
        // Remember to use Utils.drawTextureRegion() and Utils.secondsSince()
        if (alpha<1)  {
            Color c = batch.getColor();
            batch.setColor(c.r, c.g, c.b, alpha);
        }

        Utils.drawTextureRegion(
                batch,
                (TextureRegion) animacion.getKeyFrame(Utils.secondsSince(startTime)),
                position,
                offset,
                scale
        );

        if (alpha<1) {
            batch.setColor(Color.WHITE);
        }
    }

    public boolean isFinished() {
        return animacion.isAnimationFinished(Utils.secondsSince(startTime));
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }
}
