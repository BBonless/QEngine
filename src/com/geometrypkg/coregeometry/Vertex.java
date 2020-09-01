package com.geometrypkg.coregeometry;

public class Vertex {

    public double vX;
    public double vY;
    public double vZ;

    public Vertex(double x, double y){
        vX = x;
        vY = y;
        vZ = 0;
    }

    public Vertex(double x, double y, double z){
        vX = x;
        vY = y;
        vZ = z;
    }

    public void UniformAdd(double val){
        vX += val;
        vY += val;
        vZ += val;
    }

    public void UniformSet(double val){
        vX = val;
        vY = val;
        vZ = val;
    }
}
