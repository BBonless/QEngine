package Root.GUI.Layers;

import Root.GUI.Layer;
import Root.Simulation.Preferences;
import Root.Simulation.SimEngine;
import Root.Textures.Texture;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import imgui.type.ImBoolean;

public class SimulationPreferences_Layer implements Layer {

    private Texture RestartTexture;
    private Texture PlayTexture;
    private Texture PauseTexture;
    private Texture StepTexture;


    public static ImBoolean PlaySim = new ImBoolean(false);

    private ImBoolean Advanced = new ImBoolean(false);

    public SimulationPreferences_Layer() {
        RestartTexture = new Texture("/Icons/Restart32.png");
        PlayTexture = new Texture("/Icons/Play32.png");
        PauseTexture = new Texture("/Icons/Pause32.png");
        StepTexture = new Texture("/Icons/Step32.png");
    }

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Simulation Preferences");

        ImGui.pushItemWidth(160);

        float CenterOffset = 0;
        if (PlaySim.get()) {
            CenterOffset = (ImGui.getWindowSizeX() / 4) + 32 + 5;
        }
        else {
            CenterOffset = (ImGui.getWindowSizeX() / 4) + 8 + 5;
        }

        ImGui.sameLine(CenterOffset);
        if (ImGui.imageButton(RestartTexture.Handle, 32, 32)) {
            SimEngine.Reset();
        }

        ImGui.sameLine();
        if (PlaySim.get()) {
            if (ImGui.imageButton(PauseTexture.Handle, 32, 32)) {
                PlaySim.set(false);
            }
        } else {
            if (ImGui.imageButton(PlayTexture.Handle, 32, 32)) {
                PlaySim.set(true);
            }

            ImGui.sameLine();
            if (ImGui.imageButton(StepTexture.Handle, 32, 32)) {
                SimEngine.FluidParticleObject.SetInstanced( SimEngine.Step() );
                SimEngine.FluidParticleObject.Mesh.UpdateInstanceBuffer();
            }
            if (ImGui.isItemHovered()) {
                ImGui.setTooltip("Steps the simulation by one frame");
            }
        }

        if (ImGui.treeNode("Preferences")) {
            if (ImGui.treeNode("Spawning")) {

                ImGui.dragFloat3("Spawn Block Location", Preferences.ParticleSpawnPoint);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the position of the particle block");
                }

                if (ImGui.dragInt("Spawn Block X Size", Preferences.ParticleBlockSizeX, 0.1f, 1, 100000)) {
                    SimEngine.FluidParticleObject.SetInstanced(SimEngine.Preview());
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many particles will be spawned on the X axis");
                }

                if (ImGui.dragInt("Spawn Block Y Size", Preferences.ParticleBlockSizeY, 0.1f, 1, 100000)) {
                    SimEngine.FluidParticleObject.SetInstanced(SimEngine.Preview());
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many particles will be spawned on the Y axis");
                }

                if (ImGui.dragInt("Spawn Block Z Size", Preferences.ParticleBlockSizeZ, 0.1f, 1, 100000)) {
                    SimEngine.FluidParticleObject.SetInstanced(SimEngine.Preview());
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many particles will be spawned on the Z axis");
                }

                ImGui.dragFloat("Particle Spacing", Preferences.ParticleBlockGap, 0.01f);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how far apart each particle is from eachother when spawned");
                }

                ImGui.treePop();
            }

            if (ImGui.treeNode("Simulation")) {

                ImGui.sliderFloat("Timestep", Preferences.Timestep, 0.001f, 0.025f, "%.3f");
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the gap in time between each frame.");
                }

                if (Advanced.get()) {
                    if (ImGui.treeNode("Override Slider")) {
                        ImGui.dragFloat("Timestep", Preferences.Timestep, 0.001f);

                        ImGui.treePop();
                    }
                    if (ImGui.isItemHovered()) {
                        ImGui.setTooltip("Lets you set a timestep value outside the scope of the timestep slider.\n0 will freeze the simulation.\nValues over 0.025 or under 0 are likely to make the simulation explode.");
                    }
                }

                if (ImGui.dragFloat("Influence Radius", Preferences.SmoothingRadius, 0.01f, 0, 100000, "%.2f")) {
                    Preferences.UpdateRadius();
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the maximum distance that particles will have an effect on each other.\nHigher values may cost performance.");
                }

                ImGui.dragInt("Simulation Iterations", Preferences.SimulationIterations, 0.1f);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many times the simulation will be ran before displaying a new frame.\nYou can use this to change the speed of the simulation, but it also means it will perform the simulation more than once, so performance will decrease.");
                }

                ImGui.dragInt(Advanced.get() ? "Solver Iterations" : "Quality", Preferences.SolverIterations, 0.1f);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many times to repeat the particle's physics calculations.\nHigher usually means higher accuracy simulations, but at the cost of performance");
                }

                ImGui.treePop();
            }

            if (ImGui.treeNode("Particle")) {

                ImGui.dragFloat("Mass", Preferences.ParticleMass);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the mass of the particles");
                }

                ImGui.dragFloat("Stiffness", Preferences.Stiffness);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how much influence particles have on each other (Pressure)");
                }

                ImGui.dragFloat("Viscosity", Preferences.ParticleViscosity, 0.00025f, 0, Float.POSITIVE_INFINITY, "%.5f");
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the viscosity of the particles");
                }

                ImGui.dragFloat("Rest Density", Preferences.RestDensity);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the the minimum density of the particles");
                }

                ImGui.treePop();
            }

            if (ImGui.treeNode("World")) {

                if (ImGui.dragFloat3("Boundary Dimensions", Preferences.BoundarySize, 1, 0, 100000, "%.0f", ImGuiSliderFlags.AlwaysClamp)) {
                    SimEngine.UpdateGrid();
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the dimensions of the box that contains the particles");
                }

                ImGui.sliderFloat("Boundary Elasticity", Preferences.BoundaryElasticity, 0, 1f, "%.2f");
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the ratio of energy that particles will lose upon collision with the bounding box");
                }

                if (Advanced.get()) {
                    if (ImGui.treeNode("Override Slider")) {
                        ImGui.dragFloat("Boundary Elasticity", Preferences.BoundaryElasticity, 0.001f);

                        ImGui.treePop();
                    }
                    if (ImGui.isItemHovered()) {
                        ImGui.setTooltip("Lets you set an elasticity value outside the scope of the slider.\nValues over 1 will cause the simulation to explode, as energy is no longer being conserved.");
                    }
                }

                ImGui.dragFloat3("Gravity", Preferences.Gravity, 0.025f);

                ImGui.treePop();
            }

            if (ImGui.treeNode("Grid")) {

                if (ImGui.dragInt("Grid X Divisons", Preferences.GridX, 0.05f)) {
                    SimEngine.UpdateGrid();
                }

                if (ImGui.dragInt("Grid Y Divisons", Preferences.GridY, 0.05f)) {
                    SimEngine.UpdateGrid();
                }


                if (ImGui.dragInt("Grid Z Divisons", Preferences.GridZ, 0.05f)) {
                    SimEngine.UpdateGrid();
                }

                ImGui.treePop();
            }

            ImGui.checkbox("Show Advanced Settings?", Advanced);

            ImGui.popItemWidth();

            ImGui.treePop();
        }

        ImGui.end();
    }
}
