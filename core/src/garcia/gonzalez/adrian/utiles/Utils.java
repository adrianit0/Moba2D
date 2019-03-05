package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Clase utilidad
 * */
public class Utils {

    public static void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y);
    }

    public static void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset, float scale) {
        batch.draw(
                region.getTexture(),
                position.x - offset.x,
                position.y - offset.y,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                scale,
                scale,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
    }

    public static void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y) {
        batch.draw(
                region.getTexture(),
                x,
                y,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                1,
                1,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
    }

    /**
     * Crea una animación a partir de un array de String para por ejemplo los personajes
     * */
    public static Animation<TextureRegion> createAnimation (TextureAtlas atlas, float duracion, String[] animaciones, Animation.PlayMode playmode) {
        Array<AtlasRegion> sprites = new Array<AtlasRegion>();
        for (String s : animaciones) {
            sprites.add(atlas.findRegion(s));
        }
        return new Animation(duracion, sprites, playmode);
    }

    /**
     * Crea una animación a partir de un array de String para por ejemplo los minions
     * */
    public static Animation<TextureRegion> createAnimationMinions (TextureAtlas atlas, float duracion, String[] animaciones, Animation.PlayMode playmode, String extraInfo) {
        Array<AtlasRegion> sprites = new Array<AtlasRegion>();
        for (String s : animaciones) {
            sprites.add(atlas.findRegion(String.format(s, extraInfo)));
        }
        return new Animation(duracion, sprites, playmode);
    }

    public static float secondsSince(long timeNanos) {
        return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos);
    }

    public static float secondsSince (float timeSeconds) {
        return MathUtils.nanoToSec * TimeUtils.nanoTime() - timeSeconds;
    }

    public static long secondsToNano (double seconds) {
        return Math.round(seconds/MathUtils.nanoToSec);
    }

    public static Vector3 moveTowards (Vector3 current, Vector3 target, float maxDistanceDelta) {
        Vector3 a = target.cpy().sub(current);
        float magnitude = a.len();
        if (magnitude <= maxDistanceDelta || magnitude == 0f)
            return target;

        return new Vector3(
                current.x + a.x / magnitude * maxDistanceDelta,
                current.y + a.y / magnitude * maxDistanceDelta,
                current.z);
    }

    public static Vector2 moveTowards (Vector2 current, Vector2 target, float maxDistanceDelta) {
        Vector2 a = target.cpy().sub(current);
        float magnitude = a.len();
        if (magnitude <= maxDistanceDelta || magnitude == 0f)
            return target;

        return new Vector2(
                current.x + a.x / magnitude * maxDistanceDelta,
                current.y + a.y / magnitude * maxDistanceDelta);
    }
}
