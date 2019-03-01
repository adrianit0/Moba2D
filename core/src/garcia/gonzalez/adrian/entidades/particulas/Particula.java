package garcia.gonzalez.adrian.entidades.particulas;

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

    private Animation animacion;
    //TODO: Hacer solo abstracto si algún efecto necesita efectos adicionales.

    public Particula(Vector2 position, Animation animacion, Vector2 offset) {
        this.position = position;
        this.animacion = animacion;
        startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        // Remember to use Utils.drawTextureRegion() and Utils.secondsSince()
        Utils.drawTextureRegion(
                batch,
                (TextureRegion) animacion.getKeyFrame(Utils.secondsSince(startTime)),
                position,
                offset
        );
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
