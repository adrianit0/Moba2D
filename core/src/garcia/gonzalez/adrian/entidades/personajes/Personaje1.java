package garcia.gonzalez.adrian.entidades.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Personaje;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Habilidad;

public class Personaje1 extends Personaje {

    public enum MaquinaEstados {
        IDLE, WALKING, JUMPING
    };

    private boolean jumping;

    // TODO: Mejorar el Width y height
    private int width;
    private int height;

    private MaquinaEstados estado;


    public Personaje1(Controlador controller, Bando bando, int x, int y, Level level) {
        super(controller,
                new Habilidad(1),
                new Habilidad(4),
                new Habilidad(12),
                bando, x, y, level);


        getAtributos().setAttr(AtribEnum.VELOCIDAD, 120);
        getAtributos().setAttr(AtribEnum.SALUD, 800);
        getAtributos().setAttr(AtribEnum.ATAQUE, 80);
        getAtributos().setAttr(AtribEnum.SALTO, 350);

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
    public boolean canCastHability(int hab) {
        return true;
    }

    @Override
    public boolean onHabilityDown(int hab) {
        Gdx.app.log("HAB "+hab, "Habilidad pulsada");
        return false;
    }

    @Override
    public boolean onHabilityStay(int hab, float delta) {
        Gdx.app.log("HAB "+hab, "Habilidad pulsando");
        return false;
    }

    @Override
    public boolean onHabilityUp(int hab) {
        Gdx.app.log("HAB "+hab, "Habilidad soltado");
        return false;
    }

    @Override
    public void onIdle(float delta) {
        if (!jumping)
            estado = MaquinaEstados.IDLE;
    }

    @Override
    public boolean onMove (Direccion dir, float delta) {
        if (!jumping)
            estado = MaquinaEstados.WALKING;
        return true;
    }

    @Override
    public boolean onJumpStart(EstadoSalto estadoSalto) {
        jumping=true;
        estado=MaquinaEstados.JUMPING;
        return true;
    }

    @Override
    public void onJumpFinish() {
        jumping=false;
    }

    @Override
    public void onRender(SpriteBatch batch) {
        TextureRegion region;
        float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - 0); // TODO: Mirar que es ese 0 final
        switch (estado) {
            case WALKING:
                region = Assets.instance.personaje1.running.getKeyFrame(walkTimeSeconds);
                break;

            case JUMPING:
                region = Assets.instance.personaje1.jumping.getKeyFrame(walkTimeSeconds);
                break;

            case IDLE:
            default:
                region = Assets.instance.personaje1.idle.getKeyFrame(walkTimeSeconds);
                break;
        }

        width=region.getRegionWidth();
        height=region.getRegionHeight();
        Vector2 position = getPosition();
        Vector2 offset = getOffset();

        batch.draw(
                region.getTexture(),
                position.x - offset.x,
                position.y - offset.y,
                0,
                0,
                width,
                height,
                2,
                2,
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
    public Rectangle getCollider() {
        Vector2 pos = getPosition();
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
    public Vector2 getOffset() {
        return new Vector2(width, 0);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(20, 48);
    }
}
