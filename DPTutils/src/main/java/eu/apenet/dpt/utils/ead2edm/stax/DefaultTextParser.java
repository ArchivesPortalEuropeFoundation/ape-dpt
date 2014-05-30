package eu.apenet.dpt.utils.ead2edm.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class DefaultTextParser extends AbstractParser {

	public DefaultTextParser() {
		super(null);
	}

	public DefaultTextParser(QName qname) {
		super(qname);
	}

	public String parse(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
		return parse(xmlReader, xmlWriter);
	}

	public String parse(String identifier, XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
		String value = xmlReader.getElementText();
		writeElement(identifier, xmlWriter, value,  xmlReader.getName());
		return value;
	}

}
