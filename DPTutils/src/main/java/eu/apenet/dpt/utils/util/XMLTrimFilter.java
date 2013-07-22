package eu.apenet.dpt.utils.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.CharArrayWriter;

/**
 * User: Yoann Moranville
 * Date: Sep 24, 2010
 *
 * @author Yoann Moranville
 */
public class XMLTrimFilter extends XMLFilterImpl {

    private CharArrayWriter contents = new CharArrayWriter();

    public XMLTrimFilter(XMLReader parent){
        super(parent);
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{
        writeContents();
        super.startElement(uri, localName, qName, atts);
    }

    public void characters(char ch[], int start, int length){
        contents.write(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException{
        writeContents();
        super.endElement(uri, localName, qName);
    }

    public void ignorableWhitespace(char ch[], int start, int length){}

    private void writeContents() throws SAXException {
        char ch[] = contents.toCharArray();
        if(!isWhiteSpace(ch))
            super.characters(ch, 0, ch.length);
        contents.reset();
    }

    private boolean isWhiteSpace(char ch[]){
        for (char aCh : ch) {
            if (!Character.isWhitespace(aCh))
                return false;
        }
        return true;
    }
}