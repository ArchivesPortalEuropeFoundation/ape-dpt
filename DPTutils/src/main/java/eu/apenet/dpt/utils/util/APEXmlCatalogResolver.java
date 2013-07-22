package eu.apenet.dpt.utils.util;

import org.apache.log4j.Logger;
import org.apache.xerces.util.XMLCatalogResolver;
import java.io.File;
import java.net.URL;

/**
 * User: Yoann Moranville
 * Date: 29/03/2012
 *
 * @author Yoann Moranville
 */
public class APEXmlCatalogResolver extends XMLCatalogResolver {
    private static final Logger LOG = Logger.getLogger(APEXmlCatalogResolver.class);
    public APEXmlCatalogResolver() {
        super();
        URL xmlCatalogURL = APEXmlCatalogResolver.class.getResource("/catalog.xml");
        try {
            String[] catalogs = {xmlCatalogURL.toExternalForm()};
            setCatalogList(catalogs);
            setPreferPublic(true);
        } catch (Exception e) {
            LOG.error("Could not create URI", e);
        }
    }

}
