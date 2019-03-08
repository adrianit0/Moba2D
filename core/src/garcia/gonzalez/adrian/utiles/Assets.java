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

import garcia.gonzalez.adrian.enums.Enums;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    // Singleton EscenarioAssets
    public EscenarioAssets escenarioAssets;

    // Personajes
    public Character_01Assets personaje1;
    public Character_01_EffectsAssets efectosPersonaje1;
    public Character_02Assets personaje2;

    // Singleton minions
    public MinionAssets blueMinionAssets;
    public MinionAssets redMinionAssets;

    // Singleton plataforma
    public StructureAssets estructuraAssets;

    // Singleton Overlay
    public MenuAssets menuAssets;
    public OverlayAssets overlayAssets;


    private AssetManager assetManager;

    public void init() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_MINIONS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_CHARACTERS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_HABILITIES, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas_minions = assetManager.get(Constants.TEXTURE_ATLAS_MINIONS);
        TextureAtlas atlas_characters = assetManager.get(Constants.TEXTURE_ATLAS_CHARACTERS);
        TextureAtlas atlas_habilidades = assetManager.get(Constants.TEXTURE_ATLAS_HABILITIES);

        escenarioAssets = new EscenarioAssets();

        personaje1 = new Character_01Assets(atlas_characters);
        efectosPersonaje1 = new Character_01_EffectsAssets(atlas_characters);
        personaje2 = new Character_02Assets(atlas_characters);

        estructuraAssets = new StructureAssets(atlas_characters);

        blueMinionAssets = new MinionAssets(atlas_minions, Enums.Bando.ALIADO);
        redMinionAssets = new MinionAssets(atlas_minions, Enums.Bando.ENEMIGO);

        menuAssets = new MenuAssets();
        overlayAssets = new OverlayAssets(atlas_habilidades);
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
    public class Character_01Assets {
        public final Animation<TextureRegion> idle;
        public final Animation<TextureRegion> running;
        public final Animation<TextureRegion> jumping;
        public final Animation<TextureRegion> attack01;
        public final Animation<TextureRegion> attack02;
        public final Animation<TextureRegion> attack03;
        public final Animation<TextureRegion> death;

        public Character_01Assets (TextureAtlas atlas) {
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

    public class Character_02Assets {
        public final Animation<TextureRegion> idle;
        public final Animation<TextureRegion> running;
        public final Animation<TextureRegion> jumping;
        public final Animation<TextureRegion> attack01;
        public final Animation<TextureRegion> attack02;
        public final Animation<TextureRegion> attack03;
        public final Animation<TextureRegion> death;

        public Character_02Assets (TextureAtlas atlas) {
            idle = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_02_IDLE, Animation.PlayMode.LOOP);
            running = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_02_WALK, Animation.PlayMode.LOOP);
            jumping = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_02_JUMP, Animation.PlayMode.LOOP);
            death = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION, Constants.CHARACTER_02_DEATH, Animation.PlayMode.NORMAL);

            attack01 = Utils.createAnimation(atlas, Constants.CHARACTER_02_HAB_DURATION, Constants.CHARACTER_02_ATTACK_01, Animation.PlayMode.NORMAL);
            attack02 = Utils.createAnimation(atlas, Constants.CHARACTER_02_HAB_DURATION, Constants.CHARACTER_02_ATTACK_02, Animation.PlayMode.LOOP);
            attack03 = Utils.createAnimation(atlas, Constants.CHARACTER_02_HAB_DURATION, Constants.CHARACTER_02_ATTACK_03, Animation.PlayMode.NORMAL);
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
            spawn = Utils.createAnimationMinions(atlas, Constants.MINION_SPAWN_DURATION, Constants.MINION_SPAWN, Animation.PlayMode.NORMAL, _bando);
            andar = Utils.createAnimationMinions(atlas, Constants.MINION_WALK_DURATION, Constants.MINION_WALK, Animation.PlayMode.LOOP, _bando);
            atacar = Utils.createAnimationMinions(atlas, Constants.MINION_ATTACK_DURATION, Constants.MINION_ATTACK, Animation.PlayMode.NORMAL, _bando);
            muerte = Utils.createAnimationMinions(atlas, Constants.MINION_DEATH_DURATION, Constants.MINION_DEAD, Animation.PlayMode.NORMAL, _bando);
        }
    }

    public class StructureAssets {
        public final Texture torreAliada;
        public final Texture torreEnemiga;
        public final Texture nexoAliado;
        public final Texture nexoEnemigo;
        public final Texture tienda;

        public final Animation<TextureRegion> explosion;

        public StructureAssets (TextureAtlas atlas) {
            torreAliada = new Texture("images/torreAl.png");
            torreEnemiga = new Texture("images/torreEn.png");

            nexoAliado = new Texture("images/nexoAL.png");
            nexoEnemigo = new Texture("images/nexoEN.png");

            tienda = new Texture("images/tienda.png");

            explosion = Utils.createAnimation(atlas, Constants.GENERIC_HABILITY_DURATION/3, Constants.TOWER_CANNON_EXPLOSION, Animation.PlayMode.NORMAL);
        }
    }

    public class EscenarioAssets {
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

    public class MenuAssets {
        public final Animation<Texture> fondo;

        public MenuAssets () {
            fondo = Utils.createAnimationTexture(Constants.GENERIC_HABILITY_DURATION, Constants.escenario, Animation.PlayMode.LOOP);

        }

    }

    public class OverlayAssets {
        public final Texture mainHud;
        public final Texture vida;
        public final Texture mana;

        public final Texture derecha;
        public final Texture izquierda;
        public final Texture salto;

        public final TextureRegion character01_hab01;
        public final TextureRegion character01_hab02;
        public final TextureRegion character01_hab03;

        public final TextureRegion character02_hab01;
        public final TextureRegion character02_hab02;
        public final TextureRegion character02_hab03;

        public OverlayAssets (TextureAtlas atlas) {
            mainHud = new Texture("GUI/interfaz_character.png");
            vida = new Texture("GUI/vida.png");
            mana = new Texture("GUI/mana.png");

            derecha = new Texture("GUI/derecha.png");
            izquierda = new Texture("GUI/izquierda.png");
            salto = new Texture("GUI/salto.png");

            character01_hab01 = atlas.findRegion("habilidades-078");
            character01_hab02 = atlas.findRegion("habilidades-001");
            character01_hab03 = atlas.findRegion("habilidades-055");

            character02_hab01 = atlas.findRegion("habilidades-062");
            character02_hab02 = atlas.findRegion("habilidades-087");
            character02_hab03 = atlas.findRegion("habilidades-046");
        }
    }
}
