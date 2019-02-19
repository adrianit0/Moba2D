package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import garcia.gonzalez.adrian.enums.EstadEnum;
import garcia.gonzalez.adrian.utiles.Estadistica;

public abstract class Entidad {
    public enum Bando { ALIADO, ENEMIGO };


    private Bando bando;

    // Posición, será algo en común en todas las entidades
    private int x;
    private int y;

    // ESTADISTICAS
    // Ver enum "EstadEnum" para ver cuantas estadísticas tiene una entidad.
    private Estadistica estadisticas;

    public Entidad (Bando bando) {
        x=0;
        y=0;
        estadisticas= new Estadistica();
        this.bando = bando;
    }

    public Entidad(Bando bando, int x, int y) {
        this.x=x;
        this.y=y;
        estadisticas= new Estadistica();
        this.bando = bando;
    }

    public Bando getBando() {
        return bando;
    }

    // Método que se activará una única vez cuando se cree
    public abstract void onCreate();

    // Método que se actualizará una vez por frame
    public abstract void update(float delta);

    // Método que se utilizará para renderizar
    public abstract void render (SpriteBatch batch);

    // Método que se utilizará cuando sufra daño
    // Consejo para evitar bucles infinitos: No realizar un ataque espejo (Pe: Devolver un % de vida a quien le ha hecho el ataque)
    public abstract int onDamageTaken (int damage, Entidad destinatario);

    // Método que se utilizará cuando se haga daño.
    public abstract int onAttack (Entidad objetivo);

    // Método que sucede tras haber atacado.
    public abstract void onAfterAttact (Entidad objetivo);

    // Método que se activará al morir
    public abstract void onDeath ();


    /**
     * Esta entidad ataca a la entidad objetivo.
     *
     * El daño base será onAttack, que será el implementado por sus hijos.
     *
     * Si realiza daño activará el método defender
     * */
    public void atacar (Entidad objetivo) {
        // En este juego no hay fuego amigo!
        if (bando==objetivo.bando) {
            return;
        }
        int damage = onAttack(objetivo);

        // Si hace 0 o menos daño ni se activa el trigger enemigo
        if (damage<=0) {
            return;
        }

        // Ya tenemos el daño hecho, ahora le toca al objetivo defenderse.
        objetivo.defender(damage, this, estadisticas.getAttr(EstadEnum.PENETRACION));

        // Ahora miramos si por ejemplo hemos matado al enemigo, sirve por ejemplo para algunas pasivas
        onAfterAttact(objetivo);
    }

    /**
     * Primero una entidad realiza un ataque, y luego otra la recibe.
     * Y realiza sus cálculos
     * */
    public void defender (int dmg, Entidad destinatario, int penetracion) {
        // En este juego no hay fuego amigo!
        if (bando==destinatario.bando) {
            return;
        }

        // TODO: SEGUIR POR AQUI
    }

}
