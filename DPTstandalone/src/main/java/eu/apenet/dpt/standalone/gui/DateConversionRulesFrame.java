/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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
    JTable ruleTable;

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

        DefaultTableModel dm = new DefaultTableModel(loadDataFromFile(FILENAME), columnNames);
        ruleTable = new JTable(dm);

        JButton saveButton = new JButton(labels.getString("saveBtn"));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Vector data = ((DefaultTableModel) ruleTable.getModel()).getDataVector();
                saveDataToFile(data, FILENAME);
            }
        });

        JButton cancelButton = new JButton(labels.getString("cancelBtn"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JButton downloadButton = new JButton(labels.getString("downloadBtn"));
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
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
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);

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
        System.out.println("Button 'save' has been pushed");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("datelist");
            doc.appendChild(root);

            for (int i = 0; i < data.size(); i++) {
                Element dataElement = doc.createElement("data");
                root.appendChild(dataElement);

                Vector row = (Vector) data.get(i);
                Element valueRead = doc.createElement("valueread");
                valueRead.appendChild(doc.createTextNode((String) row.get(0)));
                Element valueConverted = doc.createElement("valueconverted");
                valueConverted.appendChild(doc.createTextNode((String) row.get(1)));
                dataElement.appendChild(valueRead);
                dataElement.appendChild(valueConverted);
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
                System.out.println(xmlString);
                byte[] contentInBytes = xmlString.getBytes();

                fop.write(contentInBytes);
                fop.flush();
                fop.close();

                System.out.println("Done");

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
}
