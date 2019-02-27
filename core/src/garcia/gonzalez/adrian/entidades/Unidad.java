package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.Gdx;
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

    //TODO: Meter los buffos
    private ArrayList<CC> crownControl;

    //TODO: Meter la gravityForce

    //TODO: Meter el estado salto
    private Direccion direccion;
    private EstadoSalto estadoSalto;

    private float gravityForce;    // Movimiento total del personaje
    private float salto;        // Potencia del salto
    private Vector2 knockUp;    // Movimiento involuntario, provocado por el enemigo

    private boolean moving;

    // TODO: Pruebas con Colisionadores, eliminar o comentar
    private ShapeRenderer shapeRenderer;
    static private boolean projectionMatrixSet;

    public Unidad(Bando bando, int x, int y, Level level) {
        super(bando, x, y, level);

        estadoSalto=EstadoSalto.EN_SUELO;
        crownControl = new ArrayList();

        gravityForce = 0;
        salto = 0;

        shapeRenderer = new ShapeRenderer(); // TODO: BORRAR
    }

    @Override
    public final void render (SpriteBatch sprite) {
        // Usamos el hijo
        super.render(sprite);

        //TODO: Probando las colisiones
        sprite.end();
        if(!projectionMatrixSet){
            shapeRenderer.setProjectionMatrix(sprite.getProjectionMatrix());
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // COLLIDER
        shapeRenderer.setColor(Color.GREEN);
        Rectangle col = getCollider();
        Vector2 offset = getPosition();
        Vector2 position = getPosition();
        shapeRenderer.rect(col.x, col.y, col.width, col.height);

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // OFFSET DEL PERSONAJE
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(offset.x, offset.y,1);

        // POSICION DEL PERSONAJE
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(position.x, position.y, 1);

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(0, 0, 1, 200);
        shapeRenderer.end();
        sprite.begin();
        //TODO: Borrar esto
    }

    // Cada vez que el personaje se mueva
    public abstract boolean onMove(Direccion dir, float delta);
    // Cada vez que el personaje esté quieto
    public abstract void onIdle (float delta);

    // Al empezar el salto
    public abstract boolean onJumpStart (EstadoSalto estado);
    // Al caer al suelo
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
        knockUp = new Vector2(0,0);
        gravityForce -= Constants.GRAVITY;

        // Aplicamos todos los CC
        for (int i = 0;i < crownControl.size(); i++) {
            CC c = crownControl.get(i);

            c.aplicandoCCUpdate(this, delta);
            if (c.hasFinished(this,System.currentTimeMillis())) {
                crownControl.remove(c);
                i--;
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

        if (salto>0) {
            estadoSalto = EstadoSalto.SALTANDO;
        }

        movePosition(new Vector2 (0, (salto+gravityForce) * delta)); //TODO: Mejorar la gravedad de los objetos

        if (y<5f) {
            // Si en el anterior frame estaba saltando
            if (estadoSalto == EstadoSalto.SALTANDO) {
                onJumpFinish();
                estadoSalto = EstadoSalto.EN_SUELO;
                salto = 0;
            }

            y = 5;
            gravityForce = 0;
        }

        //TODO: MEJORAR
        if (!moving) {
            onIdle(delta);
        }
        moving=false;
    }

    //TODO: Seguir
    /**
     * Mueve el personaje, este método no es heredable, usar en su lugar el método onMove().
     * */
    public final void mover(Direccion dir, float delta) {
        //TODO: No deja moverse si está CCeado por Aturdimiento o KnockUp
        // if cc return

        boolean mover = onMove(dir, delta);

        // Es probable que sea el propio personaje quien voluntariamente decida no moverse
        if (!mover) {
            return;
        }

        direccion = dir;

        // TODO: Seguir
        float velocidad = getAtributos().getAttr(AtribEnum.VELOCIDAD);

        velocidad *= delta * direccion.getDir();

        movePosition(new Vector2(velocidad, 0));
        moving=true;
    }

    /**
     * Salta el personaje, este método no es heredable, en su lugar utilizar el método onJumpStart()
     * */
    public final void saltar() {
        //TODO: No deja moverse si está CCeado por Aturdimiento, pesado o KnockUp
        // if cc return

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

        // TODO: SEGUIR
        gravityForce=0;
        salto = potenciaSalto;
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
        crownControl.add(cc);

        // Aplicamos el primer efecto del CC, si procede.
        cc.aplicarCC(this);
    }

    public final void aumentarKnockUp (Vector2 pos) {
        knockUp.add(pos);
    }

    public final Direccion getDireccion() {
        return direccion;
    }
}
