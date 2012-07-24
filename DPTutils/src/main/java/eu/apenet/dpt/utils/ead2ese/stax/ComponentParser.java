package eu.apenet.dpt.utils.ead2ese.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;


public interface ComponentParser {
  
  /**
   * Parse some XML data using the supplied stax reader.
   * @param xmlReader STAX reader.
   * @throws XMLStreamException
   */
  public String parse(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException;
  
  public String parse(String identifier, XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException;
  
  public QName getQName();
 
}
