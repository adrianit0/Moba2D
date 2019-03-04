package garcia.gonzalez.adrian.crownControl;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

/**
 * Es igual que el Knock-Up pero no supone una denegación del control, podrá seguir movimiendose.
 * */
public class Recoil extends CC{

    // El lanzamiento a los aires
    private Vector2 inicio;
    private Vector2 golpe;
    private float lerp;


    public Recoil(String nombreIdentificativo, Vector2 golpe, float duracion) {
        super(nombreIdentificativo, Enums.CrowdControl.RECOIL, duracion);
        this.inicio = golpe;
        this.golpe = golpe;
    }

    @Override
    // Los knockUp no terminan con el tiempo, terminan cuando el objetivo toca el suelo.
    public boolean hasFinished(Unidad unidad, long actual) {
        return unidad.getEstadoSalto()== Enums.EstadoSalto.EN_SUELO && lerp==1;
    }

    @Override
    public void aplicarCC(Unidad unidad) {
        unidad.resetJump();
    }

    @Override
    public void aplicandoCCUpdate(Unidad unidad, float deltaUpdate) {
        // De ser mayor de 1, entonces no sigue
        if (lerp<1) {
            lerp += deltaUpdate/getDuracion();

            if (lerp>1)
                lerp=1;
        }

        // Reducimos el efecto del knockBack hasta llegar a 0
        golpe = new Vector2(
                unidad.getEstadoSalto()== Enums.EstadoSalto.EN_SUELO ? MathUtils.lerp(inicio.x,0, lerp) : inicio.x,
                MathUtils.lerp(inicio.y,0, lerp));

        // Incluimos el golpe del knock-up
        unidad.aumentarKnockUp(golpe);
    }

    @Override
    public void aplicandoCCTick(Unidad unidad, float deltaTick) { }

    @Override
    public void terminarCC(Unidad unidad) {

    }
}
