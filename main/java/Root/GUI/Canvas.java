package Root.GUI;

import Root.Textures.Texture;

import java.util.ArrayList;

public class Canvas {
    public static ArrayList<Layer> Layers = new ArrayList<>();

    public static Texture RestartTexture = null;

    public static void Render() {
        for (Layer CurrentLayer: Layers) {
            CurrentLayer.Render_ImGUI();
        }
    }
}
