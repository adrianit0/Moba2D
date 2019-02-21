package garcia.gonzalez.adrian;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;

import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Esbirro;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Escenario;

public class Level {

    private Viewport viewport;

    //TODO: Seguir con el escenarioAssets
    private Escenario escenario;

    private Entidad minion;

    private DelayedRemovalArray<Entidad> entidades; // Personajes, aliados, torres, etc
    //TODO: Incluir lista para escenarioAssets, proyectiles, particulas y plataformas

    public Level(Viewport viewport) {
        this.viewport = viewport;

        escenario = new Escenario(this.viewport);
        minion = new Esbirro(Enums.Bando.ALIADO,0,0);   // DE PRUEBAS
        entidades = new DelayedRemovalArray<Entidad>();

        entidades.add(minion);
        entidades.add (new Esbirro(Enums.Bando.ENEMIGO,100,0));
    }

    private void nivel1() {
        //TODO: Modificar o eliminar esto
        /*
        // Inicializamos las variables
        gigaGal = new GigaGal(new Vector2(15, 40), this);

        platforms = new Array<Platform>();
        enemies = new DelayedRemovalArray<Enemy>();
        bullets = new DelayedRemovalArray<Bullet>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();

        // Añadimos las plataformas
        platforms.add(new Platform(15, 100, 30, 20));
        Platform enemyPlatform = new Platform(75, 90, 100, 65);
        enemies.add(new Enemy(enemyPlatform));
        platforms.add(enemyPlatform);
        platforms.add(new Platform(35, 55, 50, 20));
        platforms.add(new Platform(10, 20, 20, 9));
        platforms.add(new Platform(-50, 20, 20, 9));
        platforms.add(new Platform(-150, 20, 20, 9));
        //platforms.add(new Platform(-100, 0, 600, 10));

        powerups.add(new Powerup(new Vector2(20, 110)));*/
    }

    public void update(float delta) {
        // Actualizamos los enemigos
        for (int i = 0; i < entidades.size; i++) {
            Entidad entidad = entidades.get(i);
            entidad.update(delta);

            //TODO: Configurar que hacer cuando este muerto
            /*
            if (entidad.health < 1) {
                spawnExplosion(entidad.position);
                enemies.removeIndex(i);
                // Sumamos los puntos en caso de matar al enemigo
                score += Constants.ENEMY_KILL_SCORE;
            }*/
        }

        /*explosions.begin();
        for (int i = 0; i < explosions.size; i++) {
            if (explosions.get(i).isFinished()) {
                explosions.removeIndex(i);
            }
        }
        explosions.end();*/
    }

    public void render(SpriteBatch batch) {
        batch.begin();

        escenario.render(batch);

        for (Entidad entidad : entidades) {
            entidad.render(batch);
        }


        batch.end();
    }
}
