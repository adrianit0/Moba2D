package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.crownControl.KnockUp;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Utils;

public class Esbirro extends Unidad {

    private enum MaquinaEstados {SPAWN, ANDAR, ATACAR, MUERTO, DESAPARECIENDO};

    private int width;
    private int height;

    private long timeSinceMinionBorn;
    private long timeSinceStartAttack;

    // Variables para cuando los minions mueren
    private boolean muerto = false;
    private long timeSinceMinionStartDying; // Le vamos a dar unos milisegundos para que el minion pueda lanzar su ataque si es que lo tuviera en cola
    private long timeSinceMinionDeath;
    private long timeSinceMinionStartDissapear;
    private float dissapearRatio=1f;

    private Unidad seleccionado;

    private boolean attacked;

    // Los assets para los minions, debido a que hay diferentes según si es el equipo azul o rojo
    private Assets.MinionAssets assets;

    private MaquinaEstados estado;

    public Esbirro(Bando bando, int x, int y, Level level) {
        super(bando, x, y, level);

        width=64;
        height=64;

        // Le damos valores por defecto
        getEstadisticas().setAttr(AtribEnum.VELOCIDAD, 50);
        getEstadisticas().setAttr(AtribEnum.SALUD, 100);
        getEstadisticas().setAttr(AtribEnum.ATAQUE, 80);

        estado=MaquinaEstados.SPAWN;

        timeSinceMinionBorn = TimeUtils.nanoTime();

        assets = bando== Bando.ALIADO ? Assets.instance.blueMinionAssets : Assets.instance.redMinionAssets;
    }

    @Override
    public boolean onMove(Direccion dir) {
        return true;
    }

    @Override
    public void onIdle() { }

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


        switch (estado) {
            case SPAWN:
                // TODO: Refactorizar nombre
                float elapsedTime2 = Utils.secondsSince(timeSinceMinionBorn);
                boolean terminado2 = assets.spawn.isAnimationFinished(elapsedTime2);

                if (terminado2) {
                    estado=MaquinaEstados.ANDAR;
                }
                break;
            case ANDAR:
                Bando bando = getBando();
                Entidad e = level.getEntidad(getCenter(), 24, bando.getContrario());
                if (e!=null) {
                    estado=MaquinaEstados.ATACAR;
                    seleccionado=(Unidad) e;
                    timeSinceStartAttack = TimeUtils.nanoTime();
                    attacked=false;
                } else {
                    mover(bando==Bando.ALIADO ? Direccion.DERECHA : Direccion.IZQUIERDA, delta);
                }
                break;

            case ATACAR:
                float elapsedTime = Utils.secondsSince(timeSinceStartAttack);
                boolean terminado = assets.atacar.isAnimationFinished(elapsedTime);

                // TODO: Cambiar (Los minions haran daño, no lanzaran por los aires al enemigo)
                if (!attacked && elapsedTime> Constants.MINION_ATTACK_DURATION) {
                    // TODO: Solo aplicar el Knock-Up si ataca a otro minion, no a un personaje
                    seleccionado.aplicarCC(new KnockUp("knock-Up minion", CrowdControl.KNOCK_BACK, new Vector2(getBando()==Bando.ALIADO ? 20 : -20,150), 0.25f), this);
                    this.atacar(seleccionado);  // Atacamos al personaje
                    attacked=true;
                }

                if (terminado) {
                    estado=MaquinaEstados.ANDAR;
                    //TODO: Cambiar todo esto
                    seleccionado=null;
                }
                break;

            case MUERTO:
                boolean terminado3 = assets.muerte.isAnimationFinished(Utils.secondsSince(timeSinceMinionDeath));
                if (terminado3)
                    estado = MaquinaEstados.DESAPARECIENDO;

                break;
            case DESAPARECIENDO:
                dissapearRatio -= delta * Constants.MINION_DISSAPEAR_DURATION;
                break;
        }

        // Si ya ha muerto vamos a darle unos milisegunds para que pueda lanzar su último ataque (Si le da tiempo)
        if (muerto && (estado!=MaquinaEstados.MUERTO&&estado!=MaquinaEstados.DESAPARECIENDO) && Utils.secondsSince(timeSinceMinionStartDying)>Constants.MINION_LAST_WHISPERS) {
            estado=MaquinaEstados.MUERTO;
            timeSinceMinionDeath = TimeUtils.nanoTime();
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

        TextureRegion region;
        switch (estado) {
            case SPAWN:
                region = assets.spawn.getKeyFrame(Utils.secondsSince(timeSinceMinionBorn));
                break;
            case ATACAR:
                region =assets.atacar.getKeyFrame(Utils.secondsSince(timeSinceStartAttack));
                break;
            case MUERTO:
            case DESAPARECIENDO:
                region =assets.muerte.getKeyFrame(Utils.secondsSince(timeSinceMinionDeath));
                break;
            case ANDAR:
            default:
                float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - 0); // TODO: Mirar que es ese 0 final
                region = assets.andar.getKeyFrame(walkTimeSeconds);
                break;
        }


        //TODO: Incluir OFFSET
        width=region.getRegionWidth();
        height=region.getRegionHeight();
        Vector2 position = getPosition();

        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, dissapearRatio);//set alpha to 0.3

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

        // Devolvemos en color
        batch.setColor(c.r, c.g, c.b, 1f);//set alpha to 0.3
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
        // Si despues de defenderse se muere, entonces cambiamos el estado
        if (!vivo) {
            muerto = true;
            timeSinceMinionStartDying = TimeUtils.nanoTime();
        }
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
        return new Vector2(pos.x+width/2, pos.y);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(22, 28);
    }

    @Override
    /**
     * El minion se eliminará cuando haya desaparecido completamente de pantalla
     * */
    public boolean canBeCleaned() {
        return dissapearRatio<=0;
    }
}
