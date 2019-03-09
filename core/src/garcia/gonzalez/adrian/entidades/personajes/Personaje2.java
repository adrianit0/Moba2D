package garcia.gonzalez.adrian.entidades.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.crownControl.CC;
import garcia.gonzalez.adrian.crownControl.DebuffCC;
import garcia.gonzalez.adrian.crownControl.KnockUp;
import garcia.gonzalez.adrian.crownControl.Recoil;
import garcia.gonzalez.adrian.crownControl.VelocidadCC;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Habilidad;
import garcia.gonzalez.adrian.enums.Enums.*;

/**
 *
 * Personaje 2: ProjectDAM
 *
 * Pasiva: Se cura un 3% de su vida máxima por cada unidad que mate (Un 15% si mata a otro personaje).
 *
 * Hab1: Prepara un ataque y arremete hacia delante con bastante potencia,
 *      inflingiendo 80 (+0.80) hasta 300 (+2.0) (Max 400 a esbirros y torres) según lo que hayas cargado.
 *      Además, quedará aturdido durante 1.5s.
 *
 * Hab2: Se mueve rapidamente hacia delante, mientras está en esta forma no podrá ser controlado.
 *
 * Hab3: Lanza una super explosión contra el suelo, apartando a todos los objetivos que hubiese dentro.
 *
 * */
public class Personaje2 extends Personaje {

    public enum MaquinaEstados {
        IDLE, WALKING, JUMPING, ATTACK_01, ATTACK_02, ATTACK_03, DEATH
    };

