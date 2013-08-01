package eu.apenet.dpt.utils.service.stax;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.*;


/**
 * User: Yoann Moranville
 * Date: Jul 5, 2010
 * This class is used in order to check the file before conversion. It verifies the eadid field, and if it is empty, will retrieve the archdesc/did/unitid content.
 * @author Yoann Moranville
 */
public class StaxTransformationTool implements Runnable {

    private File file;
    private boolean abort;
    private boolean eadidExists;
    private String unitid;

    public StaxTransformationTool(File file){
        this.file = file;
        abort = false;
        eadidExists = false;
    }

    public boolean isFileWithEadid(){
        return eadidExists;
    }

    public String getUnitid(){
        return unitid;
    }

    public void run() {
        try {
            XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            xmlif.configureForSpeed();

            XMLStreamReader2 input = xmlif.createXMLStreamReader(file);

            boolean isInsideNodeEadid = false;
            boolean isInsideNodeDid = false;
            boolean isInsideNodeUnitid = false;


            while (!abort) {
                switch (input.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        break;
                    case XMLEvent.START_ELEMENT:
                        if(input.getLocalName().equals("eadid")){
                            isInsideNodeEadid = true;
                        } else if(input.getLocalName().equals("did")){
                            isInsideNodeDid = true;
                        } else if(isInsideNodeDid && input.getLocalName().equals("unitid")){
                            isInsideNodeUnitid = true;
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                    case XMLEvent.CDATA:
                        if(isInsideNodeEadid)
                            eadidExists = true;
                        if(isInsideNodeUnitid)
                            unitid = input.getText();
                        break;
                    case XMLEvent.END_ELEMENT:
                        if(input.getLocalName().equals("eadid")){
                            if(eadidExists)
                                abort = true;
                            else
                                isInsideNodeEadid = false;
                        } else if(input.getLocalName().equals("unitid") && isInsideNodeDid && isInsideNodeUnitid){
                            isInsideNodeDid = false;
                            isInsideNodeUnitid = false;
                            abort = true;
                        } else if(input.getLocalName().equals("did")){
                            abort = true;
                        }
                        break;
                }
                if(input.hasNext())
                    input.next();
                else
                    abort = true;
            }
        } catch (XMLStreamException e){
            throw new RuntimeException(e);
        }
    }
}
