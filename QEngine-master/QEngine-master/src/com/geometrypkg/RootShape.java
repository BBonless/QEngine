package com.geometrypkg;

import com.basepkg.InputManager;
import com.basepkg.TestTex;
import com.geometrypkg.coregeometry.Vertex;
import com.geometrypkg.coregeometry.NGon;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.FileInputStream;


public abstract class RootShape implements GLEventListener {

    protected Vertex ObjectLocation = new Vertex(0, 0, 0);

    public Vertex[] Verts = {};

    public NGon[] Polygons = {};

    public InputManager Input = null;

    private GLU vGLU = new GLU();

    public String TexSrcPath = "C:\\Users\\quent\\Desktop\\trump.jpg";
    private File TexSrc = new File(TexSrcPath);


    abstract void Update(GLAutoDrawable DRW);

    private void Render(GLAutoDrawable DRW){
        GL2 GfxLib = DRW.getGL().getGL2();
        try{
            Texture Tex = TextureIO.newTexture(TexSrc, true);
            Tex.bind(GfxLib);
            Tex.enable(GfxLib);

            GfxLib.glClear(GfxLib.GL_COLOR_BUFFER_BIT | GfxLib.GL_DEPTH_BUFFER_BIT);

            for (NGon Polygon : Polygons) {
                GfxLib.glBegin(GfxLib.GL_TRIANGLES);
                GfxLib.glNormal3d(0, 0, 1);
                int x = 1, y = 0;
                for (Vertex Vert : Polygon.Verts) {
                    GfxLib.glTexCoord2d(x, y);
                    GfxLib.glVertex3d(ObjectLocation.vX + Vert.vX, ObjectLocation.vY + Vert.vY, ObjectLocation.vZ + Vert.vZ);
                    if (y == 0){
                        y++;
                    }
                    else if (y == 1){
                        x--;
                    }
                }
                GfxLib.glEnd();
            }

        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 GfxLib = glAutoDrawable.getGL().getGL2();

        GfxLib.glShadeModel(GfxLib.GL_SMOOTH);
        //?
        GfxLib.glClearColor(0, 0, 0, 0);
        GfxLib.glClearDepth(1);
        //
        GfxLib.glEnable(GfxLib.GL_DEPTH_TEST);
        GfxLib.glDepthFunc(GfxLib.GL_LEQUAL);
        GfxLib.glHint(GfxLib.GL_PERSPECTIVE_CORRECTION_HINT, GfxLib.GL_NICEST);

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        Update(glAutoDrawable);
        Render(glAutoDrawable);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
        /*GL2 gl = glAutoDrawable.getGL().getGL2();
        if(h <= 0){
            h = 1;
        }

        final float height = (float) w / (float) h;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        vGLU.gluPerspective(45.0f, height, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();*/
    }

    public void HookInput(InputManager IM){
        Input = IM;
    }

}
