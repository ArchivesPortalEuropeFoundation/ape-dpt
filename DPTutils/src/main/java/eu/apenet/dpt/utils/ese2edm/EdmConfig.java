/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.ese2edm;

import eu.apenet.dpt.utils.ead2ese.XMLTransformer;
import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author papp
 */
public class EdmConfig implements Serializable {

    private static final long serialVersionUID = -3232731426711003838L;
    private XMLTransformer transformerXML2XML;
    
    private String edmIdentifier;
    private String prefixUrl;
    private Properties properties;

    private boolean transferToFileOutput;
    
    public EdmConfig(boolean transferToFileOutput) {
        this.transferToFileOutput = transferToFileOutput;
        this.properties = new Properties();
    }

    public XMLTransformer getTransformerXML2XML() {
        if (transformerXML2XML == null) {
            transformerXML2XML = new XMLTransformer("/ese2edm/ese2edm_1.xslt", getProperties());
        }
        return transformerXML2XML;
    }

    public String getEdmIdentifier() {
        return edmIdentifier;
    }

    public void setEdmIdentifier(String edmIdentifier) {
        this.edmIdentifier = edmIdentifier;
    }

    public String getPrefixUrl() {
        return prefixUrl;
    }

    public void setPrefixUrl(String prefixUrl) {
        this.prefixUrl = prefixUrl;
    }

    public boolean isTransferToFileOutput() {
        return transferToFileOutput;
    }

    public void setTransferToFileOutput(boolean transferToFileOutput) {
        this.transferToFileOutput = transferToFileOutput;
            transformerXML2XML = new XMLTransformer("/ese2edm/ese2edm_1.xslt", getProperties());
    }

    public Properties getProperties() {
        if (properties == null) {
			properties = new Properties();
			properties.put("oai_pmh_location", getString(getEdmIdentifier()));
        }
        return properties;
    }
    
    private static String getString(String string) {
        if (string == null) {
            return "";
        } else {
            return string;
        }
    }
}
