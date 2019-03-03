package garcia.gonzalez.adrian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.List;

import garcia.gonzalez.adrian.controladorPersonaje.ControladorJugador1;
import garcia.gonzalez.adrian.controladorPersonaje.MyInputProcessor;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.Esbirro;
import garcia.gonzalez.adrian.entidades.Personaje;
import garcia.gonzalez.adrian.entidades.particulas.Particula;
import garcia.gonzalez.adrian.entidades.personajes.Personaje1;
import garcia.gonzalez.adrian.entidades.proyectiles.BolaEnergia;
import garcia.gonzalez.adrian.entidades.proyectiles.Proyectil;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Escenario;

public class Level {

    private Viewport viewport;

    //TODO: Seguir con el escenarioAssets
    private Escenario escenario;

    private Personaje personaje;

    private float tickUpdate;
    private MyInputProcessor input;

    private DelayedRemovalArray<Entidad> entidades; // Personajes, aliados, torres, etc
    private DelayedRemovalArray<Proyectil> proyectiles;
    private DelayedRemovalArray<Particula> particulas;
    //TODO: Incluir lista para escenarioAssets, proyectiles, particulas y plataformas

    public Level(Viewport viewport) {
        this.viewport = viewport;

        input = new MyInputProcessor();
        Gdx.input.setInputProcessor(input);

        tickUpdate = 0;

        escenario = new Escenario(this.viewport);
        entidades = new DelayedRemovalArray<Entidad>();
        proyectiles = new DelayedRemovalArray<Proyectil>();
        particulas = new DelayedRemovalArray<Particula>();

        //TODO: Borrar contenido de pruebas
        // Incluimos esbirro de pruebas
        //entidades.add (new Esbirro(Enums.Bando.ALIADO,-180,5, this));
        //entidades.add (new Esbirro(Enums.Bando.ALIADO,-140,5, this));
        //entidades.add (new Esbirro(Enums.Bando.ALIADO,-100,5, this));
        entidades.add (new Esbirro(Enums.Bando.ALIADO,-60,5, this));
        entidades.add (new Esbirro(Enums.Bando.ENEMIGO,60,5, this));
        entidades.add (new Esbirro(Enums.Bando.ENEMIGO,100,5, this));
        entidades.add (new Esbirro(Enums.Bando.ENEMIGO,140,5, this));
        entidades.add (new Esbirro(Enums.Bando.ENEMIGO,180,5, this));



        //Controlador controller, Bando bando, int x, int y, Level level
        Personaje1 mainCharacter = new Personaje1(new ControladorJugador1(), Enums.Bando.ALIADO, -220,5, this);
        entidades.add(mainCharacter);
        this.personaje = mainCharacter;

        generarProyecitl(new BolaEnergia(mainCharacter, this, -300, 10));
    }

    public Personaje getPersonaje () {
        return personaje;
    }

    public void generarProyecitl (Proyectil p) {
        proyectiles.add(p);
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
        tickUpdate+=delta;

        // Actualizamos los enemigos
        for (int i = 0; i < entidades.size; i++) {
            Entidad entidad = entidades.get(i);
            entidad.update(delta);

            //TODO: Configurar que hacer cuando este muerto
            if (entidad.canBeCleaned()) {
                entidades.removeIndex(i);
                i--;
            }
        }

        for (int i = 0; i < proyectiles.size; i++) {
            Proyectil proyectil  = proyectiles.get(i);

            proyectil.update(delta);
            if (proyectil.hasFinished()) {
                proyectiles.removeIndex(i);
                i--;
            }
        }

        // Activamos el tick_update, cuando proceda
        if (tickUpdate> Constants.TICK_UPDATE) {
            for (Entidad e : entidades) {
                e.tickUpdate(tickUpdate);
            }
            // Se reinicia a 0
            tickUpdate=0;
        }




        /*explosions.begin();
        for (int i = 0; i < explosions.size; i++) {
            if (explosions.get(i).isFinished()) {
                explosions.removeIndex(i);
            }
        }
        explosions.end();*/

        input.nextFrame();  // siguiente framea
    }

    public void render(SpriteBatch batch) {
        batch.begin();

        escenario.render(batch);

        for (int i = 0; i < entidades.size; i++) {
            entidades.get(i).render(batch);
        }

        for (int i = 0; i < proyectiles.size; i++) {
            proyectiles.get(i).render(batch);
        }

        for (int i = 0; i < particulas.size; i++) {
            Particula p = particulas.get(i);
            p.render(batch);

            if(p.isFinished()) {
                particulas.removeIndex(i);
                i--;
            }
        }

        /* TODO: error #iterator() cannot be used nested. BORRAR ESTO
        for (Entidad entidad : entidades) {
            entidad.render(batch);
        }*/

        batch.end();
    }


    // TODO: Cambiar y mejorar
    /**
     * Coge todas las entidades dentro de un posición en un rango
     * */
    public Entidad getEntidad(Vector2 pos, float distancia, Enums.Bando bando) {
        for(Entidad e : entidades) {
            // TODO incluir
            if (e.estaVivo() && e.getBando()==bando && pos.dst(e.getPosition()) < distancia) {
                return e;
            }
        }
        return null;
    }

    public Entidad getEntidad(Vector2 pos, float distancia, Enums.Direccion dir) {
        for(Entidad e : entidades) {
            // TODO incluir
            if (e.estaVivo() && pos.dst(e.getPosition()) < distancia) {
                return e;
            }
        }
        return null;
    }

    /**
     * Devuelve una lista con todas las entidades colisionadas
     * */
    public List<Entidad> getCollisionEntities (Rectangle rect) {
        LinkedList<Entidad> ent = new LinkedList<Entidad>();
        for (Entidad e :  entidades) {
            if (e.estaVivo() && rect.overlaps(e.getCollider())) {
                ent.add(e);
            }
        }
        return ent;
    }
}
