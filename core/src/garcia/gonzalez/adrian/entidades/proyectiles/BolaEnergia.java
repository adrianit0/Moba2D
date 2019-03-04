package garcia.gonzalez.adrian.entidades.proyectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.crownControl.KnockUp;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.entidades.particulas.Particula;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Assets;

public class BolaEnergia extends Proyectil {

    private boolean activado;
    private boolean finalizado;

    private Vector2 inicio, fin;
    private float lerp;

    public BolaEnergia(Entidad creador, Level level, float x, float y) {
        super(creador, level, x, y);

        activado = false;
        finalizado = false;
    }

    @Override
    protected Rectangle _getCollider() {
        return new Rectangle(-8, -8, 16, 16);
    }

    @Override
    protected Vector2 _getOffset() {
        return new Vector2(24, 6);
    }

    @Override
    public void onCollisionEnter(Entidad entidad) {
        if (!activado || getCreador().getBando()==entidad.getBando())
            return;

        Gdx.app.log("Tipo", entidad.getTipoEntidad()+"");
        if (entidad.getTipoEntidad().esUnidad()) {
            Unidad u = (Unidad) entidad;
            float lanzamiento = (getPosition().x>entidad.getPosition().x) ? -300 : 300; //TODO: Hacer constante
            u.aplicarCC(new KnockUp("Salto", new Vector2(lanzamiento, 0),0.5f), (Unidad) getCreador());
        }
        finalizado=true;
        entidad.recibirAtaque(getLevel().getHabilityDamage(getCreador(), 30, 0.4f), getCreador()); //Le hacemos daño
        entidad.generarParticula(new Particula(getPosition(), Assets.instance.efectosPersonaje1.explotion, _getOffset(), 0.5f));
        //TODO: Añadir daño por salpicadura 10 (+0.40)
        List<Entidad> entidades = getLevel().getEntidades(getPosition(), 30);
        for (int i =0 ; i < entidades.size(); i++) {
            entidades.get(i).recibirAtaque(getLevel().getHabilityDamage(getCreador(),10, 0.4f), getCreador());
        }
    }

    // Libera la bola hacia un objetivo
    public void soltar (Vector2 posicion) {
        activado=true;
        inicio = getPosition();
        fin = posicion;
        lerp = 0;
    }

    @Override
    public void onUpdate(float delta) {
        if (!activado)
            return;

        setPosition(inicio.cpy().lerp(fin, lerp));
        lerp+=delta*2;

        if (lerp>2f)
            finalizado=true;
    }

    @Override
    public void onRender(SpriteBatch batch) {
        Rectangle rect = getCollider();
        Vector2 offset = _getOffset();
        TextureRegion region = Assets.instance.efectosPersonaje1.bolaEnergia;
        Color color = batch.getColor();
        batch.setColor(new Color(color.r, color.g, color.b, 0.5f));
        batch.draw(
                region.getTexture(),
                rect.x-offset.x,
                rect.y-offset.y,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                0.5f,
                0.5f,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void onCollisionStay(Entidad entidad, float delta) {

    }

    @Override
    public void onCollisionExit(Entidad entidad) {

    }

    @Override
    public boolean hasFinished() {
        return finalizado;
    }
}
