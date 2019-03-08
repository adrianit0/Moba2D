package garcia.gonzalez.adrian.screens;

import com.badlogic.gdx.Game;

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

		setScreen(new MenuScreen (this));
	}

	public void empezarPartida (Dificultad dificultad) {
		// TODO: Ponerle la dificultad
		setScreen(new GameplayScreen());
	}
}
