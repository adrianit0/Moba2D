package garcia.gonzalez.adrian.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Utils;

public class MenuScreen extends InputAdapter implements Screen {

    private MobaGame game;

    private long timeStarted;

    private ShapeRenderer renderer;
    private SpriteBatch batch;
    private Viewport viewport;

    private BitmapFont font;

    public MenuScreen(MobaGame game) {
        this.game = game;
        this.timeStarted = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();

        viewport = new ExtendViewport(Constants.DIFFICULTY_WORLD_SIZE, Constants.DIFFICULTY_WORLD_SIZE);
        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        Gdx.gl.glClearColor(Constants.MENU_BACKGROUND_COLOR.r, Constants.MENU_BACKGROUND_COLOR.g, Constants.MENU_BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        Texture t = Assets.instance.menuAssets.fondo.getKeyFrame(Utils.secondsSince(timeStarted));
        batch.draw(t,viewport.getWorldWidth()/2-t.getWidth()/2, 0);

        batch.end();

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        Vector2 EASY_CENTER = new Vector2( viewport.getWorldWidth()/4, Constants.EASY_CENTER.y);
        Vector2 MEDIUM_CENTER = new Vector2( viewport.getWorldWidth()/2, Constants.MEDIUM_CENTER.y);
        Vector2 HARD_CENTER = new Vector2( viewport.getWorldWidth()*3/4, Constants.HARD_CENTER.y);

        renderer.setColor(Color.GREEN);
        renderer.circle(EASY_CENTER.x, EASY_CENTER.y, Constants.DIFFICULTY_BUBBLE_RADIUS);

        renderer.setColor(Color.GREEN);
        renderer.circle(MEDIUM_CENTER.x, MEDIUM_CENTER.y, Constants.DIFFICULTY_BUBBLE_RADIUS);

        renderer.setColor(Color.GREEN);
        renderer.circle(HARD_CENTER.x, HARD_CENTER.y, Constants.DIFFICULTY_BUBBLE_RADIUS);

        renderer.end();

        batch.begin();
        final String EASY_LABEL = "Fácil";
        final String MEDIUM_LABEL = "Medio";
        final String HARD_LABEL = "Dificil";

        // Usamos los GlyphLayout para saber la posición final que tendrá el texto
        // y así poder situarlo en la pantalla en el centro
        final GlyphLayout easyLayout = new GlyphLayout(font, EASY_LABEL);
        font.draw(batch, EASY_LABEL, EASY_CENTER.x, EASY_CENTER.y + easyLayout.height / 2, 0, Align.center, false);

        final GlyphLayout mediumLayout = new GlyphLayout(font, MEDIUM_LABEL);
        font.draw(batch, MEDIUM_LABEL, MEDIUM_CENTER.x, MEDIUM_CENTER.y + mediumLayout.height / 2, 0, Align.center, false);

        final GlyphLayout hardLayout = new GlyphLayout(font, HARD_LABEL);
        font.draw(batch, HARD_LABEL, HARD_CENTER.x, HARD_CENTER.y + hardLayout.height / 2, 0, Align.center, false);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() { }
    @Override
    public void resume() { }

    @Override
    public void hide() {
        batch.dispose();
        font.dispose();
        renderer.dispose();
    }

    @Override
    public void dispose() {}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldTouch = viewport.unproject(new Vector2(screenX, screenY));

        if (worldTouch.dst(Constants.EASY_CENTER) < Constants.DIFFICULTY_BUBBLE_RADIUS){
            game.empezarPartida(Enums.Dificultad.JUSTO);
        }

        if (worldTouch.dst(Constants.MEDIUM_CENTER) < Constants.DIFFICULTY_BUBBLE_RADIUS){
            game.empezarPartida(Enums.Dificultad.JUSTO);
        }

        if (worldTouch.dst(Constants.HARD_CENTER) < Constants.DIFFICULTY_BUBBLE_RADIUS){
            game.empezarPartida(Enums.Dificultad.JUSTO);
        }

        return true;
    }
}
