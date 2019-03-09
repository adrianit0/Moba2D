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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Utils;

public class ChooseCharacterScreen extends InputAdapter implements Screen {

    private MobaGame game;

    private long timeStarted;
    private Enums.Dificultad dificultad;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Viewport viewport;

    private BitmapFont font;

    private HashMap<Rectangle, Integer> botonesPersonajes;

    public ChooseCharacterScreen(MobaGame game, Enums.Dificultad dificultadSeleccionada) {
        this.game = game;
        this.timeStarted = TimeUtils.nanoTime();

        botonesPersonajes = new HashMap<Rectangle, Integer>();

        this.dificultad = dificultadSeleccionada;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        viewport = new ExtendViewport(Constants.DIFFICULTY_WORLD_SIZE, Constants.DIFFICULTY_WORLD_SIZE);
        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void crearBotones () {
        Vector2 CHARACTER_1 = new Vector2( viewport.getWorldWidth()/4, Constants.MENU_BUTTON_Y);
        Vector2 CHARACTER_2 = new Vector2( viewport.getWorldWidth()/2, Constants.MENU_BUTTON_Y);
        Vector2 CHARACTER_3 = new Vector2( viewport.getWorldWidth()*3/4, Constants.MENU_BUTTON_Y);

        Vector2 tam = Constants.MENU_CHARACTER_SIZE;

        botonesPersonajes.put(new Rectangle(CHARACTER_1.x-tam.x/2, CHARACTER_1.y, tam.x, tam.y), 0);
        botonesPersonajes.put(new Rectangle(CHARACTER_2.x-tam.x/2, CHARACTER_2.y, tam.x, tam.y), 1);
    }

    @Override
    public void render(float delta) {
        if (botonesPersonajes.size()==0 && viewport.getWorldWidth()!=0)
            crearBotones();

        viewport.apply();
        Gdx.gl.glClearColor(Constants.MENU_BACKGROUND_COLOR.r, Constants.MENU_BACKGROUND_COLOR.g, Constants.MENU_BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        Texture t = Assets.instance.menuAssets.fondoSeleccionPersonaje.getKeyFrame(Utils.secondsSince(timeStarted));
        float width = t.getWidth()*viewport.getWorldHeight()/t.getHeight();    // Una simple regla de 3 para que se vea toda la imagen de alto.
        batch.draw(t,viewport.getWorldWidth()/2-width/2, 0, width, viewport.getWorldHeight());



        Vector2 CHARACTER_1 = new Vector2( viewport.getWorldWidth()/4, Constants.MENU_BUTTON_Y);
        Vector2 CHARACTER_2 = new Vector2( viewport.getWorldWidth()/2, Constants.MENU_BUTTON_Y);
        Vector2 CHARACTER_3 = new Vector2( viewport.getWorldWidth()*3/4, Constants.MENU_BUTTON_Y);

        Vector2 tam = Constants.MENU_CHARACTER_SIZE;

        Assets.instance.menuAssets.fondoPersonaje.draw(batch, CHARACTER_1.x-tam.x/2, CHARACTER_1.y, tam.x, tam.y);
        Assets.instance.menuAssets.fondoPersonaje.draw(batch, CHARACTER_2.x-tam.x/2, CHARACTER_2.y, tam.x, tam.y);
        Assets.instance.menuAssets.fondoPersonaje.draw(batch, CHARACTER_3.x-tam.x/2, CHARACTER_3.y, tam.x, tam.y);

        Texture lazo = Assets.instance.menuAssets.lazoTitulo;

        batch.draw(lazo, CHARACTER_1.x-lazo.getWidth()/6, CHARACTER_1.y - lazo.getHeight()/3, lazo.getWidth()/3, lazo.getHeight()/2);
        batch.draw(lazo, CHARACTER_2.x-lazo.getWidth()/6, CHARACTER_2.y - lazo.getHeight()/3, lazo.getWidth()/3, lazo.getHeight()/2);
        batch.draw(lazo, CHARACTER_3.x-lazo.getWidth()/6, CHARACTER_3.y - lazo.getHeight()/3, lazo.getWidth()/3, lazo.getHeight()/2);

        TextureRegion p1 = Assets.instance.personaje1.idle.getKeyFrame(Utils.secondsSince(timeStarted));
        TextureRegion p2 = Assets.instance.personaje2.idle.getKeyFrame(Utils.secondsSince(timeStarted));

        batch.draw(p1, CHARACTER_1.x-p1.getRegionWidth()*2f+10, CHARACTER_1.y+25, p1.getRegionWidth()*4, p1.getRegionHeight()*4);
        batch.draw(p2, CHARACTER_2.x-p2.getRegionWidth()*1.5f+10, CHARACTER_2.y+25, p2.getRegionWidth()*3, p2.getRegionHeight()*3);

        final String CHOOSE_DIFFICULT = "Selecciona un personaje";
        final String NOMBRE_1 = Constants.CHARACTER_01_NAME;
        final String NOMBRE_2 = Constants.CHARACTER_02_NAME;
        final String NOMBRE_3 = "???";

        // Usamos los GlyphLayout para saber la posición final que tendrá el texto
        // y así poder situarlo en la pantalla en el centro
        final GlyphLayout chooseDifficult = new GlyphLayout(font, CHOOSE_DIFFICULT);
        // Para el outline de la frase
        font.getData().setScale(3f);
        font.setColor(Color.BLACK);
        font.draw(batch, CHOOSE_DIFFICULT, CHARACTER_2.x+2, CHARACTER_2.y + 302 + chooseDifficult.height / 2, 0, Align.center, false);
        font.draw(batch, CHOOSE_DIFFICULT, CHARACTER_2.x+2, CHARACTER_2.y + 298 + chooseDifficult.height / 2, 0, Align.center, false);
        font.draw(batch, CHOOSE_DIFFICULT, CHARACTER_2.x-2, CHARACTER_2.y + 302 + chooseDifficult.height / 2, 0, Align.center, false);
        font.draw(batch, CHOOSE_DIFFICULT, CHARACTER_2.x-2, CHARACTER_2.y + 298 + chooseDifficult.height / 2, 0, Align.center, false);

        font.setColor(Color.WHITE);
        font.draw(batch, CHOOSE_DIFFICULT, CHARACTER_2.x, CHARACTER_2.y + 300 + chooseDifficult.height / 2, 0, Align.center, false);

        font.getData().setScale(1.5f);
        font.setColor(Color.BLACK);
        final GlyphLayout easyLayout = new GlyphLayout(font, NOMBRE_1);
        font.draw(batch, NOMBRE_1, CHARACTER_1.x, CHARACTER_1.y + easyLayout.height / 2, 0, Align.center, false);

        final GlyphLayout mediumLayout = new GlyphLayout(font, NOMBRE_2);
        font.draw(batch, NOMBRE_2, CHARACTER_2.x, CHARACTER_2.y + mediumLayout.height / 2, 0, Align.center, false);

        final GlyphLayout hardLayout = new GlyphLayout(font, NOMBRE_3);
        font.draw(batch, NOMBRE_3, CHARACTER_3.x, CHARACTER_3.y + hardLayout.height / 2, 0, Align.center, false);

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
        shapeRenderer.dispose();
    }

    @Override
    public void dispose() {}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldTouch = viewport.unproject(new Vector2(screenX, screenY));

        for (Map.Entry<Rectangle, Integer> entry : botonesPersonajes.entrySet()) {
            if (entry.getKey().contains(worldTouch)) {
                game.empezarPartida(dificultad, entry.getValue());
            }
        }

        return true;
    }
}