package Root.Geometry;

import Root.Misc.Util.Util;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.assimp.Assimp.aiImportFile;

public class MeshLoader {

    public static Mesh[] Import(String Path, int Flags) {
        AIScene Scene = aiImportFile(Util.CorrectPath(Path), Flags);
        if (Scene == null) {
            System.err.println("Could not load model at: " + Path);
            return null;
        }

        /*int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials, texturesDir);
        }*/

        int MeshCount = Scene.mNumMeshes();
        PointerBuffer Meshes = Scene.mMeshes();
        Mesh[] MeshArray = new Mesh[MeshCount];
        for (int i = 0; i < MeshCount; i++) {
            AIMesh Mesh = AIMesh.create(Meshes.get(i));
            MeshArray[i] = ProcessMesh(Mesh);
        }

        return MeshArray;
    }

    private static Mesh ProcessMesh(AIMesh MeshIn) {
        float[] GetVerticesResult = GetVertices(MeshIn);

        Mesh NewMesh = new Mesh(
                GetVerticesResult,
                GetNormals(MeshIn),
                GetTextureCoordinates(MeshIn),
                GetIndices(MeshIn)
        );

        return NewMesh;
    }

    private static float[] GetVertices(AIMesh MeshIn) {
        ArrayList<Float> Vertices = new ArrayList<>();

        AIVector3D.Buffer VertexBuffer = MeshIn.mVertices();
        while (VertexBuffer.remaining() > 0) {
            AIVector3D Vertex = VertexBuffer.get();
            Vertices.add(Vertex.x());
            Vertices.add(Vertex.y());
            Vertices.add(Vertex.z());
        }

        return Util.FloatArrList2Arr(Vertices);
    }

    private static float[] GetNormals(AIMesh MeshIn) {
        ArrayList<Float> Normals = new ArrayList<>();
        AIVector3D.Buffer NormalBuffer = MeshIn.mNormals();
        while (NormalBuffer.remaining() > 0) {
            AIVector3D Normal = NormalBuffer.get();
            Normals.add(Normal.x());
            Normals.add(Normal.y());
            Normals.add(Normal.z());
        }
        return Util.FloatArrList2Arr(Normals);
    }

    private static float[] GetTextureCoordinates(AIMesh MeshIn) {
        ArrayList<Float> TexCoords = new ArrayList<>();
        AIVector3D.Buffer TexCoordBuffer = MeshIn.mTextureCoords(0);
        if (TexCoordBuffer == null) {
            float[] TexCoordsArr = new float[MeshIn.mNumVertices() * 2];
            return TexCoordsArr;
        }
        else {
            while (TexCoordBuffer.remaining() > 0) {
                AIVector3D TexCoord = TexCoordBuffer.get();
                TexCoords.add(TexCoord.x());
                TexCoords.add(TexCoord.y());
            }
            return Util.FloatArrList2Arr(TexCoords);
        }
    }

    private static int[] GetIndices(AIMesh MeshIn) {
        ArrayList<Integer> Indices = new ArrayList<>();

        for (int FaceIndex = 0; FaceIndex < MeshIn.mNumFaces(); FaceIndex++) {
            AIFace Face = MeshIn.mFaces().get(FaceIndex);
            for (int i = 0; i < Face.mNumIndices(); i++) {
                Indices.add(Face.mIndices().get(i));
            }
        }

        return Util.IntArrList2Arr(Indices);
    }
}
