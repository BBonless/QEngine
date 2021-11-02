package Root.Shaders;

import Root.Misc.Util.Util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShaderManager {

    public static Map<String, ShaderProgram> Shaders = new HashMap<>();

    public static void Init() {
        Shaders.put(
                "Lit",
                new ShaderProgram(
                        LoadShaderResource("/LitInstanced.vert"),
                        LoadShaderResource("/LitInstanced.frag")
                )
        );

        Shaders.put(
                "Debug",
                new ShaderProgram(
                        LoadShaderResource("/Debug.vert"),
                        LoadShaderResource("/Debug.frag")
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

    public static String LoadShaderResource(String ResourceToken) {
        try {
            return Util.InputStreamToString(ShaderProgram.class.getResourceAsStream(ResourceToken));
        } catch (Exception E) {
            System.err.println("Could not read Shader at (Resource Token): " + ResourceToken + " !!");
            return "";
        }
    }

    public static String LoadShaderFile(String Path) {
        try {
            return Files.readString(Paths.get(Path));
        } catch (Exception E) {
            System.err.println("Could not read Shader at: " + Path + " !!");
            return "";
        }
    }

}
