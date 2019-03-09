package garcia.gonzalez.adrian.controladorPersonaje;

import garcia.gonzalez.adrian.enums.Enums;

public class ControladorInteligenciaArtificial extends Controlador {

    private InteligenciaArtificial ia;

    public ControladorInteligenciaArtificial (InteligenciaArtificial ia) {
        super();

        this.ia = ia;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        ia.nextFrame();
        ia.update(delta);

    }

    @Override
    public boolean onKeyDown(Enums.TeclasJugador tecla) {
        return ia.isKeyDown(tecla);
    }

    @Override
    public boolean onKeyPressing(Enums.TeclasJugador tecla) {
        return ia.isKeyStay(tecla);
    }

    @Override
    public boolean onKeyUp(Enums.TeclasJugador tecla) {
        return ia.isKeyUp(tecla);
    }
}
