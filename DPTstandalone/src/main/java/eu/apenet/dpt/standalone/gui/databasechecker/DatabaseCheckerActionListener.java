package eu.apenet.dpt.standalone.gui.databasechecker;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Yoann Moranville
 * Date: 07/02/2013
 *
 * @author Yoann Moranville
 */
public class DatabaseCheckerActionListener implements ActionListener {
    private RetrieveFromDb retrieveFromDb;
    private Container contentPane;

    public DatabaseCheckerActionListener(RetrieveFromDb retrieveFromDb, Container contentPane) {
        this.retrieveFromDb = retrieveFromDb;
        this.contentPane = contentPane;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JFrame databaseCheckerFrame = new DatabaseCheckerFrame(retrieveFromDb);
        Dimension contentPaneDimensions = contentPane.getSize();
        databaseCheckerFrame.setPreferredSize(new Dimension(new Double(contentPaneDimensions.getWidth() * 3 / 4).intValue(), new Double(contentPaneDimensions.getHeight() * 3 / 4).intValue()));
        databaseCheckerFrame.setLocation(new Double(contentPaneDimensions.getWidth() / 8).intValue(), new Double(contentPaneDimensions.getHeight() / 8).intValue());

        databaseCheckerFrame.pack();
        databaseCheckerFrame.setVisible(true);
    }
}
