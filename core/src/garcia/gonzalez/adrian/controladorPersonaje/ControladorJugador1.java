package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Input;

import garcia.gonzalez.adrian.enums.Enums;

public class ControladorJugador1 extends Controlador {

    @Override
    public boolean onKeyDown(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return input.isKeyDown(Input.Keys.D);
            case MOVER_IZQUIERDA:       return input.isKeyDown(Input.Keys.A);
            case SALTAR:                return input.isKeyDown(Input.Keys.W);
            case AGACHAR:               return input.isKeyDown(Input.Keys.S);
            case BOTON_HABILIDAD_1:     return input.isKeyDown(Input.Keys.J);
            case BOTON_HABILIDAD_2:     return input.isKeyDown(Input.Keys.K);
            case BOTON_HABILIDAD_3:     return input.isKeyDown(Input.Keys.L);
        }
        return false;
    }

    @Override
    public boolean onKeyPressing(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return input.isKeyPressing(Input.Keys.D);
            case MOVER_IZQUIERDA:       return input.isKeyPressing(Input.Keys.A);
            case SALTAR:                return input.isKeyPressing(Input.Keys.W);
            case AGACHAR:               return input.isKeyPressing(Input.Keys.S);
            case BOTON_HABILIDAD_1:     return input.isKeyPressing(Input.Keys.J);
            case BOTON_HABILIDAD_2:     return input.isKeyPressing(Input.Keys.K);
            case BOTON_HABILIDAD_3:     return input.isKeyPressing(Input.Keys.L);
        }
        return false;
    }

    @Override
    public boolean onKeyUp(Enums.TeclasJugador tecla) {
        switch (tecla) {
            case MOVER_DERECHA:         return input.isKeyUp(Input.Keys.D);
            case MOVER_IZQUIERDA:       return input.isKeyUp(Input.Keys.A);
            case SALTAR:                return input.isKeyUp(Input.Keys.W);
            case AGACHAR:               return input.isKeyUp(Input.Keys.S);
            case BOTON_HABILIDAD_1:     return input.isKeyUp(Input.Keys.J);
            case BOTON_HABILIDAD_2:     return input.isKeyUp(Input.Keys.K);
            case BOTON_HABILIDAD_3:     return input.isKeyUp(Input.Keys.L);
        }
        return false;
    }
}
