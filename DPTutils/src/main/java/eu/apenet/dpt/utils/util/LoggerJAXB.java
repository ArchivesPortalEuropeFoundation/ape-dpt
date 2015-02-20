package eu.apenet.dpt.utils.util;

import eu.apenet.dpt.utils.eaccpf.MultipleIdentities;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPathFactory;
import java.security.CodeSource;
import java.text.MessageFormat;

public class LoggerJAXB {
    private static Logger logger = Logger.getLogger(LoggerJAXB.class);

    public static void outputJaxpImplementationInfo() {
        logger.info("Java version: " + System.getProperty("java.version"));
        logger.info(getImplementationInfo("DocumentBuilderFactory", DocumentBuilderFactory.newInstance().getClass()));
        logger.info(getImplementationInfo("XPathFactory", XPathFactory.newInstance().getClass()));
        logger.info(getImplementationInfo("TransformerFactory", TransformerFactory.newInstance().getClass()));
        logger.info(getImplementationInfo("SAXParserFactory", SAXParserFactory.newInstance().getClass()));
        try {
            logger.info(getImplementationInfo("JAXBContext", JAXBContext.newInstance(MultipleIdentities.class).getClass()));
        } catch (JAXBException e) {
            logger.error("Could not get information about the JAXBContext");
        }
    }

    /**
     * Get the JAXB implementation information for a particular class
     * @param componentName
     * @param componentClass
     * @return
     */
    private static String getImplementationInfo(String componentName, Class componentClass) {
        CodeSource source = componentClass.getProtectionDomain().getCodeSource();
        return MessageFormat.format(
                "{0} implementation: {1} loaded from: {2}",
                componentName,
                componentClass.getName(),
                source == null ? "Java Runtime" : source.getLocation());
    }
}
