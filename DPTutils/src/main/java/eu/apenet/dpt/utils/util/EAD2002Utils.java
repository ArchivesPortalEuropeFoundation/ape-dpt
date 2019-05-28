/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author apef
 */
public class EAD2002Utils {

    public static String retrieveEadid(File file) throws XMLStreamException, FileNotFoundException {
        String result = null;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileInputStream(file));
        while (reader.hasNext()) {
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                String XMLTag = reader.getLocalName();
                if (XMLTag.equals("eadid")) {
                    result = reader.getElementText();
                }
            }
            reader.next();
        }
        return result;
    }

}
