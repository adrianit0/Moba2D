package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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

    private ArrayList<CC> crowdControl;

    private Direccion direccion;
    private EstadoSalto estadoSalto;

    // Distintas fuerzas aplicadas sobre una unidad
    private float gravityForce;
    private float jumpForce;
    private Vector2 knockUpForce;

    private boolean moving;



    public Unidad(Bando bando, int x, int y, TipoEntidad tipoEntidad, Level level) {
        super(bando, x, y, tipoEntidad, level);

        estadoSalto=EstadoSalto.EN_SUELO;
        direccion=getBando()==Bando.ALIADO ? Direccion.DERECHA : Direccion.IZQUIERDA;
        crowdControl = new ArrayList();

        gravityForce = 0;
        jumpForce = 0;

    }

    @Override
    public final void render (SpriteBatch sprite) {
        super.render(sprite);
    }

    @Override
    public void onDebugRender(ShapeRenderer shapeRenderer) {
        super.onDebugRender(shapeRenderer);

        // COLLIDER
        shapeRenderer.setColor(Color.GREEN);
        Rectangle col = getCollider();
        Vector2 position = getPosition();
        shapeRenderer.rect(col.x, col.y, col.width, col.height);

        // POSICION DEL PERSONAJE
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(position.x, position.y, 1);
    }

    // Cada vez que el personaje se mueva
    public abstract boolean onMove(Direccion dir, float delta);
    // Cada vez que el personaje esté quieto
    public abstract void onIdle (float delta);

    // Al empezar el jumpForce
    public abstract boolean onJumpStart (EstadoSalto estado);
    public abstract void onJumpStay ();
    public abstract void onJumpFinish();
    // Al ser cceado, le pregunta al personaje si quiere ser afectado por su efecto.
    public abstract boolean onCrownControl (CC cc, Unidad destinatario);

    public final EstadoSalto getEstadoSalto() {
        return estadoSalto;
    }

    @Override
    /**
     * Activamos los CC, luego el update del super, y luego aplicamos el Knock-Up, si procede.
     * */
    public void update(float delta) {
        // Volvemos a poner el KnockUp a 0
        knockUpForce = new Vector2(0,0);
        gravityForce -= Constants.GRAVITY;

        // Aplicamos todos los CC
        for (int i = 0; i < crowdControl.size(); i++) {
            CC c = crowdControl.get(i);

            c.aplicandoCCUpdate(this, delta);
            if (c.hasFinished(this,System.currentTimeMillis())) {
                c.terminarCC(this);
                crowdControl.remove(c);
                i--;
            }
        }

        // Aplicamos el update de la clase padre
        super.update(delta);

        // Si el knock no es igual a 0
        if (!knockUpForce.equals(Vector2.Zero)) {
            movePosition(new Vector2(knockUpForce.x*delta, knockUpForce.y*delta));
        }

        if (jumpForce >0) {
            estadoSalto = EstadoSalto.SALTANDO;
        }

        movePosition(new Vector2 (0, (jumpForce + gravityForce) * delta));

        if (y<5f) {
            // Si en el anterior frame estaba saltando
            if (estadoSalto == EstadoSalto.SALTANDO) {
                onJumpFinish();
                estadoSalto = EstadoSalto.EN_SUELO;
                jumpForce = 0;
            }

            y = 5;
            gravityForce = 0;
        } else {
            if (estadoSalto==EstadoSalto.SALTANDO)
                onJumpStay();
        }

        if (estaVivo() && !moving) {
            onIdle(delta);
        }
        moving=false;
    }

    @Override
    public final void tickUpdate(float tickDelta) {
        for (CC c : crowdControl) {
            c.aplicandoCCTick(this, tickDelta);
        }

        super.tickUpdate(tickDelta);
    }

    public final void resetJump () {
        gravityForce = 0;
        jumpForce = 0;
    }

    public final boolean hasCrowdControl(CrowdControl... cc) {
        for (CC c : crowdControl) {
            for (CrowdControl t : cc) {
                if (c.getTipo()==t)
                    return true;
            }
        }
        return false;
    }

    /**
     * Mueve el personaje, este método no es heredable, usar en su lugar el método onMove().
     * */
    public final void mover(Direccion dir, float delta) {
        if (!estaVivo() || hasCrowdControl(CrowdControl.ATURDIMIENTO, CrowdControl.KNOCK_UP))
            return;

        boolean mover = onMove(dir, delta);

        // Es probable que sea el propio personaje quien voluntariamente decida no moverse
        if (!mover) {
            return;
        }

        direccion = dir;

        float velocidad = getAtributos().getAttr(AtribEnum.VELOCIDAD);

        velocidad *= delta * direccion.getDir();

        movePosition(new Vector2(velocidad, 0));
        moving=true;
    }

    /**
     * Salta el personaje, este método no es heredable, en su lugar utilizar el método onJumpStart()
     * */
    public final void saltar() {
        if (!estaVivo() || hasCrowdControl(CrowdControl.ATURDIMIENTO, CrowdControl.KNOCK_UP, CrowdControl.PESADO))
            return;

        // Activamos el onJumpStart, le pasamos si está o no en el suelo.
        // Será el personaje quien gestionará si quiere saltar (Incluso saltar en el aire)
        boolean saltar = onJumpStart(estadoSalto);

        // Es probable que sea el propio personaje quien voluntariamente decida no saltar
        if (!saltar) {
            return;
        }

        int potenciaSalto = getAtributos().getAttr(AtribEnum.SALTO);

        // No va a saltar si no tiene la potencia suficiente para ello
        if (potenciaSalto<=0) {
            return;
        }

        estadoSalto = EstadoSalto.SALTANDO;

        gravityForce=0;
        jumpForce = potenciaSalto;
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
            float tenacidad = 1- getAtributos().getAttr(AtribEnum.TENACIDAD);

            cc.efectoTenacidad(tenacidad);
        }

        // Añadimos el efecto a la unidad
        crowdControl.add(cc);

        // Aplicamos el primer efecto del CC, si procede.
        cc.aplicarCC(this);
    }

    /**
     * Elimina todos los CC que tengan el mismo nombre
     * */
    public final void eliminarCC (String nombreDescriptivo) {
        for (int i = 0; i < crowdControl.size(); i++) {
            CC cc = crowdControl.get(i);

            if (cc.getNombreIdentificativo().equals(nombreDescriptivo)) {
                cc.terminarCC(this);
                crowdControl.remove(i);
                i--;
            }
        }
    }

    /**
     * Se queda con la fuerza de knockback más grande que tenga
     * */
    public final void aumentarKnockUp (Vector2 pos) {
        knockUpForce.set(
                Math.abs(pos.x)> Math.abs(knockUpForce.x) ? pos.x : knockUpForce.x,
                Math.abs(pos.y)> Math.abs(knockUpForce.y) ? pos.y : knockUpForce.y);
    }

    public final Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
