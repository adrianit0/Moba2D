package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.utiles.Constants;

/**
 * Clase CC, que es el control sobre el adversario, aquí se incluiran tanto beneficiosos
 * como contraproducentes, pero para resumir todos seran CC.
 *
 * Es abstracto porque tendrá 1 o varios hijos según el tipo de CC que sea:
 *      - Aturdimiento, ralentizacion: Cantidad (float)
 *      - Pesado, Sangrado: Nada
 *      - KnockBack:    Tendrá un Vector2 con la dirección que lo lanza
 *      - AumentoAtrib: Tendrá un enum con el parámetro que aumenta, y la cantidad (FLOAT)
 *      - DamageOverTime, HealOverTime:     Tendrá el valor de daño o cura por segundo (Un FLOAT)
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
    public boolean hasFinished(Unidad unidad, long actual) {
        return actual>=finCC;
    }

    // Empezar el efecto del CC
    public abstract void aplicarCC (Unidad unidad);

    // Aplicando el efecto del CC en cada tick. Sirve para el DOT.
    public abstract void aplicandoCCTick (Unidad unidad, float deltaTick);

    // Aplicando el efecto del CC en cada frame. Sirve para los Knock-back
    public abstract void aplicandoCCUpdate (Unidad unidad, float deltaUpdate);

    // Terminar el efecto del CC
    public abstract void terminarCC (Unidad unidad);

    /* Lo máximo de tenacidad es 99%*/
    public void efectoTenacidad (float tenacidad) {
        if (tenacidad>Constants.TENACITY_LIMIT)
            tenacidad=Constants.TENACITY_LIMIT;

        duracion *= tenacidad;
    }

    public String getNombreIdentificativo() {
        return nombreIdentificativo;
    }

    public CrowdControl getTipo() {
        return tipo;
    }

    public float getDuracion() {
        return duracion;
    }

    public long getFinCC() {
        return finCC;
    }
}
