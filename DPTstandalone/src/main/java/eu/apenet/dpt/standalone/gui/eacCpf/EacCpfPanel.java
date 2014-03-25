package eu.apenet.dpt.standalone.gui.eacCpf;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.CommonsPropertiesPanels;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The container of the different EacCpf Panels
 */
public abstract class EacCpfPanel extends CommonsPropertiesPanels{
    protected static final Logger LOG = Logger.getLogger(EacCpfPanel.class);

    protected JTabbedPane tabbedPane;
    protected JTabbedPane mainTabbedPane;
    protected EacCpf eaccpf;
	protected JFrame eacCpfFrame;
    protected ProfileListModel model;
    protected ResourceBundle labels;

  
    public EacCpfPanel(EacCpf eaccpf, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, ResourceBundle labels) {
        this.eacCpfFrame = eacCpfFrame;
        this.eaccpf = eaccpf;
        this.tabbedPane = tabbedPane;
        this.mainTabbedPane = mainTabbedPane;
        this.model = model;
        this.labels = labels;
    }

    protected void closeFrame() {
    	this.eacCpfFrame.dispose();
    	this.eacCpfFrame.setVisible(false);
    }

    protected void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
  
    protected class ExitBtnAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            EacCpfFrame.inUse(false);
            closeFrame();
        }
    }
}
