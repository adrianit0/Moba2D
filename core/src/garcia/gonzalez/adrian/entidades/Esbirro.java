package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.crownControl.KnockUp;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Utils;

public class Esbirro extends Unidad {

    private enum MaquinaEstados {ANDAR, ATACAR};

    private int width;
    private int height;

    private long timeSinceStartAttack;
    private Unidad seleccionado;

    private MaquinaEstados estado;

    public Esbirro(Bando bando, int x, int y, Level level) {
        super(bando, x, y, level);

        width=64;
        height=64;

        getEstadisticas().setBonus(AtribEnum.VELOCIDAD, 50);
        estado=MaquinaEstados.ANDAR;
    }

    @Override
    public boolean onMove(Direccion dir) {
        return true;
    }

    @Override
    // Los minions por defecto no saltaran por voluntad propia
    public boolean onJumpStart(EstadoSalto estado) {
        return false;
    }

    @Override
    public void onJumpFinish() {}

    @Override
    public boolean onCrownControl(CC cc, Unidad destinatario) {
        return true;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate(float delta) {
        //TODO: cambiar esto

        if (estado==MaquinaEstados.ANDAR) {
            Bando bando = getBando();
            Entidad e = level.getEntidad(getCenter(), 24, bando.getContrario());
            if (e!=null) {
                estado=MaquinaEstados.ATACAR;
                seleccionado=(Unidad) e;
                timeSinceStartAttack = TimeUtils.nanoTime();
            } else {
                mover(bando==Bando.ALIADO ? Direccion.DERECHA : Direccion.IZQUIERDA, delta);
            }

        } else if (estado==MaquinaEstados.ATACAR) {
            if (getBando()==Bando.ENEMIGO)
                return;

            float elapsedTime = Utils.secondsSince(timeSinceStartAttack);
            boolean terminado = Assets.instance.blueMinionAssets.atacar.isAnimationFinished(elapsedTime);

            if (terminado) {
                estado=MaquinaEstados.ANDAR;
                //TODO: Cambiar todo esto
                seleccionado.aplicarCC(new KnockUp("knock-Up pruebas", CrowdControl.KNOCK_BACK, new Vector2(60,80), 0.5f), this);
                seleccionado=null;
            }
        }



        //TODO: SEGUIR
    }

    @Override
    public void onTickUpdate(float tickDelta) {

    }

    @Override
    public void onRender(SpriteBatch batch) {
        // TODO: Crear MECANIM
        //TextureRegion region = Assets.instance.blueMinionAssets.;
        // TODO: Hacer esto bien
        float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - 0);
        TextureRegion region;
        if (getBando()==Bando.ALIADO && estado==MaquinaEstados.ANDAR) {
            region = Assets.instance.blueMinionAssets.andar.getKeyFrame(walkTimeSeconds);
        } else if (getBando()==Bando.ALIADO && estado==MaquinaEstados.ATACAR) {
            region = Assets.instance.blueMinionAssets.atacar.getKeyFrame(Utils.secondsSince(timeSinceStartAttack));
        } else {
            region = Assets.instance.redMinionAssets.andar.getKeyFrame(walkTimeSeconds);
        }


        //TODO: Incluir OFFSET
        width=region.getRegionWidth();
        height=region.getRegionHeight();
        Vector2 position = getCenter();

        batch.draw(
                region.getTexture(),
                position.x,
                position.y,
                0,
                0,
                width,
                height,
                1,
                1,
                0,
                region.getRegionX(),
                region.getRegionY(),
                width,
                height,
                false,
                false);
    }

    @Override
    public int onBeforeDefend(int damage, Entidad destinatario) {
        return damage;
    }

    @Override
    public int onAttack(Entidad objetivo) {
        return getEstadisticas().getAttr(AtribEnum.ATAQUE);
    }

    @Override
    public void onAfterAttact(Entidad objetivo) {

    }

    @Override
    public void onAfterDefend(int receivedDamage, Entidad destinatario, boolean vivo) {

    }

    @Override
    public int onBeforeHeal(int cura, Entidad destinatario) {
        return cura;
    }

    @Override
    public void onAfterHeal(int saludCurada) {}

    @Override
    public boolean onDeath() {
        return true;
    }

    @Override
    public Rectangle getCollider() {
        Vector2 pos = getCenter();
        Vector2 size = getSize();
        final Rectangle rect = new Rectangle(
                pos.x,
                pos.y,
                size.x,
                size.y
        );
        return rect;
    }

    @Override
    public Vector2 getCenter() {
        Vector2 pos = getPosition();
        return new Vector2(pos.x-width/2, pos.y);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(22, 28);
    }
}
