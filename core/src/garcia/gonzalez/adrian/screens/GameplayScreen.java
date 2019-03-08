package garcia.gonzalez.adrian.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.overlay.AndroidOverlay;
import garcia.gonzalez.adrian.entidades.overlay.DesktopOverlay;
import garcia.gonzalez.adrian.entidades.overlay.Overlay;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.ChaseCam;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.GrayScaleView;

public class GameplayScreen extends ScreenAdapter {

    public static final String TAG = GameplayScreen.class.getName();

    // Nivel
    private Level level;
    // Para los Sprites
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    // El ViewPort
    private ExtendViewport viewport;

    // Para seguir la cámara
    private ChaseCam chaseCam;
    private GrayScaleView grayScaleView;

    // Tiempo de partida
    private long timeSinceGameStarted;

    private Overlay hud;

    @Override
    public void show() {
        timeSinceGameStarted=0;

        // Creamos el ViewPort
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);

        level = new Level(viewport, this);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        hud = isPhoneDevice() ? new AndroidOverlay(level, Constants.HUD_VIEWPORT_SIZE) : new DesktopOverlay(level, Constants.HUD_VIEWPORT_SIZE);
        chaseCam = new ChaseCam(viewport.getCamera(), level.getPersonaje());
        grayScaleView = new GrayScaleView(batch);

        level.getInput().setViewport(hud.getViewport());
    }

    public long getTimeSinceGameStarted () {
        return timeSinceGameStarted;
    }

    public boolean isPhoneDevice () {
        return Gdx.app.getType() == Application.ApplicationType.Android ||
                Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    public GrayScaleView getGrayScaleView() {
        return grayScaleView;
    }

    @Override
    public void resize(int width, int height) {
        // Actualizamos el ViewPort cuando hagamos un resize
        chaseCam.onResize(viewport, width, height);
        hud.getViewport().update(width, height, true);
        level.getInput().resize();
    }

    @Override
    public void dispose() {
        // Al cerrar el juego, cerramos tambien los assets y el SpriteBatch
        Assets.instance.dispose();
        batch.dispose();
        grayScaleView.dispose();
    }

    @Override
    public void render(float delta) {
        timeSinceGameStarted+=delta;

        grayScaleView.update(delta);

        level.update(delta);

        // Actualiza la cámara que sigue al personaje
        chaseCam.update(delta);

        viewport.apply();
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // TODO: CAMBIAR DE MODO 1 JUGADOR A 2 JUGADORES
        //Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );

        batch.setProjectionMatrix(viewport.getCamera().combined);
        level.render(batch, shapeRenderer);

        // TODO: CONFIGURAR MODO 2 JUGADORES PANTALLA DIVIDIDAD
        //Gdx.gl.glViewport( Gdx.graphics.getWidth()/2,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );
        //level.render(batch);
        grayScaleView.setGrayscale(false);

        hud.render(batch, shapeRenderer);
    }
}