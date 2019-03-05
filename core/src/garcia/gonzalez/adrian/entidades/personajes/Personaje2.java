package garcia.gonzalez.adrian.entidades.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Habilidad;
import garcia.gonzalez.adrian.utiles.Utils;
import garcia.gonzalez.adrian.enums.Enums.*;

/**
 *
 * Personaje 2: ProjectDAM
 *
 * Pasiva: Se cura un 1% de su vida máxima por cada unidad que derrote.
 *      Este valor aumenta un 1% por cada 1% de vida que le falte a Project DAM
 *
 * Hab1: Prepara un ataque y arremete hacia delante con bastante potencia,
 *      inflingiendo 50 (+0.60) hasta 200 (+1.0) según lo que hayas cargado.
 *
 * Hab2: Se mueve rapidamente hacia delante, mientras está en esta forma no podrá recibir daño
 *      de ninguna fuente.
 *
 * Hab3: Lanza una super explosión contra el suelo, apartando a todos los objetivos que hubiese dentro.
 *
 * */
public class Personaje2 extends Personaje {

    public enum MaquinaEstados {
        IDLE, WALKING, JUMPING, ATTACK_01, ATTACK_02, ATTACK_03, DEATH
    };

    private boolean jumping;

    // TODO: Mejorar el Width y height
    private int width;
    private int height;

    // Animaciones
    private MaquinaEstados estado;
    private boolean animation;
    private float animationTime;
    private int animationFrame;
    private Animation<TextureRegion> actualAnimation;



    public Personaje2(Controlador controller, Bando bando, int x, int y, Level level) {
        super(controller,
                new Habilidad(2, 20, Assets.instance.overlayAssets.character02_hab01),
                new Habilidad(1, 30, Assets.instance.overlayAssets.character02_hab02),
                new Habilidad(10, 120, Assets.instance.overlayAssets.character02_hab03),
                bando, x, y, level);

        getAtributos().setAttr(AtribEnum.SALUD, 850);
        getAtributos().setAttr(AtribEnum.MANA, 350);
        getAtributos().setAttr(AtribEnum.DEFENSA, 35);
        getAtributos().setAttr(AtribEnum.REG_SALUD, 8);
        getAtributos().setAttr(AtribEnum.REG_MANA, 4);
        getAtributos().setAttr(AtribEnum.ATAQUE, 90);
        getAtributos().setAttr(AtribEnum.SALTO, 380);
        getAtributos().setAttr(AtribEnum.VELOCIDAD, 140);

        estado = MaquinaEstados.IDLE;
        animation =false;
        animationTime=0;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate(float delta) {
        if (estado==MaquinaEstados.ATTACK_02) {
            final Vector2 distancia = new Vector2(500*delta*getDireccion().getDir(), 0);    // TODO: Convertir velocidad en constante
            resetJump();
            movePosition(distancia);
        }
    }

    @Override
    public void onTickUpdate(float tickDelta) {

    }

    @Override
    public boolean canCastHability(int hab) {
        return !animation;
    }

    @Override
    public boolean onHabilityDown(int hab) {
        startAnimation(hab);
        if (hab==1) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onHabilityStay(int hab, float delta) {
        if (hab==1 && animationFrame==7) {
            onHabilityUp(hab);
            return true;
        }
        return false;
    }

    @Override
    public boolean onHabilityUp(int hab) {
        if (hab==1) {
            float fuerzaTotal = (float) animationFrame/7; //TODO: Hace esto

            return true;
        }
        return false;
    }

    private void startAnimation (int estado) {
        switch (estado) {
            case 1:
                this.estado=MaquinaEstados.ATTACK_01;
                actualAnimation = Assets.instance.personaje2.attack01;
                break;
            case 2:
                this.estado=MaquinaEstados.ATTACK_02;
                actualAnimation = Assets.instance.personaje2.attack02;
                break;
            case 3:
                this.estado=MaquinaEstados.ATTACK_03;
                actualAnimation = Assets.instance.personaje2.attack03;
                break;
            case 4:
                this.estado = MaquinaEstados.DEATH;
                actualAnimation = Assets.instance.personaje2.death;
                break;
        }

        animation=true;
        animationTime=TimeUtils.nanoTime()*MathUtils.nanoToSec;
    }

    private void finishAnimation () {
        animationFrame=-1;
        animation=false;
    }

    private void onAnimationFrame (int frame) {
        if (animationFrame==frame)
            return;

        animationFrame=frame;

        if (estado==MaquinaEstados.ATTACK_01 && frame==1) {

        } else if (estado==MaquinaEstados.ATTACK_03 && (frame==3 || frame==6 || frame==9 || frame==12)) {

        }
    }

    @Override
    public int onBeforeDefend(int damage, Entidad destinatario) {
        if (estado==MaquinaEstados.ATTACK_02)
            return 0;

        return super.onBeforeDefend(damage, destinatario);
    }

    @Override
    public void onIdle(float delta) {
        if (!jumping && !animation)
            estado = MaquinaEstados.IDLE;
    }

    @Override
    public boolean onMove (Direccion dir, float delta) {
        if (!jumping && !animation)
            estado = MaquinaEstados.WALKING;

        return !animation;
    }

    @Override
    public boolean onJumpStart(EstadoSalto estadoSalto) {
        jumping=true;
        if (!animation)
            estado=MaquinaEstados.JUMPING;
        return super.onJumpStart(estadoSalto);
    }

    @Override
    public void onJumpStay() {
        if (!animation)
            estado=MaquinaEstados.JUMPING;
    }

    @Override
    public void onJumpFinish() {
        jumping=false;
    }

    @Override
    public void onRender(SpriteBatch batch) {
        TextureRegion region;
        float animTime = animation ?  MathUtils.nanoToSec*TimeUtils.nanoTime()-animationTime : MathUtils.nanoToSec * TimeUtils.nanoTime();
        switch (estado) {
            case WALKING:
                region = Assets.instance.personaje2.running.getKeyFrame(animTime);
                break;

            case JUMPING:
                region = Assets.instance.personaje2.jumping.getKeyFrame(animTime);
                break;

            case ATTACK_01:
            case ATTACK_02:
            case ATTACK_03:
            case DEATH:
                region = actualAnimation.getKeyFrame(animTime);
                break;

            case IDLE:
            default:
                region = Assets.instance.personaje2.idle.getKeyFrame(animTime);
                break;
        }

        if (animation) {
            onAnimationFrame(actualAnimation.getKeyFrameIndex(Utils.secondsSince(animationTime)));
            if (actualAnimation.isAnimationFinished(Utils.secondsSince(animationTime))) {
                if (estado==MaquinaEstados.ATTACK_02 && getEstadoSalto()==EstadoSalto.EN_SUELO) {
                    setDireccion(getDireccion().getContrario());
                }
                finishAnimation();
            }

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
                1.5f,
                1.5f,
                0,
                region.getRegionX(),
                region.getRegionY(),
                width,
                height,
                getDireccion()==Direccion.IZQUIERDA,
                false);
    }

    @Override
    public void onDebugRender(ShapeRenderer shapeRenderer) {
        super.onDebugRender(shapeRenderer);


    }

    @Override
    public int onAttack(int damage, Entidad objetivo) {
        return damage;
    }

    @Override
    public boolean onDeath() {
        startAnimation(4);
        return super.onDeath();
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
        return new Vector2(width*0.75f, 0);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(18, 48);
    }
}

