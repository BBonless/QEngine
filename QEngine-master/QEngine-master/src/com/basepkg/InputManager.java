package com.basepkg;

import com.geometrypkg.coregeometry.Vertex;
import com.jogamp.common.util.locks.RecursiveLock;
import com.jogamp.nativewindow.NativeSurface;
import com.jogamp.opengl.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class InputManager implements KeyListener, GLAutoDrawable {

    private JFrame ParentFrame = null;

    public Vertex MouseRaw = new Vertex(0, 0);
    public Vertex MouseDelta = new Vertex(0, 0);

    public boolean PHeld = false;
    public boolean UPArHeld = false;
    public boolean DOWNArHeld = false;
    public boolean RIGHTArHeld = false;
    public boolean LEFTArHeld = false;

    public InputManager(JFrame iParentFrame){
        ParentFrame = iParentFrame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case 80:
                PHeld = true;
                break;
            case 37:
                LEFTArHeld = true;
                break;
            case 38:
                UPArHeld = true;
                break;
            case 39:
                RIGHTArHeld = true;
                break;
            case 40:
                DOWNArHeld = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case 80:
                PHeld = false;
                break;
            case 37:
                LEFTArHeld = false;
                break;
            case 38:
                UPArHeld = false;
                break;
            case 39:
                RIGHTArHeld = false;
                break;
            case 40:
                DOWNArHeld = false;
                break;
        }
    }

    @Override
    public void display() {
        int MouseX = MouseInfo.getPointerInfo().getLocation().x - ParentFrame.getLocationOnScreen().x;
        int MouseY = MouseInfo.getPointerInfo().getLocation().y - ParentFrame.getLocationOnScreen().y;

        MouseDelta.vX = MouseX - MouseRaw.vX;
        MouseDelta.vY = MouseY - MouseRaw.vY;

        MouseRaw.vX = MouseX;
        MouseRaw.vY = MouseY;

    }

    //region GLAutoDrawable Methods >:(
    @Override
    public GLDrawable getDelegatedDrawable() {
        return null;
    }

    @Override
    public GLContext getContext() {
        return null;
    }

    @Override
    public GLContext setContext(GLContext glContext, boolean b) {
        return null;
    }

    @Override
    public void addGLEventListener(GLEventListener glEventListener) {

    }

    @Override
    public void addGLEventListener(int i, GLEventListener glEventListener) throws IndexOutOfBoundsException {

    }

    @Override
    public int getGLEventListenerCount() {
        return 0;
    }

    @Override
    public boolean areAllGLEventListenerInitialized() {
        return false;
    }

    @Override
    public GLEventListener getGLEventListener(int i) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public boolean getGLEventListenerInitState(GLEventListener glEventListener) {
        return false;
    }

    @Override
    public void setGLEventListenerInitState(GLEventListener glEventListener, boolean b) {

    }

    @Override
    public GLEventListener disposeGLEventListener(GLEventListener glEventListener, boolean b) {
        return null;
    }

    @Override
    public GLEventListener removeGLEventListener(GLEventListener glEventListener) {
        return null;
    }

    @Override
    public void setAnimator(GLAnimatorControl glAnimatorControl) throws GLException {

    }

    @Override
    public GLAnimatorControl getAnimator() {
        return null;
    }

    @Override
    public Thread setExclusiveContextThread(Thread thread) throws GLException {
        return null;
    }

    @Override
    public Thread getExclusiveContextThread() {
        return null;
    }

    @Override
    public boolean invoke(boolean b, GLRunnable glRunnable) throws IllegalStateException {
        return false;
    }

    @Override
    public boolean invoke(boolean b, List<GLRunnable> list) throws IllegalStateException {
        return false;
    }

    @Override
    public void flushGLRunnables() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void setAutoSwapBufferMode(boolean b) {

    }

    @Override
    public boolean getAutoSwapBufferMode() {
        return false;
    }

    @Override
    public void setContextCreationFlags(int i) {

    }

    @Override
    public int getContextCreationFlags() {
        return 0;
    }

    @Override
    public GLContext createContext(GLContext glContext) {
        return null;
    }

    @Override
    public void setRealized(boolean b) {

    }

    @Override
    public boolean isRealized() {
        return false;
    }

    @Override
    public int getSurfaceWidth() {
        return 0;
    }

    @Override
    public int getSurfaceHeight() {
        return 0;
    }

    @Override
    public boolean isGLOriented() {
        return false;
    }

    @Override
    public void swapBuffers() throws GLException {

    }

    @Override
    public GLCapabilitiesImmutable getChosenGLCapabilities() {
        return null;
    }

    @Override
    public GLCapabilitiesImmutable getRequestedGLCapabilities() {
        return null;
    }

    @Override
    public GLProfile getGLProfile() {
        return null;
    }

    @Override
    public NativeSurface getNativeSurface() {
        return null;
    }

    @Override
    public long getHandle() {
        return 0;
    }

    @Override
    public GLDrawableFactory getFactory() {
        return null;
    }

    @Override
    public GL getGL() {
        return null;
    }

    @Override
    public GL setGL(GL gl) {
        return null;
    }

    @Override
    public Object getUpstreamWidget() {
        return null;
    }

    @Override
    public RecursiveLock getUpstreamLock() {
        return null;
    }

    @Override
    public boolean isThreadGLCapable() {
        return false;
    }
    //endregion
}
