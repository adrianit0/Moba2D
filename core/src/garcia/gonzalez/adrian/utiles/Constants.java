package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    // CONFIGURACION INICIAL
    public static final Color BACKGROUND_COLOR = Color.SKY;
    public static final float WORLD_SIZE = 160; // Para la cámara
    public static final float HUD_VIEWPORT_SIZE = 240;  // Para el HUD
    public static final float HUD_MARGIN = 5;
    public static final String TEXTURE_ATLAS_MINIONS = "images/minions.atlas";
    public static final String TEXTURE_ATLAS_CHARACTERS = "images/characters.atlas";
    public static final float CHASE_CAM_MOVE_SPEED = 128;   // Velocidad de la camara
    public static final float GRAVITY = 12;                 // Gravedad de todas las unidades
    public static final float TICK_UPDATE = 0.25f;          // Cada cuanto se hará el tick_update
    public static final float COOLDOWN_LIMIT = 0.5f;        // Límite de cooldown.

    public static final float GENERIC_HABILITY_DURATION = 0.15f;    // Duración para una animación genérica

    // PERSONAJE JUGABLE 01
    public static final String[] CHARACTER_01_IDLE = {
            "character_1-00", "character_1-01", "character_1-02", "character_1-03",
            "character_1-04", "character_1-05", "character_1-06", "character_1-07",
            "character_1-08", "character_1-09", "character_1-10", "character_1-11"};
    public static final String[] CHARACTER_01_WALK = {
            "character_1-27", "character_1-28", "character_1-29", "character_1-30",
            "character_1-31", "character_1-32", "character_1-33", "character_1-34",
            "character_1-35", "character_1-36", "character_1-37", "character_1-38"};
    public static final String[] CHARACTER_01_JUMP = {
            "character_1-12", "character_1-13", "character_1-14", "character_1-15",
            "character_1-16", "character_1-17", "character_1-18", "character_1-19",
            "character_1-20", "character_1-21", "character_1-22", "character_1-23",
            "character_1-24", "character_1-25", "character_1-26"};

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
