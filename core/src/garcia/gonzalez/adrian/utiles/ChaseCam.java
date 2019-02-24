package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import garcia.gonzalez.adrian.entidades.Entidad;

public class ChaseCam {

    // Cámara que usaremos para seguir
    private Camera camera;

    // Objetivo de la cámara a seguir
    private Entidad target;

    // Si la cámara sigue o no a GigaGal
    private boolean following=true;

    public ChaseCam(Camera camera, Entidad target) {
        this.camera = camera;
        this.target = target;

    }

    // Actualizamos la posición de la cámara respecto a la posición de GigaGal
    public void update(float delta) {
        // Cambiamos de modo si pulsamos espacio
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
            following = ! following;
        }

        if (following) {
            Vector2 position = target.getCenter();
            camera.position.x = position.x;
            camera.position.y = position.y<50 ? 50 : position.y;
        } else {
            // Si pulsamos las teclas A S D W mientras está en modo no following
            // Modificaremos la posición de la cámara
            if (Gdx.input.isKeyPressed(Keys.A)) {
                camera.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }

            if (Gdx.input.isKeyPressed(Keys.D)) {
                camera.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }

            if (Gdx.input.isKeyPressed(Keys.W)) {
                camera.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }

            if (Gdx.input.isKeyPressed(Keys.S)) {
                camera.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED;

            }
        }
        if (camera.position.y-Constants.WORLD_SIZE/2<0)
            camera.position.y = Constants.WORLD_SIZE/2;
    }
}

