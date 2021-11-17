package Root.IO.Control;

import org.joml.Vector2d;

import java.util.HashMap;
import java.util.Map;

public class Mouse {
    public static final int Moving = -1;

    private static Vector2d PreviousPos = new Vector2d(0,0);
    private static Vector2d DeltaPos = new Vector2d(0,0);
    private static Vector2d CurrentPos = new Vector2d(0,0);

    public static Map<Integer, Boolean> Map = new HashMap<Integer, Boolean>();

    public static boolean Query(int Target) {
        return Map.getOrDefault(Target, false);
    }

    public static void Update(double PosX, double PosY) {
        CurrentPos = new Vector2d(PosX, PosY);

        CurrentPos.sub(PreviousPos, DeltaPos);

        CurrentPos.get(PreviousPos);
    }

    public static Vector2d GetDeltaPos() {
        Vector2d AuxDeltaPos = new Vector2d();

        DeltaPos.get(AuxDeltaPos);

        DeltaPos = new Vector2d(0,0);

        return AuxDeltaPos;
    }

}
