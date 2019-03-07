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
import garcia.gonzalez.adrian.crownControl.HealOverTime;
import garcia.gonzalez.adrian.crownControl.KnockUp;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.particulas.Particula;
import garcia.gonzalez.adrian.entidades.proyectiles.BolaEnergia;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Habilidad;
import garcia.gonzalez.adrian.utiles.Utils;

/**
 *
 * Personaje 1 (Nombre aún por poner)
 *
 * Pasiva: Por cada 300 unidades que se mueva genera 1 bola de energía (Max 5). Estas bolas de
 *      energía al explotar contra un enemigo provoca 30 (+0.40) y
 *      a los enemigos cercanos 10 (+0.40) de daño.
 *
 * Hab1: Dispara infligiendo 50 (+0.80). Además, si aciertas y tienes bolas de energía, lanzas una
 *      al objetivo.
 *
 * Hab2: Se cura así mismo 50 (+1.50) durante los siguientes 5 segundos, genera 2 bolas de energías
 *      y lanza las que tuviera a los enemigos cercanos si los hubiera.
 *
 * Hab3: Lanza 4 ataques consecutivos infligiendo 100 (+1.10) por ataque,
 *      y por cada objetivo acertado obtienes una bola de poder y lanzas otra.
 *
 * */
public class Personaje1 extends Personaje {

    public enum MaquinaEstados {
        IDLE, WALKING, JUMPING, ATTACK_01, ATTACK_02, ATTACK_03, DEATH
    };

    private boolean jumping;

    private int width;
    private int height;

    // Animaciones
    private MaquinaEstados estado;
    private boolean animation;
    private long animationTime;
    private int animationFrame;
    private Animation<TextureRegion> actualAnimation;

    // Pasiva
    private float distFrameAnterior;
    private float distanciaTotal;
    private BolaEnergia[] bolasPasiva;

    // Todas las posiciones en las que puede estar las bolas
    // que orbitan sobre el personaje
    private final Vector2[] ballPosition = {
            new Vector2(-30,25),
            new Vector2(15, 40),
            new Vector2(-15, 40),
            new Vector2(30, 25),
            new Vector2(0, 50)
    };

    public Personaje1(Controlador controller, Bando bando, int x, int y, Level level) {
        super(controller,
                new Habilidad(1, 20, Assets.instance.overlayAssets.character01_hab01),
                new Habilidad(4, 50, Assets.instance.overlayAssets.character01_hab02),
                new Habilidad(12, 100, Assets.instance.overlayAssets.character01_hab03),
                bando, x, y, level);

        getAtributos().setAttr(AtribEnum.SALUD, 900);
        getAtributos().setAttr(AtribEnum.MANA, 400);
        getAtributos().setAttr(AtribEnum.DEFENSA, 25);
        getAtributos().setAttr(AtribEnum.REG_SALUD, 5);
        getAtributos().setAttr(AtribEnum.REG_MANA, 8);
        getAtributos().setAttr(AtribEnum.ATAQUE, 80);
        getAtributos().setAttr(AtribEnum.SALTO, 350);
        getAtributos().setAttr(AtribEnum.VELOCIDAD, 120);

        estado = MaquinaEstados.IDLE;
        animation =false;
        animationTime=0;

        distFrameAnterior=0;
        distanciaTotal=0;
        bolasPasiva = new BolaEnergia[Constants.CHARACTER_01_MAX_BALLS];
    }

    private void addBall () {
        if (isBallFull())
            return;

        Vector2 pos = getCenter();
        BolaEnergia bola = (BolaEnergia) generarProyectil(new BolaEnergia(this, level, pos.x, pos.y));

        for (int i = 0; i < bolasPasiva.length; i++){
            if(bolasPasiva[i]==null) {
                bolasPasiva[i]=bola;
                return;
            }
        }
    }

    private boolean isBallFull () {
        for (BolaEnergia bola : bolasPasiva) {
            if (bola==null)
                return false;
        }
        return true;
    }

    private boolean hasBall () {
        for (BolaEnergia bola : bolasPasiva) {
            if (bola!=null)
                return true;
        }
        return false;
    }

    private BolaEnergia nextBall () {
        BolaEnergia bola = null;
        for (int i = bolasPasiva.length-1; i >= 0 && bola==null; i--){
            if(bolasPasiva[i]!=null) {
                bola = bolasPasiva[i];
                bolasPasiva[i]=null;
            }
        }
        return bola;
    }

