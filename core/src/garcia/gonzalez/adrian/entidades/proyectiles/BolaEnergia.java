package garcia.gonzalez.adrian.entidades.proyectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.crownControl.KnockUp;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Unidad;

public class BolaEnergia extends Proyectil {

    public BolaEnergia(Entidad creador, Level level, float x, float y) {
        super(creador, level, x, y);
    }

    @Override
    protected Rectangle _getCollider() {
        return new Rectangle(10, 10, 10, 10);
    }

    @Override
    public void onCollisionEnter(Entidad entidad) {
        // TODO: Corregir todo esto
        Gdx.app.log("CollisionEnter", "Ha entrado");
        if (entidad instanceof Unidad) {
            Unidad u = (Unidad) entidad;
            u.aplicarCC(new KnockUp("Salto", new Vector2(100, 250),0.5f), (Unidad) getCreador());
        }
    }

    @Override
    public void onCollisionStay(Entidad entidad, float delta) {
        
    }

    @Override
    public void onCollisionExit(Entidad entidad) {

    }

    @Override
    public void onUpdate(float delta) {

    }

    @Override
    public void onRender(SpriteBatch batch) {

    }

    @Override
    public boolean hasFinished() {
        return false;
    }
}
