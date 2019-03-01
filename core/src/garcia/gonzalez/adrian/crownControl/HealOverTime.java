package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

public class HealOverTime extends CC {
    private float cantidad;

    public HealOverTime(String nombreIdentificativo, float cantidad, float duracion) {
        super(nombreIdentificativo, Enums.CrowdControl.HEAL_OVER_TIME, duracion);

        this.cantidad = cantidad;
    }
    // TODO: SEGUIr

    @Override
    public void aplicarCC(Unidad unidad) {

    }

    @Override
    public void aplicandoCCTick(Unidad unidad, float deltaTick) {
        unidad.getAtributos().curarSalud(cantidad*deltaTick/getDuracion());
    }

    @Override
    public void aplicandoCCUpdate(Unidad unidad, float deltaUpdate) {

    }

    @Override
    public void terminarCC(Unidad unidad) {

    }
}
