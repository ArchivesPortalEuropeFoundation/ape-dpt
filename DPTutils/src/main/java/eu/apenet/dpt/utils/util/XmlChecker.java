package eu.apenet.dpt.utils.util;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;

import java.io.*;

/**
 * User: Yoann Moranville
 * Date: Jan 31, 2011
 *
 * @author Yoann Moranville
 */
public class XmlChecker {

    public static String isXmlParseable(File file){
        try {
            InputStream xmlFile = new FileInputStream(file);
            InputSource source = new InputSource();
            source.setByteStream(xmlFile);
            SAXParser saxParser = new SAXParser();
            saxParser.setEntityResolver(new DiscardDtdEntityResolver());
            saxParser.parse(source);
            return null;
        } catch(Exception e){
            return e.getMessage();
        }
    }
}
