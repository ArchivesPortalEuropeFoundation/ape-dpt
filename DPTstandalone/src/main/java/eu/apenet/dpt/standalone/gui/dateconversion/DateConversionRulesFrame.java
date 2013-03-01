/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui.dateconversion;

import eu.apenet.dpt.utils.util.DateConversionXMLFilehandler;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author papp
 */
public class DateConversionRulesFrame extends JFrame {

    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private DateConversionXMLFilehandler xmlFilehandler;
    private final String FILENAME = Utilities.SYSTEM_DIR + "dateconversion.xml";
    private JTable ruleTable;
    private DefaultTableModel dm;

    public DateConversionRulesFrame(ResourceBundle labels, RetrieveFromDb retrieveFromDb) {
        xmlFilehandler = new DateConversionXMLFilehandler();
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
        createDataConversionRulesList();
    }

    private void createDataConversionRulesList() {
    super.setTitle(labels.getString("dateConversionListTitle"));

        Vector columnNames = new Vector();
        columnNames.add(labels.getString("dateConversion.valueRead"));
        columnNames.add(labels.getString("dateConversion.valueConverted"));
        dm = new DefaultTableModel(xmlFilehandler.loadDataFromFile(FILENAME), columnNames);
        dm.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (ruleTable.getEditingRow() == ruleTable.getRowCount() - 1) {
                    dm.addRow(new Vector());
                }
                if (ruleTable.getEditingColumn() == 1) {
                    if (!isCorrectDateFormat((String) dm.getValueAt(ruleTable.getEditingRow(), 1))) {
                        createOptionPaneForIsoDate(ruleTable.getEditingRow(), 1);
                    }
                }
            }
        });

        ruleTable = new JTable(dm);

        JButton saveButton = new JButton(labels.getString("saveBtn"));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ruleTable.isEditing()) {
                    ruleTable.getCellEditor().stopCellEditing();
                }
                Vector data = ((DefaultTableModel) ruleTable.getModel()).getDataVector();
                xmlFilehandler.saveDataToFile(data, FILENAME);
            }
        });

        JButton cancelButton = new JButton(labels.getString("cancelBtn"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: Ask for unsaved changes
                dispose();
            }
        });

        JButton downloadButton = new JButton(labels.getString("downloadBtn"));
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ruleTable.isEditing()) {
                    ruleTable.getCellEditor().stopCellEditing();
                }
                Vector data = ((DefaultTableModel) ruleTable.getModel()).getDataVector();
                File currentLocation = new File(retrieveFromDb.retrieveOpenLocation());
                JFileChooser fileChooser = new JFileChooser(currentLocation);
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
                int returnedVal = fileChooser.showSaveDialog(getParent());

                if (returnedVal == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().toString();
                    if (!fileName.endsWith(".xml")) {
                        fileName = fileName + ".xml";
                    }
                    xmlFilehandler.saveDataToFile(data, fileName);
                    //additionally save data to standard file
                    xmlFilehandler.saveDataToFile(data, FILENAME);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(downloadButton);

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(new JScrollPane(ruleTable), BorderLayout.PAGE_START);
        pane.add(buttonPanel, BorderLayout.PAGE_END);
        add(pane);
    }

    private boolean isCorrectDateFormat(String date) {
        Pattern isoDatePattern = Pattern.compile("(\\-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|\\-((01|02|03|04|05|06|07|08|09|10|11|12)(\\-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)(/\\-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|\\-((01|02|03|04|05|06|07|08|09|10|11|12)(\\-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)?");
        Matcher matcher_correct_simple = isoDatePattern.matcher(date);
        if (matcher_correct_simple.matches()) {
            //We need a second check for YYYYMMDD and YYYYMMDD/YYYYMMDD which passes above
            if (!Pattern.compile("([0-9]{8})").matcher(date).matches() && !Pattern.compile("([0-9]{8})/([0-9]{8})").matcher(date).matches()) {
                return true;
            }
        }
        return false;
    }

    private void createOptionPaneForIsoDate(int row, int column) {
        DateNormalization dateNormalization = new DateNormalization();
        String currentResult = (String) dm.getValueAt(row, column);
        String explanation = "'" + currentResult + "' " + labels.getString("dateConversion.notValidDate") + "\n" + labels.getString("dateConversion.enterCorrectIsoDate") + "\n" + labels.getString("dateConversion.validValues");
        int i = 0;
        String result;
        do {
            result = (String) JOptionPane.showInputDialog(getContentPane(), explanation, labels.getString("chooseRepositoryCode"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            if (result == null) {
                break;
            }
            i++;
        } while (dateNormalization.checkForNormalAttribute(result) == null);
        if (result != null) {
            dm.setValueAt(result, row, column);
            dm.fireTableCellUpdated(row, column);
        }
    }
}
