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
    private XMLTransformer transformerXML2HTML;

    public EdmConfig() {
    }

    public XMLTransformer getTransformerXML2XML() {
        Properties properties = new Properties();
        if (transformerXML2XML == null) {
            transformerXML2XML = new XMLTransformer("/ese2edm/ese2edm.xslt", properties);
        }
        return transformerXML2XML;
    }
}
