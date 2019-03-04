package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Es la clase donde va almacenado la información de las habilidades de los personajes.
 * Aquí van cosas como el coste de mana que tiene utilizar la habilidad
 * */
public class Habilidad {

    private int coste;
    private TextureRegion textureRegion;

    private long lastUsed;  // Última vez que ha sido utilizado la habilidad
    private long nextUse;   // Próxima vez que puede utilizar la habilidad
    private long cooldown;  // Tiempo de enfriamiento de la habilidad. Cuando se utilice

    private boolean isUsed; // Ha sido utilizada ya. Se activa en DOWN y se activa en UP


    public Habilidad (float segundos, int coste, TextureRegion textureRegion) {
        nextUse = lastUsed = TimeUtils.nanoTime();
        this.coste = coste;
        this.textureRegion= textureRegion;
        setCooldown(segundos);
    }

    public void setCooldown (float segundos) {
        cooldown = Utils.secondsToNano(segundos);
    }

    public void reducirCooldown (float segundos) {
        if (TimeUtils.nanoTime()>nextUse)
            return;

        nextUse -= Utils.secondsToNano(segundos);
    }

    public boolean isCooldown () {
        return TimeUtils.nanoTime() < nextUse;
    }

    /**
     * Obtiene el porcentaje de 0 a 1 que le queda a la habilidad para que pueda
     * volver a usarse. Utilizado sobretodo para la interfaz del jugador.
     * */
    public float getCooldown () {
        float total =  nextUse - lastUsed;
        float actual = nextUse - TimeUtils.nanoTime();
        if (actual>total)
            return 1;
        return 1-actual/total;
    }

    public void setInCooldown (float cd) {
        if (cd>Constants.COOLDOWN_LIMIT)
            cd=Constants.COOLDOWN_LIMIT;

        long actual = TimeUtils.nanoTime();

        // Ponemos el tiempo actual y el tiempo que queda en nanosegundos para poderlo lanzar otra vez
        lastUsed = actual;
        nextUse = actual + Math.round(cooldown * ((double)(1-cd)));

    }

    public boolean isUsed () {
        return isUsed;
    }

    public void setUsed (boolean s) {
        isUsed = s;
    }

    public int getCoste() {
        return coste;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
