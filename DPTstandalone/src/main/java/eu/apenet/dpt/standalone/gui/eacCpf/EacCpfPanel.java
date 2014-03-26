package eu.apenet.dpt.standalone.gui.eacCpf;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

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
