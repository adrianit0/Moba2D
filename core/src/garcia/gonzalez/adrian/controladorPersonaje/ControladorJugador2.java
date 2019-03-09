package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Input;

import garcia.gonzalez.adrian.enums.Enums;

public class ControladorJugador2 extends Controlador {

    @Override
    public boolean onKeyDown(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return input.isKeyDown(Input.Keys.RIGHT);
            case MOVER_IZQUIERDA:       return input.isKeyDown(Input.Keys.LEFT);
            case SALTAR:                return input.isKeyDown(Input.Keys.UP);
            case AGACHAR:               return input.isKeyDown(Input.Keys.DOWN);
            case BOTON_HABILIDAD_1:     return input.isKeyDown(Input.Keys.NUMPAD_1);
            case BOTON_HABILIDAD_2:     return input.isKeyDown(Input.Keys.NUMPAD_2);
            case BOTON_HABILIDAD_3:     return input.isKeyDown(Input.Keys.NUMPAD_3);
        }
        return false;
    }

    @Override
    public boolean onKeyPressing(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return input.isKeyPressing(Input.Keys.RIGHT);
            case MOVER_IZQUIERDA:       return input.isKeyPressing(Input.Keys.LEFT);
            case SALTAR:                return input.isKeyPressing(Input.Keys.UP);
            case AGACHAR:               return input.isKeyPressing(Input.Keys.DOWN);
            case BOTON_HABILIDAD_1:     return input.isKeyPressing(Input.Keys.NUMPAD_1);
            case BOTON_HABILIDAD_2:     return input.isKeyPressing(Input.Keys.NUMPAD_2);
            case BOTON_HABILIDAD_3:     return input.isKeyPressing(Input.Keys.NUMPAD_3);
        }
        return false;
    }

    @Override
    public boolean onKeyUp(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return input.isKeyUp(Input.Keys.RIGHT);
            case MOVER_IZQUIERDA:       return input.isKeyUp(Input.Keys.LEFT);
            case SALTAR:                return input.isKeyUp(Input.Keys.UP);
            case AGACHAR:               return input.isKeyUp(Input.Keys.DOWN);
            case BOTON_HABILIDAD_1:     return input.isKeyUp(Input.Keys.NUMPAD_1);
            case BOTON_HABILIDAD_2:     return input.isKeyUp(Input.Keys.NUMPAD_2);
            case BOTON_HABILIDAD_3:     return input.isKeyUp(Input.Keys.NUMPAD_3);
        }
        return false;
    }
}
