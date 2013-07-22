package eu.apenet.dpt.utils.service;

import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Yoann Moranville
 * Date: Jul 6, 2010
 *
 * @author Yoann Moranville
 */
public class SAXParseAPEnetException extends SAXParseException {
    private static final Logger LOG = Logger.getLogger(SAXParseAPEnetException.class);

    public SAXParseAPEnetException(String message, String publicId, String systemId, int lineNumber, int columnNumber){
        super(message, publicId, systemId, lineNumber, columnNumber);
    }

    public static SAXParseAPEnetException changeException(String newMessage, SAXParseException spe){
        return new SAXParseAPEnetException(SAXErrorAPEnetParser.doParser(newMessage), spe.getPublicId(), spe.getSystemId(), spe.getLineNumber(), spe.getColumnNumber());
    }


    private static String changeMessageUserReadable(String message){
//        message = "cvc-attribute.3: The value 'yes' of attribute 'acronym' on element 'word' is not valid with respect to its type.";
        Pattern pattern = Pattern.compile("'[^']*'");
        LOG.trace(message);
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()){
            LOG.trace("Found");
            return matcher.group(0);
        }
        return message;
    }
}
