package garcia.gonzalez.adrian.entidades.overlay;

import com.badlogic.gdx.Gdx;
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

public class DesktopOverlay extends Overlay {

    public DesktopOverlay(Level level, float viewportSize) {
        super(level, viewportSize);
    }

    @Override
    public void onRender(SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer) {
        Texture t = Assets.instance.overlayAssets.mainHud;

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

        Personaje personaje = getLevel().getPersonaje();

        float porcVida = (float)  personaje.getAtributos().getSaludActual()/personaje.getAtributos().getMaxAttr(Enums.AtribEnum.SALUD);
        if (porcVida<0)
            porcVida=0;

        float porcMana = (float) personaje.getAtributos().getManaActual()/personaje.getAtributos().getMaxAttr(Enums.AtribEnum.MANA);
        if (porcMana<0)
            porcMana=0;

        if (!personaje.estaVivo()) {
            font.getData().setScale(1.5f,1.5f);
            font.setColor(Color.BLACK);
            String time = personaje.getTiempoMuerto()+"";

            font.draw(batch,
                    time.length()==1?"0"+time:time,
                    Constants.HUD_MARGIN*2,
                    viewport.getWorldHeight()-t.getHeight()/2-Constants.HUD_MARGIN/2);
        }
        font.setColor(Color.WHITE);
        font.getData().setScale(0.5f,0.5f);

        Texture vida = Assets.instance.overlayAssets.vida;
        Texture t_mana = Assets.instance.overlayAssets.mana;

        batch.draw(
                vida,
                Constants.HUD_MARGIN + 31,
                viewport.getWorldHeight()-vida.getHeight()-Constants.HUD_MARGIN*2-2,
                0,
                0,
                vida.getWidth()*porcVida,
                vida.getHeight(),
                1,
                1,
                0,
                0,
                0,
                Math.round(vida.getWidth()*porcVida),
                vida.getHeight(),
                false,
                false);

        batch.draw(
                t_mana,
                Constants.HUD_MARGIN + 31,
                viewport.getWorldHeight()-t_mana.getHeight()-vida.getHeight()-Constants.HUD_MARGIN*2-3,
                0,
                0,
                t_mana.getWidth()*porcMana,
                t_mana.getHeight(),
                1,
                1,
                0,
                0,
                0,
                Math.round(t_mana.getWidth()*porcMana),
                t_mana.getHeight(),
                false,
                false);


        Habilidad[] habilidades = personaje.getHabilidades();
        final Vector2 tamHab = new Vector2(32,32);
        final String[] botones = { "J", "K", "L" };
        final int mana = personaje.getAtributos().getManaActual();

        for (int i = 0; i < habilidades.length; i++) {
            Habilidad h = habilidades[i];
            TextureRegion textura = h.getTextureRegion();
            // De no existir la textura le ponemos una cualquiera
            if (textura==null) {
                textura = Assets.instance.overlayAssets.character01_hab01;
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
