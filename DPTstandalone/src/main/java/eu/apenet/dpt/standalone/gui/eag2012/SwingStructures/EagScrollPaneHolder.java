package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import javax.swing.*;

/**
 * User: Yoann Moranville
 * Date: 25/04/2013
 *
 * @author Yoann Moranville
 */
public class EagScrollPaneHolder {
    private EagTextAreaScrollable eagTextAreaScrollable;
    private EagScrollPane eagScrollPane;

    public EagScrollPaneHolder(String title) {
        eagTextAreaScrollable = new EagTextAreaScrollable(title);
        eagScrollPane = new EagScrollPane(eagTextAreaScrollable);
    }
    public EagScrollPaneHolder() {
        new EagScrollPaneHolder("");
    }

    public EagScrollPane getScrollPane() {
        return eagScrollPane;
    }

    public String getText() {
        return eagTextAreaScrollable.getText();
    }
}