    private boolean jumping;
    private boolean doubleJump;

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
                new Habilidad(2, 0, Assets.instance.overlayAssets.character02_hab01),
                new Habilidad(1, 30, Assets.instance.overlayAssets.character02_hab02),
                new Habilidad(9, 100, Assets.instance.overlayAssets.character02_hab03),
                bando, x, y, level);

        getAtributos().setAttr(AtribEnum.SALUD, 750);
        getAtributos().setAttr(AtribEnum.MANA, 450);
        getAtributos().setAttr(AtribEnum.DEFENSA, 35);
        getAtributos().setAttr(AtribEnum.REG_SALUD, 12);
        getAtributos().setAttr(AtribEnum.REG_MANA, 6);
        getAtributos().setAttr(AtribEnum.ATAQUE, 110);
        getAtributos().setAttr(AtribEnum.SALTO, 310);
        getAtributos().setAttr(AtribEnum.VELOCIDAD, 140);

        estado = MaquinaEstados.IDLE;
        animation =false;
        animationTime=0;
        doubleJump=true;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate(float delta) {
        if (animation) {
            animationTime+=delta;
        }
        if (estado==MaquinaEstados.ATTACK_02) {
            final Vector2 distancia = new Vector2(500*delta*getDireccion().getDir(), 0);
            resetJump();
            movePosition(distancia);
        } else if (estado==MaquinaEstados.ATTACK_01 && animationFrame<7) {
            resetJump();
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
            // El personaje podrá moverse mientras carga, pero lo hará lentamente
            aplicarCC(new VelocidadCC("ralentizacionEfecto", -0.5f, 5f), this);
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
            // Eliminamos el efecto
            eliminarCC("ralentizacionEfecto");
            float fuerzaTotal = (float) animationFrame/7;
            animationTime = actualAnimation.getFrameDuration()*7;

            // Se mueve rapidamente hacia delante de la potencia del golpe
            aplicarCC(new Recoil("recoilHab1",
                    new Vector2(getDireccion().getDir()*(50+500*fuerzaTotal), 0),
                    0.25f), this);

            final Rectangle col = getCollider();
            final Vector2 rangoAtaque = Constants.CHARACTER_02_HAB1_RANGE;

            Rectangle areaDamage = new Rectangle(
                    getDireccion()==Direccion.DERECHA ? col.x+col.width/2 : col.x+col.width/2-rangoAtaque.x,
                    col.y, rangoAtaque.x, rangoAtaque.y
            );

            List<Entidad> enemigos = level.getCollisionEntities(areaDamage, getBando().getContrario());
            // Calcula el daño que recibiran los enemigos, dependerá de cuanto has cargado el golpe
            int damage = getBaseAndPorcentualValue(MathUtils.lerp(80, 300, fuerzaTotal), MathUtils.lerp(0.8f, 2.0f, fuerzaTotal));
            float potenciaGolpe = MathUtils.lerp(10, 800, fuerzaTotal);
            // Aplicamos el daño dentro del rango de ataque
            for (Entidad entidad : enemigos) {
                if (entidad.getTipoEntidad()==TipoEntidad.ESBIRRO ||
                        entidad.getTipoEntidad()==TipoEntidad.TORRE) {
                    entidad.recibirAtaque(Math.min(damage, 400), this);
                } else {
                    entidad.recibirAtaque(damage, this);
                }

                if (entidad.getTipoEntidad().esUnidad()) {
                    ((Unidad)entidad).aplicarCC(new KnockUp(
                            "Golpe KnockBack",
                            new Vector2(potenciaGolpe*getDireccion().getDir(),
                                    -potenciaGolpe),
                            0.5f),
                            this);
                    ((Unidad)entidad).aplicarCC(new DebuffCC(
                                    "Golpe aturdidor",
                                    CrowdControl.ATURDIMIENTO,
                                    1.5f),
                            this);
                }
            }

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
        animationTime=0;
    }

    private void finishAnimation () {
        animationFrame=-1;
        animation=false;
        animationTime=0;
    }

    private void onAnimationFrame (int frame) {
        if (animationFrame==frame)
            return;

        animationFrame=frame;

        if (estado==MaquinaEstados.ATTACK_03 && frame==13) {
            Rectangle col = getCollider();
            Vector2 rangoAtaque = Constants.CHARACTER_02_HAB3_RANGE;

            Rectangle areaEfecto = new Rectangle(
                    col.x+col.width/2-rangoAtaque.x/2, col.y,
                    rangoAtaque.x, rangoAtaque.y);

            List<Entidad> enemigos = level.getCollisionEntities(areaEfecto, getBando().getContrario());
            int damage = getBaseAndPorcentualValue(250, 1.0f);
            float potenciaGolpe = 800;
            // Aplicamos el daño dentro del rango de ataque
            for (Entidad entidad : enemigos) {
                entidad.recibirAtaque(damage, this);

                if (entidad.getTipoEntidad().esUnidad()) {
                    ((Unidad)entidad).aplicarCC(new KnockUp(
                                    "Golpe KnockBack",
                                    new Vector2(entidad.getPosition().x>getPosition().x ? potenciaGolpe : -potenciaGolpe,0),
                                    0.5f),
                            this);
                }
            }
        }
    }

    @Override
    public int onBeforeDefend(int damage, Entidad destinatario) {
        return super.onBeforeDefend(damage, destinatario);
    }

    @Override
    public boolean onCrownControl(CC cc, Unidad destinatario) {
        if (estado==MaquinaEstados.ATTACK_02 && cc.getTipo().getTipo()==TipoCC.DEBUFF)
            return false;
        return super.onCrownControl(cc, destinatario);
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

        return !animation || estado==MaquinaEstados.ATTACK_01;
    }

    @Override
    public boolean onJumpStart(EstadoSalto estadoSalto) {
        jumping=true;
        if (!animation)
            estado=MaquinaEstados.JUMPING;

        if (doubleJump && estadoSalto==EstadoSalto.SALTANDO) {
            doubleJump=false;
            return true;
        }


        return doubleJump;
    }

    @Override
    public void onJumpStay() {
        if (!animation)
            estado=MaquinaEstados.JUMPING;
    }

    @Override
    public void onJumpFinish() {
        jumping=false;
        doubleJump=true;
    }

    @Override
    public void onRender(SpriteBatch batch) {
        TextureRegion region;
        float animTime = animation ?  animationTime : MathUtils.nanoToSec * TimeUtils.nanoTime();
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
            onAnimationFrame(actualAnimation.getKeyFrameIndex(animationTime));
            if (actualAnimation.isAnimationFinished(animationTime)) {
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

        final Rectangle col = getCollider();
        final Vector2 rangoAtaque = Constants.CHARACTER_02_HAB1_RANGE;
        final Vector2 rangoAtaque2 = Constants.CHARACTER_02_HAB3_RANGE;

        shapeRenderer.setColor(Color.RED);
        /*shapeRenderer.rect(
                getDireccion()==Direccion.DERECHA ? col.x+col.width/2 : col.x+col.width/2-rangoAtaque.x,
                col.y, rangoAtaque.x, rangoAtaque.y
        );*/

        shapeRenderer.rect(
                col.x+col.width/2-rangoAtaque2.x/2, col.y,
                rangoAtaque2.x, rangoAtaque2.y
        );
    }

    @Override
    public int onAttack(int damage, Entidad objetivo) {
        return damage;
    }

    @Override
    public void onEntityKilled(Entidad objetivo) {
        super.onEntityKilled(objetivo);

        if (objetivo.getTipoEntidad()==TipoEntidad.PERSONAJE) {
            curarPersonaje(Math.round(getAtributos().getMaxAttr(AtribEnum.SALUD)*0.15f), this);
        } else {
            curarPersonaje(Math.round(getAtributos().getMaxAttr(AtribEnum.SALUD)*0.03f), this);
        }

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

