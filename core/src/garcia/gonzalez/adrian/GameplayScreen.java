package garcia.gonzalez.adrian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

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

    // TODO: Hacer el chaseCam
    // Para seguir la cámara
    private ChaseCam chaseCam;
    private GrayScaleView grayScaleView;

    // Tiempo de partida
    private long timeSinceGameStarted;

    //TODO: Hacer el HUD Manager.
    // HUD
    private Overlay hud;

    @Override
    public void show() {
        // Inicializamos el singleton de los assets
        Assets.instance.init();

        // Creamos el ViewPort
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);

        level = new Level(viewport, this);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // TODO: Poner que si está en Android utilice el AndroidOverlay, si no el DesktopOverlay
        hud = isPhoneDevice() ? new AndroidOverlay(level, Constants.HUD_VIEWPORT_SIZE) : new DesktopOverlay(level, Constants.HUD_VIEWPORT_SIZE);
        chaseCam = new ChaseCam(viewport.getCamera(), level.getPersonaje());
        grayScaleView = new GrayScaleView(batch);

        level.getInput().setViewport(hud.getViewport());
    }

    public boolean isPhoneDevice () {
        // TODO: Cambiar a según el servicio
        return true;
    }

    public GrayScaleView getGrayScaleView() {
        return grayScaleView;
    }

    @Override
    public void resize(int width, int height) {
        // TODO: comparar RESIZE con gigaGAL
        // Actualizamos el ViewPort cuando hagamos un resize
        //TODO: Mirar si está correcto
        chaseCam.onResize(viewport, width, height);
        // TODO: Programar la HUD
        hud.getViewport().update(width, height, true);
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
        grayScaleView.update(delta);

        // TODO: Comparar onRender con gigagal
        level.update(delta);

        //TODO: Camara del personaje
        // Actualiza la cámara que sigue al personaje
        chaseCam.update(delta);

        viewport.apply();
        Gdx.gl.glClearColor( // TODO: Cambiar el color a gris en caso de morir
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

        // TODO: Ajustar hud onRender
        hud.render(batch, shapeRenderer);
    }

    // TODO: Programar pantalla de fin de nivel
    /*
    private void renderLevelEndOverlays(SpriteBatch batch) {
        if (level.victory) {
            if (levelEndOverlayStartTime == 0) {
                // Ponemos el tiempo de inicio
                levelEndOverlayStartTime = TimeUtils.nanoTime();

                // Inicializamos el overlay de victoria
                victoryOverlay.init();
            }

            // Renderizamos el HUD de victoria
            victoryOverlay.onRender(batch);

            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {

                // Reiniciamos el tiempo
                levelEndOverlayStartTime = 0;

                // Llamamos a levelComplete
                levelComplete();
            }
        }
    }*/

    private void startNewLevel() {

        // level = Level.debugLevel();

//        String levelName = Constants.LEVELS[MathUtils.random(Constants.LEVELS.length - 1)];
//        level = LevelLoader.load(levelName);

        /*chaseCam.camera = level.viewport.getCamera();
        chaseCam.target = level.getGigaGal();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());*/
    }

    public void levelComplete() {
        startNewLevel();
    }
}