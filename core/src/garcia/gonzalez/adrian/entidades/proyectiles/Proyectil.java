package garcia.gonzalez.adrian.entidades.proyectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Personaje;
import sun.awt.image.ImageWatched;

/**
 * Una clase proyectil, deberá ser implementado por algún hijo.
 * */
public abstract class Proyectil {

    private float x;
    private float y;
    private Entidad creador;    // Cada proyectil creado debe ser generador por una entidad.
    private Level level;

    private List<Entidad> lastFrameEntities;
    private List<Entidad> thisFrameEntities;

    // TODO: Pruebas con Colisionadores, eliminar o comentar
    private ShapeRenderer shapeRenderer;

    public Proyectil (Entidad creador, Level level, float x, float y){
        this.x = x;
        this.y = y;
        this.creador = creador;
        this.level = level;

        lastFrameEntities = new LinkedList<Entidad>();
        thisFrameEntities = new LinkedList<Entidad>();

        shapeRenderer = new ShapeRenderer(); // TODO: BORRAR
    }

    protected abstract Rectangle _getCollider ();

    public Rectangle getCollider () {
        Rectangle rect = _getCollider();
        rect.x += x;
        rect.y += y;
        return rect;
    }

    public abstract void onCollisionEnter (Entidad entidad);
    public abstract void onCollisionStay  (Entidad entidad, float delta);
    public abstract void onCollisionExit  (Entidad entidad);

    public abstract void onUpdate(float delta);
    public abstract void onRender (SpriteBatch batch);

    public abstract boolean hasFinished();

    public final void update (float delta) {
        thisFrameEntities = level.getCollisionEntities(getCollider());
        // Cogemos todas las entidades colisionadas y diferencias si es la primera vez que
        // colisiona o si ya sigue colisionando (Comparando con la lista del frame anterior)
        for (Entidad e : thisFrameEntities) {
            if (!lastFrameEntities.contains(e)) {
                onCollisionEnter(e);
            } else {
                onCollisionStay(e, delta);
                lastFrameEntities.remove(e); // Eliminamos la referencia
            }
        }

        // Ya que está eliminado la referencia de los que siguen colisionando solo quedan los
        // que se han salido
        for (Entidad e : lastFrameEntities) {
            onCollisionExit(e);
        }

        onUpdate(delta);

        lastFrameEntities = thisFrameEntities;
    }

    public final void render (SpriteBatch batch) {
        onRender(batch);

        //TODO: Probando las colisiones
        batch.end();       // Dejamos de usar el SpriteBatch
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // COLLIDER
        shapeRenderer.setColor(Color.BLUE);
        Rectangle col = getCollider();
        shapeRenderer.rect(col.x, col.y, col.width, col.height);

        shapeRenderer.end();
        batch.begin(); // Volvemos a usar el SpriteBatch
    }

    public final void nextframe () {
        lastFrameEntities = thisFrameEntities;
    }

    public void setPosition (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition (Vector2 position) {
        x = position.x;
        y = position.y;
    }

    public Vector2 getPosition() {
        return new Vector2(x,y);
    }

    public Entidad getCreador() {
        return creador;
    }
}
