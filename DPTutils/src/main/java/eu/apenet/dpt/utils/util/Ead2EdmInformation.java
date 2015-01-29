package eu.apenet.dpt.utils.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * User: Yoann Moranville Date: 15/04/2013
 *
 * @author Yoann Moranville
 */
public class Ead2EdmInformation {

    private static final Logger LOG = Logger.getLogger(Ead2EdmInformation.class);
    private String languageCode;
    private String repository;
    private String roleType;
    private StringBuilder archdescValue = new StringBuilder();
    private String archdescRepository;
    private TreeSet<String> languagesCodes;	// List of languages exist inside "<langusage>" element.
    private TreeSet<String> alternativeLanguages;	// List of languages exist inside "<archdesc><langmaterial>" element.
    private boolean languagesOnParent;	// Check the existence of languages in "<archdesc>" element.
    private boolean languagesOnAllCLevels;	// Check the existence of languages in all "<c>" element.
    private String userestrictDaoLicenceType;
    private TreeSet<String> userestrictDaoLicenceText;

    public String getLanguageCode() {
        return this.languageCode;
    }

    public String getRepository() {
        return this.repository;
    }

    public String getRoleType() {
        return this.roleType;
    }

    public String getArchdescRepository() {
        return this.archdescRepository;
    }

    public TreeSet<String> getLanguagesCodes() {
        return this.languagesCodes;
    }

    public TreeSet<String> getAlternativeLanguages() {
        return this.alternativeLanguages;
    }

    public boolean isLanguagesOnParent() {
        return this.languagesOnParent;
    }

    public boolean isLanguagesOnAllCLevels() {
        return this.languagesOnAllCLevels;
    }

    public String getUserestrictDaoLicenceType() {
        return userestrictDaoLicenceType;
    }

    public TreeSet<String> getUserestrictDaoLicenceText() {
        return this.userestrictDaoLicenceText;
    }

    public Ead2EdmInformation(File fileToRead, String databaseRoleType, String archdescRepository) throws IOException, SAXException, ParserConfigurationException {
        this();
        this.archdescRepository = archdescRepository;
        this.languagesCodes = new TreeSet<String>();
        this.alternativeLanguages = new TreeSet<String>();
        this.languagesOnParent = false;
        this.languagesOnAllCLevels = true;
        this.languageCode = "";
        this.repository = "";
        this.roleType = databaseRoleType;
        this.userestrictDaoLicenceType = "";
        this.userestrictDaoLicenceText = new TreeSet<String>();
        this.determineDaoInformation(fileToRead);
    }

    public Ead2EdmInformation() {
        this.languageCode = "";
        this.repository = "";
        this.roleType = "";
        this.archdescRepository = "";
        this.languagesCodes = new TreeSet<String>();
        this.alternativeLanguages = new TreeSet<String>();
        this.languagesOnParent = false;
        this.languagesOnAllCLevels = true;
        this.userestrictDaoLicenceType = "";
        this.userestrictDaoLicenceText = new TreeSet<String>();
    }

    private void determineDaoInformation(File fileToRead) throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        EadContentHandler myContentHandler = new EadContentHandler();
        xr.setContentHandler(myContentHandler);
        xr.parse(new InputSource(new InputStreamReader(new BOMInputStream(new FileInputStream(fileToRead)))));

