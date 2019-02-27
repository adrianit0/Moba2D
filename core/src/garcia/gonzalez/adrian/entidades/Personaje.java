package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.entidades.items.Item;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Habilidad;

/**
 * Clase base para los personajes, estan la mayoría de los métodos implementados para
 * evitar forzar implementarlo en los hijos.
 *
 * Además, los personajes a diferencia del resto de tipos, podrán ser controlador por el jugador o la IA
 * */
public abstract class Personaje extends Unidad {

    // Quien controla al personaje, este puede ser un jugador o la IA
    private Controlador controller;
    private Habilidad[] habilidades;

    public Personaje(Controlador controller, Habilidad hab1, Habilidad hab2, Habilidad hab3, Enums.Bando bando, int x, int y, Level level) {
        super(bando, x, y, level);

        this.controller = controller;
        this.habilidades = new Habilidad[] { hab1, hab2, hab3};
    }

    // MÉTODOS ABSTRACTOS
    public abstract boolean canCastHability (int hab);
    public abstract boolean onHabilityDown (int hab);
    public abstract boolean onHabilityStay (int hab, float delta);
    public abstract boolean onHabilityUp (int hab);

    // TODO: Implementar QUEUE de habilidad
    // public void startQueueHability ();
    // public ... getQueueHability ();

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
                castHabilityDown(1);
            }
            if (controller.onKeyDown(TeclasJugador.BOTON_HABILIDAD_2)) {
                castHabilityDown(2);
            }
            if (controller.onKeyDown(TeclasJugador.BOTON_HABILIDAD_3)) {
                castHabilityDown(3);
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
                castHabilityStay(1, delta);
            }
            if (controller.onKeyPressing(TeclasJugador.BOTON_HABILIDAD_2)) {
                castHabilityStay(2, delta);
            }
            if (controller.onKeyPressing(TeclasJugador.BOTON_HABILIDAD_3)) {
                castHabilityStay(3, delta);
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
                castHabilityUp(1);
            }
            if (controller.onKeyUp(TeclasJugador.BOTON_HABILIDAD_2)) {
                castHabilityUp(2);
            }
            if (controller.onKeyUp(TeclasJugador.BOTON_HABILIDAD_3)) {
                castHabilityUp(3);
            }
        }

        // aplicamos el update del padre
        super.update(delta);

    }

    public void castHabilityDown (int id) {
        Habilidad hab = habilidades [id-1];
        if (hab.isCooldown())
            return;

        // Le preguntamos al personaje si puede/quiere realizar la habilidad
        boolean canCast = canCastHability(id);
        if (!canCast) {
            hab.setUsed(true);
            return;
        }


        boolean enterInCooldown = onHabilityDown(id);
        if (enterInCooldown) {
            hab.setInCooldown( getAtributos().getAttrPorc(AtribEnum.COOLDOWN) );
            hab.setUsed (true);
        }
    }

    public void castHabilityStay (int id, float delta) {
        Habilidad hab = habilidades [id-1];
        if (hab.isUsed())
            return;

        boolean enterInCooldown = onHabilityStay(id, delta);
        if (enterInCooldown) {
            hab.setInCooldown( getAtributos().getAttrPorc(AtribEnum.COOLDOWN) );
            hab.setUsed (true);
        }
    }

    public void castHabilityUp (int id) {
        Habilidad hab = habilidades [id-1];
        if (!hab.isUsed())  {
            boolean enterInCooldown = onHabilityUp(id);
            if (enterInCooldown) {
                hab.setInCooldown( getAtributos().getAttrPorc(AtribEnum.COOLDOWN) );
            }
        }
        hab.setUsed (false);
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
    public void onEntityKilled(Entidad objetivo) {

    }

    @Override
    public void onLevelUp() {

    }

    @Override
    public void onSpawn() {

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
