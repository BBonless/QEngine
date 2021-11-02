package Root.MeshGen;

import Root.Simulation.Particle;
import Root.Simulation.SimEngine;
import org.joml.Vector3f;

public class MarchingFunctions {
    public static float MetaballRadius = 0.7f;
    public static float MetaballRadiusSqr = MetaballRadius * MetaballRadius;

    public static float Metaball(Vector3f Point) {
        float Total = 0;
        for (Particle P : SimEngine.Grid.NeighborQueryMeshifyOnly(Point, MetaballRadius * 2)) {
            Total += (
                    MetaballRadiusSqr /
                            (Math.pow(Point.x - P.Position.x, 2) +
                             Math.pow(Point.y - P.Position.y, 2) +
                             Math.pow(Point.z - P.Position.z, 2))
            );
        }
        return Total;
    }
}
