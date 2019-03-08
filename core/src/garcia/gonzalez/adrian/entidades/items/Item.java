package garcia.gonzalez.adrian.entidades.items;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import garcia.gonzalez.adrian.entidades.personajes.Personaje;

// No implementado en esta versión del juego
public abstract class Item {

    private Personaje owner;

    public Item (Personaje character){
        owner = character;
    }

    public abstract void onItemBuy();
    public abstract void onItemSell();

    public abstract int getPrize();
    public abstract String getDescription();

    public abstract void onUpdate(float delta);

    /**
     * No implementado, se ejecutaría al realizar un evento que lo active
     * Pe: Al recibir un ataque, al saltar, o al destruir una torre, a petición
     * de cada item. Como parametro de entrada se le pasa el evento que ha ocurrido.
     * (Pe: GameEvent.Attack)
     * */
    public abstract void onEvent (/* GameEvent evento */);

    public abstract Item getCopy ();

    // Se recomienda que sea una textura de 32x32
    public abstract AtlasRegion onImageRender();
}
