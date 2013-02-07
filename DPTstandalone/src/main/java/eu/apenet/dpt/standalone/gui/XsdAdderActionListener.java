package eu.apenet.dpt.standalone.gui;

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

    public XsdAdderActionListener(DataPreparationToolGUI dataPreparationToolGUI, ResourceBundle labels) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.parent = dataPreparationToolGUI.getContentPane();
        this.labels = labels;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser xsdChooser = new JFileChooser();
        xsdChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if(xsdChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION){
            File file = xsdChooser.getSelectedFile();
            if(isXSD(file)){
                if(saveXsd(file)){
                    JRadioButton newButton = new JRadioButton(file.getName());
                    newButton.addActionListener(new XsdSelectorListener(dataPreparationToolGUI));
                    dataPreparationToolGUI.getGroupXsd().add(newButton);
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsdPanel(newButton);
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().addToXsdPanel(Box.createRigidArea(new Dimension(0, 10)));
                    JOptionPane.showMessageDialog(parent, labels.getString("xsltSaved")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                } else {
                    JOptionPane.showMessageDialog(parent, labels.getString("xsltNotSaved")+".", labels.getString("fileNotSaved"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
                }
            } else {
                JOptionPane.showMessageDialog(parent, labels.getString("xsltNotSaved")+".", labels.getString("fileNotSaved"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
            }
        }
    }

    private boolean isXSD(File file){
        return (file.isFile() && file.getName().endsWith(".xsd") && XsdChecker.isXsdFile(file));
    }

    private boolean saveXsd(File file){
        try{
            FileUtils.copyFile(file, new File(Utilities.CONFIG_DIR + file.getName()));
            //todo: add to database!
            return true;
        } catch (IOException e){
            LOG.error("Moving file " + file.getAbsolutePath() + " failed", e);
            return false;
        }
    }
}