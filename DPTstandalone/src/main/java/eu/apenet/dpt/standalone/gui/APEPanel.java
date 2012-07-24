package eu.apenet.dpt.standalone.gui;

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

    public APEPanel(ResourceBundle labels, Component parent, DataPreparationToolGUI dataPreparationToolGUI) {
        super(new BorderLayout());
        this.labels = labels;
        filename = new JLabel();
        setFilename("");
        filename.setHorizontalAlignment(JLabel.LEFT);
        add(filename, BorderLayout.NORTH);
        apeTabbedPane = new APETabbedPane(labels, this, parent, dataPreparationToolGUI);
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
