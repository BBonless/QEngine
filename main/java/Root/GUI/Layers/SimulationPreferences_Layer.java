package Root.GUI.Layers;

import Root.GUI.Layer;
import Root.Simulation.Preferences;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import imgui.type.ImBoolean;

public class SimulationPreferences_Layer implements Layer {

    public static ImBoolean PlaySim = new ImBoolean(false);

    //region Spawning
    float[] SpawnBlockLocationValues = new float[] {0,0,0};
    int[] SpawnBlockSizeXValue = new int[] {10};
    int[] SpawnBlockSizeYValue = new int[] {10};
    int[] SpawnBlockSizeZValue = new int[] {10};
    float[] SpawnBlockGapValues = new float[] {1.25f};
    //endregion

    //region Simulation
    float[] TimestepValue = new float[] {0.025f};
    float[] SmoothingRadiusValue = new float[] {0.7f};
    int[] SimulationIterationsValue = new int[] {1};
    int[] SolverIterationsValue = new int[] {3};
    //endregion

    //region Particle
    float[] MassValue = new float[] {100};
    float[] StiffnessValue = new float[] {50};
    float[] ViscosityValue = new float[] {0.07f};
    float[] RestDensityValue = new float[] {30};
    //endregion

    //region World
    float[] BoundarySizeValues = new float[] {50,50,50};
    float[] BoundaryMaxSize = new float[] {200};
    float[] BoundaryElasticityValue = new float[] {0.1f};
    float[] GravityValues = new float[] {0,-9.81f,0};
    //endregion

    //region Grid
    int[] GridXValue = new int[] {40};
    int[] GridYValue = new int[] {40};
    int[] GridZValue = new int[] {40};
    //endregion

    public static ImBoolean Advanced = new ImBoolean(false);

