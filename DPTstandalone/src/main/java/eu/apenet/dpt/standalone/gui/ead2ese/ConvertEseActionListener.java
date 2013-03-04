package eu.apenet.dpt.standalone.gui.ead2ese;

import eu.apenet.dpt.standalone.gui.APEPanel;
import eu.apenet.dpt.standalone.gui.APETabbedPane;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class ConvertEseActionListener implements ActionListener {
    private DataPreparationToolGUI dataPreparationToolGUI;
    private ResourceBundle labels;
    private APEPanel apePanel;

    public ConvertEseActionListener(ResourceBundle labels, DataPreparationToolGUI dataPreparationToolGUI, APEPanel apePanel) {
        this.labels = labels;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.apePanel = apePanel;
    }

    public void actionPerformed(ActionEvent e) {
        apePanel.getApeTabbedPane().disableConversionEseBtn();
        dataPreparationToolGUI.disableConversionEseBtn();

        JFrame eseOptionFrame = new JFrame(labels.getString("ese.eseOptionFrame"));
        eseOptionFrame.add(new EseOptionsPanel(labels, dataPreparationToolGUI.getXmlEadList().getSelectedValues(), eseOptionFrame, apePanel.getApeTabbedPane(), dataPreparationToolGUI.getFileInstances()));
        eseOptionFrame.setPreferredSize(new Dimension(dataPreparationToolGUI.getContentPane().getWidth() *3/4, dataPreparationToolGUI.getContentPane().getHeight() *3/4));
        eseOptionFrame.pack();
        eseOptionFrame.setVisible(true);
    }
}