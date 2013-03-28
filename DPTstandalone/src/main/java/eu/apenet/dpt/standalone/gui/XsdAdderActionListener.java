package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.xsdaddition.XsdInfoQueryComponent;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.utils.util.XsdChecker;
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
                //todo: Ask user to provide a readable name for this schema
                XsdInfoQueryComponent xsdInfoQueryComponent = new XsdInfoQueryComponent(labels, file.getName());

                int result = JOptionPane.showConfirmDialog(parent, xsdInfoQueryComponent, "title", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION) {
                    LOG.info("Ok");
                    if(saveXsd(file, xsdInfoQueryComponent.getName(), false, xsdInfoQueryComponent.getXsdVersion(), xsdInfoQueryComponent.getFileType())) {
                        JRadioButton newButton = new JRadioButton(file.getName());
                        newButton.addActionListener(new XsdSelectorListener(dataPreparationToolGUI));
                        dataPreparationToolGUI.getGroupXsd().add(newButton);
                        dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsdPanel(newButton);
                        dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsdPanel(Box.createRigidArea(new Dimension(0, 10)));
                        JOptionPane.showMessageDialog(parent, labels.getString("xsdSaved")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                    } else {
                        errorMessage();
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