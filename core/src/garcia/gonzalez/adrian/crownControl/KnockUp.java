package garcia.gonzalez.adrian.crownControl;

import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

public class KnockUp extends CC {

    // El lanzamiento a los aires
    private Vector2 inicio;
    private Vector2 golpe;
    private float lerp;


    public KnockUp(String nombreIdentificativo, Enums.CrowdControl tipo, Vector2 golpe, float duracion) {
        super(nombreIdentificativo, tipo, duracion);
        this.inicio = golpe;
        this.golpe = golpe;
    }

    @Override
    // Los knockUp acaban cuando deja de aplicar fuerza.
    // La duración es el tiempo en el que tarda en dejar de aplicar dicha fuerza
    public boolean hasFinished(long actual) {
        return super.hasFinished(actual);
    }

    @Override
    public void aplicarCC(Unidad unidad) {

    }

    @Override
    public void aplicandoCC(Unidad unidad, float tickUpdate) {
        lerp += getDuracion() * tickUpdate;

        if (lerp>1)
            lerp=1;

        // Reducimos el efecto del knockBack hasta llegar a 0
        golpe = inicio.lerp(Vector2.Zero, lerp);

        // TODO: Aplicar el efecto del golpe
    }

    @Override
    public void terminarCC(Unidad unidad) {

    }
}
