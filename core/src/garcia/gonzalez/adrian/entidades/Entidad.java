package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.particulas.Particula;
import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.entidades.proyectiles.Proyectil;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Atributos;

public abstract class Entidad {
    private Bando bando;
    private TipoEntidad tipoEntidad;

    protected float x;
    protected float y;

    protected Level level;

    private Atributos atributos; // Todos los atributos del personaje

    public Entidad(Bando bando, int x, int y, TipoEntidad tipoEntidad, Level level) {
        this.x=x;
        this.y=y;
        atributos = new Atributos();

        // Atributos por defecto
        atributos.setAttr(AtribEnum.SALUD, 300);
        atributos.setAttr(AtribEnum.VELOCIDAD, 300);
        atributos.setAttr(AtribEnum.SALTO, 180);

        this.bando = bando;
        this.level = level;
        this.tipoEntidad=tipoEntidad;

        // Lanzamos los eventos de crear y spawnear
        onCreate();
        onSpawn();
    }

    public Bando getBando() {
        return bando;
    }

    public TipoEntidad getTipoEntidad() {
        return tipoEntidad;
    }

    public abstract void onCreate();
    public abstract void onSpawn();

    public abstract void onUpdate(float delta);
    public abstract void onTickUpdate(float tickDelta);
    public abstract void onRender(SpriteBatch batch);

    public abstract void onHudRender (SpriteBatch batch, ShapeRenderer shapeRenderer);

    /**
     * Un debug especial para saber entre otras cosas donde estan lo colisionadores
     * */
    public void onDebugRender (ShapeRenderer shapeRenderer) {

    }

    public abstract int onAttack (int damage, Entidad objetivo);
    public abstract void onAfterAttact (Entidad objetivo);

    public abstract void onEntityKilled (Entidad objetivo);

    public abstract int onBeforeDefend(int damage, Entidad destinatario);
    public abstract void onAfterDefend(int receivedDamage, Entidad destinatario, boolean vivo);

    public abstract int onBeforeHeal (int cura, Entidad destinatario);
    public abstract void onAfterHeal (int saludCurada);

    public abstract void onLevelUp();

    public abstract boolean onDeath ();

    public abstract Rectangle getCollider ();
    public abstract Vector2 getOffset();
    public abstract Vector2 getSize();

    // Coge el centro del collider
    public final Vector2 getCenter() {
        Rectangle rect = getCollider();
        return new Vector2(rect.x+rect.width/2, rect.y+rect.height/2);
    }

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

    public final Proyectil generarProyectil (Proyectil p) {
        level.generarProyecitl(p);
        return p;
    }

    public final Particula generarParticula (Particula p) {
        level.generarParticula(p);
        return p;
    }

    public final void recibirAtaque (int damage, Entidad destinanatario) {
        if (destinanatario==null || bando==destinanatario.bando) {
            return;
        }

        damage = destinanatario.onAttack(damage, this);

        if (damage<=0)
            return;

        damage = defender(damage, destinanatario, destinanatario.atributos.getAttr(AtribEnum.PENETRACION));

        destinanatario.onAfterAttact(this);

        if (damage>0)
            destinanatario.succionHechizo(damage);
    }

    public final void succionHechizo (float damage) {
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

        // Si este golpe ha matado este objetivo.
        // Es probable que el objetivo pueda seguir vivo si así funciona su personaje.
        if (vida<=0) {
            morir (destinatario);
        }

        // Activamos el onAfterDefend
        onAfterDefend(dmg, destinatario, vida>0);

        // Finalmente devolvemos el daño final y real inflingido
        return dmg;
    }

    public final void morir (Entidad asesino) {
        onDeath();
        asesino.onEntityKilled(this);

        /*if (this==level.getMainCharacter()) {
            level.setGrayscale(true);
        }*/

        // Si es un personaje este estará muerto durante unos segundos
        // Luego volverá a la fuente de inicio.
        if (getTipoEntidad() == TipoEntidad.PERSONAJE) {
            // Le asignamos el tiempo en el que está muerto.
            // Contra más se alargue la partida, más tiempo estará muerto
            ((Personaje) this).setTiempoMuerto(10+2*level.getOleada());
        }
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

        /**
         *
        if (hasCrowdControl(CrowdControl.SANGRADO)) {
            cantidad/=2;    // Si el objetivo está sangrando se curará la mitad
        }
         */

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

        if (x<-1350)
            x=-1350;
        else if (x>1350)
            x=1350;
    }

    /**
     * Mira si la entidad sigue viva o en su contrario ha muerto
     * */
    public boolean estaVivo () {
        return atributos.getSaludActual()>0;
    }
}