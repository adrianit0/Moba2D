package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.awt.Rectangle;

public class Constants {
    // CONFIGURACION INICIAL
    public static final Color MENU_BACKGROUND_COLOR = Color.BLACK;
    public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
    public static final float WORLD_SIZE = 240; // Para la cámara
    public static final float HUD_VIEWPORT_SIZE = 240;  // Para el HUD
    public static final float HUD_MARGIN = 5;
    public static final Vector2 TAM_BOTONES = new Vector2(32,32);

    public static final String TEXTURE_ATLAS_MINIONS = "images/minions.atlas";
    public static final String TEXTURE_ATLAS_CHARACTERS = "images/characters.atlas";
    public static final String TEXTURE_ATLAS_HABILITIES = "images/habilidades.atlas";

    // CONFIGURACION MENU
    public static final float DIFFICULTY_WORLD_SIZE = 480;

    public static final String GAME_NAME = "Nexus Defense";
    public static final String[] FONDO_MENU_DIFICULTAD = {
            "menu/menu_00.png", "menu/menu_01.png", "menu/menu_02.png", "menu/menu_03.png",
            "menu/menu_04.png", "menu/menu_05.png", "menu/menu_06.png", "menu/menu_07.png",
            "menu/menu_08.png", "menu/menu_09.png", "menu/menu_10.png", "menu/menu_11.png",
            "menu/menu_12.png", "menu/menu_13.png", "menu/menu_14.png", "menu/menu_15.png",
            "menu/menu_16.png", "menu/menu_17.png", "menu/menu_18.png", "menu/menu_19.png",
            "menu/menu_20.png", "menu/menu_21.png", "menu/menu_22.png", "menu/menu_23.png"
    };

    public static final String[] FONDO_MENU_PERSONAJE = {
            "menu/fondo2_0.png", "menu/fondo2_1.png", "menu/fondo2_2.png", "menu/fondo2_3.png"
    };

    public static final String[] FONDO_PARTIDA = {
            "escenario/fondo_juego_0.png", "escenario/fondo_juego_1.png",
            "escenario/fondo_juego_2.png", "escenario/fondo_juego_3.png"
    };

    public static final Vector2 MENU_BUTTON_SIZE = new Vector2(114, 45);
    public static final float MENU_BUTTON_Y = 120;

    public static final Vector2 MENU_CHARACTER_SIZE = new Vector2(120, 200);


    // CONFIGURACION PARTIDA
    public static final float CHASE_CAM_MOVE_SPEED = 30;   // Velocidad de la camara
    public static final float CHASE_CAM_MOVE_INCREMENT = 0.15f;
    public static final float CHASE_CAM_SEPARATION = 50;
    public static final float CHASE_CAM_MIN_HEIGHT = 50;
    public static final float CHASE_CAM_MIN_HEIGHT_2_PLAYER = 100;

    public static final Vector2 HUD_CHARACTER_LIFE_SIZE = new Vector2(50, 8);
    public static final Vector2 HUD_MINION_LIFE_SIZE = new Vector2(30, 4);
    public static final Vector2 HUD_STRUCTURE_LIFE_SIZE = new Vector2(100, 8);

    public static final float GRAVITY = 12;                 // Gravedad de todas las unidades
    public static final float TICK_UPDATE = 0.25f;          // Cada cuanto se hará el tick_update

    public static final float TENACITY_LIMIT = 0.99f;
    public static final float COOLDOWN_LIMIT = 0.5f;        // Límite de cooldown.

    public static final float GENERIC_HABILITY_DURATION = 0.15f;    // Duración para una animación genérica

    public static final int ALLY_CHARACTER_SPAWN_POSITION = -1300;
    public static final int ENEMY_CHARACTER_SPAWN_POSITION = 1300;
    public static final float TIME_FIRST_MINION_SPAWN = 5;   // Primera vez que spawnean los minions nada más empezar la partida
    public static final float TIME_MINION_SPAWN = 30;        // Tiempo que tarda en spawnearse los minions

