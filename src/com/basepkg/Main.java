package com.basepkg;
import com.geometrypkg.*;

import com.geometrypkg.coregeometry.Vertex;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<RootShape> SceneObjects = new ArrayList<RootShape>();

    public static InputManager vInputManager = null;
    public static FPSAnimator vAnimator = null;
    public static GLCanvas vRenderer = null;
    public static JFrame vWindow = null;

    private static void Create(RootShape Shape, boolean HookInput){
        vRenderer.addGLEventListener(Shape);
        if (HookInput){
            Shape.HookInput(vInputManager);
        }
        SceneObjects.add(Shape);
    }

    private static void Initialize(){
        vRenderer = new GLCanvas( new GLCapabilities( GLProfile.get( GLProfile.GL2 )));
        vInputManager = new InputManager();

        vWindow = new JFrame("QEngine");
        vWindow.setSize(720, 480);
        vWindow.add(vRenderer);
        vWindow.setVisible(true);

        vAnimator = new FPSAnimator(vRenderer, 300);
        vAnimator.start();

        vRenderer.addKeyListener(vInputManager);
    }

    public static void main(String[] args) {
        Initialize();

        Create(new Pyramid(), true);
    }
}
