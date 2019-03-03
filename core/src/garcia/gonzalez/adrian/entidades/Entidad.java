package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.proyectiles.Proyectil;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Atributos;

public abstract class Entidad {
    //TODO: BORRAR
    private Bando bando;

    protected float x;
    protected float y;

    private int nivel;

    protected Level level;

    private Atributos atributos; // Todos los atributos del personaje

    public Entidad(Bando bando, int x, int y, Level level) {
        this.x=x;
        this.y=y;
        atributos = new Atributos();
        this.bando = bando;
        this.level = level;
    }

    public Bando getBando() {
        return bando;
    }

    public abstract void onCreate();
    public abstract void onSpawn();

    public abstract void onUpdate(float delta);
    public abstract void onTickUpdate(float tickDelta);
    public abstract void onRender(SpriteBatch batch);

    public abstract int onAttack (int damage, Entidad objetivo);
    public abstract void onAfterAttact (Entidad objetivo);

    public abstract void onEntityKilled (Entidad objetivo);

    public abstract int onBeforeDefend(int damage, Entidad destinatario);
    public abstract void onAfterDefend(int receivedDamage, Entidad destinatario, boolean vivo);

    public abstract int onBeforeHeal (int cura, Entidad destinatario);
    public abstract void onAfterHeal (int saludCurada);

    public abstract void onLevelUp();

    public abstract boolean onDeath ();

    // Métodos abstractos de colisión:
    // TODO: Implementar, unicamente si hace falta
    //public abstract void onCollisionEnter(Entidad e);
    //public abstract void onCollisionStay(Entidad e, float delta);
    //public abstract void onCollisionExit(Entidad e);

    public abstract Rectangle getCollider ();
    public abstract Vector2 getOffset();
    public abstract Vector2 getSize();

    // Si una vez muerto, el personaje puede ser eliminado de la lista.
    // Es probable que despues de muerto siga apareciendo para mostrar la animación de muerte.
    // Asi que sea el propio personaje quien decida cuando eliminarse.
    public abstract boolean canBeCleaned();


    public void update(float delta) {
        onUpdate(delta);
    }
    public void render (SpriteBatch sprite) {
        onRender(sprite);
    }

    public void tickUpdate(float tickDelta) {
        regeneracionPasiva(tickDelta);

        onTickUpdate(tickDelta);
    }

    private void regeneracionPasiva (float tickDelta) {
        if (!estaVivo())
            return;

        final float porcVida = getAtributos().getAttrPorc(AtribEnum.REG_SALUD) * tickDelta;
        final float porcMana = getAtributos().getAttrPorc(AtribEnum.REG_MANA) * tickDelta;

        atributos.curarSalud(porcVida);
        atributos.aumentarManaActual(porcMana);
    }

    public final void generarProyectil (Proyectil p) {
        //TODO: Añadir proyectil al level
    }

    public final void atacar (int damage, Entidad objetivo) {
        if (objetivo==null || bando==objetivo.bando) {
            return;
        }

        damage = onAttack(damage, objetivo);

        if (damage<=0)
            return;

        damage = objetivo.defender(damage, this, atributos.getAttr(AtribEnum.PENETRACION));

        onAfterAttact(objetivo);

        if (damage<=0)
            return;

        // A partir de aquí va la succión de hechizos
        float succion = atributos.getAttr(AtribEnum.SUCCION);
        if (succion==0)
            return;

        int cantidad = Math.round(damage*succion);

        if (cantidad>0)
            curarPersonaje(cantidad, this);

    }

