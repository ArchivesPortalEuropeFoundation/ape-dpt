package eu.apenet.dpt.standalone.gui.eag2012;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * User: Yoann Moranville
 * Date: 26/09/2012
 *
 * @author Yoann Moranville
 */

public class EagNamespaceMapper extends NamespacePrefixMapper {

    private static final String PREFIX = "";
    public static final String EAG_URI = "http://www.archivesportaleurope.net/Portal/profiles/eag_2012/";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(EAG_URI.equals(namespaceUri)) {
            return PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] {EAG_URI};
    }

}