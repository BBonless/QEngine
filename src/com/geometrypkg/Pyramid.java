package com.geometrypkg;

import com.geometrypkg.coregeometry.NGon;
import com.geometrypkg.coregeometry.Vertex;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class Pyramid extends RootShape {

    Vertex loc = new Vertex(0, 0 );

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
    void Update(GLAutoDrawable DRW) {
        GL2 GfxLib = DRW.getGL().getGL2();
    }

}
