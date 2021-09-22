package Root;

import imgui.ImGui;
import imgui.app.Application;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        if (SplashScreen.getSplashScreen() != null) {
            SplashScreen.getSplashScreen().close();
        }

        Environment E = new Environment();

    }

}
