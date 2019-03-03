package garcia.gonzalez.adrian.entidades.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import org.w3c.dom.css.Rect;

import java.util.List;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.crownControl.DamageOverTime;
import garcia.gonzalez.adrian.crownControl.HealOverTime;
import garcia.gonzalez.adrian.crownControl.KnockUp;
import garcia.gonzalez.adrian.crownControl.VelocidadCC;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Personaje;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Habilidad;
import garcia.gonzalez.adrian.utiles.Utils;

/**
 *
 * Personaje 1 (Nombre aún por poner)
 *
 * Pasiva: Por cada 100 unidades que se mueva genera 1 bola de energía (Max 5). Estas bolas de
 *      energía al explotar contra un enemigo provoca 30 (+0.40) y
 *      a los enemigos cercanos 10 (+0.40) de daño.
 *
 * Hab1: Dispara infligiendo 50 (+0.80). Además, si aciertas y tienes bolas de energía, lanzas una
 *      al objetivo.
 *
 * Hab2: Se cura así mismo 100 (+1.50) durante los siguientes 5 segundos, genera 2 bolas de energías
 *      y lanza las que tuviera a los enemigos cercanos si los hubiera.
 *
 * Hab3: Lanza 3 ataques consecutivos infligiendo 100 (+1.10) por ataque,
 *      y por cada objetivo acertado obtienes una bola de poder.
 *
 * */
public class Personaje1 extends Personaje {

    public enum MaquinaEstados {
        IDLE, WALKING, JUMPING, ATTACK_01, ATTACK_02, ATTACK_03, DEATH
    };

    private boolean jumping;

    // TODO: Mejorar el Width y height
    private int width;
    private int height;

    private MaquinaEstados estado;
    private boolean animation;
    private long animationTime;
    private int animationFrame;
    private Animation<TextureRegion> actualAnimation;

    // Pasiva
    private float distFrameAnterior;
    private float distanciaTotal;

    public Personaje1(Controlador controller, Bando bando, int x, int y, Level level) {
        super(controller,
                new Habilidad(1),
                new Habilidad(4),
                new Habilidad(12),
                bando, x, y, level);

        getAtributos().setAttr(AtribEnum.SALUD, 800);
        getAtributos().setAttr(AtribEnum.REG_SALUD, 5);
        getAtributos().setAttr(AtribEnum.ATAQUE, 80);
        getAtributos().setAttr(AtribEnum.SALTO, 350);
        getAtributos().setAttr(AtribEnum.VELOCIDAD, 120);

        estado = MaquinaEstados.IDLE;
        animation =false;
        animationTime=0;

        distFrameAnterior=0;
        distanciaTotal=0;
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
        startAnimation(hab);
        return true;
    }

    @Override
    public boolean onHabilityStay(int hab, float delta) {
        //Gdx.app.log("HAB "+hab, "Habilidad pulsando");
        return false;
    }

    @Override
    public boolean onHabilityUp(int hab) {
        //Gdx.app.log("HAB "+hab, "Habilidad soltado");
        return false;
    }

    private void startAnimation (int estado) {
        switch (estado) {
            case 1:
                this.estado=MaquinaEstados.ATTACK_01;
                actualAnimation = Assets.instance.personaje1.attack01;
                break;
            case 2:
                this.estado=MaquinaEstados.ATTACK_02;
                actualAnimation = Assets.instance.personaje1.attack02;
                break;
            case 3:
                this.estado=MaquinaEstados.ATTACK_03;
                actualAnimation = Assets.instance.personaje1.attack03;
                break;
            case 4:
                this.estado = MaquinaEstados.DEATH;
                actualAnimation = Assets.instance.personaje1.death;
                break;
        }

        animation=true;
        animationTime=TimeUtils.nanoTime();
    }

    private void finishAnimation () {
        animationFrame=-1;
        animation=false;
    }

    private void onAnimationFrame (int frame) {
        if (animationFrame==frame)
            return;

        animationFrame=frame;

        //TODO: Ajustar los frames
        if (estado==MaquinaEstados.ATTACK_01 && frame==1) {
            if (getEstadoSalto()==EstadoSalto.SALTANDO) {
                aplicarCC(new KnockUp("recoil_aereo_pers1", new Vector2(20*-getDireccion().getDir(),0), 0.25f), this);
            }
            Rectangle col = getCollider();
            final Rectangle rect = new Rectangle(
                    getDireccion()==Direccion.DERECHA ? col.x+col.width :  col.x - Constants.RANGE_HABILITY_1,
                    col.height/4+getPosition().y, Constants.RANGE_HABILITY_1,
                    col.height/2);


            List<Entidad> entidades = level.getCollisionEntities(rect);
            for (Entidad e : entidades) {
                this.atacar(getHabilityDamage(50, 0.8f), e);
            }
        } else if (estado==MaquinaEstados.ATTACK_02 && frame==6) {
            // TODO: Reajustarlo
            aplicarCC(new DamageOverTime("daño", this, getHabilityDamage(100, 1.5f), 5), this);
            aplicarCC(new VelocidadCC("velocidad", 0.50f, 5),this);
        }
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

        if (distFrameAnterior!=0) {
            float pos = getPosition().x;
            distanciaTotal += Math.abs(pos-distFrameAnterior);
            distFrameAnterior = pos;

            if (distanciaTotal>500) {
                distanciaTotal-=500;    // TODO: Añadirlo a una constante
                Gdx.app.log("PASIVA", "Ha generado una bola");  // TODO: Borrar log
                // TODO: Seguir
            }
        } else {
            distFrameAnterior=getPosition().x;
        }

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
    public void onJumpFinish() {
        jumping=false;
    }

    @Override
    public void onRender(SpriteBatch batch) {
        TextureRegion region;
        float animTime = animation ?  MathUtils.nanoToSec * (TimeUtils.nanoTime()-animationTime) : MathUtils.nanoToSec * TimeUtils.nanoTime();
        switch (estado) {
            case WALKING:
                region = Assets.instance.personaje1.running.getKeyFrame(animTime);
                break;

            case JUMPING:
                region = Assets.instance.personaje1.jumping.getKeyFrame(animTime);
                break;

            case ATTACK_01:
            case ATTACK_02:
            case ATTACK_03:
            case DEATH:
                region = actualAnimation.getKeyFrame(animTime);
                break;

            case IDLE:
            default:
                region = Assets.instance.personaje1.idle.getKeyFrame(animTime);
                break;
        }

        if (animation) {
            onAnimationFrame(actualAnimation.getKeyFrameIndex(Utils.secondsSince(animationTime)));
            if (actualAnimation.isAnimationFinished(Utils.secondsSince(animationTime)))
                finishAnimation();
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
    public int onAttack(int damage, Entidad objetivo) {
        return damage;
    }

    @Override
    public boolean onDeath() {
        startAnimation(4);

        Gdx.app.log("Muerto", "Estas muerto"); //TODO: Eliminar LOG
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
        return new Vector2(width, 0);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(20, 48);
    }
}
