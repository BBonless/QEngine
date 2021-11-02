package Root.Misc.Util;

public class ArrayU {
    public static boolean StrArrContains(String[] Arr, String Target) {
        for (String Element : Arr) {
            if (Element.equals(Target)) {
                return true;
            }
        }
        return false;
    }
}
