package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;

/**
 * Clase base para los personajes, estan la mayoría de los métodos implementados para
 * evitar forzar implementarlo en los hijos.
 * */
public abstract class Personaje extends Unidad {
    public Personaje(Enums.Bando bando) {
        super(bando);
    }

    public Personaje(Enums.Bando bando, int x, int y) {
        super(bando, x, y);
    }

    // METODOS QUE SIGUEN ABSTRACTOS DE LOS HIJOS, por lo que tendrá que implementarlo los hijos
    @Override
    public abstract void onCreate();
    @Override
    public abstract void update(float delta);
    @Override
    public abstract void tickUpdate(float tickDelta);
    @Override
    public abstract void render(SpriteBatch batch);
    @Override
    public abstract int onAttack(Entidad objetivo);

    @Override
    public boolean onMove(Enums.Direccion dir) {
        return true;
    }

    @Override
    // Por defecto, para todos los personajes solo podran saltar estando en el suelo
    // Aunque este método es heredable y configurable para cada personaje.
    public boolean onJumpStart(EstadoSalto estado) {
        return estado==EstadoSalto.EN_SUELO;
    }

    @Override
    public void onJumpFinish() {

    }

    @Override
    public float onCrownControl(Enums.CrowdControl cc, float time, Unidad destinatario) {
        return time;
    }

    @Override
    public int onDamageTaken(int damage, Entidad destinatario) {
        return damage;
    }

    @Override
    public void onAfterAttact(Entidad objetivo) {

    }

    @Override
    public void onAfterDefend(int receivedDamage, Entidad destinatario, boolean vivo) {

    }

    @Override
    public int onBeforeHeal(int cura, Entidad destinatario) {
        return cura;
    }

    @Override
    public void onAfterHeal(int saludCurada) {

    }

    @Override
    public boolean onDeath() {
        return true;
    }
}
