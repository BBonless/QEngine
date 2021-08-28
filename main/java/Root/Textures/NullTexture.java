package Root.Textures;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL40.*;

public class NullTexture {
    private static Texture Tex;

    public static void Create() {
        Texture Texture = new Texture(1,1);
        Texture.Handle = glGenTextures();

        ByteBuffer Data = BufferUtils.createByteBuffer(4 * 4);
        for (int i = 0; i < 4; i++) {
            Data.put((byte)255);
        }
        Data.position(0);

        glBindTexture(GL_TEXTURE_2D, Texture.Handle);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Texture.Width, Texture.Height, 0, GL_RGBA, GL_UNSIGNED_BYTE, Data);

        Tex = Texture;
    }

    public static int Get() {
        return Tex.Handle;
    }

}
