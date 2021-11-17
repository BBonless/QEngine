package Root.Engine;

import Root.Compute.GPU;
import Root.Engine.Rendering.Camera;
import Root.Engine.Rendering.Gizmo;
import Root.Engine.Rendering.Renderer;
import Root.GUI.Canvas;
import Root.GUI.Layers.*;
import Root.GUI.Window;
import Root.IO.Control.Keyboard;
import Root.IO.Control.Mouse;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Engine.Rendering.Camera;
import Root.Engine.Rendering.Gizmo;
import Root.Engine.Rendering.Renderer;
import Root.Shaders.ShaderManager;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import org.joml.Vector2d;
import org.lwjgl.*;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

public class Engine {

    public Window Window;

    public static List<WorldObject> RenderQueue = new LinkedList<>();

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

            Update();
        }

        GPU.Dispose();
    }

    public void GUI_Update() {
        Camera_Layer.SetCamPos(Camera.Position);
        Camera_Layer.SetCamRot(Camera.Rotation);
    }

    public void Update() {

        Performance_Layer.TrackFPS();

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
            Camera.Position.add( Camera.GetForwardVector().mul((float)Performance_Layer.Deltatime * Camera.Speed) );
        }
        if (Keyboard.Query(GLFW_KEY_A)) {
            Camera.Position.sub( Camera.GetRightVector().mul((float)Performance_Layer.Deltatime * Camera.Speed) );
        }
        if (Keyboard.Query(GLFW_KEY_S)) {
            Camera.Position.sub( Camera.GetForwardVector().mul((float)Performance_Layer.Deltatime * Camera.Speed) );
        }
        if (Keyboard.Query(GLFW_KEY_D)) {
            Camera.Position.add( Camera.GetRightVector().mul((float)Performance_Layer.Deltatime * Camera.Speed) );
        }

        if (Mouse.Query(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2d DeltaPos = Mouse.GetDeltaPos().mul(Camera.Sensitivity);
            Camera.Rotation.x += DeltaPos.y;
            Camera.Rotation.y += DeltaPos.x;
        }

        if (SimulationPreferences_Layer.PlaySim.get()) {
            SimEngine.FluidParticleObject.SetInstanced( SimEngine.Step() );
            SimEngine.FluidParticleObject.Mesh.UpdateInstanceBuffer();
        }
        else if (GPUTesting_Layer.Play.get()) {
            GPUTesting_Layer.GPU_TEST_STEP();
        }

        ObjectManager.ComponentUpdate();

    }

}