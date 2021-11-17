package Root.GUI;

import Root.IO.Control.InputCallbacks;
import Root.Engine.Rendering.Renderer;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector2f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public long Handle;

    public final ImGuiImplGlfw imGuiGLFW = new ImGuiImplGlfw();
    public final ImGuiImplGl3 imGuiGL3 = new ImGuiImplGl3();
    public ImGuiIO imGuiIO = null;

    private String GLSL_Version = "";

    public Window() {
        Init();
    }

    public void Init() {
        InitWindow();
        InitImGUI();
        imGuiGLFW.init(Handle, true);
        imGuiGL3.init(GLSL_Version);
    }

    private void InitWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Could not Initialize GLFW!");
        }

        GLSL_Version = "#version 400";

        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        Handle = glfwCreateWindow(1600, 900, "QEngine", NULL, NULL);
        if (Handle == NULL) {
            throw new IllegalStateException("Could not create window!");
        }

        try (MemoryStack ThreadStack = stackPush()) {
            IntBuffer Width = ThreadStack.mallocInt(1);
            IntBuffer Height = ThreadStack.mallocInt(1);

            glfwGetWindowSize(Handle, Width, Height);

            GLFWVidMode VideoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(Handle,
                    (VideoMode.width() - Width.get(0)) / 2,
                    (VideoMode.height() - Height.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(Handle);
        glfwSwapInterval(0); //V-Sync
        glfwShowWindow(Handle);

        GL.createCapabilities();

        glClearColor(1f, 0.6f, 0.6f, 1f);

        InputCallbacks Input = new InputCallbacks();

        glfwSetKeyCallback(Handle, Input.KeyboardCallback);
        glfwSetCursorPosCallback(Handle, Input.MouseCallback);
        glfwSetMouseButtonCallback(Handle, Input.MouseButtonCallback);
        glfwSetFramebufferSizeCallback(Handle, new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
                Renderer.Camera.SetDimensions(new Vector2f(width, height));
            }
        });

        Renderer.Init();
    }

    private void InitImGUI() {
        ImGui.createContext();

        imGuiIO = ImGui.getIO();

        imGuiIO.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        imGuiIO.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        imGuiIO.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    public void Destroy() {
        imGuiGLFW.dispose();
        imGuiGLFW.dispose();
        ImGui.destroyContext();

        Callbacks.glfwFreeCallbacks(Handle);
        glfwSetErrorCallback(null).free();

        glfwDestroyWindow(Handle);
        glfwTerminate();
    }
}