    @Override
    public void Render_ImGUI() {
        ImGui.begin("Simulation Preferences");

        ImGui.pushItemWidth(160);

        if (ImGui.button("Init")) {
            SimEngine.Init();
        }

        ImGui.sameLine();
        if (ImGui.button("Reset")) {
            SimEngine.Reset();
        }

        ImGui.sameLine();
        if (ImGui.button("Step") && PlaySim.get() == false) {
            SimEngine.ParticleObject.SetInstanced( SimEngine.Step() );
            SimEngine.ParticleObject.Mesh.UpdateInstanceBuffer();
        }

        ImGui.checkbox("Play?", PlaySim);

        if (ImGui.treeNode("Preferences")) {
            if (ImGui.treeNode("Spawning")) {

                if (ImGui.dragFloat3("Spawn Block Location", SpawnBlockLocationValues)) {
                    Preferences.ParticleSpawnPoint.x = SpawnBlockLocationValues[0];
                    Preferences.ParticleSpawnPoint.y = SpawnBlockLocationValues[1];
                    Preferences.ParticleSpawnPoint.z = SpawnBlockLocationValues[2];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the position of the particle block");
                }

                if (ImGui.dragInt("Spawn Block X Size", SpawnBlockSizeXValue, 0.1f, 1, 100000)) {
                    Preferences.ParticleBlockSizeX = SpawnBlockSizeXValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many particles will be spawned on the X axis");
                }

                if (ImGui.dragInt("Spawn Block Y Size", SpawnBlockSizeYValue, 0.1f, 1, 100000)) {
                    Preferences.ParticleBlockSizeY = SpawnBlockSizeYValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many particles will be spawned on the Y axis");
                }

                if (ImGui.dragInt("Spawn Block Z Size", SpawnBlockSizeZValue, 0.1f, 1, 100000)) {
                    Preferences.ParticleBlockSizeZ = SpawnBlockSizeZValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many particles will be spawned on the Z axis");
                }

                if (ImGui.dragFloat("Particle Spacing", SpawnBlockGapValues, 0.01f)) {
                    Preferences.ParticleBlockGap = SpawnBlockGapValues[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how far apart each particle is from eachother when spawned");
                }

                Preferences.UpdateSize();

                ImGui.treePop();
            }

            if (ImGui.treeNode("Simulation")) {

                if (ImGui.sliderFloat("Timestep", TimestepValue, 0.001f, 0.025f, "%.3f")) {
                    Preferences.Timestep = TimestepValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the gap in time between each frame.");
                }

                if (Advanced.get()) {
                    if (ImGui.treeNode("Override Slider")) {
                        if (ImGui.dragFloat("Timestep", TimestepValue, 0.001f)) {
                            Preferences.Timestep = TimestepValue[0];
                        }

                        ImGui.treePop();
                    }
                    if (ImGui.isItemHovered()) {
                        ImGui.setTooltip("Lets you set a timestep value outside the scope of the timestep slider.\n0 will freeze the simulation.\nValues over 0.025 or under 0 are likely to make the simulation explode.");
                    }
                }

                if (ImGui.dragFloat("Influence Radius", SmoothingRadiusValue, 0.01f, 0, BoundaryMaxSize[0], "%.2f")) {
                    Preferences.SmoothingRadius = SmoothingRadiusValue[0];
                    Preferences.UpdateRadius();
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the maximum distance that particles will have an effect on each other.\nHigher values may cost performance.");
                }

                if (ImGui.dragInt("Simulation Iterations", SimulationIterationsValue, 0.1f)) {
                    Preferences.SimulationIterations = SimulationIterationsValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many times the simulation will be ran before displaying a new frame.\nYou can use this to change the speed of the simulation, but it also means it will perform the simulation more than once, so performance will decrease.");
                }

                if (ImGui.dragInt(Advanced.get() ? "Solver Iterations" : "Quality", SolverIterationsValue, 0.1f)) {
                    Preferences.SolverIterations = SolverIterationsValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how many times to repeat the particle's physics calculations.\nHigher usually means higher accuracy simulations, but at the cost of performance");
                }

                ImGui.treePop();
            }

            if (ImGui.treeNode("Particle")) {

                if (ImGui.dragFloat("Mass", MassValue)) {
                    Preferences.ParticleMass = MassValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the mass of the particles");
                }

                if (ImGui.dragFloat("Stiffness", StiffnessValue)) {
                    Preferences.Stiffness = StiffnessValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets how much influence particles have on each other (Pressure)");
                }

                if (ImGui.dragFloat("Viscosity", ViscosityValue, 0.00025f, 0, Float.POSITIVE_INFINITY, "%.5f")) {
                    Preferences.ParticleViscosity = ViscosityValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the viscosity of the particles");
                }

                if (ImGui.dragFloat("Rest Density", RestDensityValue)) {
                    Preferences.RestDensity = RestDensityValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the the minimum density of the particles");
                }

                ImGui.treePop();
            }

            if (ImGui.treeNode("World")) {

                if (ImGui.sliderFloat3("Boundary Dimensions", BoundarySizeValues, 0, BoundaryMaxSize[0], "%.0f", ImGuiSliderFlags.AlwaysClamp)) {
                    Preferences.BoundarySize.x = BoundarySizeValues[0];
                    Preferences.BoundarySize.y = BoundarySizeValues[1];
                    Preferences.BoundarySize.z = BoundarySizeValues[2];
                    SimEngine.UpdateGrid();
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the dimensions of the box that contains the particles");
                }

                ImGui.dragFloat("Maximum Size", BoundaryMaxSize, 1, 0, 10000);
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the maximum value for boundary dimensions");
                }

                if (ImGui.sliderFloat("Boundary Elasticity", BoundaryElasticityValue, 0, 1f, "%.2f")) {
                    Preferences.BoundaryElasticity = BoundaryElasticityValue[0];
                }
                if (ImGui.isItemHovered()) {
                    ImGui.setTooltip("Sets the ratio of energy that particles will lose upon collision with the bounding box");
                }

                if (Advanced.get()) {
                    if (ImGui.treeNode("Override Slider")) {
                        if (ImGui.dragFloat("Boundary Elasticity", BoundaryElasticityValue, 0.001f)) {
                            Preferences.BoundaryElasticity = BoundaryElasticityValue[0];
                        }

                        ImGui.treePop();
                    }
                    if (ImGui.isItemHovered()) {
                        ImGui.setTooltip("Lets you set an elasticity value outside the scope of the slider.\nValues over 1 will cause the simulation to explode, as energy is no longer being conserved.");
                    }
                }

                if (ImGui.dragFloat3("Gravity", GravityValues, 0.025f)) {
                    Preferences.Gravity.x = GravityValues[0];
                    Preferences.Gravity.y = GravityValues[1];
                    Preferences.Gravity.z = GravityValues[2];
                }

                ImGui.treePop();
            }

            if (ImGui.treeNode("Grid")) {

                if (ImGui.dragInt("Grid X Divisons", GridXValue, 0.05f)) {
                    Preferences.GridX = GridXValue[0];
                    SimEngine.UpdateGrid();
                }

                if (ImGui.dragInt("Grid Y Divisons", GridYValue, 0.05f)) {
                    Preferences.GridY = GridYValue[0];
                    SimEngine.UpdateGrid();
                }

                if (ImGui.dragInt("Grid Z Divisons", GridZValue, 0.05f)) {
                    Preferences.GridZ = GridZValue[0];
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
