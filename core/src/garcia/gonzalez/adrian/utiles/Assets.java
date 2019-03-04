package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import java.util.concurrent.CopyOnWriteArraySet;

import garcia.gonzalez.adrian.enums.Enums;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    // Singleton EscenarioAssets
    public EscenarioAssets escenarioAssets;

    // Personajes
    public Character_01Assets personaje1;
    public Character_01_EffectsAssets efectosPersonaje1;

    // Singleton minions
    public MinionAssets blueMinionAssets;
    public MinionAssets redMinionAssets;

    // Singleton plataforma
    public StructureAssets estructuraAssets;
    public PlatformAssets platformAssets;

    // Singleton Overlay
    public OverlayAssets overlayAssets;


    private AssetManager assetManager;

    public void init() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_MINIONS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_CHARACTERS, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas_minions = assetManager.get(Constants.TEXTURE_ATLAS_MINIONS);
        TextureAtlas atlas_characters = assetManager.get(Constants.TEXTURE_ATLAS_CHARACTERS);

        escenarioAssets = new EscenarioAssets();

        personaje1 = new Character_01Assets(atlas_characters);
        efectosPersonaje1 = new Character_01_EffectsAssets(atlas_characters);

        estructuraAssets = new StructureAssets();

        blueMinionAssets = new MinionAssets(atlas_minions, Enums.Bando.ALIADO);
        redMinionAssets = new MinionAssets(atlas_minions, Enums.Bando.ENEMIGO);

        overlayAssets = new OverlayAssets();

        // Cargamos el singleton de la plataforma
        // TODO: Si no meto plataformas, borrar esto
        platformAssets = new PlatformAssets(atlas_minions);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "No puede cargar el asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    /**
     * Assets del personaje número 1.
     * */
    // TODO: Ponerle un nombre y refactorizarlo
    public class Character_01Assets {
        public final Animation<TextureRegion> idle;
        public final Animation<TextureRegion> running;
        public final Animation<TextureRegion> jumping;
        public final Animation<TextureRegion> attack01;
        public final Animation<TextureRegion> attack02;
        public final Animation<TextureRegion> attack03;
        public final Animation<TextureRegion> death;

        public Character_01Assets (TextureAtlas atlas) {
            //TODO: Cambiar el temporizador
            idle = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_01_IDLE, Animation.PlayMode.LOOP);
            running = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_01_WALK, Animation.PlayMode.LOOP);
            jumping = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION*5, Constants.CHARACTER_01_JUMP, Animation.PlayMode.LOOP);
            death = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_01_DEATH, Animation.PlayMode.NORMAL);

            attack01 = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_01_ATTACK_01, Animation.PlayMode.NORMAL);
            attack02 = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_01_ATTACK_02, Animation.PlayMode.NORMAL);
            attack03 = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_01_ATTACK_03, Animation.PlayMode.NORMAL);
        }
    }

    public class Character_01_EffectsAssets {
        public final Animation<TextureRegion> explotion;
        public final TextureRegion bolaEnergia;
        public final Animation<TextureRegion> hit;

        public Character_01_EffectsAssets (TextureAtlas atlas) {
            explotion = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_01_POWER_BALL_EXPLOSION, Animation.PlayMode.NORMAL);
            bolaEnergia = explotion.getKeyFrame(0);
            hit = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION/5, Constants.CHARACTER_01_HIT, Animation.PlayMode.NORMAL);
        }
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

    public class StructureAssets {
        public final Texture torreAliada;
        public final Texture torreEnemiga;

        public StructureAssets () {
            torreAliada = new Texture("images/torreAl.png");
            torreEnemiga = new Texture("images/torreEn.png");
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
            //TODO: Pasar esto a constantes
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

    public class OverlayAssets {
        public Texture mainHud;
        public Texture vida;
        public Texture mana;

        public OverlayAssets () {
            // TODO: Seguir y meter el contenido en constantes
            mainHud = new Texture("GUI/interfaz_character.png");
            vida = new Texture("GUI/vida.png");
            mana = new Texture("GUI/mana.png");
        }
    }
}
