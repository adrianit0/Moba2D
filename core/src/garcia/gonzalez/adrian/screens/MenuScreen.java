package garcia.gonzalez.adrian.screens;

import com.badlogic.gdx.Application;
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

public class MenuScreen extends InputAdapter implements Screen {

    private MobaGame game;

    private long timeStarted;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Viewport viewport;

    private BitmapFont font;

    private HashMap<Rectangle, Enums.Dificultad> botonesDificultad;

    public MenuScreen(MobaGame game) {
        this.game = game;
        this.timeStarted = TimeUtils.nanoTime();

        botonesDificultad = new HashMap<Rectangle, Enums.Dificultad>();
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
        Vector2 tam = Constants.MENU_BUTTON_SIZE;
        Vector2 EASY_CENTER = new Vector2( viewport.getWorldWidth()/4, Constants.MENU_BUTTON_Y);
        Vector2 MEDIUM_CENTER = new Vector2( viewport.getWorldWidth()/2, Constants.MENU_BUTTON_Y);
        Vector2 HARD_CENTER = new Vector2( viewport.getWorldWidth()*3/4, Constants.MENU_BUTTON_Y);

        botonesDificultad.put(new Rectangle(EASY_CENTER.x-tam.x/2,  EASY_CENTER.y-tam.y/2, tam.x, tam.y), Enums.Dificultad.FACIL);
        botonesDificultad.put(new Rectangle(MEDIUM_CENTER.x-tam.x/2,  MEDIUM_CENTER.y-tam.y/2, tam.x, tam.y), Enums.Dificultad.NORMAL);
        // El modo 2 jugadores estará desactivado en ANDROID por obvias razones
        if (!isPhoneDevice())
            botonesDificultad.put(new Rectangle(HARD_CENTER.x-tam.x/2,  HARD_CENTER.y-tam.y/2, tam.x, tam.y), Enums.Dificultad.TWO_PLAYER);
    }

    @Override
    public void render(float delta) {
        if (botonesDificultad.size()==0 && viewport.getWorldWidth()!=0) {
            crearBotones();
        }
        viewport.apply();
        Gdx.gl.glClearColor(Constants.MENU_BACKGROUND_COLOR.r, Constants.MENU_BACKGROUND_COLOR.g, Constants.MENU_BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();


        Texture t = Assets.instance.menuAssets.fondoMenu.getKeyFrame(Utils.secondsSince(timeStarted));
        float width = t.getWidth()*viewport.getScreenHeight()/t.getHeight();    // Una simple regla de 3 para que se vea toda la imagen de alto.
        batch.draw(t,viewport.getWorldWidth()/2-width/2, 0, width, viewport.getScreenHeight());

        /* Para algunas pruebas
        batch.end();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.end();

        batch.begin();
        */



        Vector2 EASY_CENTER = new Vector2( viewport.getWorldWidth()/4, Constants.MENU_BUTTON_Y);
        Vector2 MEDIUM_CENTER = new Vector2( viewport.getWorldWidth()/2, Constants.MENU_BUTTON_Y);
        Vector2 HARD_CENTER = new Vector2( viewport.getWorldWidth()*3/4, Constants.MENU_BUTTON_Y);

        Vector2 tam = Constants.MENU_BUTTON_SIZE;

        Texture nombreJuego = Assets.instance.menuAssets.nombreJuego;

        batch.draw(Assets.instance.menuAssets.nombreJuego, MEDIUM_CENTER.x-nombreJuego.getWidth()/4, 300, nombreJuego.getWidth()/2, nombreJuego.getHeight()/2);

        batch.draw(Assets.instance.menuAssets.botonFacil, EASY_CENTER.x-tam.x/2,  EASY_CENTER.y-tam.y/2, tam.x, tam.y);
        batch.draw(Assets.instance.menuAssets.botonNormal, MEDIUM_CENTER.x-tam.x/2,  MEDIUM_CENTER.y-tam.y/2, tam.x, tam.y);
        batch.draw(Assets.instance.menuAssets.botonDificil, HARD_CENTER.x-tam.x/2,  HARD_CENTER.y-tam.y/2, tam.x, tam.y);

        final String CHOOSE_DIFFICULT = "Por favor, seleccione una dificultad";
        final String EASY_LABEL = "Normal";
        final String MEDIUM_LABEL = "Dificil";
        final String HARD_LABEL = "2 Jugadores";

        // Usamos los GlyphLayout para saber la posición final que tendrá el texto
        // y así poder situarlo en la pantalla en el centro
        final GlyphLayout chooseDifficult = new GlyphLayout(font, CHOOSE_DIFFICULT);
        // Para el outline de la frase
        font.getData().setScale(1.5f);
        font.setColor(Color.BLACK);
        font.draw(batch, CHOOSE_DIFFICULT, MEDIUM_CENTER.x+2, MEDIUM_CENTER.y + 52 + chooseDifficult.height / 2, 0, Align.center, false);
        font.draw(batch, CHOOSE_DIFFICULT, MEDIUM_CENTER.x+2, MEDIUM_CENTER.y + 48 + chooseDifficult.height / 2, 0, Align.center, false);
        font.draw(batch, CHOOSE_DIFFICULT, MEDIUM_CENTER.x-2, MEDIUM_CENTER.y + 52 + chooseDifficult.height / 2, 0, Align.center, false);
        font.draw(batch, CHOOSE_DIFFICULT, MEDIUM_CENTER.x-2, MEDIUM_CENTER.y + 48 + chooseDifficult.height / 2, 0, Align.center, false);

        font.setColor(Color.WHITE);
        font.draw(batch, CHOOSE_DIFFICULT, MEDIUM_CENTER.x, MEDIUM_CENTER.y + 50 + chooseDifficult.height / 2, 0, Align.center, false);

        final GlyphLayout easyLayout = new GlyphLayout(font, EASY_LABEL);
        font.draw(batch, EASY_LABEL, EASY_CENTER.x, EASY_CENTER.y + easyLayout.height / 2, 0, Align.center, false);

        final GlyphLayout mediumLayout = new GlyphLayout(font, MEDIUM_LABEL);
        font.draw(batch, MEDIUM_LABEL, MEDIUM_CENTER.x, MEDIUM_CENTER.y + mediumLayout.height / 2, 0, Align.center, false);

        final GlyphLayout hardLayout = new GlyphLayout(font, HARD_LABEL);
        font.getData().setScale(1f);
        font.setColor(isPhoneDevice() ? Color.RED : Color.WHITE);
        font.draw(batch, HARD_LABEL, HARD_CENTER.x, HARD_CENTER.y + hardLayout.height / 2, 0, Align.center, false);

        batch.end();
    }

    public boolean isPhoneDevice () {
        return Gdx.app.getType() == Application.ApplicationType.Android ||
                Gdx.app.getType() == Application.ApplicationType.iOS;
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

        for (Map.Entry<Rectangle, Enums.Dificultad> entry : botonesDificultad.entrySet()) {
            if (entry.getKey().contains(worldTouch)) {
                game.seleccionarPersonaje(entry.getValue());
            }
        }

        return true;
    }
}
