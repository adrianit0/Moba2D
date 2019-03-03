package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.text.View;

public class Escenario {

    private Viewport view;

    private final float w = 300;
    private final float oW = 150; // offset de width
    private final float h = 256;
    private final float oH = -10;  // offset de height

    public Escenario (Viewport view) {
        this.view = view;
    }

    public void render(SpriteBatch batch) {
        //TODO: Continuar con el escenarioAssets
        float posX = view.getCamera().position.x;
        float posY = view.getCamera().position.y-view.getCamera().viewportHeight/2;

        for (int i = -5; i < 6; i++) {
            batch.draw(Assets.instance.escenarioAssets.troncos5, posX*0.9f+w*i-oW, posY*0.25f-oH, w, h);
        }
        for (int i = -5; i < 6; i++) {
            batch.draw(Assets.instance.escenarioAssets.troncos4, posX*0.75f+w*i-oW, posY*0.2f-oH, w, h);
            batch.draw(Assets.instance.escenarioAssets.luz2, posX*0.55f+w*i-oW, posY*0.2f-oH, w, h);
        }
        for (int i = -5; i < 6; i++) {
            batch.draw(Assets.instance.escenarioAssets.troncos3, posX*0.5f+w*i-oW, posY*0.1f-oH, w, h);
        }
        for (int i = -5; i < 6; i++) {
            batch.draw(Assets.instance.escenarioAssets.troncos2, posX*0.25f+w*i-oW, posY*0.05f-oH, w, h);
            batch.draw(Assets.instance.escenarioAssets.luz1, posX*0.20f+w*i-oW, posY*0.05f-oH, w, h);
        }

        final Texture suelo2 = Assets.instance.escenarioAssets.suelo2;
        for (int i = -5; i < 6; i++) {
            batch.draw(Assets.instance.escenarioAssets.suelo1, posX * 0 + w * i - oW, 10, w, h);
            batch.draw(Assets.instance.escenarioAssets.troncos1, posX*0+w*i-oW, 0, w, h);
            batch.draw(suelo2, posX*0f+w*i-oW, 0, w, h);

            // TODO: Ver como puedo cambiar esto
            batch.draw(suelo2, posX*0f+w*i-oW, -256, w, h, 0, 0, suelo2.getWidth(), suelo2.getHeight(),false, true );
            batch.draw(suelo2, posX*0f+w*i-oW, -271, w, h, 0, 0, suelo2.getWidth(), suelo2.getHeight(),false, true );
            batch.draw(suelo2, posX*0f+w*i-oW, -286, w, h, 0, 0, suelo2.getWidth(), suelo2.getHeight(),false, true );
            batch.draw(suelo2, posX*0f+w*i-oW, -300, w, h, 0, 0, suelo2.getWidth(), suelo2.getHeight(),false, true );
            batch.draw(suelo2, posX*0f+w*i-oW, -315, w, h, 0, 0, suelo2.getWidth(), suelo2.getHeight(),false, true );
        }
        for (int i = -5; i < 6; i++) {
            batch.draw(Assets.instance.escenarioAssets.copa, posX*0+w*i-oW, -posY*0.05f+oH, w, h);
        }
    }
}
