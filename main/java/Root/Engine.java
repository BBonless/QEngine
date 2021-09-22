package Root;

import Root.Compute.GPU;
import Root.GUI.Canvas;
import Root.GUI.Layers.Camera_Layer;
import Root.GUI.Layers.GPUTesting_Layer;
import Root.GUI.Layers.Performance_Layer;
import Root.GUI.Layers.SimulationPreferences_Layer;
import Root.GUI.Window;
import Root.IO.Keyboard;
import Root.IO.Mouse;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Rendering.Camera;
import Root.Rendering.Gizmo;
import Root.Rendering.Renderer;
import Root.Shaders.ShaderManager;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.*;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

public class Engine {

    private static final boolean PFPS = true;
    public Window Window;

    public static List<WorldObject> RenderQueue = new LinkedList<WorldObject>();

    public static float FrameCounter = 0;
    public static float OneSecondTimer = 0;
    public static float PreviousTime = 0;

    public static double FrameStartTime = 0;
    public static float DeltaTime = 0;

    public Engine() {
        Window = new Window();
    }

    public void Start() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        InternalUpdate();

        Window.Destroy();
    }

    private void InternalUpdate() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        while (!glfwWindowShouldClose(Window.Handle)) {
            FrameStartTime = glfwGetTime();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for (WorldObject Object : RenderQueue) {
                if (Object.Mesh == null) {
                    continue;
                }

                if (Object.Shader != null) {
                    Renderer.CurrentShader = Object.Shader;
                }
                else {
                    Renderer.CurrentShader = ShaderManager.GetDefault();
                }

                Renderer.Render(Object);
            }

            while (Gizmo.Gizmos.isEmpty() == false) {
                WorldObject CurrentGizmo = Gizmo.Gizmos.pop();
                Renderer.Render(CurrentGizmo);
                CurrentGizmo = null;
            }

            GUI_Update();

            Window.imGuiGLFW.newFrame();
            ImGui.newFrame();

            Canvas.Render();

            ImGui.render();
            Window.imGuiGL3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupWindowPtr);
            }

            glfwSwapBuffers(Window.Handle);

            glfwPollEvents();

            DeltaTime = (float) (glfwGetTime() - FrameStartTime);

            Update();
        }

        GPU.Dispose();
    }

    private void TrackFPS() {
        FrameCounter++;
        float CurrentTime = (float)glfwGetTime();
        PreviousTime = CurrentTime;
        if (CurrentTime - OneSecondTimer > 1.0) {
            OneSecondTimer = CurrentTime;

            Performance_Layer.SetData(FrameCounter, DeltaTime);

            FrameCounter = 0;
        }
    }

    public void GUI_Update() {
        Camera_Layer.SetCamPos(Camera.Position);
        Camera_Layer.SetCamRot(Camera.Rotation);

        TrackFPS();
    }

    public void Update() {

        if (ImGui.isKeyDown(GLFW_KEY_LEFT_SHIFT) || ImGui.isKeyDown(GLFW_KEY_RIGHT_SHIFT)) {
            Camera.Speed = Camera.BaseSpeed * 2;
        }
        else if (ImGui.isKeyDown(GLFW_KEY_LEFT_ALT) || ImGui.isKeyDown(GLFW_KEY_RIGHT_ALT)) {
            Camera.Speed = Camera.BaseSpeed / 4;
        }
        else {
            Camera.Speed = Camera.BaseSpeed;
        }

        if (ImGui.isKeyDown(GLFW_KEY_W)) {
            Camera.Position.add( Camera.GetForwardVector().mul(DeltaTime * Camera.Speed) );
        }
        if (Keyboard.Query(GLFW_KEY_A)) {
            Camera.Position.sub( Camera.GetRightVector().mul(DeltaTime * Camera.Speed) );
        }
        if (Keyboard.Query(GLFW_KEY_S)) {
            Camera.Position.sub( Camera.GetForwardVector().mul(DeltaTime * Camera.Speed) );
        }
        if (Keyboard.Query(GLFW_KEY_D)) {
            Camera.Position.add( Camera.GetRightVector().mul(DeltaTime * Camera.Speed) );
        }

        if (Mouse.Query(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2d DeltaPos = Mouse.GetDeltaPos().mul(Camera.Sensitivity);
            Camera.Rotation.x += DeltaPos.y;
            Camera.Rotation.y += DeltaPos.x;
        }

        if (SimulationPreferences_Layer.PlaySim.get()) {
            SimEngine.ParticleObject.SetInstanced( SimEngine.Step() );
            SimEngine.ParticleObject.Mesh.UpdateInstanceBuffer();
        }
        else if (GPUTesting_Layer.Play.get()) {
            GPUTesting_Layer.GPU_TEST_STEP();
        }

        /*Gizmo.PushSphereGizmo(new Vector3f(15, 0, 0), 0.15f); SHOW AXES
        Gizmo.SetGizmoColor(new Vector3f(15, 0, 0));

        Gizmo.PushSphereGizmo(new Vector3f(0, 15, 0), 0.15f);
        Gizmo.SetGizmoColor(new Vector3f(0, 15, 0));

        Gizmo.PushSphereGizmo(new Vector3f(0, 0, 15), 0.15f);
        Gizmo.SetGizmoColor(new Vector3f(0, 0, 15));*/

        /*Gizmo.PushLineGizmo(
                new Vector3f(),
                new Vector3f(15, 15, 15)
        );

        Gizmo.PushLineGizmo(Operations.RayStart, Operations.RayEndX);
        Gizmo.PushLineGizmo(Operations.RayStart, Operations.RayEndY);
        Gizmo.PushLineGizmo(Operations.RayStart, Operations.RayEndZ);
        System.out.println(Gizmo.Gizmos.size());*/

        ObjectManager.ComponentUpdate();

    }

}