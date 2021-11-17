package Root.Objects.Components;

import Root.Engine.Engine;
import Root.Geometry.*;
import Root.Misc.Util.Util;
import Root.Objects.WorldObject;
import Root.Shaders.Material;
import Root.Shaders.ShaderManager;
import Root.Shaders.ShaderProgram;
import Root.Simulation.Particle;
import Root.Simulation.Preferences;
import Root.Simulation.SimEngine;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Collision_Component extends Component {

    private int[] Resolution = new int[] {0};

    public ArrayList<Particle> Particles = new ArrayList<>();
    ArrayList<Float> ParticleLocations = new ArrayList<>();

    WorldObject CollisionObject;

    public float[] Density = new float[] {Preferences.RestDensity[0] * 100};
    public ImBoolean Include = new ImBoolean(false);

    public Collision_Component() {
        Name = "Collision";
        Type = ComponentManager.ComponentType.Collision;
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
                TriParticle.Pressure = Preferences.Stiffness[0] * (TriParticle.Density - Preferences.RestDensity[0]);
                TriParticle.DoNotMeshify = true;
                Particles.add(TriParticle);
            }
        }

        if (CollisionObject != null) {
            Engine.RenderQueue.remove(CollisionObject);
        }

        Mesh PointMesh = PlaneMesh.Generate(0.025f);

        CollisionObject = new WorldObject(PointMesh);

        CollisionObject.Name = "#TEST";

        CollisionObject.Material = new Material(
                new Vector3f(1f, 0.05f, 0.05f),
                new Vector3f(0),
                new Vector3f(0),
                0f
        );

        CollisionObject.Shader = new ShaderProgram(
                ShaderManager.LoadShaderResource("/Billboard.vert"),
                ShaderManager.LoadShaderResource("/Billboard.frag")
        );

        CollisionObject.SetInstanced(Util.FloatArrList2Arr(Points));

        Engine.RenderQueue.add(CollisionObject);
    }

    private void TransformParticlePoint(Vector3f Point) {
        Point.mul(Parent.Scale);


        Point
                .rotateX(Math.toRadians(Parent.Rotation.x))
                .rotateY(Math.toRadians(Parent.Rotation.y))
                .rotateZ(Math.toRadians(Parent.Rotation.z));

        Point.add(Parent.Position);
    }

    private void RecursiveBreakTriangles(Vector3f Tri1, Vector3f Tri2, Vector3f Tri3, int Level) {
        if (Level <= 0) {
            return;
        }

        Vector3f Midpoint = new Vector3f(
                (Tri1.x+Tri2.x+Tri3.x) / 3,
                (Tri1.y+Tri2.y+Tri3.y) / 3,
                (Tri1.z+Tri2.z+Tri3.z) / 3
        );

        //TransformParticlePoint(Midpoint);

        for (int i = 0; i < 3; i++) {
            ParticleLocations.add(Midpoint.get(i));
        }

        Particle MidpointParticle = new Particle();
        MidpointParticle.Position = Midpoint;
        MidpointParticle.Density = Density[0];
        MidpointParticle.Pressure = Preferences.Stiffness[0] * (MidpointParticle.Density - Preferences.RestDensity[0]);
        MidpointParticle.DoNotMeshify = true;
        Particles.add(MidpointParticle);

        RecursiveBreakTriangles(Midpoint, Tri1, Tri2, Level - 1);
        RecursiveBreakTriangles(Midpoint, Tri2, Tri3, Level - 1);
        RecursiveBreakTriangles(Midpoint, Tri3, Tri1, Level - 1);
    }

    private void UpdatePoints() {
        float[] TriData = new float[Parent.Mesh.Indices.length * 3];
        ParticleLocations.clear();
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
               TransformParticlePoint(TriVert);

                for (int j = 0; j < 3; j++) {
                    ParticleLocations.add(TriVert.get(j));
                }

                Particle TriParticle = new Particle();
                TriParticle.Position = TriVert;
                TriParticle.Density = Density[0];
                TriParticle.Pressure = Preferences.Stiffness[0] * (TriParticle.Density - Preferences.RestDensity[0]);
                TriParticle.DoNotMeshify = true;
                Particles.add(TriParticle);
            }

            RecursiveBreakTriangles(TriVerts[3], TriVerts[0], TriVerts[1], Resolution[0]);
            RecursiveBreakTriangles(TriVerts[3], TriVerts[1], TriVerts[2], Resolution[0]);
            RecursiveBreakTriangles(TriVerts[3], TriVerts[2], TriVerts[0], Resolution[0]);
        }

        CollisionObject.SetInstanced(Util.FloatArrList2Arr(ParticleLocations));
        CollisionObject.Mesh.UpdateInstanceBuffer();
    }

    @Override
    public void Attach(WorldObject Target) {
        super.Attach(Target);
        CreateParticles();
    }

    @Override
    public void Upload(ShaderProgram Shader) {

    }

    @Override
    public void InternalGUI() {
        if (ImGui.dragInt("Resolution", Resolution, 0.01f)) {
            InternalUpdate();
        }

        if (ImGui.dragFloat("Density", Density)) {
            for (Particle P1 : Particles) {
                P1.Density = Density[0];
                P1.Pressure = Preferences.Stiffness[0] * (P1.Density - Preferences.RestDensity[0]);
            }
        }

        if(ImGui.checkbox("Include in Sim?", Include)) {
            if (Include.get()) {
                SimEngine.StaticParticles.add(Particles);
            }
            else {
                SimEngine.StaticParticles.remove(Particles);
            }
        }
    }

    @Override
    public void InternalUpdate() {
        UpdatePoints();
    }

    @Override
    public void Update() {

    }

    @Override
    public void Delete() {
        Engine.RenderQueue.remove(CollisionObject);
        SimEngine.StaticParticles.remove(Particles);
        super.Delete();
    }
}
