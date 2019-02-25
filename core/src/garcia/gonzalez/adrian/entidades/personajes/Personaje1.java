package garcia.gonzalez.adrian.entidades.personajes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Item;
import garcia.gonzalez.adrian.entidades.Personaje;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;

public class Personaje1 extends Personaje {

    public enum MaquinaEstados {
        IDLE, WALKING
    };

    private int width;
    private int height;

    private MaquinaEstados estado;


    public Personaje1(Controlador controller, Bando bando, int x, int y, Level level) {
        super(controller, bando, x, y, level);

        getAtributos().setAttr(AtribEnum.VELOCIDAD, 120);
        getAtributos().setAttr(AtribEnum.SALUD, 800);
        getAtributos().setAttr(AtribEnum.ATAQUE, 80);
        getAtributos().setAttr(AtribEnum.SALTO, 80);


        estado = MaquinaEstados.IDLE;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate(float delta) {

    }

    @Override
    public void onTickUpdate(float tickDelta) {

    }

    @Override
    public void onIdle(float delta) {
        estado = MaquinaEstados.IDLE;
    }

    @Override
    public boolean onMove (Direccion dir, float delta) {
        estado = MaquinaEstados.WALKING;
        return true;
    }

    @Override
    public void onRender(SpriteBatch batch) {
        TextureRegion region;
        float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - 0); // TODO: Mirar que es ese 0 final
        switch (estado) {
            case WALKING:
                region = Assets.instance.personaje1.running.getKeyFrame(walkTimeSeconds);
                break;

            case IDLE:
            default:
                region = Assets.instance.personaje1.idle.getKeyFrame(walkTimeSeconds);
                break;

        }


        width=region.getRegionWidth();
        height=region.getRegionHeight();
        Vector2 position = getPosition();

        batch.draw(
                region.getTexture(),
                position.x,
                position.y,
                0,
                0,
                width*2,
                height*2,
                1,
                1,
                0,
                region.getRegionX(),
                region.getRegionY(),
                width,
                height,
                getDireccion()==Direccion.IZQUIERDA,
                false);
    }

    @Override
    public int onAttack(Entidad objetivo) {
        return 0;
    }

    @Override
    public void onEntityKilled(Entidad objetivo) {

    }

    @Override
    public void onLevelUp() {

    }

    @Override
    public void onSpawn() {

    }

    @Override
    public Rectangle getCollider() {
        Vector2 pos = getCenter();
        Vector2 size = getSize();
        final Rectangle rect = new Rectangle(
                pos.x-size.x/2,
                pos.y,
                size.x,
                size.y
        );
        return rect;
    }

    @Override
    public Vector2 getCenter() {
        Vector2 pos = getPosition();
        return new Vector2(pos.x+width, pos.y);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(20, 48);
    }
}
