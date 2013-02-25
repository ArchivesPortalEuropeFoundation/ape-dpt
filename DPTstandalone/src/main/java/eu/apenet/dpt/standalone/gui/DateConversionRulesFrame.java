/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author papp
 */
public class DateConversionRulesFrame extends JFrame {

    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private final String FILENAME = "xsl/system/dateconversion.xml";
    private JTable ruleTable;
    private DefaultTableModel dm;

    public DateConversionRulesFrame(ResourceBundle labels, RetrieveFromDb retrieveFromDb) {
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
        createDataConversionRulesList();
    }

    private void createDataConversionRulesList() {
        super.setTitle(labels.getString("dateConversionListTitle"));

        Vector columnNames = new Vector();
        columnNames.add(labels.getString("dateConversion.valueRead"));
        columnNames.add(labels.getString("dateConversion.valueConverted"));
        dm = new DefaultTableModel(loadDataFromFile(FILENAME), columnNames);
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
                saveDataToFile(data, FILENAME);
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
                    saveDataToFile(data, fileName);
                    //additionally save data to standard file
                    saveDataToFile(data, FILENAME);
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

    private Vector loadDataFromFile(String xmlFile) {
        Vector result = new Vector();
        File file = new File(xmlFile);
        try {
            if (!file.exists()) {
                saveDataToFile(result, xmlFile);
            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            Element root = doc.getDocumentElement();
            NodeList dateNodes = root.getElementsByTagName("date");
            for (int i = 0; i < dateNodes.getLength(); i++) {
                Node node = dateNodes.item(i);
                Vector row = new Vector();
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    row.add(element.getElementsByTagName("valueread").item(0).getTextContent());
                    row.add(element.getElementsByTagName("valueconverted").item(0).getTextContent());
                }
                result.add(row);
            }
            result.add(new Vector());
        } catch (SAXException ex) {
            Logger.getLogger(DateConversionRulesFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DateConversionRulesFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DateConversionRulesFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void saveDataToFile(Vector data, String xmlFile) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("datelist");
            doc.appendChild(root);

            for (int i = 0; i < data.size(); i++) {
                Vector row = (Vector) data.get(i);
                if (row.get(0) != "" && row.get(1) != "" && row.get(0) != null) {
                    Element dataElement = doc.createElement("date");
                    root.appendChild(dataElement);

                    Element valueRead = doc.createElement("valueread");
                    valueRead.appendChild(doc.createTextNode((String) row.get(0)));
                    Element valueConverted = doc.createElement("valueconverted");
                    valueConverted.appendChild(doc.createTextNode((String) row.get(1)));
                    dataElement.appendChild(valueRead);
                    dataElement.appendChild(valueConverted);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new StringWriter());//new StreamResult(System.out);//
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            //writing to file
            FileOutputStream fop = null;
            File file;
            try {
                file = new File(xmlFile);
                fop = new FileOutputStream(file);

                // if file doesnt exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }

                // get the content in bytes
                String xmlString = result.getWriter().toString();
                byte[] contentInBytes = xmlString.getBytes();

                fop.write(contentInBytes);
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fop != null) {
                        fop.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (TransformerException ex) {
            Logger.getLogger(DateConversionRulesFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DateConversionRulesFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
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
