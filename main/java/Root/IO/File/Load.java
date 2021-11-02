package Root.IO.File;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Load {

    public static void Load(String File) {
        try {
            JsonObject Scene = JsonParser.parseString(Files.readString(Paths.get(File))).getAsJsonObject();

            System.out.println(Scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
