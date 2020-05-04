package eu.apenet.dpt.utils.ead2edm.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * User: stefanpapp-ape Date: 04/05/20
 *
 * @author stefanpapp-ape
 */
public class IsShownByParser extends AbstractParser {

    private static final QName IS_SHOWN_BY = new QName(EDM, "isShownBy");
    private boolean hasIsShownBy = false;

    public IsShownByParser() {
        super(IS_SHOWN_BY);
        registerParser(new DefaultTextParser());
    }

    @Override
    public String parse(String identifier, XMLStreamReader xmlReader, XMLStreamWriter xmlWriter)
            throws XMLStreamException {
        return null;
    }

    @Override
    public String parse(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
        QName elementName = xmlReader.getName();
        writeStartElement(xmlWriter);
        boolean foundEndElement = false;
        String identifier = null;
        for (int event = xmlReader.next(); !foundEndElement && event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                elementName = xmlReader.getName();
                if (getDelegates().containsKey(elementName)) {
                    getDelegates().get(elementName).parse(identifier, xmlReader, xmlWriter);

                } else if (getDefaultParser() != null) {
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                elementName = xmlReader.getName();
                if (getQName().equals(elementName)) {
                    foundEndElement = true;
                    hasIsShownBy = true;
                    writeEndElement(xmlWriter);
                }
            }
        }
        return null;

    }

    public int getNumberOfIsShownBy() {
        return hasIsShownBy ? 1 : 0;
    }

}
