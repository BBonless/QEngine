package Root.Shaders;

import Root.Misc.Util.Util;
import org.joml.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL40.*;

public class ShaderProgram {

    public int ProgramHandle, VertexHandle, GeometryHandle, FragmentHandle;

    public ShaderProgram(String VertexShader, String FragmentShader) {
        ProgramHandle = glCreateProgram();

        VertexHandle = glCreateShader(GL_VERTEX_SHADER);
        FragmentHandle = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(VertexHandle, VertexShader);
        glShaderSource(FragmentHandle, FragmentShader);

        glCompileShader(VertexHandle);
        if (glGetShaderi(VertexHandle, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Could not compile Vertex Shader!");
        }

        glCompileShader(FragmentHandle);
        if (glGetShaderi(FragmentHandle, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Could not compile Fragment Shader!");
        }

        glAttachShader(ProgramHandle, VertexHandle);
        glAttachShader(ProgramHandle, FragmentHandle);

        glLinkProgram(ProgramHandle);
        glValidateProgram(ProgramHandle);
        if (glGetProgrami(ProgramHandle, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Could not validate shader!");
        }

        glDeleteShader(VertexHandle);
        glDeleteShader(FragmentHandle);
    }

    public ShaderProgram(String VertexShader, String GeometryShader, String FragmentShader) {
        ProgramHandle = glCreateProgram();

        VertexHandle = glCreateShader(GL_VERTEX_SHADER);
        GeometryHandle = glCreateShader(GL_GEOMETRY_SHADER);
        FragmentHandle = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(VertexHandle, VertexShader);
        glShaderSource(GeometryHandle, GeometryShader);
        glShaderSource(FragmentHandle, FragmentShader);

        glCompileShader(VertexHandle);
        if (glGetShaderi(VertexHandle, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Could not compile Vertex Shader!");
        }

        glCompileShader(GeometryHandle);
        if (glGetShaderi(GeometryHandle, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Could not compile Geometry Shader!");
        }

        glCompileShader(FragmentHandle);
        if (glGetShaderi(FragmentHandle, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Could not compile Fragment Shader!");
        }

        glAttachShader(ProgramHandle, VertexHandle);
        glAttachShader(ProgramHandle, GeometryHandle);
        glAttachShader(ProgramHandle, FragmentHandle);

        glLinkProgram(ProgramHandle);
        glValidateProgram(ProgramHandle);
        if (glGetProgrami(ProgramHandle, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Could not validate shader!");
        }

        glDeleteShader(VertexHandle);
        glDeleteShader(GeometryHandle);
        glDeleteShader(FragmentHandle);
    }

    public void Bind() {
        glUseProgram(ProgramHandle);
    }

    public void Unbind() {
        glUseProgram(0);
    }

    public void Destroy() {
        glDeleteProgram(ProgramHandle);
    }

    public void SetUniform(String UniformName, Object Value, UniformTypes Type) {
        switch (Type) {
            case FLOAT:
                glUniform1f(glGetUniformLocation(ProgramHandle, UniformName), (float)Value);
                break;
            case INT: case BOOL:
                glUniform1i(glGetUniformLocation(ProgramHandle, UniformName), (int)Value);
                break;
            case VEC2:
                Vector2f Vector2Value = (Vector2f)Value;
                glUniform2f(glGetUniformLocation(ProgramHandle, UniformName), Vector2Value.x, Vector2Value.y);
                break;
            case VEC3:
                Vector3f Vector3Value = (Vector3f)Value;
                glUniform3f(glGetUniformLocation(ProgramHandle, UniformName), Vector3Value.x, Vector3Value.y, Vector3Value.z);
                break;
            case MAT4:
                Matrix4f Matrix4Value = (Matrix4f)Value;
                FloatBuffer UniformMatrix4 = MemoryUtil.memAllocFloat(4 * 4);
                Matrix4Value.get(UniformMatrix4);
                //UniformMatrix.flip();

                glUniformMatrix4fv(glGetUniformLocation(ProgramHandle, UniformName),false,  UniformMatrix4);
                break;
            case MAT3:
                Matrix3f Matrix3Value = (Matrix3f)Value;
                FloatBuffer UniformMatrix3 = MemoryUtil.memAllocFloat(3 * 3);
                Matrix3Value.get(UniformMatrix3);
                //UniformMatrix3.flip();

                glUniformMatrix3fv(glGetUniformLocation(ProgramHandle, UniformName),false,  UniformMatrix3);
                break;
            default:
                break;
        }
    }

    public static String LoadShaderResource(String ResourceToken) {
        try {
            return Util.InputStreamToString(ShaderProgram.class.getResourceAsStream(ResourceToken));
        } catch (Exception E) {
            System.err.println("Could not read Shader at (Resource Token): " + ResourceToken + " !!");
            return "";
        }
    }

    public static String LoadShaderFile(String Path) {
        try {
            return Files.readString(Paths.get(Path));
        } catch (Exception E) {
            System.err.println("Could not read Shader at: " + Path + " !!");
            return "";
        }
    }


}

