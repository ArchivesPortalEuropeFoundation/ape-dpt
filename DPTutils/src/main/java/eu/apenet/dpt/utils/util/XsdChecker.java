package eu.apenet.dpt.utils.util;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XsltCompiler;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: Yoann Moranville
 * Date: 06/02/2013
 *
 * @author Yoann Moranville
 */
public class XsdChecker {

    public static boolean isXsdParseable(File file){
        try {
            InputStream xslFile = new FileInputStream(file);
            XsltCompiler compiler = new Processor(false).newXsltCompiler();
            compiler.compile(new StreamSource(xslFile));
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public static boolean isXsdFile(File file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(file));
            return doc.getDocumentElement().getNodeName().equals("xs:schema");
        } catch (Exception ex) {
            return false;
        }
    }

}
