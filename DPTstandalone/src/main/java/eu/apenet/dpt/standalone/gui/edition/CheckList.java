package eu.apenet.dpt.standalone.gui.edition;

import eu.apenet.dpt.standalone.gui.Utilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Oct 28, 2010
 *
 * @author Yoann Moranville
 */
public class CheckList {
    private List<String> langList = new ArrayList<String>();
    private List<String> levelList = new ArrayList<String>();

    public CheckList(){
        SAXParserFactory factory = SAXParserFactory.newInstance();

        DefaultHandler handler = new DefaultHandler() {
            boolean langcode = false;
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("langcode")) {
                    langcode = true;
                }
            }
            public void characters(char ch[], int start, int length) throws SAXException {
                if (langcode) {
                    langList.add(new String(ch, start, length));
                    langcode = false;
                }
            }
            public void endElement(String uri, String localName, String qName) throws SAXException {
            }
        };

        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(Utilities.LANGUAGES_XML_FILE_PATH), handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createLevelList();
    }

    private void createLevelList(){
        levelList.add("fonds");
        levelList.add("series");
        levelList.add("subseries");
        levelList.add("file");
        levelList.add("item");
    }

    public List<String> getLangList(){
        return langList;
    }

    public List<String> getLevelList(){
        return levelList;
    }
}
