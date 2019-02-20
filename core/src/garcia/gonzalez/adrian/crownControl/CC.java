package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums.*;

/**
 * Clase CC, que es el control sobre el adversario, aquí se incluiran tanto beneficiosos
 * como contraproducentes, pero para resumir todos seran CC.
 *
 * Es abstracto porque tendrá 1 o varios hijos según el tipo de CC que sea:
 *      - Aturdimiento, ralentizacion: Cantidad (float)
 *      - Pesado, Sangrado: Nada
 *      - KnockBack:    Tendrá un Vector2 con la dirección que lo lanza
 *      - AumentoAtrib: Tendrá un enum con el parámetro que aumenta, y la cantidad (FLOAT)
 *      - DoT, HoT:     Tendrá el valor de daño o cura por segundo (Un FLOAT)
 * */
public abstract class CC {
    private String nombreIdentificativo;
    private CrowdControl tipo;
    private float duracion; // En segundos

    private long finCC;

    public CC (String nombreIdentificativo, CrowdControl tipo, float duracion) {
        this.nombreIdentificativo = nombreIdentificativo;
        this.tipo = tipo;
        this.duracion = duracion;
        this.finCC = System.currentTimeMillis() + Math.round(duracion*1000);
    }

    /**
     * Un CC termina cuando el tiempo actual es mayor o actual al del estado
     * */
    public boolean hasFinished(long actual) {
        return actual>=finCC;
    }

    // Empezar el efecto del CC
    public abstract void aplicarCC (Unidad unidad);

    // Aplicando el efecto del CC
    public abstract void aplicandoCC (Unidad unidad);

    // Terminar el efecto del CC
    public abstract void terminarCC (Unidad unidad);
}
