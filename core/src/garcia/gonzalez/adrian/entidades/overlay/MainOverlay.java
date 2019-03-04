package garcia.gonzalez.adrian.entidades.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Habilidad;

public class MainOverlay extends Overlay {

    public MainOverlay(Level level, float viewportSize) {
        super(level, viewportSize);
    }

    @Override
    public void onRender(SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer) {
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
                viewport.getWorldHeight()-t.getHeight()-Constants.HUD_MARGIN*2,
                0,
                0,
                vida.getWidth()*porc,
                vida.getHeight(),
                1,
                1,
                0,
                0,
                0,
                Math.round(vida.getWidth()*porc),
                vida.getHeight(),
                false,
                false);


        Habilidad[] habilidades = personaje.getHabilidades();
        final Vector2 tamHab = new Vector2(32,32);      //TODO: Convertir en constante
        final String[] botones = { "J", "K", "L" };         // TODO: Convertir en constante
        final int mana = personaje.getAtributos().getManaActual();

        for (int i = 0; i < habilidades.length; i++) {
            Habilidad h = habilidades[i];
            TextureRegion textura = h.getTextureRegion();
            // De no existir la textura le ponemos una cualquiera
            if (textura==null) {
                textura = Assets.instance.overlayAssets.hab1;
            }

            int inverso = habilidades.length-1-i;

            float x = viewport.getWorldWidth()-Constants.HUD_MARGIN-tamHab.x-tamHab.x*inverso-inverso*10;
            float y = viewport.getWorldHeight()-t.getHeight()-Constants.HUD_MARGIN*2;

            // Dibujamos la habilidad
            batch.draw(
                    textura.getTexture(),
                    x,
                    y,
                    0,
                    0,
                    tamHab.x,
                    tamHab.y,
                    1,
                    1,
                    0,
                    textura.getRegionX(),
                    textura.getRegionY(),
                    textura.getRegionWidth(),
                    textura.getRegionHeight(),
                    false,
                    false);

            float cd = h.getCooldown();
            if (cd<1) {
                batch.setColor(Color.DARK_GRAY);
                batch.draw(
                        textura.getTexture(),
                        x,
                        y,
                        0,
                        0,
                        tamHab.x,
                        tamHab.y,
                        1*(1-cd),
                        1,
                        0,
                        textura.getRegionX(),
                        textura.getRegionY(),
                        Math.round(textura.getRegionWidth()*(1-cd)),
                        textura.getRegionHeight(),
                        false,
                        false);
                batch.setColor(Color.WHITE);
            }



            // Dibujamos el coste de mana
            font.setColor(cd<1 ? Color.DARK_GRAY : mana>=h.getCoste() ? Color.WHITE : Color.RED);
            font.draw(batch, h.getCoste()+"", x-5, y+3 );

            font.setColor(Color.BLACK);
            font.draw(batch, botones[i], x+27, y+4 );
            font.draw(batch, botones[i], x+25, y+4 );
            font.draw(batch, botones[i], x+27, y+2 );
            font.draw(batch, botones[i], x+25, y+2 );
            font.setColor(cd<1 ? Color.DARK_GRAY : Color.WHITE);
            font.draw(batch, botones[i], x+26, y+3 );
        }

    }
}