        if (this.roleType == null) {
            this.roleType = "UNSPECIFIED";
        }
    }

    private class EadContentHandler extends DefaultHandler {

        private static final String CREATIVECOMMONS_CPDM = "cpdm";
        private static final String CREATIVECOMMONS_CC0 = "cc0";
        private static final String CREATIVECOMMONS = "creativecommons";
        private static final String EUROPEANA = "europeana";
        private static final String OUT_OF_COPYRIGHT = "outofcopyright";
        private static final String CREATIVECOMMONS_CPDM_TEXT = "Creative Commons Public Domain Mark";
        private static final String CREATIVECOMMONS_CC0_TEXT = "Creative Commons CC0";
        private static final String CREATIVECOMMONS_TEXT = "Creative Commons";
        private static final String EUROPEANA_TEXT = "Europeana rights statements";
        private static final String OUT_OF_COPYRIGHT_TEXT = "Out of copyright - non commercial re-use";

        private boolean inLangusage = false;
        private boolean inArchdesc = false;
        private boolean inDid = false;
        private boolean inRepository = false;
        private boolean inUserestrictDao = false;
        private boolean inP = false;
        private boolean inExtref = false;
        private boolean hasDao = false;
        //flags for first occuring type and DAO in XML
        private boolean typeRetrieved = false;
        private boolean daoRetrieved = false;
        private boolean metsLicenceRetrieved = false;
        private boolean languageOnCLevel;

        @Override
        public void startElement(String namespaceURI, String localName,
                String qName, Attributes atts) throws SAXException {

            if (qName.equals("langusage")) {
                this.inLangusage = true;
            } else if (qName.equals("archdesc")) {
                this.inArchdesc = true;
            } else if (qName.equals("did")) {
                this.inDid = true;
            } else if (qName.equals("dao")) {
                this.hasDao = true;
                if (this.typeRetrieved == false
                        && atts.getValue("xlink:role") != null) {
                    roleType = atts.getValue("xlink:role");
                    this.typeRetrieved = true;
                }
                languageOnCLevel = false;
            } else if (qName.equals("repository")) {
                this.inRepository = true;
            } else if (qName.equals("language")) {
                // Check if the language is in element "<langusage>".
                if (this.inLangusage
                        && atts.getValue("langcode") != null) {
                    languagesCodes.add(atts.getValue("langcode"));
                } else if (this.inArchdesc && this.inDid && !this.hasDao
                        && atts.getValue("langcode") != null) {
                    // Check if the language is in element "<archdesc><langmaterial>".
                    languagesOnParent = true;
                    alternativeLanguages.add(atts.getValue("langcode"));
                } else if (this.inArchdesc && this.inDid && this.hasDao
                        && atts.getValue("langcode") != null) {
                    // Check if the language is in element "<c>".
                    if ("".equals(languageCode)) {
                        languageCode = atts.getValue("langcode");
                    }
                    this.languageOnCLevel = true;
                }
            } else if (qName.equals("userestrict")) {
                if (atts.getValue("type") != null) {
                    if ("dao".equals(atts.getValue("type"))) {
                        this.inUserestrictDao = true;
                    }
                }
            } else if (qName.equals("p")) {
                this.inP = true;
            } else if (qName.equals("extref")) {
                this.inExtref = true;
                if (this.metsLicenceRetrieved == false && this.inUserestrictDao) {
                    String title = atts.getValue("xlink:title");
                    if (CREATIVECOMMONS_TEXT.equals(title)) {
                        userestrictDaoLicenceType = CREATIVECOMMONS;
                    } else if (CREATIVECOMMONS_CC0_TEXT.equals(title)) {
                        userestrictDaoLicenceType = CREATIVECOMMONS_CC0;
                    } else if (CREATIVECOMMONS_CPDM_TEXT.equals(title)) {
                        userestrictDaoLicenceType = CREATIVECOMMONS_CPDM;
                    } else if (EUROPEANA_TEXT.equals(title)) {
                        userestrictDaoLicenceType = EUROPEANA;
                    } else if (OUT_OF_COPYRIGHT_TEXT.equals(title)) {
                        userestrictDaoLicenceType = OUT_OF_COPYRIGHT;
                    } else {
                        userestrictDaoLicenceType = "";
                    }
                }
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName)
                throws SAXException {
            if (qName.equals("langusage")) {
                this.inLangusage = false;
            } else if (qName.equals("archdesc")) {
                this.inArchdesc = false;
            } else if (qName.equals("did")) {
                this.inDid = false;

                // Set the value of no language present in all C Levels if necessary.
                if (!this.languageOnCLevel
                        && this.hasDao) {
                    languagesOnAllCLevels = false;
                    LOG.debug("No language present in c level.");
                }

                this.hasDao = false;
            } else if (qName.equals("dao")) {
                // Nothing to do here
            } else if (qName.equals("repository")) {
                this.inRepository = false;
            } else if (qName.equals("language")) {
                // Nothing to do here
            } else if (qName.equals("userestrict")) {
                if (this.inUserestrictDao) {
                    this.metsLicenceRetrieved = true;
                }
                this.inUserestrictDao = false;
            } else if (qName.equals("p")) {
                this.inP = false;
            } else if (qName.equals("extref")) {
                this.inExtref = false;
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
                    if (this.daoRetrieved == false) {
                        repository = textBetween.substring(0, index);
                        this.daoRetrieved = true;
                    }
                }
                if (this.inArchdesc) {
                    //if (archdescRepository == null || archdescRepository.equals("")) {
                    //    archdescRepository = textBetween.substring(0, index);
                    //}
                    archdescValue.append(textBetween.substring(0, index));
                    archdescRepository = archdescValue.toString();
                }
            }
            if (this.inP) {
                if (this.metsLicenceRetrieved == false && this.inUserestrictDao && this.inExtref == false) {
                    String textBetween = new String(ch, start, length);
                    if (StringUtils.isNotEmpty(textBetween.trim())) {
                        userestrictDaoLicenceText.add(textBetween);
                    }
                }
            }
        }
    }
}