    public void throwBall (Entidad objetivo) {
        if (!hasBall())
            return;

        BolaEnergia bola = nextBall();

        bola.soltar(objetivo.getCenter());
    }

    public void removeBalls () {
        BolaEnergia energia = null;
        while ((energia=nextBall())!=null) {
            energia.eliminar();
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onUpdate(float delta) {
        for (int i = 0; i < bolasPasiva.length; i++) {
            BolaEnergia bola = bolasPasiva[i];
            if(bola==null)
                continue;

            Vector2 posFinal = new Vector2(ballPosition[i].x + getPosition().x, ballPosition[i].y + getPosition().y + (float)Math.sin(TimeUtils.nanoTime()/100000000)*0.5f);
            float distancia = posFinal.dst(bola.getPosition());
            bola.setPosition(Utils.moveTowards(bola.getPosition(), posFinal, distancia/10));
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

        if (estado==MaquinaEstados.ATTACK_01 && frame==1) {
            if (getEstadoSalto()==EstadoSalto.SALTANDO) {
                aplicarCC(new KnockUp("recoil_aereo_pers1", new Vector2(20*-getDireccion().getDir(),0), 0.25f), this);
            }
            Rectangle col = getCollider();
            final Rectangle rect = new Rectangle(
                    getDireccion()==Direccion.DERECHA ? col.x+col.width :  col.x - Constants.CHARACTER_01_RANGE_HABILITY_1,
                    col.height/4+getPosition().y, Constants.CHARACTER_01_RANGE_HABILITY_1,
                    col.height/2);


            Entidad entity = level.getNearestEntity(this, rect);
            if (entity!=null) {
                entity.recibirAtaque(getBaseAndPorcentualValue(50, 0.8f), this);
                generarParticula(new Particula(entity.getCenter(), Assets.instance.efectosPersonaje1.hit, new Vector2(32,32)));
                throwBall(entity);
            }

        } else if (estado==MaquinaEstados.ATTACK_02 && frame==6) {
            aplicarCC(new HealOverTime("cura", getBaseAndPorcentualValue(50, 0.75f), 2), this);
            Entidad entidad = level.getEntidad(getPosition(), 200, getBando().getContrario());

            if (entidad!=null) {
                while (hasBall())
                    throwBall(entidad);
            }

            addBall();
            addBall();
        } else if (estado==MaquinaEstados.ATTACK_03 && (frame==3 || frame==6 || frame==9 || frame==12)) {
            Rectangle col = getCollider();
            final Rectangle rect = new Rectangle(
                    getDireccion()==Direccion.DERECHA ? col.x+col.width :  col.x - Constants.CHARACTER_01_RANGE_HABILITY_1,
                    col.height/4+getPosition().y, Constants.CHARACTER_01_RANGE_HABILITY_1,
                    col.height/2);


            Entidad entity = level.getNearestEntity(this, rect);
            if (entity!=null) {
                entity.recibirAtaque(getBaseAndPorcentualValue(100, 1.5f), this);
                generarParticula(new Particula(entity.getCenter(), Assets.instance.efectosPersonaje1.hit, new Vector2(32,32)));
                addBall();
                throwBall(entity);
            }
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

            if (distanciaTotal>Constants.CHARACTER_01_PASSIVE_MAX_DISTANCE) {
                distanciaTotal-=Constants.CHARACTER_01_PASSIVE_MAX_DISTANCE;
                addBall(); // Añade una bola de energía por su pasiva
            }
        } else {
            distFrameAnterior=getPosition().x;
        }

        if (estado == MaquinaEstados.ATTACK_03) {
            setDireccion(dir);
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
    public void onDebugRender(ShapeRenderer shapeRenderer) {
        super.onDebugRender(shapeRenderer);

        Rectangle col = getCollider();
        Vector2 position = getPosition();

        Rectangle ataq = null;
        final float distancia = 150f;
        if (getDireccion()==Direccion.DERECHA) {
            ataq = new Rectangle((col.x+col.width), col.height/4+position.y, distancia, col.height/2);
        } else{
            ataq = new Rectangle(col.x-distancia, col.height/4+position.y, distancia, col.height/2);
        }
        shapeRenderer.rect(ataq.x, ataq.y, ataq.width, ataq.height);

    }

    @Override
    public int onAttack(int damage, Entidad objetivo) {
        return damage;
    }

    @Override
    public boolean onDeath() {
        startAnimation(4);
        removeBalls();  // Eliminamos las bolas que tuviera cargada
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
