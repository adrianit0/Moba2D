package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;

public class Esbirro extends Unidad {
    public Esbirro(Bando bando, int x, int y) {
        super(bando, x, y);
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
    public float onCrownControl(CrowdControl cc, float time, Unidad destinatario) {
        return time;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void update(float delta) {

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
        Vector2 position = getPosition();

        batch.draw(
                region.getTexture(),
                position.x,
                position.y+5,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                1,
                1,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
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
}
