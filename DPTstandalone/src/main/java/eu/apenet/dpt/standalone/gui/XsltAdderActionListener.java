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

import eu.apenet.dpt.utils.util.XsltChecker;
import org.apache.commons.io.FileUtils;
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
 * Date: 06/04/2012
 *
 * @author Yoann Moranville
 */
public class XsltAdderActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(XsltAdderActionListener.class);
    private DataPreparationToolGUI dataPreparationToolGUI;
    private Component parent;
    private ResourceBundle labels;

    public XsltAdderActionListener(DataPreparationToolGUI dataPreparationToolGUI, ResourceBundle labels) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.parent = dataPreparationToolGUI.getContentPane();
        this.labels = labels;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser xsltChooser = new JFileChooser();
        xsltChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if(xsltChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION){
            File file = xsltChooser.getSelectedFile();
            if(isXSLT(file)){
                if(saveXslt(file)){
                    JRadioButton newButton = new JRadioButton(file.getName());
                    newButton.addActionListener(new XsltSelectorListener(dataPreparationToolGUI));
                    dataPreparationToolGUI.getGroupXslt().add(newButton);
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsltPanel(newButton);
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsltPanel(Box.createRigidArea(new Dimension(0, 10)));
                    JOptionPane.showMessageDialog(parent, labels.getString("xsltSaved")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                } else {
                    JOptionPane.showMessageDialog(parent, labels.getString("xsltNotSaved")+".", labels.getString("fileNotSaved"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
                }
            } else {
                JOptionPane.showMessageDialog(parent, labels.getString("xsltNotSaved")+".", labels.getString("fileNotSaved"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
            }
        }
    }

    private boolean isXSLT(File file){
        return (file.isFile() && (file.getName().endsWith(".xsl") || file.getName().endsWith(".xslt")) && XsltChecker.isXsltFile(file));
    }

    private boolean saveXslt(File file){
        try{
            if(new File(Utilities.CONFIG_DIR + file.getName()).exists())
                throw new Exception("XSL file already exists");
            FileUtils.copyFile(file, new File(Utilities.CONFIG_DIR + file.getName()));
            return true;
        } catch (IOException e){
            LOG.error("Moving file " + file.getAbsolutePath() + " failed", e);
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
