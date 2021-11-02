package Root.MeshGen;

import Root.Misc.Util.MathU;
import Root.Simulation.Preferences;
import org.joml.Vector3f;

import java.util.Vector;

import static Root.MeshGen.MarchingCubes.Threshold;

public class MarchingGrid {

    public static MarchingPoint[][][] VertData;

    public static int XCount;
    public static int YCount;
    public static int ZCount;

    public static float Resolution = 0.5f;

    public static void Fill() {
        XCount = (int)Math.ceil(Preferences.BoundarySize[0] / Resolution);
        YCount = (int)Math.ceil(Preferences.BoundarySize[1] / Resolution);
        ZCount = (int)Math.ceil(Preferences.BoundarySize[2] / Resolution);

        float XStart = 0 - Preferences.BoundarySize[0] / 2;
        float YStart = 0 + Preferences.BoundarySize[1] / 2;
        float ZStart = 0 - Preferences.BoundarySize[2] / 2;

        VertData = new MarchingPoint[XCount][YCount][ZCount];

        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                for (int z = 0; z < ZCount; z++) {
                    Vector3f Position = new Vector3f(
                            XStart + x * Resolution,
                            YStart - y * Resolution,
                            ZStart + z * Resolution
                    );

                    VertData[x][y][z] = new MarchingPoint(
                            Position,
                            MarchingFunctions.Metaball(Position)
                    );
                }
            }
        }
    }

    public static void FillOverflow() {
        XCount = (int)Math.ceil(Preferences.BoundarySize[0] / Resolution) + 8;
        YCount = (int)Math.ceil(Preferences.BoundarySize[1] / Resolution) + 8;
        ZCount = (int)Math.ceil(Preferences.BoundarySize[2] / Resolution) + 8;

        float XStart = 0 - (Preferences.BoundarySize[0] / 2) - Resolution * 4;
        float YStart = 0 + (Preferences.BoundarySize[1] / 2) + Resolution * 4;
        float ZStart = 0 - (Preferences.BoundarySize[2] / 2) - Resolution * 4;

        VertData = new MarchingPoint[XCount][YCount][ZCount];

        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                for (int z = 0; z < ZCount; z++) {
                    Vector3f Position = new Vector3f(
                            XStart + x * Resolution,
                            YStart - y * Resolution,
                            ZStart + z * Resolution
                    );

                    VertData[x][y][z] = new MarchingPoint(
                            Position,
                            MarchingFunctions.Metaball(Position)
                    );
                }
            }
        }
    }

    public static MarchingCell GetCell(int x, int y, int z) {
        MarchingCell Cell = new MarchingCell();

        Cell.Vertices[0] = VertData[x  ][y+1][z  ];
        Cell.Vertices[1] = VertData[x+1][y+1][z  ];
        Cell.Vertices[2] = VertData[x+1][y+1][z+1];
        Cell.Vertices[3] = VertData[x  ][y+1][z+1];
        Cell.Vertices[4] = VertData[x  ][y  ][z  ];
        Cell.Vertices[5] = VertData[x+1][y  ][z  ];
        Cell.Vertices[6] = VertData[x+1][y  ][z+1];
        Cell.Vertices[7] = VertData[x  ][y  ][z+1];

        Cell.Edges[0] = MathU.Midpoint(Cell.Vertices[0].Position, Cell.Vertices[1].Position);
        Cell.Edges[1] = MathU.Midpoint(Cell.Vertices[1].Position, Cell.Vertices[2].Position);
        Cell.Edges[2] = MathU.Midpoint(Cell.Vertices[2].Position, Cell.Vertices[3].Position);
        Cell.Edges[3] = MathU.Midpoint(Cell.Vertices[3].Position, Cell.Vertices[0].Position);

        Cell.Edges[4] = MathU.Midpoint(Cell.Vertices[4].Position, Cell.Vertices[5].Position);
        Cell.Edges[5] = MathU.Midpoint(Cell.Vertices[5].Position, Cell.Vertices[6].Position);
        Cell.Edges[6] = MathU.Midpoint(Cell.Vertices[6].Position, Cell.Vertices[7].Position);
        Cell.Edges[7] = MathU.Midpoint(Cell.Vertices[7].Position, Cell.Vertices[4].Position);

        Cell.Edges[8] = MathU.Midpoint(Cell.Vertices[0].Position, Cell.Vertices[4].Position);
        Cell.Edges[9] = MathU.Midpoint(Cell.Vertices[1].Position, Cell.Vertices[5].Position);
        Cell.Edges[10] = MathU.Midpoint(Cell.Vertices[2].Position, Cell.Vertices[6].Position);
        Cell.Edges[11] = MathU.Midpoint(Cell.Vertices[3].Position, Cell.Vertices[7].Position);

        return Cell;
    }
}
