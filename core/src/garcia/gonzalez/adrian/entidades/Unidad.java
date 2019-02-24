package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Constants;

/**
 * La característica especial de las unidades respecto a estructura es que podran moverse
 * y sufrir CC (Crown Control)
 * */
public abstract class Unidad extends Entidad {

    //TODO: Meter los buffos
    private ArrayList<CC> crownControl;

    //TODO: Meter la velocidad

    //TODO: Meter el estado salto
    private Direccion direccion;
    private EstadoSalto estadoSalto;

    private Vector2 velocidad;  // Movimiento total del personaje
    private Vector2 knockUp;    // Movimiento involuntario, provocado por el enemigo

    public Unidad(Bando bando, int x, int y, Level level) {
        super(bando, x, y, level);

        estadoSalto=EstadoSalto.EN_SUELO;
        crownControl = new ArrayList();

        velocidad = new Vector2(0,0);
    }

    // Cada vez que el personaje se mueva
    public abstract boolean onMove(Direccion dir);
    // Al empezar el salto
    public abstract boolean onJumpStart (EstadoSalto estado);
    // Al caer al suelo
    public abstract void onJumpFinish();
    // Al ser cceado, le pregunta al personaje si quiere ser afectado por su efecto.
    public abstract boolean onCrownControl (CC cc, Unidad destinatario);

    public EstadoSalto getEstadoSalto() {
        return estadoSalto;
    }

    @Override
    /**
     * Activamos los CC, luego el update del super, y luego aplicamos el Knock-Up, si procede.
     * */
    public void update(float delta) {
        // Volvemos a poner el KnockUp a 0
        knockUp = new Vector2(0,0);
        velocidad.y -= Constants.GRAVITY;

        // Aplicamos todos los CC
        for (int i = 0;i < crownControl.size(); i++) {
            CC c = crownControl.get(i);

            c.aplicandoCCUpdate(this, delta);
            if (c.hasFinished(this,System.currentTimeMillis())) {
                crownControl.remove(c);
                i--;
                Gdx.app.log("ACABADO", "El cc se acabo");
            }
        }

        // Aplicamos el update de la clase padre
        super.update(delta);

        // Si el knock no es igual a 0
        if (!knockUp.equals(Vector2.Zero)) {
            movePosition(new Vector2(knockUp.x*delta, knockUp.y*delta));
        }

        // Si está por encima del suelo significa que tiene que caer
        //TODO: Mejorar esto
        if (getPosition().y>5) {
            estadoSalto = EstadoSalto.SALTANDO;
            movePosition(new Vector2 (0, velocidad.y * delta)); //TODO: Mejorar la gravedad de los objetos
        } else {
            estadoSalto = estadoSalto.EN_SUELO;
            velocidad.y = 0;
        }
    }

    //TODO: Seguir
    /**
     * Mueve el personaje, este método no es heredable, usar en su lugar el método onMove().
     * */
    public final void mover(Direccion dir, float delta) {
        //TODO: No deja moverse si está CCeado por Aturdimiento o KnockUp
        // if cc return

        // Activamos el onMove()
        boolean mover = onMove(dir);

        // Es probable que sea el propio personaje quien voluntariamente decida no moverse
        if (!mover) {
            return;
        }

        direccion = dir;

        // TODO: Seguir
        float velocidad = getEstadisticas().getAttr(AtribEnum.VELOCIDAD);

        velocidad *= delta * direccion.getDir();

        movePosition(new Vector2(velocidad, 0));
    }

    /**
     * Salta el personaje, este método no es heredable, en su lugar utilizar el método onJumpStart()
     * */
    public final void Saltar () {
        //TODO: No deja moverse si está CCeado por Aturdimiento, pesado o KnockUp
        // if cc return

        // Activamos el onJumpStart, le pasamos si está o no en el suelo.
        // Será el personaje quien gestionará si quiere saltar (Incluso saltar en el aire)
        boolean saltar = onJumpStart(estadoSalto);

        // Es probable que sea el propio personaje quien voluntariamente decida no saltar
        if (!saltar) {
            return;
        }



        int potenciaSalto = getEstadisticas().getAttr(AtribEnum.SALTO);

        // No va a saltar si no tiene la potencia suficiente para ello
        if (potenciaSalto<=0) {
            return;
        }

        estadoSalto = EstadoSalto.SALTANDO;
        // TODO: Seguir
    }

    /**
     *
     * Aplica un efecto a un personaje
     * */
    public final void aplicarCC (CC cc, Unidad destinatario) {
        // Preguntamos al personaje si quiere ser cceado
        // Al pasarle la referencia al objeto cc, puede modificar sus valores
        boolean afectar = onCrownControl(cc, destinatario);

        // Si el personaje no quiere ser afectado o su duración es igual o menos  0s, entonces
        // No se produce el efecto
        if (!afectar || cc.getDuracion()<=0){
            return;
        }

        // La tenacidad es la reducción del efecto a sufrir solo si es negativo..
        // Con 0% no cambia nada, con 50% reduce a la mitad la duración del efecto
        // Con -50% de tenacidad el efecto durará un 50% más.
        if (cc.getTipo().getTipo() == TipoCC.DEBUFF) {
            float tenacidad = 1-getEstadisticas().getAttr(AtribEnum.TENACIDAD);

            cc.efectoTenacidad(tenacidad);
        }

        // Añadimos el efecto a la unidad
        crownControl.add(cc);

        // Aplicamos el primer efecto del CC, si procede.
        cc.aplicarCC(this);

        Gdx.app.log("", "CC aplicado"); //TODO: Borrar log
    }

    public void aumentarKnockUp (Vector2 pos) {
        knockUp.add(pos);
    }
}
