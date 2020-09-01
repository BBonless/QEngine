package com.geometrypkg;

import com.basepkg.InputManager;
import com.geometrypkg.coregeometry.Vertex;
import com.geometrypkg.coregeometry.NGon;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;



public abstract class RootShape implements GLEventListener {

    protected Vertex[] Verts = {};

    protected NGon[] Polygons = {};

    protected InputManager Input = null;

    abstract void Update(GLAutoDrawable DRW);

    private void Render(GLAutoDrawable DRW){
        GL2 GfxLib = DRW.getGL().getGL2();

        GfxLib.glClear(GfxLib.GL_COLOR_BUFFER_BIT | GfxLib.GL_DEPTH_BUFFER_BIT);

        int dbcolindex = 1; //dbdel

        for (NGon Polygon : Polygons) {
            dbcolindex++; //dbdel
            GfxLib.glBegin(GfxLib.GL_TRIANGLES);
            GfxLib.glColor3b((byte)(255 / dbcolindex), (byte)(255 / dbcolindex), (byte)128); //dbdel
            for (Vertex Vert : Polygon.Verts) {
                GfxLib.glVertex3d(Vert.vX, Vert.vY, Vert.vZ);
            }
            GfxLib.glEnd();
        }
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 GfxLib = glAutoDrawable.getGL().getGL2();

        GfxLib.glShadeModel(GfxLib.GL_SMOOTH);
        //?
        //GfxLib.glClearColor(0, 0, 0, 0);
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
    }

    public void HookInput(InputManager IM){
        Input = IM;
    }
}
