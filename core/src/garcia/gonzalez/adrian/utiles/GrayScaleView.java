package garcia.gonzalez.adrian.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GrayScaleView {
    final String VERT =
            "attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
                    "attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "attribute vec2 "+ ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +

                    "uniform mat4 u_projTrans;\n" +
                    " \n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +

                    "void main() {\n" +
                    "       vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "       vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
                    "       gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "}";

    final String FRAG =
            //GL ES specific stuff
            "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" + //
                    "varying LOWP vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform float grayscale;\n" +
                    "void main() {\n" +
                    "       vec4 texColor = texture2D(u_texture, vTexCoord);\n" +
                    "       \n" +
                    "       float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
                    "       texColor.rgb = mix(vec3(gray), texColor.rgb, grayscale);\n" +
                    "       \n" +
                    "       gl_FragColor = texColor * vColor;\n" +
                    "}";

    private ShaderProgram shader;
    private float grayscale = 0f;

    private float fromGrayscale = 0f;
    private float toGrayscale = 1f;
    private boolean working = true;

    private boolean animation = false;
    private float lerp = 0;

    public GrayScaleView (SpriteBatch batch) {
        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(VERT, FRAG);

        //shader didn't compile.. handle it somehow
        if (!shader.isCompiled()) {
            working=false;
            Gdx.app.error("SHADER", "El shader no ha compilado correctamente: "+shader.getLog());
            return;
        }

        //En caso de que hubiera algún aviso
        if (shader.getLog().length()!=0)
            Gdx.app.log("Shaders Warnings", shader.getLog());

        batch.setShader(shader);
    }

    public void update (float delta) {
        if (!working)
            return;

        if (animation) {
            lerp += delta;

            if (lerp>1) {
                //animation=false;
                lerp=1;
            }

            grayscale = MathUtils.lerp(fromGrayscale,toGrayscale, lerp);
        }


        shader.begin();
        shader.setUniformf("grayscale", grayscale);
        shader.end();

        Gdx.app.log("Grayscale", grayscale+"");
    }

    public void setGrayscaleTime (boolean setGrayscale) {
        if (!working )
            return;

        fromGrayscale = setGrayscale ? 1 : 0;
        toGrayscale = setGrayscale ? 0 : 1;

        lerp = 0;
        animation = true;
    }


    public void setGrayscale(boolean setGrayscale) {
        if (!working)
            return;

        grayscale = setGrayscale ? 0 : 1;

        shader.begin();
        shader.setUniformf("grayscale", grayscale);
        shader.end();
    }

    public void dispose() {
        shader.dispose();
    }
}
