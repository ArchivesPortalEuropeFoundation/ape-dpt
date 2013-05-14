package eu.apenet.dpt.standalone.gui.progress;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Yoann Moranville
 * Date: 17/12/2012
 *
 * @author Yoann Moranville
 */
public abstract class ApexActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(ApexActionListener.class);
    protected boolean continueLoop;

    public void abort() {
        continueLoop = false;
    }

    public abstract void actionPerformed(ActionEvent actionEvent);
}
