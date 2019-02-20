package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;

public class Esbirro extends Unidad {
    public Esbirro(Bando bando) {
        super(bando);
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
