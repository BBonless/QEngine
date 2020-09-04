package com.geometrypkg;

import com.geometrypkg.coregeometry.NGon;
import com.geometrypkg.coregeometry.Vertex;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

public class Pyramid extends RootShape {

    private void MeshData(){
        Verts = new Vertex[]{
                new Vertex(1,0,1),
                new Vertex(-1,0,1),
                new Vertex(1,0,-1),
                new Vertex(-1,0,-1),
                new Vertex(0,2,0)
        };
        Polygons = new NGon[]{
                new NGon(new Vertex[]{
                        Verts[0], Verts[2], Verts[3]
                }),
                new NGon(new Vertex[]{
                        Verts[0], Verts[1], Verts[3]
                }),
                new NGon(new Vertex[]{
                        Verts[0], Verts[1], Verts[4]
                }),
                new NGon(new Vertex[]{
                        Verts[1], Verts[3], Verts[4]
                }),
                new NGon(new Vertex[]{
                        Verts[3], Verts[2], Verts[4]
                }),
                new NGon(new Vertex[]{
                        Verts[0], Verts[2], Verts[4]
                })
        };
    }

    public Pyramid(){
        MeshData();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
        super.reshape(glAutoDrawable, x, y, w, h);

    }

    @Override
    void Update(GLAutoDrawable DRW) {
        GL2 GfxLib = DRW.getGL().getGL2();

        if (Input.PHeld){
            GfxLib.glRotated(0.5f, 0, 1, 0);
        }
        if (Input.RIGHTArHeld){
            GfxLib.glRotated(-0.5f, 0, 0, 1);
        }
        if (Input.LEFTArHeld){
            GfxLib.glRotated(0.5f, 0, 0, 1);
        }
        if (Input.UPArHeld){
            GfxLib.glRotated(-0.5f, 1, 0, 0);
        }
        if (Input.DOWNArHeld){
            GfxLib.glRotated(0.5f, 1, 0, 0);
        }

    }

}
