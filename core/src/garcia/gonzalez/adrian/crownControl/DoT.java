package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

public class DoT extends CC {
    public DoT(String nombreIdentificativo, Enums.CrowdControl tipo, float duracion) {
        super(nombreIdentificativo, tipo, duracion);
    }
    // TODO: SEGUIr

    @Override
    public void aplicarCC(Unidad unidad) {

    }

    @Override
    public void aplicandoCCTick(Unidad unidad, float deltaTick) {

    }

    @Override
    public void aplicandoCCUpdate(Unidad unidad, float deltaUpdate) {

    }

    @Override
    public void terminarCC(Unidad unidad) {

    }
}
