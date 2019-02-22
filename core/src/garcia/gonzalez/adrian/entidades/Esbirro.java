package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;

public class Esbirro extends Unidad {

    private enum MaquinaEstados {ANDAR, ATACAR};

    private int width;
    private int height;

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
    public void update(float delta) {
        //TODO: cambiar esto

        if (estado==MaquinaEstados.ANDAR) {
            Bando bando = getBando();
            mover(bando==Bando.ALIADO ? Direccion.DERECHA : Direccion.IZQUIERDA, delta);
            if (level.getEntidad(getCenter(), 32, bando.getContrario())!=null)
                estado=MaquinaEstados.ATACAR;
        }



        //TODO: SEGUIR
    }

    @Override
    public void tickUpdate(float tickDelta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO: Crear MECANIM
        //TextureRegion region = Assets.instance.blueMinionAssets.;
        // TODO: Hacer esto bien
        float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - 0);
        TextureRegion region;
        if (getBando()==Bando.ALIADO)
            region = (TextureRegion) Assets.instance.blueMinionAssets.andar.getKeyFrame(walkTimeSeconds);
        else
            region = (TextureRegion) Assets.instance.redMinionAssets.andar.getKeyFrame(walkTimeSeconds);

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
    public int onDamageTaken(int damage, Entidad destinatario) {
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
        final Rectangle rect = new Rectangle(
                pos.x,
                pos.y,
                width,
                height
        );
        return rect;
    }

    @Override
    public Vector2 getCenter() {
        Vector2 pos = getPosition();
        return new Vector2(pos.x-width, pos.y);
    }
}
