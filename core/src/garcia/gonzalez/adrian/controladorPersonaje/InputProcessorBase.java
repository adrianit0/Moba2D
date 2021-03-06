package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;

public abstract class InputProcessorBase implements InputProcessor {

    private LinkedList<Integer> lastTeclas;   // Teclas pulsadas en el último frame
    private LinkedList<Integer> teclas;       // Teclas pulsadas en este frame


    public InputProcessorBase() {
        teclas = new LinkedList<Integer>();
        lastTeclas = new LinkedList<Integer>();
    }

    public abstract void setViewport (Viewport view);
    public abstract void resize  ();

    public final void addTecla (Integer tecla) {
        if (!contieneTecla(tecla))
            teclas.add(tecla);
    }

    public final void removeTecla (Integer tecla) {
        if (contieneTecla(tecla))
            teclas.remove(tecla);
    }

    public final boolean contieneTecla (Integer tecla) {
        return teclas.contains(tecla);
    }

    public final Integer getTecla (Integer pointer) {
        if (pointer>=teclas.size())
            return -1;
        return teclas.get(pointer);
    }

    public final void nextFrame () {
        lastTeclas = new LinkedList<Integer>(teclas);
    }

    public final boolean isKeyDown (int keycode) {
        //Gdx.app.log("KeyDOWN", "LT: "+lastTeclas+" AL: "+teclas + " keycode: " +keycode);
        return !lastTeclas.contains(keycode) && teclas.contains(keycode);
    }

    public final boolean isKeyPressing (int keycode) {
        return lastTeclas.contains(keycode) && teclas.contains(keycode);
    }

    public final boolean isKeyUp (int keycode) {
        //Gdx.app.log("KeyUP", "LT: "+lastTeclas+" AL: "+teclas + " keycode: " +keycode);
        return lastTeclas.contains(keycode) && !teclas.contains(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }
}
