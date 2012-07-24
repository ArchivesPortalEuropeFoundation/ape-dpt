package eu.apenet.dpt.utils.ead2ese.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class ESEParser extends AbstractParser{


	private static final String METADATA = "metadata";

	public ESEParser() {
		super(new QName(EUROPEANA, METADATA));
	}

	public String parse(String identifier, XMLStreamReader xmlReader, XMLStreamWriter xmlWriter)
			throws XMLStreamException {
		return null;
	}

	public String parse(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
		for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				QName elementName = xmlReader.getName();
				if (getQName().equals(elementName)) {
					writeMetadata(xmlReader, xmlWriter);
				}else if (getDelegates().containsKey(elementName)){
					getDelegates().get(elementName).parse(xmlReader, xmlWriter);
				}
			} else if (event == XMLStreamConstants.END_ELEMENT) {
				QName elementName = xmlReader.getName();
				if (getQName().equals(elementName)) {
					writeEndElement(xmlWriter);
				}
			} 
		}
		return null;
	}

	private void writeMetadata(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
		if (xmlWriter != null){
			QName name = xmlReader.getName();
			xmlWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
			xmlWriter.writeDefaultNamespace(EUROPEANA);
			xmlWriter.writeNamespace("dc", DC);
			xmlWriter.writeNamespace("dcterms", DCTERMS);
			xmlWriter.writeNamespace("europeana", EUROPEANA);
		}
	}
}
