package com.basepkg;
import com.geometrypkg.coregeometry.*;
import com.geometrypkg.*;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<RootShape> SceneObjects = new ArrayList<RootShape>();

    public static InputManager vInputManager = null;
    public static FPSAnimator vAnimator = null;
    public static GLCanvas vRenderer = null;
    public static JFrame vWindow = null;

    static TextureData TexDat = null;

    private static void Create(RootShape Shape, boolean HookInput){
        vRenderer.addGLEventListener(Shape);
        if (HookInput){
            Shape.HookInput(vInputManager);
        }
        SceneObjects.add(Shape);
    }

    private static void Scale(RootShape Shape, double Val){
        for (Vertex Vert : Shape.Verts){
            Vert.UniformMult(Val);
        }
    }

    private static void Initialize(){
        vRenderer = new GLCanvas( new GLCapabilities( GLProfile.get( GLProfile.GL2 )));

        vWindow = new JFrame("QEngine");
        vWindow.setSize(720, 480);
        vWindow.add(vRenderer);
        vWindow.setVisible(true);

        vInputManager = new InputManager(vWindow);

        vAnimator = new FPSAnimator(vRenderer, 300);
        vAnimator.add(vInputManager);
        vAnimator.start();

        vRenderer.addKeyListener(vInputManager);
    }

    public static void main(String[] args) {
        Initialize();

        Create(new Pyramid(), true);
        Scale(SceneObjects.get(0), 0.50d);


    }
}
