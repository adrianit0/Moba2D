package garcia.gonzalez.adrian.entidades.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.Personaje;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Utils;

public class MainOverlay extends Overlay {

    public MainOverlay(Level level, float viewportSize) {
        super(level, viewportSize);
    }

    @Override
    public void onRender(SpriteBatch batch, BitmapFont font) {
        // TODO: Seguir haciendolo
        Texture t = Assets.instance.overlayAssets.mainHud;

        // TODO: Borrar
        //font.draw(batch, "HOLA QUE TALL!!???", 20, 50 );


        font.getData().setScale(0.5f,0.5f);
        font.draw(batch, "Personaje 1", Constants.HUD_MARGIN, viewport.getWorldHeight()-Constants.HUD_MARGIN );
        batch.draw(
                t,
                Constants.HUD_MARGIN,
                viewport.getWorldHeight()-t.getHeight()-Constants.HUD_MARGIN*2,
                0,
                0,
                t.getWidth(),
                t.getHeight(),
                1,
                1,
                0,
                0,
                0,
                t.getWidth(),
                t.getHeight(),
                false,
                false);

        //TODO: Seguir y borrar esto
        Personaje personaje = getLevel().getPersonaje();
        float actual = personaje.getAtributos().getSaludActual();
        float total = personaje.getAtributos().getMaxAttr(Enums.AtribEnum.SALUD);

        float porc = actual/total;

        Texture vida = Assets.instance.overlayAssets.vida;

        batch.draw(
                vida,
                Constants.HUD_MARGIN,
                viewport.getWorldHeight()-vida.getHeight()-Constants.HUD_MARGIN*2,
                0,
                0,
                vida.getWidth(),
                vida.getHeight(),
                1,
                1,
                0,
                0,
                0,
                vida.getWidth(),
                vida.getHeight(),
                false,
                false);
    }
}
