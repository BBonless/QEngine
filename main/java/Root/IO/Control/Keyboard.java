package Root.IO.Control;

import java.util.HashMap;
import java.util.Map;

public class Keyboard {
    public static Map<Integer, Boolean> Map = new HashMap<Integer, Boolean>();

    public static boolean Query(int Target) {
        return Map.getOrDefault(Target, false);
    }
}