    /**
     * Primero una entidad realiza un ataque, y luego otra la recibe.
     * Y realiza sus cálculos
     *
     * En este método es donde recibe daño, y el personaje que recibe el ataque puede
     * llegar a morir. Si eso ocurriese, activaría antes de morir el método onDeath().
     *
     * El método OnDeath si devuelve true el personaje muere, si no el personaje sigue vivo
     * incluso estando por debajo de 0 de vida (Pe: Que exista un personaje que continue vivo
     * tras morir).
     * */
    protected final int defender (int dmg, Entidad destinatario, int penetracion) {
        // En este juego no hay fuego amigo!
        if (bando==destinatario.bando) {
            return 0;
        }

        // Primero miramos como gestiona el personaje el daño.
        // Es probable que tenga una pasiva, habilidad o item que reduzca el daño.
        dmg = onBeforeDefend(dmg, destinatario);

        // Si hace 0 o menos daño entonces eso significa que no le ha inflingido daño.
        if (dmg<=0) {
            return 0;
        }

        // Sobre la defensa enemiga le quitamos la armadura.
        float defensa = atributos.getAttr(AtribEnum.DEFENSA)-penetracion;

        // Porcentaje de protección, depende de la armadura.
        // El cálculo es 50 / defensa+50 (Pe: A 50 de armadura, mitigará un 50% del daño)
        // Pero si tiene defensa negativa (Pe: -25), recibirá un 33% más del daño normal
        float multiplicador;
        if (defensa>=0) {
            multiplicador = 50 / (defensa+50);
        } else{
            multiplicador = 2 - (50 / (defensa-50));
        }

        // Finalmente calculamos el daño final
        dmg  = Math.round(dmg * multiplicador);

        // Cogemos la salud del objetivo
        int vida = atributos.getSaludActual();

        vida -= dmg;

        // Le ponemos la vida de nuevo
        atributos.setSaludActual(vida);

        // TODO: Actualizar HUD

        // Si este golpe ha matado este objetivo.
        // Es probable que el objetivo pueda seguir vivo si así funciona su personaje.
        if (vida<=0) {
            boolean sigueVido = onDeath();
            // TODO: Programar la parte de si muere y sigue vivo

        }

        // Activamos el onAfterDefend
        onAfterDefend(dmg, destinatario, vida>0);

        // Finalmente devolvemos el daño final y real inflingido
        return dmg;
    }

    /**
     * Cura el personaje mediante cura directa, no incluye la regeneración de vida pasiva
     * ni la de cura por segundo de algún efecto.
     * */
    public final void curarPersonaje (int cantidad, Entidad destinatario) {
        // Si no hay destinatario entonces será el mismo
        if (destinatario==null) {
            destinatario=this;
        }

        // Es probable que el personaje tenga un item, pasiva o buff que aumente la cura.
        cantidad = onBeforeHeal(cantidad, destinatario);

        // Si no hay valor para curar, entonces no cura
        if (cantidad<=0) {
            return;
        }

        // Cogemos la salud del objetivo
        int vida = atributos.getSaludActual();
        int vidaMax = atributos.getMaxAttr(AtribEnum.SALUD);

        // No se puede curar si ya tiene la vida entera!
        if (vida==vidaMax) {
            return;
        }

        vida += cantidad;
        if (vida>vidaMax)
            vida = vidaMax;

        // Le ponemos la vida de nuevo
        atributos.setSaludActual(vida);

        //TODO: Actualizar HUD

        // Es probable que que el personaje tenga un item, pasiva o buff con efecto
        // a posteriori de la cura (Pe: "Consigue 10 de ataque durante 3s tras curarse").
        onAfterHeal(cantidad);
    }

    public final Vector2 getPosition () {
        return new Vector2(x,y);
    }

    public final Vector3 getPosition3D () {
        return new Vector3 (x,y,0);
    }

    public final Atributos getAtributos() {
        return atributos;
    }

    public final Level getGameManager () {
        return level;
    }

    /**
    * Teletransporta a un personaje a otra posición instantaneamente
    * */
    public final void changePosition (Vector2 newPos) {
        x = newPos.x;
        y = newPos.y;
    }

    /**
     * Mueve relativamente el personaje respecto a la posición en la que está
     * */
    public final void movePosition(Vector2 relPos) {
        x+=relPos.x;
        y+=relPos.y;
    }

    /**
     * Mira si la entidad sigue viva o en su contrario ha muerto
     * */
    //TODO: Mirar y mejorar (Si procede)
    public boolean estaVivo () {
        return atributos.getSaludActual()>0;
    }
}