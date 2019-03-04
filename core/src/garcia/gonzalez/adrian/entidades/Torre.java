package garcia.gonzalez.adrian.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Assets;
import garcia.gonzalez.adrian.utiles.Constants;

public class Torre extends Estructura {

    private int width;
    private int height;

    private Torre superior;

    public Torre(Enums.Bando bando, int x, int y, Level level, Torre superior) {
        super(bando, x, y, Enums.TipoEntidad.TORRE, level);

        getAtributos().setAttr(AtribEnum.SALUD, 5000);
        getAtributos().setAttr(AtribEnum.DEFENSA, 40);
        getAtributos().setAttr(AtribEnum.REG_SALUD, 5);
        getAtributos().setAttr(AtribEnum.ATAQUE, 155);
        // TODO: ATTACK SPEED = 0.833
        // TODO: 30% por cada ataque consecutivo, hasta un máximo de 120%
        // TODO: Hace siempre un 45% del daño a los minions

        // Mientras que esta torre no esté destruida no se podrá destruir esta
        this.superior = superior;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onUpdate(float delta) {

    }

    @Override
    public void onTickUpdate(float tickDelta) {

    }

    @Override
    public void onRender(SpriteBatch batch) {
        final Vector2 pos = getPosition();

        Texture textura = getBando()== Enums.Bando.ALIADO ? Assets.instance.estructuraAssets.torreAliada : Assets.instance.estructuraAssets.torreEnemiga;

        width=textura.getWidth();
        height=textura.getWidth();
        Vector2 position = getPosition();
        Vector2 offset = getOffset();

        batch.draw(textura, pos.x-offset.x, pos.y-offset.y,textura.getWidth()/2,textura.getHeight()/2);

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
        shapeRenderer.rect(col.x-2, col.y+col.height+12, 17, 24);

        // ZONA DE RIESGO
        final Rectangle dangerous = new Rectangle(col.x+col.width, 0, 150, col.height*1.5f);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(dangerous.x, dangerous.y, dangerous.width, dangerous.height);

        // POSICION DEL PERSONAJE
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(position.x, position.y, 1);
    }

    @Override
    public final void onHudRender(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (!estaVivo())
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
        return new Vector2(width/4, 0); //TODO: Incluir width
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(25, 100);
    }

    @Override
    public boolean canBeCleaned() {
        return false;
    }
}
