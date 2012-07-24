package eu.apenet.dpt.utils.ead2ese.stax;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class RecordParser extends AbstractParser {
	private static final String EUROPEANA = "http://www.europeana.eu/schemas/ese/";
	private static final QName RECORD = new QName(EUROPEANA, "record");
	private static final QName IDENTIFIER = new QName(DC, "identifier");
	private int numberOfRecords = 0; 
	private List<String> identifiers = new ArrayList<String>();
	
	public RecordParser() {
		super(RECORD);
		registerParser(new DefaultTextParser());
	}

	public String parse(String identifier, XMLStreamReader xmlReader, XMLStreamWriter xmlWriter)
			throws XMLStreamException {
		return null;
	}
	public String parse(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
		QName elementName = xmlReader.getName();
		writeStartElement(xmlWriter);
		numberOfRecords++;
		boolean foundEndElement = false;
		String identifier = null;
		for (int event = xmlReader.next(); !foundEndElement && event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				elementName = xmlReader.getName();
				if (getDelegates().containsKey(elementName)){
					getDelegates().get(elementName).parse(identifier, xmlReader, xmlWriter);
					
				}else if (getDefaultParser() != null) {
					String content = getDefaultParser().parse(identifier, xmlReader, xmlWriter);
					if (IDENTIFIER.equals(elementName)){
						identifier = content;
						identifiers.add(identifier);
					}
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

	public int getNumberOfRecords() {
		return numberOfRecords;
	}
	public List<String> getIdentifiers() {
		return identifiers;
	}
	
}
