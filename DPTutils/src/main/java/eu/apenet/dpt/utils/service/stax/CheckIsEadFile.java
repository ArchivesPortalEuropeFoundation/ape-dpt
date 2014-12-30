package eu.apenet.dpt.utils.service.stax;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;

/**
 * Created by yoannmoranville on 22/12/14.
 */
public class CheckIsEadFile {
    private File file;
    private boolean abort;
    private boolean isEadRoot;

    public CheckIsEadFile(File file){
        this.file = file;
        abort = false;
        isEadRoot = false;
    }

    public boolean isEadRoot(){
        return isEadRoot;
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
            int counter = 0;

            while (!abort) {
                switch (input.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        break;
                    case XMLEvent.START_ELEMENT:
                        counter++;
                        if(input.getLocalName().equals("ead")){
                            isEadRoot = true;
                            abort = true;
                        }
                        if(counter > 9) {
                            abort = true;
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                    case XMLEvent.CDATA:
                    case XMLEvent.END_ELEMENT:
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
