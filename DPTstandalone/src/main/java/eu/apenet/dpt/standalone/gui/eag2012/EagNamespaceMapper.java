package eu.apenet.dpt.standalone.gui.eag2012;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * User: Yoann Moranville
 * Date: 26/09/2012
 *
 * @author Yoann Moranville
 */
//import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class EagNamespaceMapper extends NamespacePrefixMapper {

    private static final String PREFIX = "";
    private static final String URI = "http://www.archivesportaleurope.eu/profiles/eag/";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(URI.equals(namespaceUri)) {
            return PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] {URI};
    }

}