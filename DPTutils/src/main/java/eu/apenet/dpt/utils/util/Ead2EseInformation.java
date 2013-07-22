package eu.apenet.dpt.utils.util;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * User: Yoann Moranville Date: 15/04/2013
 *
 * @author Yoann Moranville
 */
public class Ead2EseInformation {

    private static final Logger LOG = Logger.getLogger(Ead2EseInformation.class);
    private String languageCode;
    private String repository;
    private String roleType;
    private String archdescRepository;
    private TreeSet<String> alternativeLanguages;

    public String getLanguageCode() {
        return languageCode;
    }

    public String getRepository() {
        return repository;
    }

    public String getRoleType() {
        return roleType;
    }

    public String getArchdescRepository() {
        return archdescRepository;
    }

    public TreeSet<String> getAlternativeLanguages() {
        return alternativeLanguages;
    }

    public Ead2EseInformation(File fileToRead, String databaseRoleType, String archdescRepository) throws IOException, SAXException, ParserConfigurationException {
        this();
        this.archdescRepository = archdescRepository;
        this.alternativeLanguages = new TreeSet<String>();
        this.languageCode = "";
        this.repository = "";
        this.roleType = databaseRoleType;
        this.determineDaoInformation(fileToRead);
    }

    public Ead2EseInformation() {
        this.languageCode = "";
        this.repository = "";
        this.roleType = "";
        this.archdescRepository = "";
        this.alternativeLanguages = new TreeSet<String>();
    }

    private void determineDaoInformation(File fileToRead) throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        EadContentHandler myContentHandler = new EadContentHandler();
        xr.setContentHandler(myContentHandler);
        xr.parse(new InputSource(new InputStreamReader(new FileInputStream(fileToRead), "UTF-8")));

        if (roleType == null) {
            roleType = "UNSPECIFIED";
        }
    }

    private class EadContentHandler extends DefaultHandler {

        private boolean inArchdesc = false;
        private boolean inDid = false;
        private boolean inRepository = false;
        private boolean hasDao = false;
        //flags for first occuring type and DAO in XML
        private boolean typeRetrieved = false;
        private boolean daoRetrieved = false;

        @Override
        public void startElement(String namespaceURI, String localName,
                String qName, Attributes atts) throws SAXException {

            if (qName.equals("archdesc")) {
                this.inArchdesc = true;
            } else if (qName.equals("did")) {
                this.inDid = true;
            } else if (qName.equals("dao")) {
                this.hasDao = true;
                if (typeRetrieved == false) {
                    roleType = atts.getValue("xlink:role");
                    typeRetrieved = true;
                }
            } else if (qName.equals("repository")) {
                this.inRepository = true;
            } else if (qName.equals("language")) {
                if (this.inDid) {
                    if (languageCode.equals("")) {
                        languageCode = atts.getValue("langcode");
                    }
                } else {
                    alternativeLanguages.add(atts.getValue("langcode"));
                }
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName)
                throws SAXException {
            if (qName.equals("archdesc")) {
                this.inArchdesc = false;
            } else if (qName.equals("did")) {
                this.inDid = false;
                this.hasDao = false;
            } else if (qName.equals("dao")) {
                // Nothing to do here
            } else if (qName.equals("repository")) {
                this.inRepository = false;
            } else if (qName.equals("language")) {
                // Nothing to do here
            }
        }

        @Override
        public void characters(char ch[], int start, int length) {
            if (this.inRepository) {
                int index = 0;
                String textBetween = new String(ch, start, length);
                while (index < textBetween.length()) {
                    if (textBetween.charAt(index) >= ' ') {
                        index++;
                    } else {
                        break;
                    }
                }
                if (this.hasDao) {
                    if (daoRetrieved == false) {
                        repository = textBetween.substring(0, index);
                        daoRetrieved = true;
                    }
                }
                if (this.inArchdesc) {
                    if (archdescRepository == null || archdescRepository.equals("")) {
                        archdescRepository = textBetween.substring(0, index);
                    }
                }
            }
        }
    }
}
