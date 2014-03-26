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

import eu.apenet.dpt.standalone.gui.xsdaddition.XsdInfoQueryComponent;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.utils.util.XsdChecker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 06/02/2013
 *
 * @author Yoann Moranville
 */
public class XsdAdderActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(XsdAdderActionListener.class);
    private DataPreparationToolGUI dataPreparationToolGUI;
    private Component parent;
    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;

    public XsdAdderActionListener(DataPreparationToolGUI dataPreparationToolGUI, ResourceBundle labels, RetrieveFromDb retrieveFromDb) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.parent = dataPreparationToolGUI.getContentPane();
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser xsdChooser = new JFileChooser();
        xsdChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if(xsdChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION){
            File file = xsdChooser.getSelectedFile();
            if(isXSD(file)){
                XsdInfoQueryComponent xsdInfoQueryComponent = new XsdInfoQueryComponent(labels, file.getName());

                int result = JOptionPane.showConfirmDialog(parent, xsdInfoQueryComponent, "title", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION) {
                    if(StringUtils.isEmpty(xsdInfoQueryComponent.getName())) {
                        errorMessage();
                    } else {
                        if(saveXsd(file, xsdInfoQueryComponent.getName(), false, xsdInfoQueryComponent.getXsdVersion(), xsdInfoQueryComponent.getFileType())) {
                            JRadioButton newButton = new JRadioButton(xsdInfoQueryComponent.getName());
                            newButton.addActionListener(new XsdSelectorListener(dataPreparationToolGUI));
                            dataPreparationToolGUI.getGroupXsd().add(newButton);
                            dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsdPanel(newButton);
                            dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsdPanel(Box.createRigidArea(new Dimension(0, 10)));
                            JOptionPane.showMessageDialog(parent, labels.getString("xsdSaved")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                        } else {
                            errorMessage();
                        }
                    }
                }
            } else {
                errorMessage();
            }
        }
    }

    private void errorMessage() {
        JOptionPane.showMessageDialog(parent, labels.getString("xsdNotSaved")+".", labels.getString("fileNotSaved"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
    }

    private boolean isXSD(File file){
        return (file.isFile() && file.getName().endsWith(".xsd") && XsdChecker.isXsdFile(file));
    }

    private boolean saveXsd(File file, String name, boolean isSystem, String isXsd11Str, String fileTypeStr){
        try {
            boolean isXsd11 = true;
            if(isXsd11Str.equals("1"))
                isXsd11 = false;
            FileInstance.FileType fileType = FileInstance.FileType.getCorrectFileType(fileTypeStr);
            if(!retrieveFromDb.saveNewAdditionalXsd(name, file.getName(), isSystem, isXsd11, fileType))
                return false;
            FileUtils.copyFile(file, new File(Utilities.CONFIG_DIR + file.getName()));
            return true;
        } catch (IOException e){
            LOG.error("Moving file " + file.getAbsolutePath() + " failed", e);
            return false;
        }
    }
}
