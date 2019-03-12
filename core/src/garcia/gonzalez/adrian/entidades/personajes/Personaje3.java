package garcia.gonzalez.adrian.entidades.personajes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Habilidad;

public class Personaje3 extends Personaje {
    public Personaje3(Controlador controller, Enums.Bando bando, int x, int y, Level level) {
        super(controller,
                new Habilidad(1, 0, Assets.instance.overlayAssets.character02_hab01),
                new Habilidad(1, 0, Assets.instance.overlayAssets.character02_hab01),
                new Habilidad(1, 0, Assets.instance.overlayAssets.character02_hab01),
                bando, x, y, level);
    }

    @Override
    public boolean canCastHability(int hab) {
        return false;
    }

    @Override
    public boolean onHabilityDown(int hab) {
        return false;
    }

    @Override
    public boolean onHabilityStay(int hab, float delta) {
        return false;
    }

    @Override
    public boolean onHabilityUp(int hab) {
        return false;
    }

    @Override
    public void onUpdate(float delta) {

    }

    @Override
    public void onTickUpdate(float tickDelta) {

    }

    @Override
    public void onRender(SpriteBatch batch) {

    }

    @Override
    public Rectangle getCollider() {
        return null;
    }

    @Override
    public Vector2 getOffset() {
        return null;
    }

    @Override
    public Vector2 getSize() {
        return null;
    }
}
