package garcia.gonzalez.adrian.enums;

public enum EstadEnum {
    SALUD,          // Cantidad de salud que tiene un personaje, a salud<=0 este muere.
    MANA,           // Cantidad de mana que tiene un personaje, a costeHab>mana no podrá realizarlo
    REG_SALUD,      // Cantidad de salud que se cura cada segundo. aunque luego aumente por cada delta.
    REG_MANA,       // Cantidad de mana que consigue cada segundo aunque luego use por delta
    ATAQUE,         // Cantidad de ataque de sus ataques, depende de las habilidades
    PENETRACION,    // Cantidad de penetracion de defensa. Se le resta a la defensa enemiga antes de calcular el daño
    DEFENSA,        // Defensa que tiene contra el daño.
    VELOCIDAD,      // Velocidad de movimiento del personaje.
    COOLDOWN        // Reducción en sus habilidades, tiene un máximo en el 50%.
};