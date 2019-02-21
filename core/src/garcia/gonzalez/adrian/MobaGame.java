package garcia.gonzalez.adrian;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MobaGame extends Game {
	/**
	 * Creamos directamente la partida
	 * */
	public void create() {
		setScreen(new GameplayScreen());
	}
}
