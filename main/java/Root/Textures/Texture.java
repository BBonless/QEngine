package Root.Textures;

import org.lwjgl.stb.*;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Texture {
    public int Width=0,Height=0,ChannelCount=0,Handle=0;

    public Texture(String Filepath) {
        int[] WidthAr = new int[1];
        int[] HeightAr = new int[1];
        int[] ChannelCountAr = new int[1];

        ByteBuffer Image = STBImage.stbi_load(Filepath, WidthAr, HeightAr, ChannelCountAr, 0);

        if (Image == null) {
            System.err.println("Could not load Texture at: " + Filepath + " !!");
            return;
        }

        Width = WidthAr[0];
        Height = HeightAr[0];
        ChannelCount = ChannelCountAr[0];

        Handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, Handle);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Width, Height, 0 , GL_RGBA, GL_UNSIGNED_BYTE, Image);
        glGenerateMipmap(GL_TEXTURE_2D);

        STBImage.stbi_image_free(Image);
    }

    public Texture(int Width, int Height) {
        this.Width = Width;
        this.Height = Height;
    }
}
