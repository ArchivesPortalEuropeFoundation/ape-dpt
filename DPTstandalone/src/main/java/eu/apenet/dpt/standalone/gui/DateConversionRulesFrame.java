/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author papp
 */
public class DateConversionRulesFrame extends JFrame {

    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;

    public DateConversionRulesFrame(ResourceBundle labels, RetrieveFromDb retrieveFromDb) {
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
        createDataConversionRulesList();
    }

    private void createDataConversionRulesList() {
        super.setTitle(labels.getString("dateConversionListTitle"));

        String[][] data = {{ "test", "tset" }};
        String[] columnNames = {labels.getString("dateConversion.valueRead"), labels.getString("dateConversion.valueConverted")};
        JTable ruleTable = new JTable(data, columnNames);

        JButton saveButton = new JButton(labels.getString("saveBtn"));
        JButton cancelButton = new JButton(labels.getString("cancelBtn"));
        JButton downloadButton = new JButton(labels.getString("downloadBtn"));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(downloadButton);
        
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(new JScrollPane(ruleTable), BorderLayout.PAGE_START);
        pane.add(buttonPanel, BorderLayout.PAGE_END);
        add(pane);
    }
}
