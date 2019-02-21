package garcia.gonzalez.adrian.utiles;

import java.util.HashMap;

import garcia.gonzalez.adrian.enums.Enums.*;

// UNA LISTA DE TODAS LAS ESTADISTICAS DEL JUEGO
// Cada entidad tendrá sus propias estadísticas
public class Estadistica {

    HashMap<AtribEnum, Stat> estadisticas;

    public Estadistica () {
        estadisticas = new HashMap<AtribEnum, Stat>();
        AtribEnum[] stats = AtribEnum.values();

        for(AtribEnum s : stats ) {
            estadisticas.put(s, new Stat(s, 0, 0));
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
    public int getAttr (AtribEnum stat) {
        Stat s = estadisticas.get(stat);
        if (s==null)
            return 0;

        return Math.round(s.actual+s.bonus);
    }

    public int getMaxAttr (AtribEnum stat){
        Stat s = estadisticas.get(stat);
        if (s==null)
            return 0;

        return Math.round(s.max);
    }

    /**
     * Pone un bonus a un atributo
     * */
    public void setBonus (AtribEnum stat, float bonus) {
        Stat s = getOrCreateStat(stat);
        s.bonus = bonus;
    }

    /**
     * Devuelve unicamente la salud actual, si VIDA<=0, entonces el personaje está muerto.
     * */
    public int getSaludActual () {
        Stat s = estadisticas.get(AtribEnum.SALUD);
        if (s==null)
            return 0;

        return Math.round(s.actual);
    }

    public void setSaludActual (int nuevaVida) {
        Stat s = estadisticas.get(AtribEnum.SALUD);
        if (s==null) // Si el objetivo no tiene vida, entonces será invencible
            return;

        s.actual = nuevaVida;
    }

    /**
     * Devuelve unicamente el mana actual.
     * */
    public int getManaActual () {
        Stat s = estadisticas.get(AtribEnum.MANA);
        if (s==null)
            return 0;

        return Math.round(s.actual);
    }

    /**
     * Pide o crea una estadísticas
     * */
    private Stat getOrCreateStat (AtribEnum stat) {
        Stat s = estadisticas.get(stat);
        if (s==null)
            estadisticas.put(stat, new Stat(stat,0,0));

        return s;
    }

    // Las estadistica para para estadística.
    private class Stat {
        private AtribEnum tipo;
        private float max;
        private float actual;
        private float bonus;
        private float porNivel;

        public Stat (AtribEnum tipo, float value, float porNivel) {
            this.tipo = tipo;
            this.max = value;
            this.actual = value;
            this.porNivel = porNivel;
            this.bonus = 0;
        }

        public Stat (AtribEnum tipo, float max, float actual, float porNivel) {
            this.tipo = tipo;
            this.max = max;
            this.actual = actual;
            this.bonus = 0;
            this.porNivel = porNivel;
        }

        /**
         * Al subir nivel, aumenta sus atributos.
         * */
        public void subirNivel () {
            max += porNivel;
            actual += porNivel;
        }
    }
}
