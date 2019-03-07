package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.Time;

import garcia.gonzalez.adrian.entidades.Entidad;
import garcia.gonzalez.adrian.enums.Enums;

public class ChaseCam {

    private Camera camera;
    private Entidad target;

    private boolean following;

    private final float separacion; //Espacio de separación entre el personaje y el centro de la cámara
    private final float velocidad;
    private final float incrementoVelocidad;

    public ChaseCam(Camera camera, Entidad target) {
        this.camera = camera;
        this.target = target;

        velocidad = Constants.CHASE_CAM_MOVE_SPEED;
        separacion = target.getBando()== Enums.Bando.ALIADO ? Constants.CHASE_CAM_SEPARATION : -Constants.CHASE_CAM_SEPARATION;
        incrementoVelocidad = Constants.CHASE_CAM_MOVE_INCREMENT;

        following=true;
    }

    public void onResize (Viewport view, int width, int height) {
        view.update(width, height, true);
        camera.position.set (target.getPosition3D());
    }

    // Actualizamos la posición de la cámara respecto a la posición de GigaGal
    public void update(float delta) {
        if (following) {
            chaseCamera(target.getPosition3D(), delta);
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

        if (camera.position.y-Constants.WORLD_SIZE/5<0)
            camera.position.y = Constants.WORLD_SIZE/5;
    }

    /**
     * Movimiento inteligente de la cámara, esta se moverá lentamente al personaje sin dar la sensación
     * de que esté anclada al personaje
     * */
    private void chaseCamera (Vector3 posicionPersonaje, float delta) {
        float _distancia = camera.position.dst(posicionPersonaje);
        if(Math.abs(_distancia) >= 0.25f) {
            // Cogemos la distancia máxima que recorrerá la cámara en este frame
            float step = velocidad * (1 + (_distancia * incrementoVelocidad)) * delta;
            // Situamos la posición que queremos que esté el personaje situado respecto la cámara
            // (Este paso es por si queremos incluirle un offset)
            Vector3 _persPosition = new Vector3(posicionPersonaje.x + separacion, posicionPersonaje.y, posicionPersonaje.z);
            // Calculamos el trozo de distancia que recorrerá desde la posición actual hasta la
            // posición de destino
            Vector3 dst = Utils.moveTowards(camera.position, _persPosition, step);
            dst.y = dst.y < Constants.CHASE_CAM_MIN_HEIGHT ? Constants.CHASE_CAM_MIN_HEIGHT : dst.y;
            // Añadimos esa distancia
            camera.position.set(dst);
        }
    }


}