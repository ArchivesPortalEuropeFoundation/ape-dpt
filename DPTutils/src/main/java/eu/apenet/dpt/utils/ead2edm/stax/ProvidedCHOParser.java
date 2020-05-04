package eu.apenet.dpt.utils.ead2edm.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class ProvidedCHOParser extends AbstractParser {

	private static final QName PROVIDED_CHO = new QName(EDM, "ProvidedCHO");
	private boolean hasProvidedCHO = false;

	public ProvidedCHOParser() {
		super(PROVIDED_CHO);
		registerParser(new DefaultTextParser());
	}

	public String parse(String identifier, XMLStreamReader xmlReader, XMLStreamWriter xmlWriter)
			throws XMLStreamException {
		return null;
	}
	public String parse(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
		QName elementName = xmlReader.getName();
		writeStartElement(xmlWriter);
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
                                        hasProvidedCHO = true;
					writeEndElement(xmlWriter);
				}
			}
		}
		return null;


	}

    public int getNumberOfProvidedCHO() {
        return hasProvidedCHO ? 1 : 0;
    }

        
}
