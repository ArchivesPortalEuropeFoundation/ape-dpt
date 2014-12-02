package eu.apenet.dpt.utils.util;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * User: Yoann Moranville
 * Date: Sep 24, 2010
 *
 * @author Yoann Moranville
 */
public class DOMUtil {
    public static Document createDocument(InputStream is) throws Exception{
        System.setProperty("javax.xml.transform.TransformerFactory","net.sf.saxon.TransformerFactoryImpl");
        DocumentBuilder builder = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new DiscardDtdEntityResolver());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder!=null?builder.parse(new InputSource(is)):null;
    }
}