package garcia.gonzalez.adrian.screens;

import com.badlogic.gdx.Game;

import garcia.gonzalez.adrian.entidades.personajes.Personaje;
import garcia.gonzalez.adrian.enums.Enums.*;
import garcia.gonzalez.adrian.screens.GameplayScreen;
import garcia.gonzalez.adrian.utiles.Assets;

public class MobaGame extends Game {
	/**
	 * Creamos directamente la partida
	 * */
	public void create() {
        // Inicializamos el singleton de los assets
        Assets.instance.init();

		irMenu();
	}

	public void seleccionarPersonaje (Dificultad dificultad) {
		setScreen(new ChooseCharacterScreen(this, dificultad));
	}

	public void empezarPartida (Dificultad dificultad, int personajeID) {
		setScreen(new GameplayScreen(this, dificultad, personajeID));
	}

	public void irMenu () {
        setScreen(new MenuScreen (this));
    }
}
