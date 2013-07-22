package eu.apenet.dpt.utils.eag2012.namespace;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * User: Yoann Moranville
 * Date: 26/09/2012
 *
 * @author Yoann Moranville
 */

public class EagNamespaceMapper extends NamespacePrefixMapper {

    private static final String PREFIX = "";
    private static final String XSI_PREFIX = "xsi";
    public static final String EAG_URI = "http://www.archivesportaleurope.net/Portal/profiles/eag_2012/";
    public static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(EAG_URI.equals(namespaceUri)) {
            return PREFIX;
        } else if(XSI_URI.equals(namespaceUri)) {
            return XSI_PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] {EAG_URI, XSI_URI};
    }

}