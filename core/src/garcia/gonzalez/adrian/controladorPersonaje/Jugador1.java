package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import garcia.gonzalez.adrian.entidades.Personaje;
import garcia.gonzalez.adrian.enums.Enums;

public class Jugador1 extends Controlador {

    @Override
    public boolean onKeyDown(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
            case MOVER_IZQUIERDA:       return Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
            case SALTAR:                return Gdx.input.isKeyJustPressed(Input.Keys.UP);
            case AGACHAR:               return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
            case BOTON_HABILIDAD_1:     return Gdx.input.isKeyJustPressed(Input.Keys.Q);
            case BOTON_HABILIDAD_2:     return Gdx.input.isKeyJustPressed(Input.Keys.W);
            case BOTON_HABILIDAD_3:     return Gdx.input.isKeyJustPressed(Input.Keys.E);
        }
        return false;
    }

    @Override
    public boolean onKeyPressing(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            case MOVER_IZQUIERDA:       return Gdx.input.isKeyPressed(Input.Keys.LEFT);
            case SALTAR:                return Gdx.input.isKeyPressed(Input.Keys.UP);
            case AGACHAR:               return Gdx.input.isKeyPressed(Input.Keys.DOWN);
            case BOTON_HABILIDAD_1:     return Gdx.input.isKeyPressed(Input.Keys.Q);
            case BOTON_HABILIDAD_2:     return Gdx.input.isKeyPressed(Input.Keys.W);
            case BOTON_HABILIDAD_3:     return Gdx.input.isKeyPressed(Input.Keys.E);
        }
        return false;
    }

    @Override
    public boolean onKeyUp(Enums.TeclasJugador tecla) {
        //TODO: Buscar implementacion para esto
        /*switch (tecla) {
            case MOVER_DERECHA:         return Gdx.input.isk(Input.Keys.RIGHT);
            case MOVER_IZQUIERDA:       return Gdx.input.isKeyPressed(Input.Keys.LEFT);
            case SALTAR:                return Gdx.input.isKeyPressed(Input.Keys.UP);
            case AGACHAR:               return Gdx.input.isKeyPressed(Input.Keys.DOWN);
            case BOTON_HABILIDAD_1:     return Gdx.input.isKeyPressed(Input.Keys.Q);
            case BOTON_HABILIDAD_2:     return Gdx.input.isKeyPressed(Input.Keys.W);
            case BOTON_HABILIDAD_3:     return Gdx.input.isKeyPressed(Input.Keys.E);
        }*/
        return false;
    }
}
