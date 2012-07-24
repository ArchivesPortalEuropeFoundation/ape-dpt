package eu.apenet.dpt.utils.util;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Nov 24, 2010
 *
 * @author Yoann Moranville
 */
public class SeparateFinnishFiles {
    private final static Logger LOG = Logger.getLogger(SeparateFinnishFiles.class);
    public static final String APE_EAD = "urn:isbn:1-931666-22-9";
	public static final String XLINK = "http://www.w3.org/1999/xlink";
	public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";

    protected static final QName EAD_ELEMENT = new QName(APE_EAD, "ead");
    protected static final QName C_ELEMENT = new QName(APE_EAD, "c");

    public SeparateFinnishFiles(File directory, String tmpDir){
        File newDir = new File(tmpDir + "/finland/");
        if(!newDir.exists())
            newDir.mkdirs();
        List<String> failed = new ArrayList<String>();
        if(directory.isDirectory()){
            for(File file : directory.listFiles()){
                if(file.getName().endsWith(".xml")){
                    try {
                        LOG.info("Separating file " + file.getName());
                        separateFile(file, newDir);
                    } catch (Exception e) {
                        failed.add(file.getName());
                        LOG.error("Failed...", e);
                    }
                }
            }
            for(String failedFile : failed)
                LOG.info(failedFile + " failed...");
        }
    }

    public SeparateFinnishFiles() {
    }

    public List<String> separateFile(File file, File newDir) throws XMLStreamException, IOException {
        List<String> listFiles = new ArrayList<String>();
        OutputStream outputStream = null;
        XMLStreamWriter xmlWriter = null;

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 input = xmlif.createXMLStreamReader(file);

        String currentId = "";
        String eadidIdentifier = "";
        boolean isInsideRecord = false;
        boolean isInsideId = false;

        while (true) {
            switch (input.getEventType()) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    if(input.getLocalName().equals("identifier")){
                        isInsideId = true;
                    } else if(input.getLocalName().equals("ead")){
                        if(xmlWriter != null){
                            xmlWriter.close();
                            outputStream.close();
                        }

                        String fileName = currentId.replace("/", "_").replace(":", "_") + ".xml";
                        outputStream = new FileOutputStream(new File(newDir.getAbsolutePath() + "/" + fileName));

                        xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream);
                        isInsideRecord = true;
                        listFiles.add(fileName);

                        writeEAD(input, xmlWriter);
                    } else if(isInsideRecord) {
                        writeNameElement(input, xmlWriter);
                    }
                    break;
                case XMLEvent.CHARACTERS:
                    if(isInsideId){
                        currentId = input.getText();
                        isInsideId = false;
                    }
                    if(isInsideRecord)
                        xmlWriter.writeCharacters(input.getText());
                    break;
                case XMLEvent.CDATA:
                    if(isInsideRecord)
                        xmlWriter.writeCData(input.getText());
                    break;
                case XMLEvent.END_ELEMENT:
                    if(isInsideRecord)
                        writeEndElement(xmlWriter);
                    if(input.getLocalName().equals("ead")){
                        isInsideRecord = false;
                    }
                    break;
            }
            if (!input.hasNext())
                break;
            try {
                input.next();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        if(xmlWriter != null){
            xmlWriter.close();
            outputStream.close();
            input.close();
        }
        return listFiles;
    }

    private void writeNameElement(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null){
            String eadidId = "";
            QName element = xmlReader.getName();
            xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
            for (int i=0; i < xmlReader.getAttributeCount(); i++){
                if(!xmlReader.getAttributeLocalName(i).equals("schemaLocation"))
                    xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
                if(xmlReader.getLocalName().equals("eadid") && xmlReader.getAttributeLocalName(i).equals("identifier"))
                    eadidId = xmlReader.getAttributeValue(i);
            }
            if(!eadidId.equals(""))
                xmlWriter.writeCharacters(eadidId);
        }
    }

    private void writeEndElement(XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (xmlWriter != null){
            xmlWriter.writeEndElement();
        }
    }

    private void writeEAD(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter) throws XMLStreamException {
		if (xmlWriter != null) {
			QName name = xmlReader.getName();
			xmlWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
            for (int i=0; i < xmlReader.getAttributeCount(); i++) {
                if(!xmlReader.getAttributeLocalName(i).equals("schemaLocation"))
                    xmlWriter.writeAttribute(xmlReader.getAttributePrefix(i), xmlReader.getAttributeNamespace(i), xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
            }
			xmlWriter.writeDefaultNamespace(APE_EAD);
			xmlWriter.writeNamespace("xlink", XLINK);
			xmlWriter.writeNamespace("xsi", XSI);
		}
	}

}
