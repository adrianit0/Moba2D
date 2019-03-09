package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.enums.Enums;
import garcia.gonzalez.adrian.utiles.Utils;

/**
 * Inteligencia artificial genérica para todos los personajes.
 * No intenta que sea buena IA, simplemente que sirva como sparring para las pruebas.
 *
 * Si hubiera más tiempo de desarrollo esta clase hubiera sido abstracta para permitir una creación
 * de IA específica para cada personaje.
 * */
public class InteligenciaArtificialGenerica extends InteligenciaArtificial {
    public enum MaquinaEstados {
        IDLE, WALKING, JUMPING, ATTACKING, DEATH
    };

    private MaquinaEstados estado;

    private Personaje personaje;
    private Level level;

    private int ataqueUsar = 0;

    private long timeSinceGameStarted;
    private long timeSinceLastAttack;

    public InteligenciaArtificialGenerica(Personaje personaje, Level level) {
        super(personaje);

        estado = MaquinaEstados.IDLE;

        this.personaje = personaje;
        this.level = level;

        timeSinceGameStarted = TimeUtils.nanoTime();
    }

    @Override
    public void update(float delta) {
        switch (estado) {
            case IDLE:
                if (Utils.secondsSince(timeSinceGameStarted)>2)
                    estado = MaquinaEstados.WALKING;
                break;

            case WALKING:
                // El objetivo final de la IA será destruir el nexo, y el nexo está en la dirección contraria al personaje.
                addTecla(personaje.getBando() == Enums.Bando.ALIADO ? Enums.TeclasJugador.MOVER_DERECHA : Enums.TeclasJugador.MOVER_IZQUIERDA);
                List<Entidad> entidades = level.getEntidades(personaje.getPosition(), 100, personaje.getBando().getContrario());
                if (entidades.size()>0) {
                    estado= MaquinaEstados.ATTACKING;
                    timeSinceLastAttack = TimeUtils.nanoTime();
                }
                break;

            case ATTACKING:
                switch (ataqueUsar) {
                    case 0:
                        addTecla(Enums.TeclasJugador.BOTON_HABILIDAD_1);
                        break;
                    case 1:
                        addTecla(Enums.TeclasJugador.BOTON_HABILIDAD_2);
                        break;
                    case 2:
                        addTecla(Enums.TeclasJugador.BOTON_HABILIDAD_3);
                        break;
                }

                if (Utils.secondsSince(timeSinceLastAttack)>2) {
                    estado = MaquinaEstados.WALKING;
                    ataqueUsar++;
                    if (ataqueUsar>2)
                        ataqueUsar=0;
                }
                break;
            case DEATH:
                if (personaje.estaVivo())
                    estado = MaquinaEstados.IDLE;
                break;
        }

        if (!personaje.estaVivo())
            estado = MaquinaEstados.DEATH;
    }
}
