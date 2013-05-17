package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import javax.swing.*;
import java.awt.*;

/**
 * User: Yoann Moranville
 * Date: 25/04/2013
 *
 * @author Yoann Moranville
 */
public class EagTextAreaScrollable extends JTextArea {
    public EagTextAreaScrollable(String title) {
        super(title);
        this.setLineWrap(true);
        this.setAutoscrolls(true);
        this.setWrapStyleWord(true);
        this.setRows(3);
    }
}
