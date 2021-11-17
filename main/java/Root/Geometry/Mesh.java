package Root.Geometry;

import Root.Misc.Util.Util;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL40.*;

public class Mesh {

    public float[] VertexData;
    public float[] VertexPosData;
    public float[] VertexNormData;
    public float[] VertexTexCoordData;

    public int[]    Indices;
    public int VAO, VBO, IBO; //Vertex Array Object, Vertex Buffer Object, Instance Vertex Buffer Object, Index Buffer Object

    public int IVBO;
    public float[] InstanceData;

    public Matrix4f Matrix = new Matrix4f().identity();

    //region Constructors

    public Mesh(float[] VertexDataIn, int[] IndicesIn) {
        VertexPosData = VertexDataIn;
        VertexNormData = new float[VertexPosData.length];
        VertexTexCoordData = new float[(VertexPosData.length / 3) * 2];
        Indices = IndicesIn;

        Interleave();

        Initialize();
    }

    public Mesh(ArrayList<Float> VertexDataIn, ArrayList<Integer> IndicesIn) {
        VertexData = new float[VertexDataIn.size()];
        for (int i = 0; i < VertexDataIn.size(); i++) {
            VertexData[i] = VertexDataIn.get(i);
        }

        Indices = new int[IndicesIn.size()];
        for (int i = 0; i < IndicesIn.size(); i++) {
            Indices[i] = IndicesIn.get(i);
        }

        Initialize();
    }

    public Mesh(ArrayList<Float> PositionData, ArrayList<Float> NormalData, ArrayList<Float> TexCoordData, ArrayList<Integer> IndexData) {
        VertexPosData = Util.FloatArrList2Arr(PositionData);
        VertexNormData = Util.FloatArrList2Arr(NormalData);
        VertexTexCoordData = Util.FloatArrList2Arr(TexCoordData);
        Indices = Util.IntArrList2Arr(IndexData);

        Interleave();

        Initialize();
    }

    public Mesh(float[] PositionData, float[] NormalData, float[] TexCoordData, int[] IndexData) {
        VertexPosData = PositionData;
        VertexNormData = NormalData;
        VertexTexCoordData = TexCoordData;
        Indices = IndexData;

        Interleave();

        Initialize();
    }

    //endregion

    public void Interleave() {
        //Create array, VertexPosData.len / 3 = Amount of vertices
        //There are 8 values for each vertex, so * 8.
        VertexData = new float[(VertexPosData.length / 3) * 8];

        //For each Vertex
        for (int i = 0; i < VertexPosData.length / 3; i++) {
            //Position
            VertexData[i*8+0] = VertexPosData[i*3+0];
            VertexData[i*8+1] = VertexPosData[i*3+1];
            VertexData[i*8+2] = VertexPosData[i*3+2];

            //Normals
            VertexData[i*8+3] = VertexNormData[i*3+0];
            VertexData[i*8+4] = VertexNormData[i*3+1];
            VertexData[i*8+5] = VertexNormData[i*3+2];

            //Texture Coords
            VertexData[i*8+6] = VertexTexCoordData[i*2+0];
            VertexData[i*8+7] = VertexTexCoordData[i*2+1];
        }

        //"Free memory"
        VertexPosData = null;
        VertexNormData = null;
        VertexTexCoordData = null;
    }

    //region Operations

    public void Translate(Vector3f Translation) {
        Matrix.translation(Translation);
    }

    public void Rotate(Vector3f Rotation) {
        Matrix.rotation(Math.toRadians(Rotation.x), new Vector3f(1, 0, 0));
        Matrix.rotation(Math.toRadians(Rotation.y), new Vector3f(0, 1, 0));
        Matrix.rotation(Math.toRadians(Rotation.z), new Vector3f(0, 0, 1));
    }

    public void Scale(Vector3f Scalefactor) {
        Matrix.scale(Scalefactor);
    }

    //endregion

    //region Initialization

    public void Initialize() {

        FloatBuffer VertexDataBuffer = MemoryUtil.memAllocFloat(VertexData.length);
        ((Buffer)VertexDataBuffer.put(VertexData)).flip();

        IntBuffer IndexBuffer = MemoryUtil.memAllocInt(Indices.length);
        ((Buffer)IndexBuffer.put(Indices)).flip();

        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, VertexDataBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        IBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, IndexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    public void Instance() {
        //16 MB max instance data
        FloatBuffer InstanceDataBuffer = MemoryUtil.memAllocFloat(16*1000000);
        ((Buffer)InstanceDataBuffer.put(InstanceData)).flip();
        InstanceDataBuffer.limit(16*1000000);

        IVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, IVBO);
        glBufferData(GL_ARRAY_BUFFER, InstanceDataBuffer, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 3 * 4, 0);
        glVertexAttribDivisor(3, 1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    public void UpdateInstanceBuffer() {
        glBindBuffer(GL_ARRAY_BUFFER, IVBO);

        FloatBuffer Buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY).order(ByteOrder.nativeOrder()).asFloatBuffer();

        //PrintArray.Float(InstanceData, 3);

        Buffer.put(InstanceData);

        glUnmapBuffer(GL_ARRAY_BUFFER);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void UnInstance() {
        InstanceData = null;
        glDeleteBuffers(IVBO);
    }

    //endregion

}
