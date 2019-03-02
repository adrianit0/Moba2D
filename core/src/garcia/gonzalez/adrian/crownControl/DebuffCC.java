package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

public class DebuffCC extends CC {
    public DebuffCC(String nombreIdentificativo, Enums.CrowdControl tipo, float duracion) {
        super(nombreIdentificativo, tipo, duracion);
    }

    @Override
    public void aplicarCC(Unidad unidad) {}
    @Override
    public void aplicandoCCTick(Unidad unidad, float deltaTick) {}
    @Override
    public void aplicandoCCUpdate(Unidad unidad, float deltaUpdate) {}
    @Override
    public void terminarCC(Unidad unidad) {}
}
