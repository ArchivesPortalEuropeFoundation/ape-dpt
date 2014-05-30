package eu.apenet.dpt.utils.ead2edm;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author bverhoef
 */
public class EAD2EDMNamespaceContext implements NamespaceContext {

    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Null prefix");
        }else if ("europeana".equals(prefix)) {
            return "http://www.europeana.eu/schemas/ese/";
        }else if ("dc".equals(prefix)) {
            return "http://purl.org/dc/elements/1.1/";
        }
        return null;
    }

    // This method isn't necessary for XPath processing.
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    // This method isn't necessary for XPath processing either.
    public Iterator<?> getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }


}
