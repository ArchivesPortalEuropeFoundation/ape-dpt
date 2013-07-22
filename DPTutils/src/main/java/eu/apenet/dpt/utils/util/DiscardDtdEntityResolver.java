package eu.apenet.dpt.utils.util;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

/**
 * User: Yoann Moranville
 * Date: Feb 2, 2011
 *
 * @author Yoann Moranville
 */
public class DiscardDtdEntityResolver implements EntityResolver {
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId.contains(".dtd")) {
            return new InputSource(new StringReader(""));
        } else {
            return null;
        }
    }
}
