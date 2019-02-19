package garcia.gonzalez.adrian.utiles;

import java.util.HashMap;

import garcia.gonzalez.adrian.enums.EstadEnum;

// UNA LISTA DE TODAS LAS ESTADISTICAS DEL JUEGO
// Cada entidad tendrá sus propias estadísticas
public class Estadistica {

    HashMap<EstadEnum, Stat> estadisticas;

    public Estadistica () {
        EstadEnum[] stats = EstadEnum.values();

        for(EstadEnum s : stats ) {
            estadisticas.put(s, new Stat(s, 0));
        }
    }

    // TODO: Seguir con las estadísticas
    public Estadistica(int saludMax /* .... */) {
        // TODO: HACER
    }

    /**
     * Cogemos el valor actual de un atributo.
     * Si el atributo no existe, entonces devolvemos 0
     * */
    public int getAttr (EstadEnum stat) {
        Stat s = estadisticas.get(stat);
        if (s==null)
            return 0;

        return s.actual+s.bonus;
    }

    public int getMaxAttr (EstadEnum stat){
        Stat s = estadisticas.get(stat);
        if (s==null)
            return 0;

        return s.max;
    }

    /**
     * Pone un bonus
     * */
    public void setBonus (EstadEnum stat, int bonus) {
        Stat s = getOrCreateStat(stat);
        s.bonus = bonus;
    }

    /**
     * Devuelve unicamente la salud actual, si VIDA<=0, entonces el personaje está muerto
     * */
    public int getSaludActual () {
        Stat s = estadisticas.get(EstadEnum.SALUD);
        if (s==null)
            return 0;

        return s.actual;
    }

    /**
     * Devuelve unicamente el mana actual.
     * */
    public int getManaActual () {
        Stat s = estadisticas.get(EstadEnum.MANA);
        if (s==null)
            return 0;

        return s.actual;
    }

    /**
     * Pide o crea una estadísticas
     * */
    private Stat getOrCreateStat (EstadEnum stat) {
        Stat s = estadisticas.get(stat);
        if (s==null)
            estadisticas.put(stat, new Stat(stat,0));

        return s;
    }

    // Las estadistica para para estadística.
    private class Stat {
        private EstadEnum tipo;
        private int max;
        private int actual;
        private int bonus;

        public Stat (EstadEnum tipo, int value) {
            this.tipo = tipo;
            this.max = value;
            this.actual = value;
            this.bonus = 0;
        }

        public Stat (EstadEnum tipo, int max, int actual) {
            this.tipo = tipo;
            this.max = max;
            this.actual = actual;
            this.bonus = 0;
        }
    }
}
