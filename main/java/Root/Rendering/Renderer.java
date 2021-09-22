package Root.Rendering;

import Root.Objects.Components.LightSource_Component;
import Root.Geometry.Mesh;
import Root.Shaders.ShaderProgram;
import Root.Shaders.UniformTypes;
import Root.Textures.NullTexture;
import Root.Objects.WorldObject;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL40.*;

public class Renderer {

    public static ShaderProgram CurrentShader;

    public static Camera Cam;

    public static LightSource_Component Light;

    public static void Init() {
        Cam = new Camera(
                45f,
                new Vector2f(1600f, 900f),
                0.1f,
                1000f
        );

        Cam.Position = new Vector3f(0, 0, 3);
    }

    public static Matrix4f GetMVP(WorldObject Object) {
        Matrix4f MVP = new Matrix4f().identity();

        Cam.GetProjectionMatrix().mul( Cam.GetViewMatrix(), MVP );
        MVP.mul( Object.GetModelMatrix() );

        return MVP;
    }

    public static void UploadMatrices(ShaderProgram Shader, WorldObject Object) {
        Shader.SetUniform("MVP", GetMVP(Object), UniformTypes.MAT4);

        Shader.SetUniform("Model", Object.GetModelMatrix(), UniformTypes.MAT4);

        Matrix4f AuxModelMatrix = new Matrix4f();
        Matrix3f NormalMatrix = new Matrix3f();

        Object.GetModelMatrix().get(AuxModelMatrix);
        AuxModelMatrix.invert();
        AuxModelMatrix.transpose();
        AuxModelMatrix.get3x3(NormalMatrix);

        Shader.SetUniform("Normal", NormalMatrix, UniformTypes.MAT3);
    }

    public static void Render(WorldObject Object) {
        Mesh ObjectMesh = Object.Mesh;

        CurrentShader.Bind();

        glBindVertexArray(ObjectMesh.VAO);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ObjectMesh.IBO);

        UploadMatrices(CurrentShader, Object);

        if (Light != null) {
            Light.Upload(CurrentShader);
        }

        Object.Material.Upload(CurrentShader);

        CurrentShader.SetUniform("viewPos", Camera.Position, UniformTypes.VEC3); //Hmmm

        if (Object.Texture != null) {
            glBindTexture(GL_TEXTURE_2D, Object.Texture.Handle);
        }
        else {
            glBindTexture(GL_TEXTURE_2D, NullTexture.Get());
        }

        int RenderMode = GL_TRIANGLES;
        if (Object.Name.equals("#WIREGIZMO")) {
            RenderMode = GL_LINES;
        }
        else if (Object.Name.equals("#POLYGIZMO")) {
            RenderMode = GL_LINES;
        }

        if (Object.Instanced) {
            glEnableVertexAttribArray(3);
            glDrawElementsInstanced(RenderMode, ObjectMesh.Indices.length, GL_UNSIGNED_INT, 0, Object.InstanceCount);
            glDisableVertexAttribArray(3);
        }
        else {
            glDrawElements(RenderMode, ObjectMesh.Indices.length, GL_UNSIGNED_INT, 0);
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        CurrentShader.Unbind();
    }
}

