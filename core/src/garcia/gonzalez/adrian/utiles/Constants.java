package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    // CONFIGURACION INICIAL
    public static final Color BACKGROUND_COLOR = Color.SKY;
    public static final float WORLD_SIZE = 160; // Para la cámara
    public static final String TEXTURE_ATLAS = "images/minions.atlas";
    public static final float CHASE_CAM_MOVE_SPEED = 128;   // Velocidad de la camara
    public static final float GRAVITY = 12;                 // Gravedad de todas las unidades
    public static final float TICK_UPDATE = 0.25f;          // Cada cuanto se hará el tick_update

    // MINIONS
    public static final String[] MINION_SPAWN =  { "minion_%s-05", "minion_%s-06", "minion_%s-07", "minion_%s-08", "minion_%s-09", "minion_%s-10" };
    public static final String[] MINION_WALK =   { "minion_%s-11", "minion_%s-12", "minion_%s-13" };
    public static final String[] MINION_ATTACK = { "minion_%s-14", "minion_%s-15", "minion_%s-16", "minion_%s-17", "minion_%s-18" };
    public static final String[] MINION_DEAD =   { "minion_%s-19", "minion_%s-20", "minion_%s-21", "minion_%s-22", "minion_%s-23", "minion_%s-24", "minion_%s-25", "minion_%s-26" };

    public static final String MINION_ALIADO = "aliado";
    public static final String MINION_ENEMIGO = "enemigo";

    public static final float MINION_SPAWN_DURATION = 0.15f;
    public static final float MINION_WALK_DURATION = 0.15f;
    public static final float MINION_ATTACK_DURATION = 0.2f;
    public static final float MINION_DEATH_DURATION = 0.15f;

    public static final float MINION_LAST_WHISPERS = 0.2f;
    public static final float MINION_DISSAPEAR_DURATION = 1f;

}
