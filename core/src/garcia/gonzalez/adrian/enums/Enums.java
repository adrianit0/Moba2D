package garcia.gonzalez.adrian.enums;

public class Enums {

    public enum Bando {
        ALIADO  (1),   // Pesonaje aliado
        ENEMIGO (-1),  // Personaje enemigo
        NEUTRAL (0);   // Personaje neutral o enemigo al mismo tiempo del aliado y enemigo.

        int dir;

        Bando (int dir) {
            this.dir = dir;
        }

        public int getDir() {
            return dir;
        }

        /**
         * Para devolver el bando contrario al que es el personaje.
         * */
        public Bando getContrario () {
            if (this==ALIADO) {
                return ENEMIGO;
            } else if (this==ENEMIGO) {
                return ALIADO;
            }

            return NEUTRAL;
        }
    };

    public enum TipoEntidad {
        PERSONAJE, ESBIRRO, TORRE, NEXO;

        public boolean esUnidad () {
            return this==PERSONAJE || this==ESBIRRO;
        }

        public boolean esEstructura () {
            return this==TORRE || this==NEXO;
        }
    }

    public enum EstadoPartida {
        VICTORIA, DERROTA
    }

    /**
     * Atributos del personaje.
     * */
    public enum AtribEnum {
        SALUD,          // Cantidad de salud que tiene un personaje, a salud<=0 este muere.
        MANA,           // Cantidad de mana que tiene un personaje, a costeHab>mana no podrá realizarlo
        REG_SALUD,      // Cantidad de salud que se cura cada segundo. aunque luego aumente por cada delta.
        REG_MANA,       // Cantidad de mana que consigue cada segundo aunque luego use por delta
        ATAQUE,         // Cantidad de ataque de sus ataques, depende de las habilidades
        SUCCION,        // Cantidad de vida que obtiene tras realizar un ataque.
        PENETRACION,    // Cantidad de penetracion de defensa. Se le resta a la defensa enemiga antes de calcular el daño
        DEFENSA,        // Defensa que tiene contra el daño.
        TENACIDAD,      // Reducción de los efectos de los CC (100% es inmune)
        VELOCIDAD,      // Velocidad de movimiento del personaje.
        SALTO,          // Fuerza que tiene el salto el personaje. Con 0 no puede saltar.
        COOLDOWN        // Reducción en sus habilidades, tiene un máximo en el 50%.
    };

    /*
    * Dirección del personaje, puede ser -1 (Izquierda), 1 (Derecha)
    * */
    public enum Direccion {
        IZQUIERDA   (-1),
        DERECHA     (1);

        int dir;
        Direccion (int dir) {
            this.dir = dir;
        }

        public int getDir(){
            return dir;
        }

        public Direccion getContrario () {
            return this==DERECHA ? IZQUIERDA : DERECHA;
        }
    };

    public enum EstadoSalto {
        PROPULSANDO,        // Los segundos entre que va a saltar y salta realmente
        SALTANDO,           // Mientras que el personaje está en el cielo
        EN_SUELO            // Cuando el personaje está en el suelo
    }

    /**
     * Esto está para diferenciar el CC, ya que hay algunos beneficiosos y otros no tanto.
     * Podría existir alguna habilidad que elimine efectos negativos, así que lo diferencio.
     * */
    public enum TipoCC {
        BUFF,   // Es un efecto beneficioso para el personaje
        DEBUFF  // Es un efecto negativo para el personaje
    }

    /**
     * Son todos los efectos que puede sufrir el personaje
     * */
    public enum CrowdControl {
        HEAL_OVER_TIME (TipoCC.BUFF),    // Cura por segundo
        DAMAGE_OVER_TIME (TipoCC.DEBUFF),// Daño por segundo
        AUMENTO_ATRIB (TipoCC.BUFF),     // Aumento temporal de un atributo (Pe: Ataque)
        REDUCION_ATRIB (TipoCC.DEBUFF),  // Reducción temporal de un atributo (Pe: Ataque)
        KNOCK_UP(TipoCC.DEBUFF),         // Movimiento forzado del objetivo (Normalmente hacia arriba).
        RECOIL(TipoCC.DEBUFF),           // Movimiento forzado del objetivo, pero no te bloquea el movimiento
        ATURDIMIENTO (TipoCC.DEBUFF),    // Negación temporal del personaje
        PESADO (TipoCC.DEBUFF),          // Anulación al salto temporal del personaje
        ACELERACION (TipoCC.BUFF),       // Aceleración temporal, se contrarresta con ralentizacion
        RALENTIZACION (TipoCC.DEBUFF),   // Ralentizacion temporal, se contrarresta con aceleracion
        SILENCIO (TipoCC.DEBUFF),        // Anulación temporal de las habilidades.
        SANGRADO (TipoCC.DEBUFF);        // Reduce al 50% la cura y la regeneración de vida.

        TipoCC tipo;

        CrowdControl (TipoCC tipo) {
            this.tipo = tipo;
        }

        public TipoCC getTipo() {
            return tipo;
        }
    }


    /**
     * Son las teclas de los jugadores que tendras que implementarse en
     * el paquete controladorPersonaje.
     *
     * */
    public enum TeclasJugador {
        MOVER_DERECHA,
        MOVER_IZQUIERDA,
        SALTAR,
        AGACHAR,
        BOTON_HABILIDAD_1,
        BOTON_HABILIDAD_2,
        BOTON_HABILIDAD_3
    }

    public enum Dificultad {
        FACIL,      // Ambas partes del mapa son iguales
        TWO_PLAYER,    // El enemigo tiene mejores atributos que los tuyos
        NORMAL  // Los propios minions podrian matarte
    }
}
