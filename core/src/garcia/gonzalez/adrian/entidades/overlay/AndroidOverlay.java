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

public class AndroidOverlay extends Overlay {

    private Personaje personaje;

    public AndroidOverlay(Level level, float viewportSize, Personaje personaje) {
        super(level, viewportSize);

        this.personaje = personaje;
    }

    @Override
    public void onRender(SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer) {
        Texture t = Assets.instance.overlayAssets.mainHud;

        font.setColor(Color.WHITE);
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


        final Vector2 tamHab = Constants.TAM_BOTONES;
        Texture derecha =  Assets.instance.overlayAssets.derecha;
        Texture izquierda =  Assets.instance.overlayAssets.izquierda;
        Texture salto =  Assets.instance.overlayAssets.salto;

        batch.draw(
                izquierda,
                Constants.HUD_MARGIN*2,
                Constants.HUD_MARGIN*2,
                0,
                0,
                tamHab.x,
                tamHab.y,
                1,
                1,
                0,
                0,
                0,
                izquierda.getWidth(),
                izquierda.getHeight(),
                false,
                false);

        batch.draw(
                salto,
                Constants.HUD_MARGIN*2+tamHab.x*1+5,
                Constants.HUD_MARGIN*5,
                0,
                0,
                tamHab.x,
                tamHab.y,
                1,
                1,
                0,
                0,
                0,
                salto.getWidth(),
                salto.getHeight(),
                false,
                false);

        batch.draw(
                derecha,
                Constants.HUD_MARGIN*2+tamHab.x*2+10,
                Constants.HUD_MARGIN*2,
                0,
                0,
                tamHab.x,
                tamHab.y,
                1,
                1,
                0,
                0,
                0,
                derecha.getWidth(),
                derecha.getHeight(),
                false,
                false);

        Habilidad[] habilidades = personaje.getHabilidades();
        final int mana = personaje.getAtributos().getManaActual();

        for (int i = 0; i < habilidades.length; i++) {
            Habilidad h = habilidades[i];
            TextureRegion textura = h.getTextureRegion();
            // De no existir la textura le ponemos una cualquiera
            if (textura==null) {
                textura = Assets.instance.overlayAssets.character01_hab01;
            }

            int inverso = habilidades.length-1-i;

            float x = viewport.getWorldWidth()-Constants.HUD_MARGIN*2-tamHab.x-tamHab.x*inverso-inverso*5;
            float y = Constants.HUD_MARGIN*2 + i*10;

            batch.setColor(mana>=h.getCoste() ? Color.WHITE : Color.GRAY);

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

            batch.setColor(Color.WHITE);

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
            font.draw(batch, h.getCoste()+"", x-4, y+textura.getRegionHeight()/2+4 );
            font.setColor(Color.WHITE);
        }

    }
}
