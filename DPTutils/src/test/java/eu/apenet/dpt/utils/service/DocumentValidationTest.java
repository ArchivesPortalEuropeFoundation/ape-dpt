package eu.apenet.dpt.utils.service;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URL;

/**
 * User: Yoann Moranville
 * Date: 09/07/2013
 *
 * @author Yoann Moranville
 */
public class DocumentValidationTest {
    private static final Logger LOG = Logger.getLogger(DocumentValidationTest.class);

    @BeforeClass
    public static void init() {
        BasicConfigurator.configure();
        LOG.info("Init of log4j!");
    }

//    @Test
    public void xmlValidationAgainstDtd() {
        String filePath = "./DPTutils/src/test/resources/UAR_PTH_Studentenkartei.xml";
        try {
            DocumentValidation.xmlValidationAgainstDtd(filePath, new URL(""));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}
