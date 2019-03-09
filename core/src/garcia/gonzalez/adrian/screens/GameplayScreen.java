package garcia.gonzalez.adrian.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.controladorPersonaje.ControladorJugador1;
import garcia.gonzalez.adrian.controladorPersonaje.ControladorJugador2;
import garcia.gonzalez.adrian.entidades.overlay.AndroidOverlay;
import garcia.gonzalez.adrian.entidades.overlay.Desktop2PlayerOverlay;
import garcia.gonzalez.adrian.entidades.overlay.DesktopOverlay;
import garcia.gonzalez.adrian.entidades.overlay.FinPartidaOverlay;
import garcia.gonzalez.adrian.entidades.overlay.Overlay;
import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.entidades.personajes.Personaje1;
import garcia.gonzalez.adrian.entidades.personajes.Personaje2;
import garcia.gonzalez.adrian.enums.Enums;
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

    private ChaseCam chaseCam2;
    private ExtendViewport viewport2;

    private int personajeID;

    private Personaje p1;   // JUGADOR 1
    private Personaje p2;   // JUGADOR 2

    // Tiempo de partida
    private long timeSinceGameStarted;
    private Enums.Dificultad dificultad;

    private Overlay hud;
    private FinPartidaOverlay finPartidaOverlay;

    private MobaGame game;

    public GameplayScreen (MobaGame game, Enums.Dificultad dificultad, int personajeID) {
        this.dificultad = dificultad;
        this.personajeID = personajeID;

        this.game = game;
    }

    @Override
    public void show() {
        timeSinceGameStarted=0;

        // Creamos el ViewPort
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);

        level = new Level( this);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // PERSONAJE 1
        p1 = getPersonaje(personajeID, new ControladorJugador1(), Enums.Bando.ALIADO, level);
        level.addCharacter(p1);    // Metemos al personaje principal

        // PERSONAJE 2. Su personaje será quien no elija el jugador 1
        // TODO: añadir controlador a la IA
        p2 = getPersonaje(personajeID==0?1:0, dificultad== Enums.Dificultad.TWO_PLAYER ? new ControladorJugador2() : null, Enums.Bando.ENEMIGO, level);
        level.addCharacter(p2);

        // SELECCIONA EL HUD SEGÚN LAS NECESIDADES QUE SE TENGA EN CADA PARTIDA.
        // Diferencia entre si es overlay ANDROID o de ESCRITORIO, y si es de escritorio si es 1 ó 2 JUGADORES.
        hud = isPhoneDevice() ? new AndroidOverlay(level, Constants.HUD_VIEWPORT_SIZE, p1) :
                dificultad == Enums.Dificultad.TWO_PLAYER ? new Desktop2PlayerOverlay(level, Constants.HUD_VIEWPORT_SIZE, p1, p2) :
                        new DesktopOverlay(level, Constants.HUD_VIEWPORT_SIZE, p1);

        finPartidaOverlay = new FinPartidaOverlay(level, Constants.HUD_VIEWPORT_SIZE, game);

        // Configuramos los viewports si elegimos jugar 2 personajes
        if (dificultad== Enums.Dificultad.TWO_PLAYER) {
            viewport2 = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
            chaseCam = new ChaseCam(viewport.getCamera(), p1, true);
            chaseCam2 = new ChaseCam(viewport2.getCamera(), p2,true);
        } else {
            chaseCam = new ChaseCam(viewport.getCamera(), p1, false);
        }
        grayScaleView = new GrayScaleView(batch);

        level.getInput().setViewport(hud.getViewport());
    }

    public Personaje getPersonaje (int id, Controlador controlador, Enums.Bando bando, Level level) {
        switch (id) {
            case 0:
                return new Personaje1(controlador, bando, 0, 0, level);
            case 1:
                return new Personaje2(controlador, bando, 0, 0, level);
        }

        return new Personaje1(controlador, bando, 0, 0, level);
    }

    public FinPartidaOverlay getFinPartidaOverlay() {
        return finPartidaOverlay;
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
        // Dependerá si está o no jugando 2 jugadores

        if (chaseCam2!=null) {
            chaseCam.onResize(viewport, width/2, height);
            chaseCam2.onResize(viewport2,width/2, height);
            viewport2.setScreenX(width/2);
        } else {
            chaseCam.onResize(viewport, width, height);
        }

        hud.getViewport().update(width, height, true);
        finPartidaOverlay.getViewport().update(width, height, true);
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

        //grayScaleView.update(delta);

        level.update(delta);

        viewport.apply();
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        grayScaleView.setGrayscale(!p1.estaVivo());

        if (dificultad== Enums.Dificultad.TWO_PLAYER)
            Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() );

        // Actualiza la cámara que sigue al personaje
        chaseCam.update(delta);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        level.render(batch, shapeRenderer, p1, viewport);

        if (dificultad== Enums.Dificultad.TWO_PLAYER) {
            viewport2.apply();

            grayScaleView.setGrayscale(!p2.estaVivo());
            Gdx.gl.glViewport( Gdx.graphics.getWidth()/2,0,Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
            chaseCam2.update(delta);

            batch.setProjectionMatrix(viewport2.getCamera().combined);
            level.render(batch, shapeRenderer, p2, viewport2);
        }

        grayScaleView.setGrayscale(false);

        hud.render(batch, shapeRenderer);

        // Si la partida ha finalizado mostramos la pantalla de fin de juego
        if (level.partidaTerminada()) {
            finPartidaOverlay.render(batch, shapeRenderer);
        }
    }
}