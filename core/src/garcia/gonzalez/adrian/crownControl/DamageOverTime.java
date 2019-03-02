package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

public class DamageOverTime extends CC {
    private float cantidad;

    public DamageOverTime(String nombreIdentificativo, float cantidad, float duracion) {
        super(nombreIdentificativo, Enums.CrowdControl.DAMAGE_OVER_TIME, duracion);

        this.cantidad = cantidad;
    }

    @Override
    public void aplicarCC(Unidad unidad) {

    }

    @Override
    public void aplicandoCCTick(Unidad unidad, float deltaTick) {
        unidad.getAtributos().quitarSalud(cantidad*deltaTick/getDuracion());
    }

    @Override
    public void aplicandoCCUpdate(Unidad unidad, float deltaUpdate) {

    }

    @Override
    public void terminarCC(Unidad unidad) {

    }


}
