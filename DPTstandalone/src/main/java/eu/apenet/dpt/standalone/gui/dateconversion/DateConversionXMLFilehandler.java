/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui.dateconversion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DateConversionXMLFilehandler {

    public void saveDataToFile(Vector data, String xmlFile) {
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
            StreamResult result = new StreamResult(new StringWriter());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            FileOutputStream fop = null;
            File file;
            try {
                file = new File(xmlFile);
                fop = new FileOutputStream(file);
                if (!file.exists()) {
                    file.createNewFile();
                }
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

    public Vector loadDataFromFile(String xmlFile) {
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
    
    public String findsEntry(String input){
        Vector data = loadDataFromFile("xsl/system/dateconversion.xml");
        for(int i = 0; i < data.size(); i++){
            if(input.equals(((Vector) data.get(i)).get(0))){
                return (String) ((Vector) data.get(i)).get(1);
            }
        }
        return null;
    }
}
