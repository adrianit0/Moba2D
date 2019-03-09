package garcia.gonzalez.adrian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.prism.image.ViewPort;

import java.util.LinkedList;
import java.util.List;

import garcia.gonzalez.adrian.controladorPersonaje.Controlador;
import garcia.gonzalez.adrian.controladorPersonaje.ControladorJugador1;
import garcia.gonzalez.adrian.controladorPersonaje.InputProcessorAndroid;
import garcia.gonzalez.adrian.controladorPersonaje.InputProcessorBase;
import garcia.gonzalez.adrian.controladorPersonaje.InputProcessorDesktop;
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
import garcia.gonzalez.adrian.screens.GameplayScreen;
import garcia.gonzalez.adrian.utiles.Constants;
import garcia.gonzalez.adrian.utiles.Escenario;

public class Level {

    private GameplayScreen gameplay;
    
    private Escenario escenario;

    private int oleada;
    private Enums.Dificultad dificultad;

    private float tickUpdate;
    private float minionSpawn;
    private InputProcessorBase input;

    private boolean partidaTeminada;

    private DelayedRemovalArray<Entidad> entidades; // Personajes, aliados, torres, etc
    private DelayedRemovalArray<Proyectil> proyectiles;
    private DelayedRemovalArray<Particula> particulas;

    public Level(GameplayScreen gameplay) {
        this.gameplay = gameplay;

        input = gameplay.isPhoneDevice() ? new InputProcessorAndroid() : new InputProcessorDesktop();
        Gdx.input.setInputProcessor(input);

        oleada=0;
        partidaTeminada = false;
        dificultad = gameplay.getDificultad();

        tickUpdate = 0;
        minionSpawn = Constants.TIME_MINION_SPAWN-Constants.TIME_FIRST_MINION_SPAWN;

        escenario = new Escenario();
        entidades = new DelayedRemovalArray<Entidad>();
        proyectiles = new DelayedRemovalArray<Proyectil>();
        particulas = new DelayedRemovalArray<Particula>();

        Torre t1 = new Torre(Enums.Bando.ALIADO, -750, 8, this, null);
        Torre t2 = new Torre(Enums.Bando.ALIADO, -1050, 8, this, t1);
        Nexo t3 = new Nexo(Enums.Bando.ALIADO, -1200, 8, this, t2);

        Torre te1 = new Torre(Enums.Bando.ENEMIGO, 750, 8, this, null);
        Torre te2 = new Torre(Enums.Bando.ENEMIGO, 1050, 8, this, te1);
        Nexo te3 = new Nexo(Enums.Bando.ENEMIGO, 1200, 8, this, te2);

        entidades.add (t1);
        entidades.add (t2);
        entidades.add (t3);

        entidades.add (te1);
        entidades.add (te2);
        entidades.add (te3);
    }

    public void terminarPartida (Enums.EstadoPartida estado) {
        partidaTeminada = true;
        gameplay.getFinPartidaOverlay().setEstado(estado);
    }

    public boolean partidaTerminada () {
        return partidaTeminada;
    }

    public void addCharacter (Personaje character) {
        entidades.add(character);

        character.changePosition(new Vector2(character.getBando()== Enums.Bando.ALIADO ? Constants.ALLY_CHARACTER_SPAWN_POSITION : Constants.ENEMY_CHARACTER_SPAWN_POSITION, 5));
    }

    public InputProcessorBase getInput() {
        return input;
    }

    public void setGrayscale (boolean state) {
        gameplay.getGrayScaleView().setGrayscaleTime(state);
    }

    public void generarProyecitl (Proyectil p) {
        proyectiles.add(p);
    }

    public void generarParticula (Particula p) {
        particulas.add(p);
    }

    public void update(float delta) {
        if (partidaTeminada)
            return;

        tickUpdate+=delta;
        minionSpawn+=delta;

        // Cada vez que pase un tiempo generamos una nueva oleada de minions
        if (minionSpawn>Constants.TIME_MINION_SPAWN) {
            minionSpawn-=Constants.TIME_MINION_SPAWN;
            oleada++;

            // Nacen los minions
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1120,5, this, oleada));
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1160,5, this, oleada));
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1200,5, this, oleada));
            entidades.add (new Esbirro(Enums.Bando.ALIADO,-1240,5, this, oleada));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1120,5, this, oleada));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1160,5, this, oleada));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1200,5, this, oleada));
            entidades.add (new Esbirro(Enums.Bando.ENEMIGO,1240,5, this, oleada));
        }

        // Actualizamos las entidades, aquí comprende cualquier elemento que tenga vida en el juego
        for (int i = 0; i < entidades.size; i++) {
            Entidad entidad = entidades.get(i);
            entidad.update(delta);

            if (entidad.canBeCleaned()) {
                entidades.removeIndex(i);
                i--;
            }

            // Si la entidad es un personaje vamos a actualizar su controlador
            // No es necesario para el InputProcessor, pero si para la IA
            if (entidad.getTipoEntidad()== Enums.TipoEntidad.PERSONAJE) {
                Controlador c = ((Personaje) entidad).getController();
                if (c!=null)
                    c.update(delta);
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


        input.nextFrame();  // siguiente frame
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, Personaje objetivo, Viewport viewport) {
        //mainCharaceter = objetivo;
        batch.begin();

        escenario.render(batch, viewport);

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

        for (Entidad entidad : entidades) {
            if (objetivo!=entidad)
                entidad.onHudRender(batch, shapeRenderer);
        }

        shapeRenderer.end();

        /*
        // SHAPERENDERER PARA EL DEBUG
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entidad e : entidades) {
            e.onDebugRender(shapeRenderer);
        }
        for (Proyectil p : proyectiles) {
            p.debugRender(shapeRenderer);
        }

        shapeRenderer.end();*/
    }

    public int getOleada () {
        return oleada;
    }

    public Enums.Dificultad getDificultad() {
        return dificultad;
    }

    /**
     * A partir de un valor base más otro porcentual obtiene el daño para cualquiera de las
     * habilidades. El funcionamiento
     * */
    public final int getHabilityDamage (Entidad entidad, float base, float porcentual) {
        return Math.round(base + entidad.getAtributos().getAttrPorc(Enums.AtribEnum.ATAQUE) * porcentual);
    }


    /**
     * Coge todas las entidades dentro de un posición en un rango
     * */
    public Entidad getEntidad(Vector2 pos, float distancia, Enums.Bando bando) {
        for(Entidad e : entidades) {
            if (e.estaVivo() && e.getBando()==bando && pos.dst(e.getPosition()) < distancia) {
                return e;
            }
        }
        return null;
    }

    public Entidad getEntidad(Vector2 pos, float distancia, Enums.Direccion dir) {
        for(Entidad e : entidades) {
            if (e.estaVivo() && pos.dst(e.getPosition()) < distancia) {
                return e;
            }
        }
        return null;
    }

    public List<Entidad> getEntidades (Vector2 pos, float distancia) {
        LinkedList<Entidad> ent = new LinkedList<Entidad>();
        for(Entidad e : entidades) {
            if (e.estaVivo() && pos.dst(e.getPosition()) < distancia) {
                ent.add(e);
            }
        }
        return ent;
    }

    public List<Entidad> getEntidades (Vector2 pos, float distancia, Enums.Bando bando) {
        LinkedList<Entidad> ent = new LinkedList<Entidad>();
        for(Entidad e : entidades) {
            if (e.estaVivo() && pos.dst(e.getPosition()) < distancia && e.getBando()==bando) {
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
