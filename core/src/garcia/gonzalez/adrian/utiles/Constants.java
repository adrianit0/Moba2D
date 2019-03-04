package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.awt.Rectangle;

public class Constants {
    // CONFIGURACION INICIAL
    public static final Color BACKGROUND_COLOR = Color.SKY;
    public static final float WORLD_SIZE = 240; // Para la cámara
    public static final float HUD_VIEWPORT_SIZE = 240;  // Para el HUD
    public static final float HUD_MARGIN = 5;

    public static final String TEXTURE_ATLAS_MINIONS = "images/minions.atlas";
    public static final String TEXTURE_ATLAS_CHARACTERS = "images/characters.atlas";
    public static final String TEXTURE_ATLAS_HABILITIES = "images/habilidades.atlas";

    public static final float CHASE_CAM_MOVE_SPEED = 30;   // Velocidad de la camara
    public static final float CHASE_CAM_MOVE_INCREMENT = 0.15f;
    public static final float CHASE_CAM_SEPARATION = 50;
    public static final float CHASE_CAM_MIN_HEIGHT = 50;

    public static final Vector2 HUD_CHARACTER_LIFE_SIZE = new Vector2(50, 8);
    public static final Vector2 HUD_MINION_LIFE_SIZE = new Vector2(30, 4);
    public static final Vector2 HUD_STRUCTURE_LIFE_SIZE = new Vector2(100, 8);

    public static final float GRAVITY = 12;                 // Gravedad de todas las unidades
    public static final float TICK_UPDATE = 0.25f;          // Cada cuanto se hará el tick_update

    public static final float TENACITY_LIMIT = 0.99f;
    public static final float COOLDOWN_LIMIT = 0.5f;        // Límite de cooldown.

    public static final float GENERIC_HABILITY_DURATION = 0.15f;    // Duración para una animación genérica

    public static final float TIME_FIRST_MINION_SPAWN = 5;   // Primera vez que spawnean los minions nada más empezar la partida
    public static final float TIME_MINION_SPAWN = 30;        // Tiempo que tarda en spawnearse los minions

    // PERSONAJE JUGABLE 01
    public static final float CHARACTER_01_RANGE_HABILITY_1 = 150f;
    public static final float CHARACTER_01_PASSIVE_MAX_DISTANCE = 300; //TODO: Cambiar
    public static final int CHARACTER_01_MAX_BALLS = 5;
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
    public static final String[] CHARACTER_01_ATTACK_01 = {
            "character_1-43", "character_1-44", "character_1-45", "character_1-46", "character_1-47"};
    public static final String[] CHARACTER_01_ATTACK_02 = {
            "character_1-48", "character_1-49", "character_1-50", "character_1-39",
            "character_1-40", "character_1-41", "character_1-42" };
    public static final String[] CHARACTER_01_ATTACK_03 = {
            "character_1-51", "character_1-52", "character_1-53", "character_1-54",
            "character_1-55", "character_1-56", "character_1-57", "character_1-58",
            "character_1-59", "character_1-60", "character_1-61", "character_1-62",
            "character_1-63", "character_1-64", "character_1-65", "character_1-66"};
    public static final String[] CHARACTER_01_DEATH = {
            "character_1-67", "character_1-68", "character_1-69", "character_1-70",
            "character_1-71", "character_1-72", "character_1-73", "character_1-74",
            "character_1-75", "character_1-76", "character_1-77", "character_1-78",
            "character_1-79", "character_1-80", "character_1-81", "character_1-82"};

    public static final String[] CHARACTER_01_POWER_BALL_EXPLOSION = {
            "explosion_energia-00", "explosion_energia-01", "explosion_energia-02",
            "explosion_energia-03", "explosion_energia-04", "explosion_energia-05",
            "explosion_energia-06", "explosion_energia-07", "explosion_energia-08",
            "explosion_energia-09", "explosion_energia-10", "explosion_energia-11"
    };
    public static final String[] CHARACTER_01_HIT = {
            "explosion_3-00", "explosion_3-01", "explosion_3-02", "explosion_3-03",
            "explosion_3-04", "explosion_3-05", "explosion_3-06", "explosion_3-07",
            "explosion_3-08", "explosion_3-09"
    };

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
