package eu.apenet.dpt.utils.service.stax;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;


/**
 * User: Yoann Moranville
 * Date: Oct 17, 2011
 * @author Yoann Moranville
 */
public class CheckXslEadidVariable implements Runnable {

    private File file;
    private boolean abort;
    private boolean eadidDefaultExists;

    public CheckXslEadidVariable(File file){
        this.file = file;
        abort = false;
        eadidDefaultExists = false;
    }

    public boolean isFileWithDefaultEadid(){
        return eadidDefaultExists;
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

            boolean isInsideNodeParam = false;


            while (!abort) {
                switch (input.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        break;
                    case XMLEvent.START_ELEMENT:
                        if(input.getLocalName().equals("param")){
                            for (int i = 0; i < input.getAttributeCount(); i++) {
                                if(input.getAttributeLocalName(i).equals("name") && input.getAttributeValue(i).equals("eadiddefault")){
                                    isInsideNodeParam = true;
                                }
                            }
                            if(isInsideNodeParam){
                                for (int i = 0; i < input.getAttributeCount(); i++) {
                                    if(input.getAttributeLocalName(i).equals("select")){
                                        if(input.getAttributeValue(i) != null){
                                            eadidDefaultExists = true;
                                            abort = true;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                    case XMLEvent.CDATA:
                        break;
                    case XMLEvent.END_ELEMENT:
                        if(input.getLocalName().equals("template"))
                            abort = true;
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
