package com.basepkg;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener {

    public boolean PHeld = false;
    public boolean UPArHeld = false;
    public boolean DOWNArHeld = false;
    public boolean RIGHTArHeld = false;
    public boolean LEFTArHeld = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case 80:
                PHeld = true;
            case 37:
                LEFTArHeld = true;
            case 38:
                UPArHeld = true;
            case 39:
                RIGHTArHeld = true;
            case 40:
                DOWNArHeld = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case 80:
                PHeld = false;
            case 37:
                LEFTArHeld = false;
            case 38:
                UPArHeld = false;
            case 39:
                RIGHTArHeld = false;
            case 40:
                DOWNArHeld = false;
        }
    }
}
