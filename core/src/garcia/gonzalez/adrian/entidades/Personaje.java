package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;

/**
 * Clase base para los personajes, estan la mayoría de los métodos implementados para
 * evitar forzar implementarlo en los hijos.
 *
 * Además, los personajes a diferencia del resto de tipos, podrán ser controlador por el jugador o la IA
 * */
public abstract class Personaje extends Unidad {

    // Quien controla al personaje, este puede ser un jugador o la IA
    private Controlador controller;

    public Personaje(Controlador controller, Enums.Bando bando, int x, int y, Level level) {
        super(bando, x, y, level);

        this.controller = controller;
    }

    // METODOS QUE SIGUEN ABSTRACTOS DE LOS PADRES, por lo que tendrá que implementarlo sus hijos
    @Override
    public abstract void onCreate();
    @Override
    public abstract void onUpdate(float delta);
    @Override
    public abstract void onTickUpdate(float tickDelta);
    @Override
    public abstract void onRender(SpriteBatch batch);
    @Override
    public abstract int onAttack(Entidad objetivo);

    @Override
    public boolean onMove(Enums.Direccion dir) {
        return true;
    }

    @Override
    public final void update(float delta) {
        TeclasJugador[] teclas = TeclasJugador.values();
        for (TeclasJugador t : teclas) {
            controller.onKeyDown(t);
        }
        for (TeclasJugador t : teclas) {
            controller.onKeyPressing(t);
        }
        for (TeclasJugador t : teclas) {
            controller.onKeyUp(t);
        }


        // aplicamos el update del padre
        super.update(delta);

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
    public boolean onCrownControl(CC cc, Unidad destinatario) {
        return true;
    }

    @Override
    public int onBeforeDefend(int damage, Entidad destinatario) {
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

    @Override
    /**
     * Los personaje al ser jugadores contrlados no podran ser borrados de la lista.
     * */
    public final boolean canBeCleaned() {
        return false;
    }
}
