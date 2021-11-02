package Root.Objects;

import Root.Misc.Structures.ObjectTree;
import Root.Objects.Components.Component;
import Root.Geometry.Mesh;
import Root.Shaders.Material;
import Root.Shaders.ShaderProgram;
import Root.Textures.NullTexture;
import Root.Textures.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class WorldObject {

    public transient ObjectTree Container;

    public String Name = "";

    public Mesh Mesh;
    public Vector3f Position = new Vector3f(0,0,0);
    public Vector3f Rotation = new Vector3f(0,0,0);
    public Vector3f Scale = new Vector3f(1,1,1);

    public ShaderProgram Shader = null;
    public Material Material = Root.Shaders.Material.Default();
    public Texture Texture = null;

    public ArrayList<Component> Components = new ArrayList<>();

    public boolean Instanced = false;
    public int InstanceCount = 0;

    public WorldObject(Mesh MeshIn) {
        Mesh = MeshIn;
    }

    public WorldObject(String NameIn) { Name = NameIn; }
    
    public WorldObject() { }
    
    public void SetTransform(Vector3f PositionIn, Vector3f RotationIn, Vector3f ScaleIn) {
        Position = PositionIn == null ? Position : PositionIn;
        Rotation = RotationIn == null ? Rotation : RotationIn;
        Scale = ScaleIn == null ? Scale : ScaleIn;
    }

    public void SetInstanced(float[] InstanceData) {
        if (InstanceData == null) {
            Mesh.UnInstance();
            Instanced = false;
            return;
        }

        if (Mesh.InstanceData == null) {
            Mesh.InstanceData = InstanceData;
            Mesh.Instance();
        }
        else {
            Mesh.InstanceData = InstanceData;
        }

        InstanceCount = InstanceData.length / 3;
        Instanced = true;
    }

    public Matrix4f GetModelMatrix() {
        Matrix4f WorldMatrix = new Matrix4f().identity();

        WorldMatrix.translate(Position).
                rotateX((float)Math.toRadians(Rotation.x)).
                rotateY((float)Math.toRadians(Rotation.y)).
                rotateZ((float)Math.toRadians(Rotation.z)).
                scale(Scale);

        return WorldMatrix;
    }

}
