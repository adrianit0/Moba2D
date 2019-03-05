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
    private  Viewport viewport;

    public InputProcessorAndroid() {
        super();


        botones = new HashMap<Rectangle, Integer>();
    }

    private void recalcularRectangulos () {
        final Vector2 tamHab = Constants.TAM_BOTONES;

        botones.put(new Rectangle(Constants.HUD_MARGIN*2, Constants.HUD_MARGIN*2, tamHab.x, tamHab.y), Input.Keys.A); // IZQUIERDA
        botones.put(new Rectangle(Constants.HUD_MARGIN*2+tamHab.x*2+10, Constants.HUD_MARGIN*2, tamHab.x, tamHab.y), Input.Keys.D); // DERECHA
        botones.put(new Rectangle(Constants.HUD_MARGIN*2+tamHab.x*1+5, Constants.HUD_MARGIN*5, tamHab.x, tamHab.y), Input.Keys.W); // SALTO

        botones.put(new Rectangle(
                viewport.getWorldWidth()-Constants.HUD_MARGIN*2-tamHab.x-tamHab.x*2-2*5,
                Constants.HUD_MARGIN*2, tamHab.x, tamHab.y),
                Input.Keys.J); // HABILIDAD 1
        botones.put(new Rectangle(viewport.getWorldWidth()-Constants.HUD_MARGIN*2-tamHab.x-tamHab.x*1-1*5,Constants.HUD_MARGIN*2 + 10, tamHab.x, tamHab.y), Input.Keys.K); // HABILIDAD 2
        botones.put(new Rectangle(viewport.getWorldWidth()-Constants.HUD_MARGIN*2-tamHab.x,Constants.HUD_MARGIN*2 + 20, tamHab.x, tamHab.y), Input.Keys.L); // HABILIDAD 3

    }

    @Override
    public void setViewport(Viewport view) {
        viewport = view;
        recalcularRectangulos();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 posScreen = new Vector2(screenX, screenY);
        Vector2 viewportPosition = viewport.unproject(posScreen);

        Rectangle rect = new Rectangle(
                viewport.getWorldWidth()-Constants.HUD_MARGIN*2-32-32*2-2*5,
                Constants.HUD_MARGIN*2, 32, 32);
        Gdx.app.log("VIEWPORT", rect.toString() + " [SCREEN] "+posScreen.toString() );

        for (Map.Entry<Rectangle, Integer> entry : botones.entrySet()) {
            Gdx.app.log("RECTANGLE", entry.getKey().toString());
            if (entry.getKey().contains(viewportPosition)) {
                Gdx.app.log("PULSADO", "Se ha detectado que se ha pulsado la tecla" + entry.getValue());
                getTeclas().add(entry.getValue());
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 posScreen = new Vector2(screenX, screenY);
        Vector2 viewportPosition = viewport.unproject(posScreen);

        for (Map.Entry<Rectangle, Integer> entry : botones.entrySet()) {
            if (entry.getKey().contains(viewportPosition)) {
                getTeclas().remove(entry.getValue());
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return true;
    }

}

