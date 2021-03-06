package garcia.gonzalez.adrian.entidades.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.entidades.items.Item;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Constants;
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

    // Si está muerto
    private float tiempoMuerto;

    public Personaje(Controlador controller, Habilidad hab1, Habilidad hab2, Habilidad hab3, Enums.Bando bando, int x, int y, Level level) {
        super(bando, x, y, TipoEntidad.PERSONAJE, level);

        this.controller = controller;
        this.habilidades = new Habilidad[] { hab1, hab2, hab3};
    }

    public void setController (Controlador controller) {
        this.controller = controller;
    }

    public Controlador getController() {
        return controller;
    }

    // MÉTODOS ABSTRACTOS
    public abstract boolean canCastHability (int hab);
    public abstract boolean onHabilityDown (int hab);
    public abstract boolean onHabilityStay (int hab, float delta);
    public abstract boolean onHabilityUp (int hab);

    // METODOS QUE SIGUEN ABSTRACTOS DE LOS PADRES, por lo que tendrá que implementarlo sus hijos

    @Override
    public abstract void onUpdate(float delta);
    @Override
    public abstract void onTickUpdate(float tickDelta);
    @Override
    public abstract void onRender(SpriteBatch batch);

    @Override
    public void onCreate() {

    }

    @Override
    public int onAttack(int damage, Entidad objetivo) {
        return damage;
    }

    @Override
    public boolean onMove(Enums.Direccion dir, float delta) {
        return true;
    }

    @Override
    public void onIdle(float delta) {

    }

    @Override
    public final void update(float delta) {
        if (controller!=null && estaVivo()) {
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
            if (controller.onKeyDown(TeclasJugador.AGACHAR)) { }
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
            if (controller.onKeyPressing(TeclasJugador.SALTAR)) { }
            if (controller.onKeyPressing(TeclasJugador.AGACHAR)) { }
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
            if (controller.onKeyUp(TeclasJugador.MOVER_DERECHA)) { }
            if (controller.onKeyUp(TeclasJugador.MOVER_IZQUIERDA)) { }
            if (controller.onKeyUp(TeclasJugador.SALTAR)) { }
            if (controller.onKeyUp(TeclasJugador.AGACHAR)) { }
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

        // Si está muerto le restamos tiempo de vida
        if (!estaVivo()) {
            tiempoMuerto -= delta;
            if (tiempoMuerto<0) {
                changePosition( new Vector2( getBando()==Bando.ALIADO ?
                                Constants.ALLY_CHARACTER_SPAWN_POSITION :
                                Constants.ENEMY_CHARACTER_SPAWN_POSITION,getPosition().y));

                /*if (this==level.getMainCharacter()) {
                    level.setGrayscale(false);
                }*/
                tiempoMuerto=0;
                getAtributos().curarCompletamente();
                onSpawn();
            }
        }



        // aplicamos el update del padre
        super.update(delta);
    }

    // El tiempo en el que personaje está muerto. Resucita tras ese tiempo
    public void setTiempoMuerto (float tiempo) {
        tiempoMuerto = tiempo;
    }

    public int getTiempoMuerto () {
        return (int) Math.ceil(tiempoMuerto);
    }

    @Override
    public final void onHudRender(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (!estaVivo())
            return;

        float porc = (float) getAtributos().getSaludActual() / getAtributos().getMaxAttr(AtribEnum.SALUD);
        float porcMana = (float) getAtributos().getManaActual() / getAtributos().getMaxAttr(AtribEnum.MANA);
        Vector2 pos = getPosition();
        final Vector2 size = Constants.HUD_CHARACTER_LIFE_SIZE;

        // Cuadrado negro de atras
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(pos.x-size.x/2, getCollider().height+size.y+5+pos.y, size.x, size.y);
        // Vida
        shapeRenderer.setColor(getBando()==Bando.ALIADO ? Color.GREEN : Color.RED);
        shapeRenderer.rect(pos.x-size.x/2+1, getCollider().height+9+size.y+pos.y, size.x*porc-2, size.y-5);

        // Mana
        shapeRenderer.setColor(Color.BLUE); // Permitir cambiar mana a otra cosa
        shapeRenderer.rect(pos.x-size.x/2+1, getCollider().height+7+size.y+pos.y, size.x*porcMana-2, size.y-7);
    }

    public final void castHabilityDown (int id) {
        Habilidad hab = habilidades [id-1];
        int mana = getAtributos().getManaActual();
        if (hab.isCooldown() || mana<hab.getCoste() || hasCrowdControl(CrowdControl.ATURDIMIENTO, CrowdControl.KNOCK_UP, CrowdControl.SILENCIO)) {
            hab.setUsed(true);
            return;
        }

        // Le preguntamos al personaje si puede/quiere realizar la habilidad
        boolean canCast = canCastHability(id);
        if (!canCast) {
            hab.setUsed(true);
            return;
        }

        //Consumimos el maná
        getAtributos().aumentarManaActual(-hab.getCoste());

        boolean enterInCooldown = onHabilityDown(id);
        if (enterInCooldown) {
            hab.setInCooldown( getAtributos().getAttrPorc(AtribEnum.COOLDOWN) );
            hab.setUsed (true);
        }
    }

    public final void castHabilityStay (int id, float delta) {
        Habilidad hab = habilidades [id-1];
        if (hab.isUsed())
            return;

        boolean enterInCooldown = onHabilityStay(id, delta);
        if (enterInCooldown) {
            hab.setInCooldown( getAtributos().getAttrPorc(AtribEnum.COOLDOWN) );
            hab.setUsed (true);
        }
    }

    public final void castHabilityUp (int id) {
        Habilidad hab = habilidades [id-1];
        if (!hab.isUsed())  {
            boolean enterInCooldown = onHabilityUp(id);
            if (enterInCooldown) {
                hab.setInCooldown( getAtributos().getAttrPorc(AtribEnum.COOLDOWN) );
            }
        }
        hab.setUsed (false);
    }

    /**
     * A partir de un valor base más otro porcentual obtiene el daño para cualquiera de las
     * habilidades.
     * */
    public final int getBaseAndPorcentualValue(float base, float porcentual) {
        return Math.round(base + getAtributos().getAttrPorc(AtribEnum.ATAQUE) * porcentual);
    }


    @Override
    // Por defecto, para todos los personajes solo podran saltar estando en el suelo
    // Aunque este método es heredable y configurable para cada personaje.
    public boolean onJumpStart(EstadoSalto estado) {
        return estado==EstadoSalto.EN_SUELO;
    }

    @Override
    public void onJumpStay() {

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

    public Habilidad[] getHabilidades() {
        return habilidades;
    }
}