    // PERSONAJE JUGABLE 01
    public static final String CHARACTER_01_NAME = "Yone";
    public static final float CHARACTER_01_RANGE_HABILITY_1 = 150f;
    public static final float CHARACTER_01_PASSIVE_MAX_DISTANCE = 300;
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
    public static final String[] TOWER_CANNON_EXPLOSION = {
            "frame0000", "frame0001", "frame0002", "frame0003", "frame0004", "frame0005", "frame0006",
            "frame0007", "frame0008", "frame0009", "frame0010", "frame0011", "frame0012", "frame0013",
            "frame0014", "frame0015", "frame0016", "frame0017", "frame0018", "frame0019", "frame0020",
            "frame0021", "frame0022", "frame0023", "frame0024", "frame0025", "frame0026", "frame0027",
            "frame0028", "frame0029", "frame0030", "frame0031", "frame0032", "frame0033", "frame0034",
            "frame0035", "frame0036", "frame0037", "frame0038", "frame0039", "frame0040", "frame0041",
            "frame0042", "frame0043", "frame0044", "frame0045", "frame0046", "frame0047", "frame0048",
            "frame0049", "frame0050", "frame0051", "frame0052", "frame0053", "frame0054", "frame0055",
            "frame0056", "frame0057", "frame0058", "frame0059", "frame0060", "frame0061", "frame0062",
            "frame0063", "frame0064", "frame0065", "frame0066", "frame0067", "frame0068", "frame0069",
            "frame0070", "frame0071", "frame0072", "frame0073", "frame0074", "frame0075", "frame0076",
            "frame0077", "frame0078", "frame0079", "frame0080", "frame0081"
    };

    // PERSONAJE JUGABLE 02
    public static final String CHARACTER_02_NAME = "Kurosuke";
    public static final String[] CHARACTER_02_IDLE = {
            "character_02-000", "character_02-001", "character_02-002", "character_02-003",
            "character_02-004", "character_02-005", "character_02-006", "character_02-007",
            "character_02-008", "character_02-009", "character_02-010"};
    public static final String[] CHARACTER_02_WALK = {
            "character_02-011", "character_02-012", "character_02-013", "character_02-014",
            "character_02-015", "character_02-016", "character_02-017"};
    public static final String[] CHARACTER_02_JUMP = {
            "character_02-021", "character_02-022", "character_02-023", "character_02-024",
            "character_02-025", "character_02-026"};
    public static final String[] CHARACTER_02_ATTACK_01 = {
            "character_02-030", "character_02-031", "character_02-032", "character_02-033",
            "character_02-034", "character_02-035", "character_02-036", "character_02-037",
            "character_02-038", "character_02-039", "character_02-040", "character_02-041",
            "character_02-042", "character_02-043", "character_02-044"};
    public static final String[] CHARACTER_02_ATTACK_02 = {
            "character_02-141", "character_02-142", "character_02-159", "character_02-195", "character_02-194"};
    public static final String[] CHARACTER_02_ATTACK_03 = {
            "character_02-070", "character_02-071", "character_02-072", "character_02-073",
            "character_02-074", "character_02-075", "character_02-076", "character_02-077",
            "character_02-078", "character_02-079", "character_02-080", "character_02-081",
            "character_02-082", "character_02-083", "character_02-084", "character_02-085",
            "character_02-086", "character_02-087", "character_02-088", "character_02-089",
            "character_02-090", "character_02-091", "character_02-092", "character_02-093",
            "character_02-094"
    };
    public static final String[] CHARACTER_02_DEATH = {
            "character_02-097", "character_02-098", "character_02-099", "character_02-100",
            "character_02-101", "character_02-102", "character_02-103", "character_02-104",
            "character_02-105", "character_02-106", "character_02-107", "character_02-108",
            "character_02-109", "character_02-110", "character_02-111", "character_02-112",
            "character_02-113", "character_02-114", "character_02-115", "character_02-116",
            "character_02-117", "character_02-118", "character_02-119", "character_02-120",
            "character_02-121", "character_02-122", "character_02-123", "character_02-124",
            "character_02-125", "character_02-126", "character_02-127"
    };
    public static final float CHARACTER_02_HAB_DURATION = 0.1f;
    public static final Vector2 CHARACTER_02_HAB1_RANGE = new Vector2(80, 80);
    public static final Vector2 CHARACTER_02_HAB3_RANGE = new Vector2(100, 50);


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

    // STRUCTURES
    public static final float TURRET_RELOAD_TIME = 1.20f;
    public static final Vector2 TURRET_RANGE = new Vector2(350, 150);

}
