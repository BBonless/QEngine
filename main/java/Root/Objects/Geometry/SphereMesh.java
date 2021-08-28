package Root.Objects.Geometry;

import org.joml.Vector3f;

import static org.joml.Math.cos;
import static org.joml.Math.sin;

import java.util.ArrayList;

public class SphereMesh {

    private final static float PI = (float)org.joml.Math.PI;

    public static Mesh Generate(float Radius, int Stacks, int Slices) {

        ArrayList<Float> VertexPosData = new ArrayList<>();
        ArrayList<Float> VertexNormData = new ArrayList<>();
        ArrayList<Float> VertexTexCoordData = new ArrayList<>();
        ArrayList<Integer> IndexData = new ArrayList<>();

        for (int i = 0; i <= Stacks; ++i) {

            float V = i / (float)Stacks;
            float Phi = V * PI - PI/2;

            for (int j = 0; j <= Slices; ++j) {

                float U = j / (float)Slices;
                float Theta = U * PI * 2;

                float X = cos(Phi) * cos(Theta);
                float Y = cos(Phi) * sin(Theta);
                float Z = sin(Phi);

                //Vertex Positions
                VertexPosData.add(X * Radius);
                VertexPosData.add(Y * Radius);
                VertexPosData.add(Z * Radius);

                //Vertex Normals
                VertexNormData.add(X);
                VertexNormData.add(Y);
                VertexNormData.add(Z);

                //Vertex Texture Coordinates
                VertexTexCoordData.add(U);
                VertexTexCoordData.add(V);
            }
        }

        int noPerSlice = Slices + 1;
        for (int i = 0; i < Stacks; ++i) {
            for (int j = 0; j < Slices; ++j) {

                int SI = (i * noPerSlice) + j;

                IndexData.add(SI);
                IndexData.add(SI + noPerSlice + 1);
                IndexData.add(SI + noPerSlice);

                IndexData.add(SI + noPerSlice + 1);
                IndexData.add(SI);
                IndexData.add(SI + 1);
            }
        }

        return new Mesh(VertexPosData, VertexNormData, VertexTexCoordData, IndexData);

    }

    public static Mesh GenerateEllipsoid(Vector3f Radii, int Stacks, int Slices) {

        ArrayList<Float> VertexPosData = new ArrayList<>();
        ArrayList<Float> VertexNormData = new ArrayList<>();
        ArrayList<Float> VertexTexCoordData = new ArrayList<>();
        ArrayList<Integer> IndexData = new ArrayList<>();

        for (int i = 0; i <= Stacks; ++i) {

            float V = i / (float)Stacks;
            float Phi = V * PI - PI/2;

            for (int j = 0; j <= Slices; ++j) {

                float U = j / (float)Slices;
                float Theta = U * PI * 2;

                float X = cos(Phi) * cos(Theta);
                float Y = cos(Phi) * sin(Theta);
                float Z = sin(Phi);

                //Vertex Positions
                VertexPosData.add(X * Radii.x);
                VertexPosData.add(Y * Radii.y);
                VertexPosData.add(Z * Radii.z);

                //Vertex Normals
                VertexNormData.add(X);
                VertexNormData.add(Y);
                VertexNormData.add(Z);

                //Vertex Texture Coordinates
                VertexTexCoordData.add(U);
                VertexTexCoordData.add(V);
            }
        }

        int noPerSlice = Slices + 1;
        for (int i = 0; i < Stacks; ++i) {
            for (int j = 0; j < Slices; ++j) {

                int SI = (i * noPerSlice) + j;

                IndexData.add(SI);
                IndexData.add(SI + noPerSlice + 1);
                IndexData.add(SI + noPerSlice);

                IndexData.add(SI + noPerSlice + 1);
                IndexData.add(SI);
                IndexData.add(SI + 1);
            }
        }

        return new Mesh(VertexPosData, VertexNormData, VertexTexCoordData, IndexData);
    }

}
