package garcia.gonzalez.adrian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import garcia.gonzalez.adrian.entidades.Esbirro;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.ChaseCam;
import garcia.gonzalez.adrian.utiles.Constants;

public class GameplayScreen extends ScreenAdapter {

    public static final String TAG = GameplayScreen.class.getName();

    // Nivel
    private Level level;
    // Para los Sprites
    private SpriteBatch batch;
    // El ViewPort
    private ExtendViewport viewport;

    // TODO: Hacer el chaseCam
    // Para seguir la cámara
    private ChaseCam chaseCam;

    // Tiempo de partida
    private long timeSinceGameStarted;

    //TODO: Hacer el HUD Manager.
    // HUD
    //private HudManager hud;

    @Override
    public void show() {
        // Inicializamos el singleton de los assets
        Assets.instance.init();

        // Creamos el ViewPort
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);

        // Creamos el nivel a partir del archivo JSON
        level = new Level(viewport);//LevelLoader.load("level1", viewport);
        batch = new SpriteBatch();

        //hud = new GigaGalHud();

        //TODO: Programar la chase cam inteligente
        // Inicializamos la cámara que sigue al jugador
        chaseCam = new ChaseCam(viewport.getCamera(), new Esbirro(Enums.Bando.ALIADO,0,0));
    }

    @Override
    public void resize(int width, int height) {
        // TODO: comparar RESIZE con gigaGAL
        // Actualizamos el ViewPort cuando hagamos un resize
        viewport.update(width, height, true);
        // TODO: Programar la HUD
        //hud.viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        // Al cerrar el juego, cerramos tambien los assets y el SpriteBatch
        Assets.instance.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        // TODO: Comparar render con gigagal
        level.update(delta);

        //TODO: Camara del personaje
        // Actualiza la cámara que sigue al personaje
        chaseCam.update(delta);

        viewport.apply();
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        level.render(batch);

        // TODO: Ajustar hud render
        //hud.render(batch, level.getGigaGal().getLives(), level.getGigaGal().getAmmo(), level.score);
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

            // Renderizamos el HUD de victorua
            victoryOverlay.render(batch);

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