package garcia.gonzalez.adrian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import garcia.gonzalez.adrian.entidades.estructuras.Nexo;
import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.entidades.estructuras.Torre;
import garcia.gonzalez.adrian.entidades.particulas.Particula;
import garcia.gonzalez.adrian.entidades.personajes.Personaje1;
import garcia.gonzalez.adrian.entidades.personajes.Personaje2;
import garcia.gonzalez.adrian.entidades.proyectiles.Proyectil;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Escenario;

public class Level {

    private Viewport viewport;
    private  GameplayScreen gameplay;

    //TODO: Seguir con el escenarioAssets
    private Escenario escenario;

    private Personaje personaje;

    private float tickUpdate;
    private float minionSpawn;
    private MyInputProcessor input;

    private DelayedRemovalArray<Entidad> entidades; // Personajes, aliados, torres, etc
    private DelayedRemovalArray<Proyectil> proyectiles;
    private DelayedRemovalArray<Particula> particulas;
    //TODO: Incluir lista para escenarioAssets,  plataformas


    public Level(Viewport viewport, GameplayScreen gameplay) {
        this.viewport = viewport;
        this.gameplay = gameplay;

        input = new MyInputProcessor();
        Gdx.input.setInputProcessor(input);

        tickUpdate = 0;
        minionSpawn = Constants.TIME_MINION_SPAWN-Constants.TIME_FIRST_MINION_SPAWN;

        escenario = new Escenario(this.viewport);
        entidades = new DelayedRemovalArray<Entidad>();
        proyectiles = new DelayedRemovalArray<Proyectil>();
        particulas = new DelayedRemovalArray<Particula>();

        Torre t1 = new Torre(Enums.Bando.ALIADO, -750, 8, this, null);
        Torre t2 = new Torre(Enums.Bando.ALIADO, -1050, 8, this, t1);
        Nexo t3 = new Nexo(Enums.Bando.ALIADO, -1200, 8, this, t2);
        Torre t4 = new Torre(Enums.Bando.ALIADO, -1350, 8, this, null);

        Torre te1 = new Torre(Enums.Bando.ENEMIGO, 750, 8, this, null);
        Torre te2 = new Torre(Enums.Bando.ENEMIGO, 1050, 8, this, te1);
        Nexo te3 = new Nexo(Enums.Bando.ENEMIGO, 1200, 8, this, te2);
        Torre te4 = new Torre(Enums.Bando.ENEMIGO, 1350, 8, this, null);

        entidades.add (t1);
        entidades.add (t2);
        entidades.add (t3); // TODO: Sustituir por nexo
        //entidades.add (t4); // TODO: Sustituir por tienda

        entidades.add (te1);
        entidades.add (te2);
        entidades.add (te3); // TODO: Sustituir por nexo
        //entidades.add (te4); // TODO: Sustituir por tienda


        //TODO: Borrar contenido de pruebas


        //Controlador controller, Bando bando, int x, int y, Level level
        personaje = new Personaje2(new ControladorJugador1(), Enums.Bando.ALIADO, -1300,5, this);
        entidades.add(personaje);

        entidades.add(new Personaje1(null, Enums.Bando.ENEMIGO, 1300, 5, this));
    }

    public void setGrayscale (boolean state) {
        gameplay.getGrayScaleView().setGrayscaleTime(state);
    }

    public Personaje getPersonaje () {
        return personaje;
    }

    public void generarProyecitl (Proyectil p) {
        proyectiles.add(p);
    }

    public void generarParticula (Particula p) {
        particulas.add(p);
    }

    public void update(float delta) {
        tickUpdate+=delta;
        minionSpawn+=delta;

        if (minionSpawn>Constants.TIME_MINION_SPAWN) {
            minionSpawn-=Constants.TIME_MINION_SPAWN;

            // Nacen los minions
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1120,5, this));
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1160,5, this));
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1200,5, this));
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1240,5, this));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1120,5, this));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1160,5, this));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1200,5, this));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1240,5, this));
        }

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
            for (int i = 0; i < entidades.size; i++) {
                entidades.get(i).tickUpdate(tickUpdate);
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

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
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

        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // TODO: Para mostrar la vida de los personajes
        // TODO: Excepto la del personaje principal
        for (Entidad entidad : entidades) {
            entidad.onHudRender(batch, shapeRenderer);
        }

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entidad e : entidades) {
            e.onDebugRender(shapeRenderer);
        }
        for (Proyectil p : proyectiles) {
            //p.debugRender(shapeRenderer);
        }

        shapeRenderer.end();
    }

    // TODO: Cambiar el nombre a otro más acertado
    /**
     * A partir de un valor base más otro porcentual obtiene el daño para cualquiera de las
     * habilidades. El funcionamiento
     * */
    public final int getHabilityDamage (Entidad entidad, float base, float porcentual) {
        return Math.round(base + entidad.getAtributos().getAttrPorc(Enums.AtribEnum.ATAQUE) * porcentual);
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

    public List<Entidad> getEntidades (Vector2 pos, float distancia) {
        LinkedList<Entidad> ent = new LinkedList<Entidad>();
        for(Entidad e : entidades) {
            // TODO incluir
            if (e.estaVivo() && pos.dst(e.getPosition()) < distancia) {
                ent.add(e);
            }
        }
        return ent;
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

    public List<Entidad> getCollisionEntities (Rectangle rect, Enums.Bando bando) {
        LinkedList<Entidad> ent = new LinkedList<Entidad>();
        for (Entidad e :  entidades) {
            if (e.estaVivo() && e.getBando()==bando && rect.overlaps(e.getCollider())) {
                ent.add(e);
            }
        }
        return ent;
    }

    public Entidad getNearestEntity (Entidad origen, Rectangle rect) {
        Entidad entity = null;

        for (Entidad e : entidades) {
            if (!e.estaVivo() || !rect.overlaps(e.getCollider()) || e.getBando()==origen.getBando())
                continue;

            if (entity==null) {
                entity=e;
            } else if (entity.getPosition().dst(origen.getPosition())>e.getPosition().dst(origen.getPosition())) {
                entity=e;
            }
        }

        return entity;
    }
}
