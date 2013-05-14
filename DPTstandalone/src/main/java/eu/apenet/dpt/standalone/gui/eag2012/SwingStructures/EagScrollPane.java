package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import javax.swing.*;

/**
 * User: Yoann Moranville
 * Date: 25/04/2013
 *
 * @author Yoann Moranville
 */
public class EagScrollPane extends JScrollPane {
    private EagTextAreaScrollable eagTextAreaScrollable;

    public EagScrollPane(EagTextAreaScrollable eagTextAreaScrollable) {
        super(eagTextAreaScrollable);
        this.eagTextAreaScrollable = eagTextAreaScrollable;
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public String getText() {
        return eagTextAreaScrollable.getText();
    }
}
