package eu.apenet.dpt.standalone.gui;

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
            FileUtils.copyFile(file, new File(Utilities.CONFIG_DIR + file.getName()));
            return true;
        } catch (IOException e){
            LOG.error("Moving file " + file.getAbsolutePath() + " failed", e);
            return false;
        }
    }
}