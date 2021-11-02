package Root.IO.File;

import Root.Misc.Structures.ObjectTree;
import Root.Objects.ObjectManager;
import Root.Objects.WorldObject;
import Root.Simulation.Preferences;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Save {

    private static Gson GSON = new Gson();


    private static JSONObject GetPreferences() {
        JSONObject PJO = new JSONObject();

        PJO.put("PtclSpwn", Preferences.ParticleSpawnPoint);
        PJO.put("PtclBlockX", Preferences.ParticleBlockSizeX[0]);
        PJO.put("PtclBlockY", Preferences.ParticleBlockSizeY[0]);
        PJO.put("PtclBlockZ", Preferences.ParticleBlockSizeZ[0]);
        PJO.put("PtclBlockGap", Preferences.ParticleBlockGap[0]);

        PJO.put("Timestep", Preferences.Timestep[0]);
        PJO.put("BndryElast", Preferences.BoundaryElasticity[0]);
        PJO.put("SlvrIt", Preferences.SolverIterations[0]);
        PJO.put("SimIt", Preferences.SimulationIterations[0]);
        PJO.put("BndrySize", Preferences.BoundarySize);
        PJO.put("Gravity", Preferences.Gravity);

        PJO.put("Stiff", Preferences.Stiffness[0]);
        PJO.put("PtclMass", Preferences.ParticleMass[0]);
        PJO.put("PtclVisc", Preferences.ParticleViscosity[0]);
        PJO.put("RestDens", Preferences.RestDensity[0]);

        PJO.put("KrnRad", Preferences.SmoothingRadius[0]);

        PJO.put("GridX", Preferences.GridX[0]);
        PJO.put("GridY", Preferences.GridY[0]);
        PJO.put("GridZ", Preferences.GridZ[0]);

        PJO.put("PtclDrawSize", Preferences.ParticleDrawSize[0]);

        return PJO;
    }

    //On Load, Init Preferences

    private static JSONArray GetObjects(ObjectTree Node) {
        JSONArray JA = new JSONArray();

        for (int i = 0; i < Node.Children.size(); i++) {
            ObjectTree ChildNode = Node.Children.get(i);
            WorldObject ChildElement = ChildNode.Element;

            JSONObject NodeObject = new JSONObject();
            NodeObject.put("Object", GSON.toJson(ChildElement));
            if (ChildNode.HasChildren()) {
                NodeObject.put("Children", GetObjects(ChildNode));
            }
            JA.put(NodeObject);
        }

        return JA;
    }


    public static void Save(String TargetPath) {
        JSONObject JOBJ = new JSONObject();

        JOBJ.put("Preferences", GetPreferences());

        JOBJ.put("Objects", GetObjects(ObjectManager.Tree));

        try {
            FileWriter FW = new FileWriter(TargetPath);

            FW.append(JOBJ.toString());

            FW.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

    }

}
