package garcia.gonzalez.adrian.entidades.proyectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import garcia.gonzalez.adrian.utiles.Utils;

public class BolaTorre extends Proyectil {

    private boolean finalizado;
    private Entidad objetivo;

    private Vector2 inicio, fin;
    private float lerp;

    public BolaTorre(Entidad creador, Entidad objetivo, Level level, Vector2 pos) {
        super(creador, level, pos.x, pos.y);

        this.objetivo=objetivo;
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
        if (objetivo!=entidad)
            return;

        if (entidad.getTipoEntidad() == Enums.TipoEntidad.PERSONAJE) {
            Unidad u = (Unidad) entidad;
            float lanzamiento = (getCreador().getBando()== Enums.Bando.ALIADO) ? 500 : -500; // TODO: Hacer constante
            // Lanzamos a los personajes de la torre para evitar que la torre los mate por error.
            // La torre siempre priorizará a los súbditos que estan dentro de su rango, por lo que
            // ese es el mejor momento para atacar a la torre.
            u.aplicarCC(new KnockUp("Salto_torre", new Vector2(lanzamiento, 0),0.5f), (Unidad) objetivo);
        }
        finalizado=true;
        entidad.recibirAtaque(getLevel().getHabilityDamage(getCreador(), 0, 2f), getCreador()); //Le hacemos daño
        entidad.generarParticula(new Particula(getPosition(), Assets.instance.estructuraAssets.explosion, _getOffset(), 0.5f, 0.75f));

    }

    @Override
    public void onUpdate(float delta) {
        setPosition(Utils.moveTowards(getPosition(), objetivo.getPosition(), 5));

        if (objetivo.getPosition().equals(getPosition()))
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
