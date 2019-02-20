package garcia.gonzalez.adrian.entidades;

import garcia.gonzalez.adrian.enums.Enums.*;

/**
 * La característica especial de las unidades respecto a estructura es que podran moverse
 * y sufrir CC (Crown Control)
 * */
public abstract class Unidad extends Entidad {

    //TODO: Meter los buffos

    //TODO: Meter la velocidad

    //TODO: Meter el estado salto
    private Direccion direccion;
    private EstadoSalto estadoSalto;

    public Unidad(Bando bando) {
        super(bando);

        estadoSalto=EstadoSalto.EN_SUELO;
    }

    public Unidad(Bando bando, int x, int y) {
        super(bando, x, y);

        estadoSalto=EstadoSalto.EN_SUELO;
    }

    // Cada vez que el personaje se mueva
    public abstract boolean onMove(Direccion dir);
    // Al empezar el salto
    public abstract boolean onJumpStart (EstadoSalto estado);
    // Al caer al suelo
    public abstract void onJumpFinish();
    // Al ser cceado, devuelve el tiempo en segundos, si el resultado es 0 evita el cc
    public abstract float onCrownControl (CrowdControl cc, float time, Unidad destinatario);


    //TODO: Seguir
    /**
     * Mueve el personaje, este método no es heredable, usar en su lugar el método onMove().
     * */
    public final void Mover (Direccion dir) {
        //TODO: No deja moverse si está CCeado por Aturdimiento o KnockUp
        // if cc return

        // Activamos el onMove()
        boolean mover = onMove(dir);

        // Es probable que sea el propio personaje quien voluntariamente decida no moverse
        if (!mover) {
            return;
        }

        direccion = dir;

        // TODO: Seguir
    }

    /**
     * Salta el personaje, este método no es heredable, en su lugar utilizar el método onJumpStart()
     * */
    public final void Saltar () {
        //TODO: No deja moverse si está CCeado por Aturdimiento, pesado o KnockUp
        // if cc return

        // Activamos el onJumpStart, le pasamos si está o no en el suelo.
        // Será el personaje quien gestionará si quiere o no hacer un segundo salto
        boolean saltar = onJumpStart(estadoSalto);

        // Es probable que sea el propio personaje quien voluntariamente decida no saltar
        if (!saltar) {
            return;
        }

        int potenciaSalto = getEstadisticas().getAttr(AtribEnum.SALTO);

        // No va a saltar si no tiene la potencia suficiente para ello
        if (potenciaSalto<=0) {
            return;
        }

        estadoSalto = EstadoSalto.SALTANDO;
        // TODO: Seguir
    }




}
