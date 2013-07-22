package eu.apenet.dpt.utils.util;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XsltCompiler;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * User: Yoann Moranville
 * Date: Oct 15, 2010
 *
 * @author Yoann Moranville
 */
public class XsltChecker {

    public static boolean isXsltParseable(File file){
        try {
            InputStream xslFile = new FileInputStream(file);
            XsltCompiler compiler = new Processor(false).newXsltCompiler();
            compiler.compile(new StreamSource(xslFile));
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public static boolean isXsltFile(File file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(file));
            if (doc.getDocumentElement().getNodeName().equals("xsl:stylesheet")) {
                return true;
            } else {
                return false;
            }
        } catch (SAXException ex) {
            System.err.println("File " + file.getName() + " is not a valid XSLT file!");
            return false;
        } catch (IOException ex) {
            return false;
        } catch (ParserConfigurationException ex) {
            return false;
        }
    }

}
