/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.utils.util.XmlChecker;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JList;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
class ConvertEdmActionListener implements ActionListener {

    private static final Logger LOG = Logger.getLogger(ConvertEdmActionListener.class);
    private Component parent;
    private ResourceBundle labels;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private APEPanel apePanel;

    public ConvertEdmActionListener(Component parent, DataPreparationToolGUI dataPreparationToolGUI, APEPanel apePanel) {
        this.parent = parent;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.apePanel = apePanel;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Button \"Convert to EDM\" has been pushed; function to be added soon");
    }
}
