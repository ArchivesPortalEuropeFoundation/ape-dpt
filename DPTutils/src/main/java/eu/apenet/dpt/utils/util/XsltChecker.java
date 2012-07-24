package eu.apenet.dpt.utils.util;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XsltCompiler;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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

}
