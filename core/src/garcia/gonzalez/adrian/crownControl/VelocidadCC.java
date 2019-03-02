package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

public class VelocidadCC extends CC {
    private float porcentaje;
    private float cantidad;

    public VelocidadCC(String nombreIdentificativo, float porcentaje, float duracion) {
        super(nombreIdentificativo, porcentaje>0? Enums.CrowdControl.ACELERACION : Enums.CrowdControl.RALENTIZACION, duracion);

        this.porcentaje = porcentaje;
    }

    @Override
    public void aplicarCC(Unidad unidad) {
        cantidad = unidad.getAtributos().getAttrPorc(Enums.AtribEnum.VELOCIDAD)*(1+porcentaje);
        unidad.getAtributos().aumentarBonus(Enums.AtribEnum.VELOCIDAD, cantidad);
    }

    @Override
    public void aplicandoCCTick(Unidad unidad, float deltaTick) {

    }

    @Override
    public void aplicandoCCUpdate(Unidad unidad, float deltaUpdate) {

    }

    @Override
    public void terminarCC(Unidad unidad) {
        unidad.getAtributos().aumentarBonus(Enums.AtribEnum.VELOCIDAD, -cantidad);
    }
}
