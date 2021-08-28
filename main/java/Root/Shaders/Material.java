package Root.Shaders;

import org.joml.Vector3f;

public class Material {
    public Vector3f Ambient = new Vector3f(0,0,0);
    public Vector3f Diffuse = new Vector3f(0,0,0);
    public Vector3f Specular = new Vector3f(0,0,0);
    public float Shininess = 0;

    public Material() {}

    public Material(Vector3f AmbientIn, Vector3f DiffuseIn, Vector3f SpecularIn, float ShineIn) {
        Ambient = AmbientIn;
        Diffuse = DiffuseIn;
        Specular = SpecularIn;
        Shininess = ShineIn;
    }

    public static Material Default() {
        Material Def = new Material(
                new Vector3f(0.5f, 0.5f, 0.5f),
                new Vector3f(0.5f, 0.5f, 0.5f),
                new Vector3f(0.5f, 0.5f, 0.5f),
                64
        );
        return Def;
    }

    public void Upload(ShaderProgram Destination) {
        Destination.SetUniform("Mat.Ambient", Ambient, UniformTypes.VEC3);
        Destination.SetUniform("Mat.Diffuse", Diffuse, UniformTypes.VEC3);
        Destination.SetUniform("Mat.Specular", Specular, UniformTypes.VEC3);
        Destination.SetUniform("Mat.Shininess", Shininess, UniformTypes.FLOAT);
    }
}
