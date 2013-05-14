package eu.apenet.dpt.utils.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Nov 24, 2010
 *
 * @author Yoann Moranville
 */
public class CountCLevels {
    private final Logger log = Logger.getLogger(getClass());
    private String mainagencycode;
    private List<String> mainagencycodes;
    private String path;
    private boolean copyInManagencyDirs = false;
    private boolean changeMainagencycode = false;

    public CountCLevels(String path){
        mainagencycodes = new ArrayList<String>();
        this.path = path;
    }

    public CountCLevels(){
        mainagencycodes = new ArrayList<String>();
        this.path = null;
    }

    public void setCopyInAppropriateDirs(boolean shouldCopy){
        copyInManagencyDirs = shouldCopy;
    }

    public void changeMainagencycodeForSweden(boolean shouldChangeMac){
        changeMainagencycode = shouldChangeMac;
    }

    public void countLevels(){
        File directory = new File(path);
        int count = 0;
        if(directory.isDirectory()){
            for(File file : directory.listFiles()){
                if(file.getName().endsWith(".xml")){
                    int temp_counter;
                    if((temp_counter = countOneFile(file)) != -1)
                        count += temp_counter;
                    if(changeMainagencycode && mainagencycode.equals("ÖLA")){
                        mainagencycode = "OLA";
                        createFileWithNewMac(file);
                    } else if(changeMainagencycode && mainagencycode.equals("VALA")){
                        mainagencycode = "VaLA";
                        createFileWithNewMac(file);
                    } else if(copyInManagencyDirs)
                        copyInDir(file);
//                    log.info("Current number of c levels: " + count + ", file: " + file.getName() + ", mainagencycode: " + mainagencycode);
                }
            }
        }
        log.info("FINAL NUMBER OF C LEVELS: " + count);
        for(String code : mainagencycodes){
            log.info(code);
        }
    }

    public int countOneFile(File file){
        try {
            return run(file);
        } catch (XMLStreamException e){
            log.error("XMLStreamException", e);
            return -1;
        }
    }

    public int run(File file) throws XMLStreamException {
        int count = 0;

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 input = xmlif.createXMLStreamReader(file);

        boolean isRepository = false;
        while (true) {
            switch (input.getEventType()) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    if(input.getLocalName().equals("eadid")){
                        for (int i=0; i < input.getAttributeCount(); i++){
                            if(input.getAttributeLocalName(i).equals("mainagencycode")){
                                mainagencycode = input.getAttributeValue(i);
                                if(!mainagencycodes.contains(mainagencycode))
                                    mainagencycodes.add(mainagencycode);
                            }
                        }
                    }
                    if(StringUtils.isEmpty(mainagencycode)){
                        if(input.getLocalName().equals("repository"))
                            isRepository = true;
                    }
                    if(input.getLocalName().equals("c") || input.getLocalName().equals("c01") || input.getLocalName().equals("c02") || input.getLocalName().equals("c03") || input.getLocalName().equals("c04") || input.getLocalName().equals("c05") || input.getLocalName().equals("c06") || input.getLocalName().equals("c07") || input.getLocalName().equals("c08") || input.getLocalName().equals("c09") || input.getLocalName().equals("c10") || input.getLocalName().equals("c11") || input.getLocalName().equals("c12")){
                        count ++;
                    }
                    break;
                case XMLEvent.CHARACTERS:
                case XMLEvent.CDATA:
                    if(isRepository){
                        isRepository = false;
                        mainagencycode = input.getText();
                        if(!mainagencycodes.contains(mainagencycode) && !mainagencycodes.contains(mainagencycode.trim().replace(" ", "")))
                            mainagencycodes.add(mainagencycode.trim().replace(" ", ""));
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    break;
            }
            if (!input.hasNext())
                break;
            try {
                input.next();
            } catch (Exception e){
                log.error("Error when trying to get the next input of XMLStreamReader", e);
                return -1;
            }
        }
        return count;
    }

    private void copyInDir(File file){
        File tempDir;
        if(mainagencycodes.contains(mainagencycode.trim().replace(" ", "")))
            if(mainagencycode.trim().startsWith("AD"))
                tempDir = new File("output/Sweden/FR" + mainagencycode.trim().replace(" ", ""));
            else
                tempDir = new File("output/Sweden/" + mainagencycode.trim().replace(" ", ""));
        else
            tempDir = new File("output/Sweden/" + mainagencycode.trim().replace("/", "_"));

        if(!tempDir.exists())
            tempDir.mkdirs();
        try {
            FileUtils.copyFileToDirectory(file, tempDir);
            mainagencycode = "";
        } catch (IOException e){
            log.error("Error copying file to temp dir for Sweden...", e);
        }
    }

    private void createFileWithNewMac(File file) {
        try {
            File outputDir = new File("output/Sweden/" + mainagencycode + "/");
            if(!outputDir.exists())
                outputDir.mkdirs();
            File outputFile = new File("output/Sweden/" + mainagencycode + "/" + file.getName());
            OutputStream outputStream = new FileOutputStream(outputFile);
            XMLStreamWriter2 xmlWriter = (XMLStreamWriter2) XMLOutputFactory2.newInstance().createXMLStreamWriter(outputStream);

            XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            xmlif.configureForSpeed();

            XMLStreamReader2 input = xmlif.createXMLStreamReader(file);
            while (true) {
                switch (input.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        break;
                    case XMLEvent.START_ELEMENT:
                        if (xmlWriter != null){
                            if(input.getLocalName().equals("ead")){
                                QName name = input.getName();
                                xmlWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
                                xmlWriter.writeDefaultNamespace(APE_EAD);
                                // xmlWriter.writeNamespace("", APE_EAD);
                                xmlWriter.writeNamespace("xlink", XLINK);
                                xmlWriter.writeNamespace("xsi", XSI);
                            } else {
                                QName element = input.getName();
                                xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
                                for (int i=0; i < input.getAttributeCount(); i++){
                                    if(input.getAttributeLocalName(i).equals("mainagencycode")){
                                        if(input.getAttributeValue(i).equals("VALA")){
                                            xmlWriter.writeAttribute(input.getAttributePrefix(i), input.getAttributeNamespace(i), input.getAttributeLocalName(i), "VaLA");
                                        } else if(input.getAttributeValue(i).equals("ÖLA")){
                                            xmlWriter.writeAttribute(input.getAttributePrefix(i), input.getAttributeNamespace(i), input.getAttributeLocalName(i), "OLA");
                                        }
                                    } else {
                                        xmlWriter.writeAttribute(input.getAttributePrefix(i), input.getAttributeNamespace(i), input.getAttributeLocalName(i), input.getAttributeValue(i));
                                    }
                                }
                            }
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        xmlWriter.writeCharacters(input.getText());
                        break;
                    case XMLEvent.CDATA:
                        xmlWriter.writeCData(input.getText());
                        break;
                    case XMLEvent.END_ELEMENT:
                        xmlWriter.writeEndElement();
                        break;
                }
                if (!input.hasNext())
                    break;

                input.next();
            }
            xmlWriter.close();
            outputStream.close();
        } catch (Exception e){
            log.error("ERROR!", e);
        }
    }

    public static final String APE_EAD = "urn:isbn:1-931666-22-9";
	public static final String XLINK = "http://www.w3.org/1999/xlink";
	public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";
}