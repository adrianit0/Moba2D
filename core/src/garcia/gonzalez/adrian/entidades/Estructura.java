package garcia.gonzalez.adrian.entidades;

import garcia.gonzalez.adrian.enums.Enums.*;

/*
* La característica de las estructuras es que estan quietas y no pueden sufrir CC.
* */
public abstract class Estructura extends Entidad {
    public Estructura(Bando bando) {
        super(bando);
    }

    public Estructura(Bando bando, int x, int y) {
        super(bando, x, y);
    }


}
