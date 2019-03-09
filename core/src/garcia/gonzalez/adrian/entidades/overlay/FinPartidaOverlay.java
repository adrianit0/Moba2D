package garcia.gonzalez.adrian.entidades.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.screens.MobaGame;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Habilidad;
import garcia.gonzalez.adrian.utiles.Utils;

public class FinPartidaOverlay extends Overlay {

    private Enums.EstadoPartida estado;

    private long timeSinceGameFinished;
    private MobaGame game;

    public FinPartidaOverlay(Level level, float viewportSize, MobaGame game) {
        super(level, viewportSize);

        this.game = game;
    }

    public void setEstado (Enums.EstadoPartida estado) {
        this.estado = estado;

        this.timeSinceGameFinished = TimeUtils.nanoTime();
    }

    @Override
    public void onRender(SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer) {
        Texture fondo = Assets.instance.overlayAssets.fondoOscuro;

        batch.setColor(new Color(0,0,0,0.5f));
        batch.draw( fondo, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.setColor(Color.WHITE);

        Texture estadoPartida = estado== Enums.EstadoPartida.VICTORIA ? Assets.instance.overlayAssets.victoria : Assets.instance.overlayAssets.derrota;
        batch.draw( estadoPartida,
                viewport.getWorldWidth()/2-estadoPartida.getWidth()/2,
                viewport.getWorldHeight()/2-estadoPartida.getHeight()/2+50,
                estadoPartida.getWidth(),
                estadoPartida.getHeight());


        // Tras pasar un tiempo vuelve al men principal
        if (Utils.secondsSince(timeSinceGameFinished)>5) {
            game.irMenu();
        }
    }
}