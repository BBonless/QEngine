package Root.Textures;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Texture {
    public int Width=0,Height=0,ChannelCount=0,Handle=0;
    private String TextureSource = null;

    public Texture(String ResourceToken) {
        int[] WidthAr = new int[1];
        int[] HeightAr = new int[1];
        int[] ChannelCountAr = new int[1];

        ByteBuffer Image = null;

        try {
            byte[] ImageData = Texture.class.getResourceAsStream(ResourceToken).readAllBytes();
            ByteBuffer ImageDataBuffer = BufferUtils.createByteBuffer(ImageData.length);
            ImageDataBuffer.put(ImageData);
            ImageDataBuffer.flip();
            Image = STBImage.stbi_load_from_memory(
                    ImageDataBuffer,
                    WidthAr, HeightAr, ChannelCountAr, 4
            );
        } catch (Exception E) {
            System.err.println("Could not read Texture at (Resource Token): " + ResourceToken + " !!");
            return;
        }

        System.out.println("Here: " + Image == null);

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

    public static Texture FromFilepath(String Filepath) {
        int[] WidthAr = new int[1];
        int[] HeightAr = new int[1];
        int[] ChannelCountAr = new int[1];

        ByteBuffer Image = STBImage.stbi_load(Filepath, WidthAr, HeightAr, ChannelCountAr, 0);
        System.out.println(Image);
        if (Image == null) {
            System.err.println("Could not load Texture at: " + Filepath + " !!");
            return null;
        }

        int Width = WidthAr[0];
        int Height = HeightAr[0];
        int ChannelCount = ChannelCountAr[0];

        int Handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, Handle);
        System.out.println(Handle);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Width, Height, 0 , GL_RGBA, GL_UNSIGNED_BYTE, Image);
        glGenerateMipmap(GL_TEXTURE_2D);
        System.out.println("Idk");

        STBImage.stbi_image_free(Image);

        Texture NewTex = new Texture(
                Width,
                Height,
                ChannelCount,
                Handle
        );

        //NewTex.SetSrc(Filepath);

        return NewTex;
    }

    public Texture(int Width, int Height) {
        this.Width = Width;
        this.Height = Height;
    }

    public Texture(int Width, int Height, int ChannelCount, int Handle) {
        this.Width = Width;
        this.Height = Height;
        this.ChannelCount = ChannelCount;
        this.Handle = Handle;
    }

    public void SetSrc(String TexSrc) { TextureSource = TexSrc; }
}
