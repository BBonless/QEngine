package Root.MeshGen;

import Root.Geometry.Mesh;
import Root.Misc.Util.Util;
import Root.Simulation.SimEngine;
import org.joml.Vector3f;

import java.util.ArrayList;

import static Root.MeshGen.MarchingGrid.*;

public class MarchingCubes {

    public static float Threshold = 1.0f;

    public static void March() {
        ArrayList<Float> Vertices = new ArrayList<>();
        ArrayList<Float> Normals = new ArrayList<>();
        ArrayList<Integer> Indices = new ArrayList<>();
        int IndexCount = 0;
        for (int x = 0; x < XCount - 1; x++) {
            for (int y = 0; y < YCount - 1; y++) {
                for (int z = 0; z < ZCount - 1; z++) {
                    MarchingCell CurrentCell = MarchingGrid.GetCell(x,y,z);

                    int CellIndex = GetCellIndex(CurrentCell);

                    if (LookupTables.EdgeTable[CellIndex] == 0 || LookupTables.EdgeTable[CellIndex] == 255) {
                        continue;
                    }


                    int[] EdgeIndices = LookupTables.TriTable[CellIndex];

                    for (int i = 0; LookupTables.TriTable[CellIndex][i] != -1; i+=3) {

                        Vector3f[] TriVertices = new Vector3f[] {
                                GetInterpolatedEdge(CurrentCell, EdgeIndices[i+0]),
                                GetInterpolatedEdge(CurrentCell, EdgeIndices[i+1]),
                                GetInterpolatedEdge(CurrentCell, EdgeIndices[i+2])
                        };

                        for (int Vert = 0; Vert < 3; Vert++) {
                            for (int Dim = 0; Dim < 3; Dim++) {
                                Vertices.add(TriVertices[Vert].get(Dim));
                            }
                        }

                        Vector3f Part1 = new Vector3f(0), Part2 = new Vector3f(0), FaceNormal = new Vector3f(0);

                        TriVertices[1].sub(TriVertices[0], Part1);
                        TriVertices[2].sub(TriVertices[0], Part2);
                        Part1.cross(Part2, FaceNormal);
                        FaceNormal.div(-FaceNormal.length());

                        for (int j = 0; j < 3; j++) {
                            Normals.add(FaceNormal.x);
                            Normals.add(FaceNormal.y);
                            Normals.add(FaceNormal.z);
                        }

                        Indices.add(IndexCount + 0);
                        Indices.add(IndexCount + 1);
                        Indices.add(IndexCount + 2);

                        IndexCount += 3;
                    }
                }
            }
        }

        SimEngine.FluidMeshObject.Mesh = new Mesh(
                Util.FloatArrList2Arr(Vertices),
                Util.FloatArrList2Arr(Normals),
                new float[(Vertices.size() / 3) * 2],
                Util.IntArrList2Arr(Indices)
        );
    }

    public static Vector3f GetInterpolatedEdge(MarchingCell CurrentCell, int EdgeIndex) {
        int[] EdgeVerts = LookupTables.InverseEdgeTable[EdgeIndex];

        MarchingPoint VertA = CurrentCell.Vertices[EdgeVerts[0]];
        MarchingPoint VertB = CurrentCell.Vertices[EdgeVerts[1]];

        if (Math.abs(Threshold-VertA.Value) < 0.00001) return VertA.Position;
        if (Math.abs(Threshold-VertB.Value) < 0.00001) return VertB.Position;
        if (Math.abs(VertA.Value - VertB.Value) < 0.00001) return VertA.Position;

        float Ratio1 = Math.abs(Threshold - VertB.Value) / Math.abs(VertB.Value - VertA.Value);
        float Ratio2 = Math.abs(Threshold - VertA.Value) / Math.abs(VertB.Value - VertA.Value);

        return new Vector3f(
                VertA.Position.x * Ratio1 + VertB.Position.x * Ratio2,
                VertA.Position.y * Ratio1 + VertB.Position.y * Ratio2,
                VertA.Position.z * Ratio1 + VertB.Position.z * Ratio2
        );

    }

    public static int GetCellIndex(MarchingCell Cell) {
        int CellIndex = 0;
        if (Cell.Vertices[0].Value < Threshold) CellIndex |= 1;
        if (Cell.Vertices[1].Value < Threshold) CellIndex |= 2;
        if (Cell.Vertices[2].Value < Threshold) CellIndex |= 4;
        if (Cell.Vertices[3].Value < Threshold) CellIndex |= 8;
        if (Cell.Vertices[4].Value < Threshold) CellIndex |= 16;
        if (Cell.Vertices[5].Value < Threshold) CellIndex |= 32;
        if (Cell.Vertices[6].Value < Threshold) CellIndex |= 64;
        if (Cell.Vertices[7].Value < Threshold) CellIndex |= 128;
        return CellIndex;
    }



}
