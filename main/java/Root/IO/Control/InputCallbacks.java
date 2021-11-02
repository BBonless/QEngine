package Root.IO.Control;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

public class InputCallbacks {

    public GLFWKeyCallback KeyboardCallback;
    public GLFWCursorPosCallback MouseCallback;
    public GLFWMouseButtonCallback MouseButtonCallback;

    public InputCallbacks() {
        KeyboardCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                    Keyboard.Map.put(key, true);
                }
                else if (action == GLFW_RELEASE) {
                    Keyboard.Map.put(key, false);
                }


            }
        };

        MouseCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                Mouse.Update(xpos, ypos);
            }
        };

        MouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    Mouse.Map.put(button, true);
                }
                if (action == GLFW_RELEASE) {
                    Mouse.Map.put(button, false);
                }
            }
        };
    }
}
