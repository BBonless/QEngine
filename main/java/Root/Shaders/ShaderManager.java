package Root.Shaders;

import java.util.HashMap;
import java.util.Map;

public class ShaderManager {

    public static Map<String, ShaderProgram> Shaders = new HashMap<>();

    public static void Init() {
        Shaders.put(
                "Lit",
                new ShaderProgram(
                        ShaderProgram.LoadShaderResource("/LitInstanced.vert"),
                        ShaderProgram.LoadShaderResource("/LitInstanced.frag")
                )
        );

        Shaders.put(
                "Debug",
                new ShaderProgram(
                        ShaderProgram.LoadShaderResource("/Debug.vert"),
                        ShaderProgram.LoadShaderResource("/Debug.frag")
                )
        );
    }

    public static ShaderProgram GetDefault() {
        return Shaders.get("Lit");
    }

    public static String[] GetShaderList() {
        String[] ShaderList = new String[Shaders.keySet().size()];
        int Count = 0;
        for (String ShaderName : Shaders.keySet()) {
            ShaderList[Count++] = ShaderName;
        }
        return ShaderList;
    }

}
