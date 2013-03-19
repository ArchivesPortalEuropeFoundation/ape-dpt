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
    
    private String oaiPmhLocation;
    private Properties properties;

    private boolean transferToFileOutput;
    
    public EdmConfig(boolean transferToFileOutput) {
        this.transferToFileOutput = transferToFileOutput;
        this.properties = new Properties();
    }

    public XMLTransformer getTransformerXML2XML() {
        if (transformerXML2XML == null) {
            if (transferToFileOutput) {
                transformerXML2XML = new XMLTransformer("/ese2edm/ese2edm_2.xslt", getProperties());
            } else {
                transformerXML2XML = new XMLTransformer("/ese2edm/ese2edm_1.xslt", getProperties());
            }
        }
        return transformerXML2XML;
    }

    public String getOaiPmhLocation() {
        return oaiPmhLocation;
    }

    public void setOaiPmhLocation(String oaiPmhLocation) {
        this.oaiPmhLocation = oaiPmhLocation;
    }

    public boolean isTransferToFileOutput() {
        return transferToFileOutput;
    }

    public void setTransferToFileOutput(boolean transferToFileOutput) {
        this.transferToFileOutput = transferToFileOutput;
        if (transferToFileOutput) {
            transformerXML2XML = new XMLTransformer("/ese2edm/ese2edm_2.xslt", properties);
        } else {
            transformerXML2XML = new XMLTransformer("/ese2edm/ese2edm_1.xslt", properties);
        }
    }

    public Properties getProperties() {
        if (properties == null) {
			properties = new Properties();
			properties.put("oai_pmh_location", getString(getOaiPmhLocation()));
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
