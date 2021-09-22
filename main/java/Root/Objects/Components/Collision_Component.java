package Root.Objects.Components;

import Root.Engine;
import Root.Geometry.Mesh;
import Root.Geometry.Operations;
import Root.Geometry.SphereMesh;
import Root.Misc.Util.MathU;
import Root.Misc.Util.Util;
import Root.Objects.WorldObject;
import Root.Rendering.Gizmo;
import Root.Shaders.Material;
import Root.Shaders.ShaderProgram;
import Root.Simulation.Particle;
import Root.Simulation.Preferences;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Collision_Component extends Component {

    private int[] Divisions = new int[] {20};

    private float[] Points;
    public ArrayList<Particle> Particles = new ArrayList<>();

    WorldObject CollisionObject;

    public float[] Density = new float[] {Preferences.RestDensity};
    public ImBoolean Include = new ImBoolean(false);

    public Collision_Component() {
        Name = "Collision";
    }

    private void CreateParticles() {
        float[] TriData = new float[Parent.Mesh.Indices.length * 3];
        ArrayList<Float> Points = new ArrayList<>();
        Particles.clear();

        int Count = 0;
        for(int Index : Parent.Mesh.Indices) {
            TriData[Count++] = Parent.Mesh.VertexData[(8 * Index) + 0];
            TriData[Count++] = Parent.Mesh.VertexData[(8 * Index) + 1];
            TriData[Count++] = Parent.Mesh.VertexData[(8 * Index) + 2];
        }

        for (int i = 0; i < TriData.length; i+=9) {
            Vector3f[] TriVerts = new Vector3f[4];
            TriVerts[0] = new Vector3f(TriData[i+0], TriData[i+1], TriData[i+2]);
            TriVerts[1] = new Vector3f(TriData[i+3], TriData[i+4], TriData[i+5]);
            TriVerts[2] = new Vector3f(TriData[i+6], TriData[i+7], TriData[i+8]);
            TriVerts[3] = new Vector3f(
                    (TriData[i+0] + TriData[i+3] + TriData[i+6]) / 3,
                    (TriData[i+1] + TriData[i+4] + TriData[i+7]) / 3,
                    (TriData[i+2] + TriData[i+5] + TriData[i+8]) / 3
            );

            for (Vector3f TriVert : TriVerts) {
                TriVert.add(Parent.Position);
                TriVert.mul(Parent.Scale);
                TriVert
                        .rotateX(Math.toRadians(Parent.Rotation.x))
                        .rotateY(Math.toRadians(Parent.Rotation.y))
                        .rotateZ(Math.toRadians(Parent.Rotation.z));

                for (int j = 0; j < 3; j++) {
                    Points.add(TriVert.get(j));
                }

                Particle TriParticle = new Particle();
                TriParticle.Position = TriVert;
                TriParticle.Density = Density[0];
                TriParticle.Pressure = Preferences.Stiffness * (TriParticle.Density - Preferences.RestDensity);
                Particles.add(TriParticle);
            }
        }

        if (CollisionObject != null) {
            Engine.RenderQueue.remove(CollisionObject);
        }

        Mesh PointMesh = SphereMesh.Generate(Preferences.ParticleDrawSize / 4, 4, 4);

        CollisionObject = new WorldObject(PointMesh);

        CollisionObject.Material = new Material(
                new Vector3f(0.6f, 0.05f, 0.05f),
                new Vector3f(0.6f, 0.05f, 0.05f),
                new Vector3f(0.5f, 0.5f, 0.5f),
                2f
        );

        CollisionObject.Shader = new ShaderProgram(
                ShaderProgram.LoadShaderResource("/LitInstanced.vert"),
                ShaderProgram.LoadShaderResource("/LitInstanced.frag")
        );

        CollisionObject.SetInstanced(Util.FloatArrList2Arr(Points));

        Engine.RenderQueue.add(CollisionObject);
    }

    private void UpdatePoints() {
        float[] TriData = new float[Parent.Mesh.Indices.length * 3];
        ArrayList<Float> Points = new ArrayList<>();
        Particles.clear();

        int Count = 0;
        for(int Index : Parent.Mesh.Indices) {
            TriData[Count++] = Parent.Mesh.VertexData[(8 * Index) + 0];
            TriData[Count++] = Parent.Mesh.VertexData[(8 * Index) + 1];
            TriData[Count++] = Parent.Mesh.VertexData[(8 * Index) + 2];
        }

        for (int i = 0; i < TriData.length; i+=9) {
            Vector3f[] TriVerts = new Vector3f[4];
            TriVerts[0] = new Vector3f(TriData[i+0], TriData[i+1], TriData[i+2]);
            TriVerts[1] = new Vector3f(TriData[i+3], TriData[i+4], TriData[i+5]);
            TriVerts[2] = new Vector3f(TriData[i+6], TriData[i+7], TriData[i+8]);
            TriVerts[3] = new Vector3f(
                    (TriData[i+0] + TriData[i+3] + TriData[i+6]) / 3,
                    (TriData[i+1] + TriData[i+4] + TriData[i+7]) / 3,
                    (TriData[i+2] + TriData[i+5] + TriData[i+8]) / 3
            );

            int ParticleCount = 0;
            for (Vector3f TriVert : TriVerts) {
                TriVert.mul(Parent.Scale);


                TriVert
                        .rotateX(Math.toRadians(Parent.Rotation.x))
                        .rotateY(Math.toRadians(Parent.Rotation.y))
                        .rotateZ(Math.toRadians(Parent.Rotation.z));

                TriVert.add(Parent.Position);

                for (int j = 0; j < 3; j++) {
                    Points.add(TriVert.get(j));
                }

                Particle TriParticle = new Particle();
                TriParticle.Position = TriVert;
                TriParticle.Density = Density[0];
                TriParticle.Pressure = Preferences.Stiffness * (TriParticle.Density - Preferences.RestDensity);
                Particles.add(TriParticle);

                //Particles.get(ParticleCount++).Position = TriVert;
            }
        }

        CollisionObject.SetInstanced(Util.FloatArrList2Arr(Points));
        CollisionObject.Mesh.UpdateInstanceBuffer();
    }

    @Override
    public void Upload(ShaderProgram Shader) {

    }

    @Override
    public void InternalGUI() {
        if (ImGui.dragInt("Resolution", Divisions)) {}
        if (ImGui.button("Test")) {
            CreateParticles();
        }
        if(ImGui.checkbox("Include in Sim?", Include)) {
            if (Include.get()) {
                SimEngine.StaticParticles.add(Particles);
            }
            else {
                SimEngine.StaticParticles.remove(Particles);
            }
        }

        if (ImGui.dragFloat("Density", Density)) {
            for (Particle P1 : Particles) {
                P1.Density = Density[0];
                P1.Pressure = Preferences.Stiffness * (P1.Density - Preferences.RestDensity);
            }
        }

        if (!Particles.isEmpty()) {
            System.out.println(Particles.get(0).Density);
        }
    }

    @Override
    public void InternalUpdate() {
        UpdatePoints();
        System.out.println("Updating");
    }

    @Override
    public void Update() {

    }
}
