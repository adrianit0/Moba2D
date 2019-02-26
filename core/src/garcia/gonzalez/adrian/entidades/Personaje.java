package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.entidades.items.Item;
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
    public boolean onMove(Enums.Direccion dir, float delta) {
        return true;
    }

    @Override
    public final void update(float delta) {
        if (controller!=null) {
            // PRESS DOWN
            if (controller.onKeyDown(TeclasJugador.MOVER_DERECHA)) {
                mover(Direccion.DERECHA, delta);
            }
            if (controller.onKeyDown(TeclasJugador.MOVER_IZQUIERDA)) {
                mover(Direccion.IZQUIERDA, delta);
            }
            if (controller.onKeyDown(TeclasJugador.SALTAR)) {
                saltar();
            }
            if (controller.onKeyDown(TeclasJugador.AGACHAR)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyDown(TeclasJugador.BOTON_HABILIDAD_1)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyDown(TeclasJugador.BOTON_HABILIDAD_2)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyDown(TeclasJugador.BOTON_HABILIDAD_3)) {
                // TODO: HACER ESTE INPUT
            }


            // PRESSING
            if (controller.onKeyPressing(TeclasJugador.MOVER_DERECHA)) {
                mover(Direccion.DERECHA, delta);
            }
            if (controller.onKeyPressing(TeclasJugador.MOVER_IZQUIERDA)) {
                mover(Direccion.IZQUIERDA, delta);
            }
            if (controller.onKeyPressing(TeclasJugador.SALTAR)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyPressing(TeclasJugador.AGACHAR)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyPressing(TeclasJugador.BOTON_HABILIDAD_1)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyPressing(TeclasJugador.BOTON_HABILIDAD_2)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyPressing(TeclasJugador.BOTON_HABILIDAD_3)) {
                // TODO: HACER ESTE INPUT
            }

            // PRESS UP
            if (controller.onKeyUp(TeclasJugador.MOVER_DERECHA)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyUp(TeclasJugador.MOVER_IZQUIERDA)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyUp(TeclasJugador.SALTAR)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyUp(TeclasJugador.AGACHAR)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyUp(TeclasJugador.BOTON_HABILIDAD_1)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyUp(TeclasJugador.BOTON_HABILIDAD_2)) {
                // TODO: HACER ESTE INPUT
            }
            if (controller.onKeyUp(TeclasJugador.BOTON_HABILIDAD_3)) {
                // TODO: HACER ESTE INPUT
            }
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

    //TODO: Si no se va a usar, borrarlo
    public void onItemBuy(garcia.gonzalez.adrian.entidades.items.Item item) {

    }

    public void onItemSell(Item item) {

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
