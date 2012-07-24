package eu.apenet.dpt.utils.ead2ese.stax;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class AbstractParser implements ComponentParser {
	public static final String DCTERMS = "http://purl.org/dc/terms/";
	public static final String EUROPEANA = "http://www.europeana.eu/schemas/ese/";
	public static final String DC = "http://purl.org/dc/elements/1.1/";
	private QName qName;
    private Map<QName, ComponentParser> delegates = new HashMap<QName, ComponentParser>();
    private ComponentParser defaultParser;
    
	public AbstractParser(QName qName){
		this.qName = qName;
	}
	

	protected void writeEndElement(XMLStreamWriter xmlWriter) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeEndElement();
		}		
	}

	protected void writeStartElement(XMLStreamWriter xmlWriter) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
		}		
	}
	
	protected void writeElement(XMLStreamWriter xmlWriter, String value) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
			xmlWriter.writeCharacters(value);
			xmlWriter.writeEndElement();
		}
		
	}
	protected void writeStartElement(XMLStreamWriter xmlWriter, QName elementName) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeStartElement(elementName.getPrefix(), elementName.getLocalPart(), elementName.getNamespaceURI());
		}		
	}
	
	protected void writeElement(XMLStreamWriter xmlWriter, String value, QName elementName) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeStartElement(elementName.getPrefix(), elementName.getLocalPart(), elementName.getNamespaceURI());
			xmlWriter.writeCharacters(value);
			xmlWriter.writeEndElement();
		}
		
	}
	protected void writeElement(String identifier, XMLStreamWriter xmlWriter, String value, QName elementName) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeStartElement(elementName.getPrefix(), elementName.getLocalPart(), elementName.getNamespaceURI());
			xmlWriter.writeCharacters(value);
			xmlWriter.writeEndElement();
		}
		
	}
	
	protected void writeCharacters(XMLStreamWriter xmlWriter, String value) throws XMLStreamException{
		if (xmlWriter != null){
			xmlWriter.writeCharacters(value);
		}		
	}
	
    public void registerParser(ComponentParser parser) {
    	if (parser.getQName() == null) {
    		defaultParser = parser;
    	}else {
    		delegates.put(parser.getQName(),parser);
    	}
    }

    public void registerParser(QName qName, ComponentParser parser) {
   		delegates.put(qName,parser);
    }
    
	public QName getQName() {
		return qName;
	}

	public Map<QName, ComponentParser> getDelegates() {
		return delegates;
	}


	public ComponentParser getDefaultParser() {
		return defaultParser;
	}
    
}
