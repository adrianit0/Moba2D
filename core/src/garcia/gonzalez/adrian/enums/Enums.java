package garcia.gonzalez.adrian.enums;

public class Enums {

    public enum Bando {
        ALIADO,     // Pesonaje aliado
        ENEMIGO,    // Personaje enemigo
        NEUTRAL;    // Personaje neutral o enemigo al mismo tiempo del aliado y enemigo.

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

    /**
     * AtribEnum del personaje.
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
    };

    public enum EstadoSalto {
        SALTANDO,
        CAYENDO,
        EN_SUELO
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
        KNOCK_BACK (TipoCC.DEBUFF),      // Movimiento forzado del objetivo (Normalmente hacia arriba).
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
}
