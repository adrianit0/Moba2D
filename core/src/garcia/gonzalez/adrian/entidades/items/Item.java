package garcia.gonzalez.adrian.entidades.items;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import garcia.gonzalez.adrian.entidades.Personaje;

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

    public abstract Item getCopy ();

    public abstract AtlasRegion onImageRender();


}