package eu.apenet.dpt.standalone.gui;

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

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class APEPanel extends JPanel {
    private APETabbedPane apeTabbedPane;
    private ResourceBundle labels;
    private JLabel filename;

    public APEPanel(ResourceBundle labels, Component parent, DataPreparationToolGUI dataPreparationToolGUI, RetrieveFromDb retrieveFromDb) {
        super(new BorderLayout());
        this.labels = labels;
        filename = new JLabel();
        setFilename("");
        filename.setHorizontalAlignment(JLabel.LEFT);
        add(filename, BorderLayout.NORTH);
        apeTabbedPane = new APETabbedPane(labels, this, parent, dataPreparationToolGUI, retrieveFromDb);
        add(apeTabbedPane, BorderLayout.CENTER);
    }

    public void changeLanguage(ResourceBundle labels) {
        this.labels = labels;

        apeTabbedPane.changeLanguage(labels);
    }

    public APETabbedPane getApeTabbedPane() {
        return apeTabbedPane;
    }

    public void setFilename(String text) {
        filename.setText(labels.getString("filename") + ": " + text);
    }
}
