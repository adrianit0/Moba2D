package garcia.gonzalez.adrian.crownControl;

import garcia.gonzalez.adrian.entidades.Unidad;
import garcia.gonzalez.adrian.enums.Enums;

public class ModificacionAtrib extends CC {
    private float cantidad;
    private Enums.AtribEnum atributo;

    public ModificacionAtrib(String nombreIdentificativo, Enums.AtribEnum atributo, float cantidad, float duracion) {
        super(nombreIdentificativo, cantidad>0? Enums.CrowdControl.AUMENTO_ATRIB : Enums.CrowdControl.REDUCION_ATRIB, duracion);

        this.cantidad = cantidad;
        this.atributo = atributo;
    }

    @Override
    public void aplicarCC(Unidad unidad) {
        unidad.getAtributos().aumentarBonus(atributo, cantidad);
    }

    @Override
    public void aplicandoCCTick(Unidad unidad, float deltaTick) {

    }

    @Override
    public void aplicandoCCUpdate(Unidad unidad, float deltaUpdate) {

    }

    @Override
    public void terminarCC(Unidad unidad) {
        unidad.getAtributos().aumentarBonus(atributo, -cantidad);
    }
}
