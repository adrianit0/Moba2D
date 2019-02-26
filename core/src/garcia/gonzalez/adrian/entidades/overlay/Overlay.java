package garcia.gonzalez.adrian.entidades.overlay;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import garcia.gonzalez.adrian.Level;

public abstract class Overlay {
    protected final Viewport viewport;
    private final BitmapFont font;

    private Level level;

    public Overlay (Level level, float viewportSize) {
        this.viewport = new ExtendViewport(viewportSize, viewportSize);
        font = new BitmapFont();
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public abstract void onRender (SpriteBatch batch, BitmapFont font);

    public void render( SpriteBatch batch) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        onRender(batch, font);

        batch.end();
    }

    public Viewport getViewport() {
        return viewport;
    }
}
