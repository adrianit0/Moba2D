package garcia.gonzalez.adrian.controladorPersonaje;

import garcia.gonzalez.adrian.entidades.Personaje;
import garcia.gonzalez.adrian.enums.Enums.TeclasJugador;

/**
 * Controlador base del personaje, es una clase abstracta porque tendrá que ser implementado por sus hijos (Player1, Player2 e IA)
 *
 * */
public abstract class Controlador {

    private Personaje personaje;

    public Controlador (){
    }

    public void setPersonaje (Personaje personaje) {
        this.personaje = personaje;
    }

    public abstract boolean onKeyDown (TeclasJugador tecla);
    public abstract boolean onKeyPressing (TeclasJugador tecla);
    public abstract boolean onKeyUp (TeclasJugador tecla);
}
