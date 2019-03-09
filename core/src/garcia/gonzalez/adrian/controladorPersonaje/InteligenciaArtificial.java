package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Gdx;

import java.util.LinkedList;

import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.enums.Enums.*;

public abstract class InteligenciaArtificial {

    private Personaje personaje;

    private LinkedList<TeclasJugador> lastMovimientoIA;   // Teclas pulsadas en el último frame
    private LinkedList<TeclasJugador> movimientoIA;

    public InteligenciaArtificial(Personaje personaje) {
        this.personaje = personaje;

        lastMovimientoIA = new LinkedList<TeclasJugador>();
        movimientoIA = new LinkedList<TeclasJugador>();
    }

    public abstract void update(float delta);

    public final void nextFrame() {
        lastMovimientoIA = new LinkedList<TeclasJugador>(movimientoIA);
        movimientoIA = new LinkedList<TeclasJugador>();
    }

    public final void addTecla (TeclasJugador tecla) {
        movimientoIA.add(tecla);
    }

    public final boolean isKeyDown (TeclasJugador tecla) {
        return movimientoIA.contains(tecla) && !lastMovimientoIA.contains(tecla);
    }

    public final boolean isKeyStay (TeclasJugador tecla) {
        return movimientoIA.contains(tecla) && lastMovimientoIA.contains(tecla);
    }

    public final boolean isKeyUp (TeclasJugador tecla) {
        return !movimientoIA.contains(tecla) && lastMovimientoIA.contains(tecla);
    }


}
