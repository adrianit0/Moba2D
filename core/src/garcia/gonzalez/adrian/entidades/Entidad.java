package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Estadistica;

public abstract class Entidad {
    private Bando bando;

    // Posición, será algo en común en todas las entidades
    private float x;
    private float y;

    // ESTADISTICAS
    // Nivel de la entidad, este estará comprendido entre 1 y 18
    private int nivel;

    // Acceso a la partida
    protected Level level;

    // Ver enum "Enums" para ver cuantas estadísticas tiene una entidad.
    private Estadistica estadisticas;

    public Entidad(Bando bando, int x, int y, Level level) {
        this.x=x;
        this.y=y;
        estadisticas= new Estadistica();
        this.bando = bando;
        this.level = level;
    }

    public Bando getBando() {
        return bando;
    }

    // Método que se activará una única vez cuando se cree
    public abstract void onCreate();

    // Método que se actualizará una vez por frame
    public abstract void onUpdate(float delta);

    // Para la actualización de bufos/vida/etc, vida, etc. Se actualizará más o menos cada 0.25s
    public abstract void onTickUpdate(float tickDelta);

    // Método que se utilizará para renderizar
    public abstract void onRender(SpriteBatch batch);

    // Método que se utilizará cuando se haga daño.
    public abstract int onAttack (Entidad objetivo);

    // Método que sucede tras haber atacado.
    public abstract void onAfterAttact (Entidad objetivo);

    // Método que se utilizará cuando sufra daño
    // Consejo para evitar bucles infinitos: No realizar un ataque espejo (Pe: Devolver un % de vida a quien le ha hecho el ataque)
    public abstract int onBeforeDefend(int damage, Entidad destinatario);

    // Método que sucede tras haber defendido, da como parametro el daño real recibido.
    public abstract void onAfterDefend(int receivedDamage, Entidad destinatario, boolean vivo);

    // Método que se activa cuando quiere curar un objetivo. destinatario puede ser null y this.
    public abstract int onBeforeHeal (int cura, Entidad destinatario);

    // Método que se activa tras haber recibido vida. No se activa con la regeneración pasiva.
    public abstract void onAfterHeal (int saludCurada);

    // Método que se activará al morir
    public abstract boolean onDeath ();

    // Métodos abstractos de colisión:
    // TODO: Implementar, unicamente si hace falta
    //public abstract void onCollisionEnter(Entidad e);
    //public abstract void onCollisionStay(Entidad e);
    //public abstract void onCollisionExit(Entidad e);
    // Colisionador del personaje
    public abstract Rectangle getCollider ();
    public abstract Vector2 getCenter();
    public abstract Vector2 getSize();      // Toma el valor real del personaje (Que no es siempre el Widht-Height del Sprite)

    // Si una vez muerto, el personaje puede ser eliminado de la lista.
    // Es probable que despues de muerto siga apareciendo para mostrar la animación de muerte.
    // Asi que sea el propio personaje quien decida cuando eliminarse.
    public abstract boolean canBeCleaned();

    /**
     * Método para el update
     * */
    public void update(float delta) {
        onUpdate(delta);
    }

    public void render (SpriteBatch sprite) {
        onRender(sprite);
    }

    public final void tickUpdate(float tickDelta) {
        onTickUpdate(tickDelta);
    }

    /**
     * Esta entidad ataca a la entidad objetivo.
     *
     * El daño base será onAttack, que será el implementado por sus hijos.
     *
     * Si realiza daño activará el método defender.
     *
     * Es final para evitar ser heredado por los hijos
     * */
    public final void atacar (Entidad objetivo) {
        // En este juego no hay fuego amigo!
        if (objetivo==null || bando==objetivo.bando) {
            return;
        }

        int damage = onAttack(objetivo);

        // Si hace 0 o menos daño ni se activa el trigger enemigo
        if (damage<=0) {
            return;
        }

        // Ya tenemos el daño hecho, ahora le toca al objetivo defenderse.
        damage = objetivo.defender(damage, this, estadisticas.getAttr(AtribEnum.PENETRACION));

        // Ahora miramos si por ejemplo hemos matado al enemigo, sirve por ejemplo para algunas pasivas
        onAfterAttact(objetivo);

        // Aunque hayas hecho daño, es probable que el enemigo lo haya mitigado completamente
        if (damage<=0) {
            return;
        }

        //Ahora miremos el porcentaje de SUCCION que tiene el personaje
        float succion = estadisticas.getAttr(AtribEnum.SUCCION);

        // Solo funcionará la succión si tiene
        if (succion==0)
            return;

        int cantidad = Math.round(damage*succion);

        // Finalmente curamos esa misma cantidad a este personaje
        if (cantidad>0) {
            curarPersonaje(cantidad, this);
        }
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
        float defensa = estadisticas.getAttr(AtribEnum.DEFENSA)-penetracion;

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
        int vida = estadisticas.getSaludActual();

        vida -= dmg;

        // Le ponemos la vida de nuevo
        estadisticas.setSaludActual(vida);

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
        int vida = estadisticas.getSaludActual();
        int vidaMax = estadisticas.getMaxAttr(AtribEnum.SALUD);

        // No se puede curar si ya tiene la vida entera!
        if (vida==vidaMax) {
            return;
        }

        vida += cantidad;
        if (vida>vidaMax)
            vida = vidaMax;

        // Le ponemos la vida de nuevo
        estadisticas.setSaludActual(vida);

        //TODO: Actualizar HUD

        // Es probable que que el personaje tenga un item, pasiva o buff con efecto
        // a posteriori de la cura (Pe: "Consigue 10 de ataque durante 3s tras curarse").
        onAfterHeal(cantidad);
    }

    public Vector2 getPosition () {
        return new Vector2(x,y);
    }

    protected Estadistica getEstadisticas() {
        return estadisticas;
    }

    public Level getGameManager () {
        return level;
    }

    public void changePosition (Vector2 newPos) {
        x = newPos.x;
        y = newPos.y;
    }

    public void movePosition(Vector2 relPos) {
        x+=relPos.x;
        y+=relPos.y;
    }

    /**
     * Mira si la entidad sigue viva o en su contrario ha muerto
     * */
    //TODO: Mirar y mejorar (Si procede)
    public boolean estaVivo () {
        return estadisticas.getSaludActual()>0;
    }
}