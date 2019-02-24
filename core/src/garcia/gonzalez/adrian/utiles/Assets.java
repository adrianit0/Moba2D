package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import garcia.gonzalez.adrian.enums.Enums;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    // Singleton EscenarioAssets
    public EscenarioAssets escenarioAssets;

    // Singleton minions
    public MinionAssets blueMinionAssets;
    public MinionAssets redMinionAssets;

    // Singleton plataforma
    public PlatformAssets platformAssets;


    private AssetManager assetManager;

    public void init() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);

        escenarioAssets = new EscenarioAssets();

        blueMinionAssets = new MinionAssets(atlas, Enums.Bando.ALIADO);
        redMinionAssets = new MinionAssets(atlas, Enums.Bando.ENEMIGO);

        // Cargamos el singleton de la plataforma
        platformAssets = new PlatformAssets(atlas);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "No puede cargar el asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public class MinionAssets {
        // ANIMACIONES DE LOS MINIONS
        public final Animation<TextureRegion> spawn;
        public final Animation<TextureRegion> andar;
        public final Animation<TextureRegion> atacar;
        public final Animation<TextureRegion> muerte;

        public MinionAssets(TextureAtlas atlas, Enums.Bando bando) {
            String _bando = bando==Enums.Bando.ALIADO ? Constants.MINION_ALIADO : Constants.MINION_ENEMIGO;

            // Creamos la animación para el movimiento de los minions
            //TODO: Configurar minions rojos para que miren tambien hacia la derecha
            spawn = Utils.createAnimationMinions(atlas, Constants.MINION_SPAWN_DURATION, Constants.MINION_SPAWN, Animation.PlayMode.NORMAL, _bando);
            andar = Utils.createAnimationMinions(atlas, Constants.MINION_WALK_DURATION, Constants.MINION_WALK, Animation.PlayMode.LOOP, _bando);
            atacar = Utils.createAnimationMinions(atlas, Constants.MINION_ATTACK_DURATION, Constants.MINION_ATTACK, Animation.PlayMode.NORMAL, _bando);
            muerte = Utils.createAnimationMinions(atlas, Constants.MINION_DEATH_DURATION, Constants.MINION_DEAD, Animation.PlayMode.NORMAL, _bando);
        }
    }

    public class PlatformAssets {
        //public final NinePatch platformNinePatch;

        public PlatformAssets(TextureAtlas atlas) {
            /*AtlasRegion region = atlas.findRegion(Constants.PLATFORM_SPRITE);

            int edge = Constants.PLATFORM_EDGE;
            platformNinePatch = new NinePatch(region, edge, edge, edge, edge);*/
        }
    }

    public class EscenarioAssets {
        // TODO: Incluir el resto de decoraciones del escenarioAssets
        public Texture troncos1;
        public Texture troncos2;
        public Texture troncos3;
        public Texture troncos4;
        public Texture troncos5;

        public Texture luz1;
        public Texture luz2;

        public Texture copa;

        public Texture suelo1;
        public Texture suelo2;

        public EscenarioAssets() {
            troncos1 = new Texture("escenario/troncos_1.png");
            troncos2 = new Texture("escenario/troncos_2.png");
            troncos3 = new Texture("escenario/troncos_3.png");
            troncos4 = new Texture("escenario/troncos_4.png");
            troncos5 = new Texture("escenario/troncos_5.png");

            luz1 = new Texture("escenario/luz_1.png");
            luz2 = new Texture("escenario/luz_2.png");

            copa = new Texture("escenario/copa_arboles.png");

            suelo1 = new Texture("escenario/suelo_1.png");
            suelo2 = new Texture("escenario/suelo_2.png");
        }
    }
}
