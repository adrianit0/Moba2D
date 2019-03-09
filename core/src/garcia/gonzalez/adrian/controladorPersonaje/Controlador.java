package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Gdx;

import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.enums.Enums.TeclasJugador;

/**
 * Controlador base del personaje, es una clase abstracta porque tendrá que ser implementado por sus hijos (Player1, Player2 e IA)
 *
 * */
public abstract class Controlador {

    private Personaje personaje;
    protected InputProcessorBase input;

    public Controlador (){
        input = (InputProcessorBase) Gdx.input.getInputProcessor();
    }

    public Controlador (InputProcessorBase inputProcessor) {
        this.input = inputProcessor;
    }

    public void update(float delta) {
    }

    public final void setPersonaje (Personaje personaje) {
        this.personaje = personaje;
    }

    public abstract boolean onKeyDown (TeclasJugador tecla);
    public abstract boolean onKeyPressing (TeclasJugador tecla);
    public abstract boolean onKeyUp (TeclasJugador tecla);

}
