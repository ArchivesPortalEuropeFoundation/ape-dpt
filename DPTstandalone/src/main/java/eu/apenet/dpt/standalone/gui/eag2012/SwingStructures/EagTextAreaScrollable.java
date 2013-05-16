package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * User: Yoann Moranville
 * Date: 25/04/2013
 *
 * @author Yoann Moranville
 */
public class EagTextAreaScrollable extends JTextArea implements KeyListener {
    public EagTextAreaScrollable(String title) {
        super(title);
        this.setLineWrap(true);
        this.setAutoscrolls(true);
        this.setWrapStyleWord(true);
        this.setRows(3);
        this.addKeyListener(this);
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_TAB:
                keyEvent.consume();
                transferFocus();
                break;
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
    }
}
