package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.LinkedList;

public class MyInputProcessor implements InputProcessor{

    private LinkedList<Integer> lastTeclas;   // Teclas pulsadas en el último frame
    private LinkedList<Integer> teclas;       // Teclas pulsadas en este frame


    public MyInputProcessor () {
        teclas = new LinkedList<Integer>();
        lastTeclas = new LinkedList<Integer>();
    }

    public void nextFrame () {
        lastTeclas = new LinkedList<Integer>(teclas);
    }

    public boolean isKeyDown (int keycode) {
        //Gdx.app.log("KeyDOWN", "LT: "+lastTeclas+" AL: "+teclas + " keycode: " +keycode);
        return !lastTeclas.contains(keycode) && teclas.contains(keycode);
    }

    public boolean isKeyPressing (int keycode) {
        return lastTeclas.contains(keycode) && teclas.contains(keycode);
    }

    public boolean isKeyUp (int keycode) {
        //Gdx.app.log("KeyUP", "LT: "+lastTeclas+" AL: "+teclas + " keycode: " +keycode);
        return lastTeclas.contains(keycode) && !teclas.contains(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        teclas.add(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        teclas.remove(new Integer(keycode));
        return true;
    }


    // TODO: Pensar en implementación para dispositivos ANDROID
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
