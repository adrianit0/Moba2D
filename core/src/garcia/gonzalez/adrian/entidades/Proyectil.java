package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Una clase proyectil, deberá ser implementado por algún hijo.
 * */
public abstract class Proyectil {

    private float x;
    private float y;
    private Entidad creador;    // Cada proyectil creado debe ser generador por una entidad.

    public Proyectil (Entidad creador, float x, float y){
        this.x = x;
        this.y = y;
        this.creador = creador;
    }

    public abstract void onCollisionEnter (Entidad entidad);
    public abstract void onCollisionStay  (Entidad entidad, float delta);
    public abstract void onCollisionExit  (Entidad entidad);

    public abstract void onUpdate(float delta);
    public abstract void onRender (SpriteBatch batch);

    public abstract boolean onCleanObject ();

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
