package eu.apenet.dpt.utils.eaccpf.namespace;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * This class manage the EAC-CPF's name space
 *
 */
public class EacCpfNamespaceMapper extends NamespacePrefixMapper {

    private static final String PREFIX = "";
    private static final String XSI_PREFIX = "xsi";
    private static final String XLINK_PREFIX = "xlink";
    public static final String EAC_CPF_URI = "urn:isbn:1-931666-33-4";
    public static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String XLINK_URI = "http://www.w3.org/1999/xlink";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(EAC_CPF_URI.equals(namespaceUri)) {
            return PREFIX;
        } else if(XSI_URI.equals(namespaceUri)) {
            return XSI_PREFIX;
        } else if(XLINK_URI.equals(namespaceUri)) {
            return XLINK_PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] {EAC_CPF_URI, XSI_URI, XLINK_URI};
    }

}