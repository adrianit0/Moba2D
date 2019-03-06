package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.LinkedList;

public class InputProcessorDesktop extends InputProcessorBase{

    public InputProcessorDesktop() {
        super();
    }

    @Override
    public void setViewport(Viewport view) {
        //No lo necesita
    }

    @Override
    public void resize() {

    }

    @Override
    public boolean keyDown(int keycode) {
        getTeclas().add(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        getTeclas().remove(new Integer(keycode));
        return true;
    }
}
