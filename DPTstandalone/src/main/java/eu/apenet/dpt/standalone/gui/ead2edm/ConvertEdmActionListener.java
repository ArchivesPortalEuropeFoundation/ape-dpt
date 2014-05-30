package eu.apenet.dpt.standalone.gui.ead2edm;

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
import eu.apenet.dpt.standalone.gui.APEPanel;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.progress.ApexActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * User: Yoann Moranville Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class ConvertEdmActionListener extends ApexActionListener {

    private final static Logger LOG = Logger.getLogger(ConvertEdmActionListener.class);
    private DataPreparationToolGUI dataPreparationToolGUI;
    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private APEPanel apePanel;

    public ConvertEdmActionListener(ResourceBundle labels, DataPreparationToolGUI dataPreparationToolGUI, APEPanel apePanel) {
        this.labels = labels;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.retrieveFromDb = new RetrieveFromDb();
        this.apePanel = apePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        continueLoop = true;
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.disableRadioButtons();
        dataPreparationToolGUI.disableAllBatchBtns();
        dataPreparationToolGUI.getAPEPanel().setFilename("");

        if (!dataPreparationToolGUI.getXmlEadList().isSelectionEmpty()) {
            JFrame edmOptionFrame = new JFrame(labels.getString("ese.eseOptionFrame"));
            if (dataPreparationToolGUI.getXmlEadList().getSelectedValues().length > 1) {
                edmOptionFrame.add(new EdmOptionsPanel(labels, dataPreparationToolGUI, edmOptionFrame, apePanel.getApeTabbedPane(), true));
            } else {
                edmOptionFrame.add(new EdmOptionsPanel(labels, dataPreparationToolGUI, edmOptionFrame, apePanel.getApeTabbedPane(), false));
            }
            edmOptionFrame.setPreferredSize(new Dimension(dataPreparationToolGUI.getContentPane().getWidth() * 3 / 4, dataPreparationToolGUI.getContentPane().getHeight() * 3 / 4));
            edmOptionFrame.pack();
            edmOptionFrame.setVisible(true);
        }
    }

    public void stopLoop() {
        continueLoop = false;
    }
}
