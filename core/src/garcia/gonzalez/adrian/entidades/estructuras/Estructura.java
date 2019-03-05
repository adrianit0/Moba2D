package garcia.gonzalez.adrian.entidades.estructuras;

import garcia.gonzalez.adrian.Level;
import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.enums.Enums.*;

/*
* La característica de las estructuras es que estan quietas y no pueden sufrir CC.
*
* de momento no tiene nada más especial aparte que comparte clase padre torre con nexo.
* */
public abstract class Estructura extends Entidad {
    //TODO: Seguir
    public Estructura(Bando bando, int x, int y, TipoEntidad tipoEntidad, Level level) {
        super(bando, x, y, tipoEntidad, level);
    }

}
