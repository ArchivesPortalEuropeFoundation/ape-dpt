package eu.apenet.dpt.utils.util;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Yoann Moranville
 * Date: 18/04/2013
 *
 * @author Yoann Moranville
 */
public class ReadXml {
    public static String getXmlNamespace(File file) throws XMLStreamException, FileNotFoundException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileInputStream(file));
        while (reader.hasNext()) {
            int evt = reader.next();
            if (evt == XMLStreamConstants.START_ELEMENT) {
                QName qName = reader.getName();
                if(qName != null){
                    if(qName.getLocalPart() != null && qName.getLocalPart().compareTo("") != 0)
                        return qName.getNamespaceURI();
                }
            }
        }
        return "";
    }

    public static boolean isEagFile(File file) throws XMLStreamException, FileNotFoundException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileInputStream(file));
        while (reader.hasNext()) {
            int evt = reader.next();
            if (evt == XMLStreamConstants.START_ELEMENT) {
                QName qName = reader.getName();
                if(qName != null){
                    if(qName.getLocalPart() != null && qName.getLocalPart().compareTo("") != 0)
                        if(qName.getLocalPart().equals("eag"))
                            return true;
                }
            }
        }
        return false;
    }
}
