package eu.apenet.dpt.utils.ead2edm.stax;

import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * User: yoannmoranville
 * Date: 30/06/14
 *
 * @author yoannmoranville
 */
public class WebResourceParser extends AbstractParser {

    private static final QName WEB_RESOURCE = new QName(EDM, "WebResource");
    private int numberOfWebResource = 0;

    public WebResourceParser() {
        super(WEB_RESOURCE);
        registerParser(new DefaultTextParser());
    }

    public String parse(String identifier, XMLStreamReader xmlReader, XMLStreamWriter xmlWriter)
            throws XMLStreamException {
        return null;
    }
    public String parse(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
        QName elementName = xmlReader.getName();
        writeStartElement(xmlWriter);
        numberOfWebResource++;
        boolean foundEndElement = false;
        String identifier = null;
        for (int event = xmlReader.next(); !foundEndElement && event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                elementName = xmlReader.getName();
                if (getDelegates().containsKey(elementName)){
                    getDelegates().get(elementName).parse(identifier, xmlReader, xmlWriter);

                }else if (getDefaultParser() != null) {
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                elementName = xmlReader.getName();
                if (getQName().equals(elementName)) {
                    foundEndElement = true;
                    writeEndElement(xmlWriter);
                }
            }
        }
        return null;


    }

    public int getNumberOfWebResource() {
        return numberOfWebResource;
    }

}