package garcia.gonzalez.adrian.entidades.estructuras;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.proyectiles.BolaTorre;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;

public class Torre extends Estructura {

    private int width;
    private int height;

    private Torre superior;

    private float recarga;

    private final float tiempoRecarga;
    private final Vector2 rangoAtaque;

    private float dissapearRatio;

    public Torre(Enums.Bando bando, int x, int y, Level level, Torre superior) {
        super(bando, x, y, Enums.TipoEntidad.TORRE, level);

        // Si tienes puesto enemigo su torre será mucho más poderosa
        // Pero la tuya sigue siendo igual de mala
        if (level.getDificultad()==Dificultad.DIFICIL && bando==Bando.ENEMIGO) {
            getAtributos().setAttr(AtribEnum.SALUD, 2500);
            getAtributos().setAttr(AtribEnum.DEFENSA, 60);
            getAtributos().setAttr(AtribEnum.REG_SALUD, 10);
            getAtributos().setAttr(AtribEnum.ATAQUE, 288);
        } else {
            getAtributos().setAttr(AtribEnum.SALUD, 1000);
            getAtributos().setAttr(AtribEnum.DEFENSA, 40);
            getAtributos().setAttr(AtribEnum.REG_SALUD, 5);
            getAtributos().setAttr(AtribEnum.ATAQUE, 155);
        }

        // Mientras que esta torre no esté destruida no se podrá destruir esta
        this.superior = superior;

        tiempoRecarga = Constants.TURRET_RELOAD_TIME;
        rangoAtaque = Constants.TURRET_RANGE;
        recarga=0;

        dissapearRatio=1;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onUpdate(float delta) {
        if(!estaVivo()) {
            dissapearRatio-=delta;
        }
    }

    @Override
    public void onTickUpdate(float tickDelta) {
        if (!estaVivo())
            return;

        recarga+=tickDelta;

        if (recarga > tiempoRecarga) {
            final Rectangle col = getCollider();
            final Rectangle dangerous = new Rectangle(
                    col.x+col.width/2-rangoAtaque.x/2,
                    0,
                    rangoAtaque.x, rangoAtaque.y);
            List<Entidad> encontrados = level.getCollisionEntities(dangerous , getBando().getContrario());

            if (encontrados.size()>0) {
                Entidad entidad = null;
                for (Entidad e : encontrados) {
                    if (e.getTipoEntidad()==TipoEntidad.ESBIRRO) {
                        entidad = e;
                        break;
                    }
                }
                if (entidad==null)
                    entidad = encontrados.get(0);

                recarga=0;

                final Rectangle coll = getCollider();
                final Vector2 posicionLanzamiento = new Vector2(
                        getBando()==Bando.ALIADO ? coll.x + 6 : coll.x + 18, col.y+col.height+24
                );
                generarProyectil(new BolaTorre(this, entidad, level, posicionLanzamiento));
            }
        }
    }

    @Override
    public void onRender(SpriteBatch batch) {
        final Vector2 pos = getPosition();

        Texture textura = getBando()== Enums.Bando.ALIADO ? Assets.instance.estructuraAssets.torreAliada : Assets.instance.estructuraAssets.torreEnemiga;

        width=textura.getWidth();
        height=textura.getWidth();
        Vector2 offset = getOffset();

        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, dissapearRatio);//set alpha to 0.3

        batch.draw(
                textura,
                pos.x-offset.x,
                pos.y-offset.y,
                textura.getWidth()/2,
                textura.getHeight()/2,
                0,
                0,
                textura.getWidth(),
                textura.getHeight(),
                getBando()==Bando.ENEMIGO,
                false);

        batch.setColor(c.r, c.g, c.b, 1f);
    }

    @Override
    public void onDebugRender(ShapeRenderer shapeRenderer) {
        super.onDebugRender(shapeRenderer);

        // COLLIDER
        shapeRenderer.setColor(Color.GREEN);
        Rectangle col = getCollider();
        Vector2 position = getPosition();
        shapeRenderer.rect(col.x, col.y, col.width, col.height);

        // POSICION DEL CRISTAL (Donde se lanza las habilidades)
        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.rect(
                getBando()==Bando.ALIADO ? col.x-2 : col.x+10,
                col.y+col.height+12,
                17, 24);

        // ZONA DE RIESGO

        final Rectangle dangerous = new Rectangle(
                col.x+col.width/2-rangoAtaque.x/2,
                0,
                rangoAtaque.x, rangoAtaque.y);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(dangerous.x, dangerous.y, dangerous.width, dangerous.height);

        // POSICION DEL PERSONAJE
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(position.x, position.y, 1);
    }

    @Override
    public final void onHudRender(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (!estaVivo() || superior!=null&&superior.estaVivo())
            return;

        float porc = (float) getAtributos().getSaludActual() / getAtributos().getMaxAttr(AtribEnum.SALUD);
        Vector2 pos = getPosition();
        final Vector2 size = Constants.HUD_STRUCTURE_LIFE_SIZE;

        // Cuadrado negro de atras
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(pos.x-size.x/2, getCollider().height+size.y+5+pos.y, size.x, size.y);
        // Vida
        shapeRenderer.setColor(getBando()==Bando.ALIADO ? Color.GREEN : Color.RED);
        shapeRenderer.rect(pos.x-size.x/2+1, getCollider().height+6+size.y+pos.y, size.x*porc-2, size.y-2);
    }

    @Override
    public int onAttack(int damage, Entidad objetivo) {
        return damage;
    }

    @Override
    public void onAfterAttact(Entidad objetivo) {

    }

    @Override
    public void onEntityKilled(Entidad objetivo) {
    }

    @Override
    public int onBeforeDefend(int damage, Entidad destinatario) {
        // Si la torre de enfrente sigue viva, entonces ignora el daño.
        if (superior!=null && superior.estaVivo())
            return 0;
        return damage;
    }

    @Override
    public void onAfterDefend(int receivedDamage, Entidad destinatario, boolean vivo) {

    }

    @Override
    public int onBeforeHeal(int cura, Entidad destinatario) {
        return cura;
    }

    @Override
    public void onAfterHeal(int saludCurada) {  }

    @Override
    public void onLevelUp() { }

    @Override
    public boolean onDeath() {
        return false;
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
        return new Vector2(width/4, 0);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(25, 100);
    }

    @Override
    public boolean canBeCleaned() {
        return dissapearRatio<=0;
    }
}
