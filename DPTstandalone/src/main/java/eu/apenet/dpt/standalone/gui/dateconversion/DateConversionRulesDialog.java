/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui.dateconversion;

import eu.apenet.dpt.utils.util.DateConversionXMLFilehandler;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
public class DateConversionRulesDialog extends JDialog{
    private static final Logger LOG = Logger.getLogger(DateConversionRulesDialog.class);

    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private DateConversionXMLFilehandler xmlFilehandler;
    private final String FILENAME = Utilities.SYSTEM_DIR + "dateconversion.xml";
    private JTable ruleTable;
    private JLabel saveMessage;
    private DefaultTableModel dm;
    private DefaultTableModel oldModel;

    public DateConversionRulesDialog(ResourceBundle labels, RetrieveFromDb retrieveFromDb) {
        super.setTitle(labels.getString("dateConversionListTitle"));
        super.setModalityType(ModalityType.APPLICATION_MODAL);
        xmlFilehandler = new DateConversionXMLFilehandler();
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
        createDataConversionRulesList();
    }

    private void createDataConversionRulesList() {
        Vector<String> columnNames = new Vector<String>();
        columnNames.add(labels.getString("dateConversion.valueRead"));
        columnNames.add(labels.getString("dateConversion.valueConverted"));
        dm = new DefaultTableModel(xmlFilehandler.loadDataFromFile(FILENAME), columnNames);
        dm.addRow(new Vector<String>());
        dm.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if(ruleTable.getEditingRow() != ruleTable.getRowCount()-1 && (StringUtils.isEmpty((String)dm.getValueAt(ruleTable.getEditingRow(), 0)) && StringUtils.isEmpty((String)dm.getValueAt(ruleTable.getEditingRow(), 1)))) {
                    dm.removeRow(ruleTable.getEditingRow());
                }
                if (ruleTable.getEditingRow() == ruleTable.getRowCount() - 1) {
                    if(StringUtils.isNotEmpty((String)dm.getValueAt(ruleTable.getEditingRow(), 0)) && StringUtils.isNotEmpty((String)dm.getValueAt(ruleTable.getEditingRow(), 1))) {
                        dm.addRow(new Vector<String>());
                    }
                }
                if (ruleTable.getEditingColumn() == 1) {
                    if (StringUtils.isNotEmpty((String)dm.getValueAt(ruleTable.getEditingRow(), 1)) && !isCorrectDateFormat((String) dm.getValueAt(ruleTable.getEditingRow(), 1))) {
                        createOptionPaneForIsoDate(ruleTable.getEditingRow(), 1);
                    }
                }
            }
        });

        ruleTable = new JTable(dm);
        oldModel = new DefaultTableModel(xmlFilehandler.loadDataFromFile(FILENAME), columnNames);


        JButton saveButton = new JButton(labels.getString("saveBtn"));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ruleTable.isEditing()) {
                    ruleTable.getCellEditor().stopCellEditing();
                }
                Vector<Vector<String>> data = ((DefaultTableModel) ruleTable.getModel()).getDataVector();
                for (int i = 0; i < data.size() - 1; i++) {
                    Vector<String> vector = data.elementAt(i);
                    if (vector.elementAt(1) != null && !isCorrectDateFormat((String) vector.elementAt(1))) {
                        createOptionPaneForIsoDate(i, 1);
                    }
                }
                xmlFilehandler.saveDataToFile(data, FILENAME);
                saveMessage.setText(MessageFormat.format(labels.getString("dateConversion.saveMsg"), new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())));
            }
        });

        JButton closeButton = new JButton(labels.getString("quit"));
        closeButton.addActionListener(new ActionListener() {
            // boolean cancel = true;

            public void actionPerformed(ActionEvent e) {
                /*System.out.println(Boolean.toString(oldModel.getRowCount() == (ruleTable.getModel().getRowCount() - 1)) + "<<" + oldModel.getRowCount() + ", " + (ruleTable.getModel().getRowCount() - 1));
                if (oldModel.getRowCount() != ruleTable.getModel().getRowCount() - 1) {
                    cancel = showUnsavedChangesDialog();
                } else {
                    for (int i = 0; i < (ruleTable.getModel().getRowCount() - 1); i++) {
                        for (int j = 0; j <= 1; j++) {
                            System.out.println(oldModel.getValueAt(i, j) == ruleTable.getModel().getValueAt(i, j) + " >> " + oldModel.getValueAt(i, j) + ", " + ruleTable.getModel().getValueAt(i, j));
                            if (oldModel.getValueAt(i, j) != ruleTable.getModel().getValueAt(i, j)) {
                                cancel = showUnsavedChangesDialog();
                            }
                        }
                    }
                }
                if (cancel) {*/
                    dispose();
              //  }
            }
        });

        JButton downloadButton = new JButton(labels.getString("downloadBtn"));
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ruleTable.isEditing()) {
                    ruleTable.getCellEditor().stopCellEditing();
                }
                Vector<Vector<String>> data = ((DefaultTableModel) ruleTable.getModel()).getDataVector();
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
                    saveMessage.setText(MessageFormat.format(labels.getString("dateConversion.saveMsg"), new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())));
                }
            }
        });

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JScrollPane(ruleTable));
        saveMessage = new JLabel();
        contentPanel.add(saveMessage, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(downloadButton);

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(contentPanel, BorderLayout.PAGE_START);
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
        String result;
        do {
            result = (String) JOptionPane.showInputDialog(getContentPane(), explanation, labels.getString("dateConversion.header"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            if(result == null)
                break;
        } while (dateNormalization.checkForNormalAttribute(result) == null);
        if (result != null) {
            dm.setValueAt(result, row, column);
            dm.fireTableCellUpdated(row, column);
        } else {
            dm.setValueAt("", row, column);
            dm.fireTableCellUpdated(row, column);
        }
    }

    private boolean showUnsavedChangesDialog() {
        int result = JOptionPane.showConfirmDialog(this, "You have unsaved changes. Cancel anyway?", "Unsaved changes", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }
}
