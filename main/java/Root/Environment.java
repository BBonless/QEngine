package Root;

import Root.Compute.GPU;
import Root.GUI.Canvas;
import Root.GUI.Layers.*;
import Root.Geometry.Mesh;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Rendering.Gizmo;
import Root.Shaders.Material;
import Root.Shaders.ShaderManager;
import Root.Simulation.Kernels;
import Root.Simulation.Preferences;
import Root.Textures.NullTexture;
import org.joml.Random;
import org.joml.Vector3f;

public class Environment {

    public static Engine Engine;
    public static Random Random;

    public static WorldObject Light;

    public static WorldObject ISphereWO;

    public Environment() {

        Start();

        //region Floor
        /*Mesh Floor = new Mesh(
                new float[] {
                         10f, -1f,  10f, 1.0f, 1.0f, 1.0f,
                         10f, -1f, -10f, 1.0f, 1.0f, 1.0f,
                        -10f, -1f,  10f, 1.0f, 1.0f, 1.0f,
                        -10f, -1f, -10f, 1.0f, 1.0f, 1.0f
                },
                new int[] {
                        0, 1, 3,
                        0, 2, 3
                }
        );
        WorldObject FloorObject = new WorldObject(Floor);
        FloorObject.Material = new Material(
                new Vector3f(0.05f, 0.6f, 0.05f),
                new Vector3f(0.05f, 0.6f, 0.05f),
                new Vector3f(0.5f, 0.5f, 0.5f),
                32f
        );
        E.RenderQueue.add(FloorObject);*/
        //endregion

        //region Cube
        Mesh FullCube = new Mesh(
            new float[] {
                    -1,  1, -1,  0,  0, -1,  0,  1, //0
                     1,  1, -1,  0,  0, -1,  1,  1,
                    -1, -1, -1,  0,  0, -1,  0,  0,
                     1, -1, -1,  0,  0, -1,  1,  0,

                     1,  1, -1,  1,  0,  0,  1,  0, //4
                     1,  1,  1,  1,  0,  0,  1,  1,
                     1, -1, -1,  1,  0,  0,  0,  0,
                     1, -1,  1,  1,  0,  0,  0,  1,

                    -1,  1,  1,  0,  0,  1,  0,  1, //8
                     1,  1,  1,  0,  0,  1,  1,  1,
                    -1, -1,  1,  0,  0,  1,  0,  0,
                     1, -1,  1,  0,  0,  1,  1,  0,

                    -1,  1, -1, -1,  0,  0,  1,  0, //12
                    -1,  1,  1, -1,  0,  0,  1,  1,
                    -1, -1, -1, -1,  0,  0,  0,  0,
                    -1, -1,  1, -1,  0,  0,  0,  1,

                    -1,  1, -1,  0,  1,  0,  0,  0, //16
                     1,  1,  1,  0,  1,  0,  1,  1,
                     1,  1, -1,  0,  1,  0,  1,  0,
                    -1,  1,  1,  0,  1,  0,  0,  1,

                    -1, -1, -1,  0, -1,  0,  0,  0, //20
                     1, -1,  1,  0, -1,  0,  1,  1,
                     1, -1, -1,  0, -1,  0,  1,  0,
                    -1, -1,  1,  0, -1,  0,  0,  1

            },
            new int[] {
                    0, 1, 3,
                    0, 2, 3,

                    4, 5, 7,
                    4, 6, 7,

                    8, 9, 11,
                    8, 10, 11,

                    12, 13, 15,
                    12, 14, 15,

                    19, 17, 18,
                    19, 16, 18,

                    23, 21, 22,
                    23, 20, 22
            }
        );

        //Texture T = new Texture("C:\\Users\\quent\\Desktop\\02-N\\FB_IMG_1611580518425.png");
        for (int i = 0; i < 10; i++) {
            float RXP = (Random.nextFloat() * 40f) - 20f;
            float RYP = (Random.nextFloat() * 40f) - 20f;
            float RZP = (Random.nextFloat() * 40f) - 20f;
            float RXR = (Random.nextFloat() * 40f) - 20f;
            float RYR = (Random.nextFloat() * 40f) - 20f;
            float RZR = (Random.nextFloat() * 40f) - 20f;
            
            Vector3f RP = new Vector3f(RXP, RYP, RZP);
            Vector3f RR = new Vector3f(RXR, RYR, RZR);

            WorldObject NewCube = new WorldObject(FullCube);
            NewCube.Position = RP;
            NewCube.Rotation = RR;
            NewCube.Material = new Material();
            NewCube.Material.Ambient = new Vector3f(0.6f,0.05f,0.05f);
            NewCube.Material.Diffuse = new Vector3f(0.6f,0.05f,0.05f);
            NewCube.Material.Specular = new Vector3f(0.5f,0.5f,0.5f);
            NewCube.Material.Shininess = 32f;
            //NewCube.Texture = T;

            Engine.RenderQueue.add(NewCube);
        }
        //endregion

        Engine.Start();
    }

    public static void Start() {
        Engine = new Engine();
        Random = new Random();

        Canvas.Layers.add(new Camera_Layer());
        Canvas.Layers.add(new Performance_Layer());
        Canvas.Layers.add(new SimulationPreferences_Layer());
        Canvas.Layers.add(new SimulationStatistics_Layer());
        Canvas.Layers.add(new Browser_Layer());
        Canvas.Layers.add(new GPUTesting_Layer());
        Canvas.Layers.add(new Test_Layer());
        Canvas.Layers.add(new Test2_Layer());

        ObjectManager.Init();
        ShaderManager.Init();

        Preferences.Init();
        Kernels.Init(Preferences.SmoothingRadius);
        Gizmo.Init();

        GPU.Init();

        GPU.AddProgram("Solver", "C:\\Users\\quent\\IdeaProjects\\QEngine 5.0\\src\\main\\java\\Root\\Compute\\Programs\\tester.cl");

        //SimEngine.Init();

        NullTexture.Create();
    }

}
