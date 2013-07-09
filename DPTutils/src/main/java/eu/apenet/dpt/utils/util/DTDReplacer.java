package eu.apenet.dpt.utils.util;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;

/**
 * User: Yoann Moranville
 * Date: 09/07/2013
 *
 * @author Yoann Moranville
 */
public class DTDReplacer implements EntityResolver {

    private String systemId = null;
    private URL substitute = null;

    public DTDReplacer(String systemId, URL substitute) {
        this.systemId = systemId;
        this.substitute = substitute;
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if(this.systemId.equals(systemId)) {
            return new InputSource(substitute.openStream());
        }
        return null;
    }
}