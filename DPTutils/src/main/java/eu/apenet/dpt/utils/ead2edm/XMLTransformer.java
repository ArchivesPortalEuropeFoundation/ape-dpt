/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.ead2edm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 *
 * @author bverhoef
 */
public class XMLTransformer {
    private static final Logger LOG = Logger.getLogger(XMLTransformer.class);

    private Transformer transformer;
    private Map<String, Object> parameters = new HashMap<String, Object>();
    private String xsltLocation;

    public XMLTransformer(String xsltLocation, Properties properties){
    	parameters = new HashMap<String, Object>();
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        if (parameters != null){
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            	String key = entry.getKey().toString();
            	String value = entry.getValue().toString();
            	if (StringUtils.isBlank(value)){
            		value = null;
            	}
            	parameters.put(key, value);
            }
        }
        this.xsltLocation = xsltLocation;
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public void transform(File inputFile, File outputFile) throws TransformerException {
        reset();
        Source inputSource = new StreamSource(inputFile);
        Result outputSource = new StreamResult(outputFile);
        transformer.transform(inputSource, outputSource);
    }

    public void transformForTest(File inputFile, File outputFile) throws TransformerException {
        resetForTest();
        Source inputSource = new StreamSource(inputFile);
        Result outputSource = new StreamResult(outputFile);
        transformer.transform(inputSource, outputSource);
    }

    public void transform(String relativeFilename, File inputFile, File outputFile) throws TransformerException {
        transform(inputFile, outputFile);
    }

    public void transform(File inputFile, Document outputFile) throws TransformerException {
        reset();
        Source inputSource = new StreamSource(inputFile);
        Result outputSource = new DOMResult(outputFile);
        transformer.transform(inputSource, outputSource);
    }

        public void transform(Document inputFile, File outputFile) throws TransformerException {
        reset();
        Source inputSource = new DOMSource(inputFile);
        Result outputSource = new StreamResult(outputFile);
        transformer.transform(inputSource, outputSource);
    }

    private void reset() throws TransformerConfigurationException {
        Source xsltSource = new StreamSource(getClass().getResourceAsStream(xsltLocation));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        transformer = transformerFactory.newTransformer(xsltSource);
        transformer.reset();
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    private void resetForTest() throws TransformerConfigurationException {
        Source xsltSource = new StreamSource(new File(xsltLocation));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        transformer = transformerFactory.newTransformer(xsltSource);
        transformer.reset();
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }
}
