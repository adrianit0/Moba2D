package garcia.gonzalez.adrian.controladorPersonaje;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import garcia.gonzalez.adrian.utiles.Constants;

public class InputProcessorAndroid extends InputProcessorBase {

    private Map<Rectangle, Integer> botones;
    private Map<Integer, Integer> teclas;

    private  Viewport viewport;

    private boolean configurado;

    public InputProcessorAndroid() {
        super();

        configurado=false;
        botones = new HashMap<Rectangle, Integer>();
        teclas = new HashMap<Integer, Integer>();
    }

    private void recalcularRectangulos () {
        final Vector2 tamHab = Constants.TAM_BOTONES;
        configurado=true;

        botones.put(new Rectangle(Constants.HUD_MARGIN*2, Constants.HUD_MARGIN*2, tamHab.x, tamHab.y), Input.Keys.A); // IZQUIERDA
        botones.put(new Rectangle(Constants.HUD_MARGIN*2+tamHab.x*2+10, Constants.HUD_MARGIN*2, tamHab.x, tamHab.y), Input.Keys.D); // DERECHA
        botones.put(new Rectangle(Constants.HUD_MARGIN*2+tamHab.x*1+5, Constants.HUD_MARGIN*5, tamHab.x, tamHab.y), Input.Keys.W); // SALTO

        botones.put(new Rectangle(viewport.getWorldWidth()-Constants.HUD_MARGIN*2-tamHab.x-tamHab.x*2-2*5,Constants.HUD_MARGIN*2, tamHab.x, tamHab.y), Input.Keys.J); // HABILIDAD 1
        botones.put(new Rectangle(viewport.getWorldWidth()-Constants.HUD_MARGIN*2-tamHab.x-tamHab.x*1-1*5,Constants.HUD_MARGIN*2 + 10, tamHab.x, tamHab.y), Input.Keys.K); // HABILIDAD 2
        botones.put(new Rectangle(viewport.getWorldWidth()-Constants.HUD_MARGIN*2-tamHab.x,Constants.HUD_MARGIN*2 + 20, tamHab.x, tamHab.y), Input.Keys.L); // HABILIDAD 3
    }

    @Override
    public void setViewport(Viewport view) {
        viewport = view;
    }

    @Override
    public void resize() {
        recalcularRectangulos();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!configurado && viewport.getWorldWidth()!=0)
            recalcularRectangulos();

        Vector2 posScreen = new Vector2(screenX, screenY);
        Vector2 viewportPosition = viewport.unproject(posScreen);

        // Para que el jugador sepa que ha pulsado el botón
        Gdx.input.vibrate(100);

        Gdx.app.log("touchDown", pointer+"");

        for (Map.Entry<Rectangle, Integer> entry : botones.entrySet()) {
            if (entry.getKey().contains(viewportPosition)) {
                getTeclas().add(entry.getValue());
                teclas.put(pointer, entry.getValue());
                break;
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 posScreen = new Vector2(screenX, screenY);
        Vector2 viewportPosition = viewport.unproject(posScreen);

        Gdx.app.log("touchUp", pointer+"");

        for (Map.Entry<Rectangle, Integer> entry : botones.entrySet()) {
            if (entry.getKey().contains(viewportPosition)) {
                getTeclas().remove(entry.getValue());
                teclas.put(pointer, -1);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 posScreen = new Vector2(screenX, screenY);
        Vector2 viewportPosition = viewport.unproject(posScreen);

        Gdx.app.log("touchDragged", pointer+"");

        boolean encontrado = false;
        for (Map.Entry<Rectangle, Integer> entry : botones.entrySet()) {
            if (entry.getKey().contains(viewportPosition)) {
                if (getTeclas().contains(pointer) && getTeclas().get(pointer)!=entry.getValue()) {
                    getTeclas().remove(getTeclas().get(pointer));
                    getTeclas().add(entry.getValue());
                    teclas.put(pointer, entry.getValue());
                }
                // TODO: Seguir con esto para arreglarlo
                /*else {
                    getTeclas().add(entry.getValue());
                    teclas.put(pointer, entry.getValue());
                }*/

                encontrado=true;
                break;
            }
        }
        if (!encontrado) {
            getTeclas().remove(teclas.get(pointer));
        }
        return true;
    }
}

