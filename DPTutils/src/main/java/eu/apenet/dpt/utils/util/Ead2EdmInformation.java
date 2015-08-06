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
    private boolean languagesOnAllCLevels;	// Check the existence of languages in all "<c>" elements.
    private String userestrictDaoLicenceType;
    private String userestrictDaoLicenceLink;
    private TreeSet<String> userestrictDaoLicenceText;
    private boolean licensesOnParent;	// Check the existence of licenses in "<archdesc>" element.
    private boolean licensesOnAllCLevels;	// Check the existence of licenses in all "<c>" elements.
    private String archdescLicenceLink;
    private String archdescLicenceType;
    private String archdescUnittitle;
    private String titlestmtTitleproper;

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

    public String getUserestrictDaoLicenceLink() {
        return userestrictDaoLicenceLink;
    }

    public TreeSet<String> getUserestrictDaoLicenceText() {
        return this.userestrictDaoLicenceText;
    }

    public boolean isLicensesOnParent() {
        return licensesOnParent;
    }

    public boolean isLicensesOnAllCLevels() {
        return licensesOnAllCLevels;
    }

    public String getArchdescLicenceLink() {
        return archdescLicenceLink;
    }

    public String getArchdescLicenceType() {
        return archdescLicenceType;
    }

    public String getArchdescUnittitle() {
        return archdescUnittitle;
    }

    public String getTitlestmtTitleproper() {
        return titlestmtTitleproper;
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
        this.userestrictDaoLicenceLink = "";
        this.userestrictDaoLicenceText = new TreeSet<String>();
        this.licensesOnParent = false;
        this.licensesOnAllCLevels = true;
        this.archdescLicenceLink = "";
        this.archdescLicenceType = "";
        this.archdescUnittitle = "";
        this.titlestmtTitleproper = "";
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
        this.userestrictDaoLicenceLink = "";
        this.userestrictDaoLicenceText = new TreeSet<String>();
        this.licensesOnParent = false;
        this.licensesOnAllCLevels = true;
        this.archdescLicenceLink = "";
        this.archdescLicenceType = "";
        this.archdescUnittitle = "";
        this.titlestmtTitleproper = "";
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
        private static final String CREATIVECOMMONS_CPDM_LINK = "http://creativecommons.org/publicdomain/mark/";
        private static final String CREATIVECOMMONS_CC0_LINK = "http://creativecommons.org/publicdomain/zero/";
        private static final String CREATIVECOMMONS_LINK = "http://creativecommons.org/licenses/";
        private static final String EUROPEANA_LINK = "http://www.europeana.eu/rights/";
        private static final String OUT_OF_COPYRIGHT_LINK = "http://www.europeana.eu/portal/rights/out-of-copyright";

        private boolean inLangusage = false;
        private boolean inArchdesc = false;
        private boolean inC = false;
        private boolean inDid = false;
        private boolean inRepository = false;
        private boolean inUserestrictDao = false;
        private boolean inP = false;
        private boolean inExtref = false;
        private boolean hasDao = false;
        private boolean inUnittitle = false;
        private boolean inFiledesc = false;
        private boolean inTitlestmt = false;
        private boolean inTitleproper = false;

        //flags for first occuring type and DAO in XML
        private boolean typeRetrieved = false;
        private boolean daoRetrieved = false;
        private boolean metsLicenceRetrieved;
        private boolean languageOnCLevel;

        @Override
        public void startElement(String namespaceURI, String localName,
                String qName, Attributes atts) throws SAXException {

            if (qName.equals("langusage")) {
                this.inLangusage = true;
            } else if (qName.equals("archdesc")) {
                this.inArchdesc = true;
            } else if (qName.equals("c")) {
                this.inC = true;
                this.metsLicenceRetrieved = false;
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
                    userestrictDaoLicenceLink = atts.getValue("xlink:href");
                    if (userestrictDaoLicenceLink.startsWith(CREATIVECOMMONS_LINK)) {
                        userestrictDaoLicenceType = CREATIVECOMMONS;
                    } else if (userestrictDaoLicenceLink.startsWith(CREATIVECOMMONS_CC0_LINK)) {
                        userestrictDaoLicenceType = CREATIVECOMMONS_CC0;
                    } else if (userestrictDaoLicenceLink.startsWith(CREATIVECOMMONS_CPDM_LINK)) {
                        userestrictDaoLicenceType = CREATIVECOMMONS_CPDM;
                    } else if (userestrictDaoLicenceLink.startsWith(EUROPEANA_LINK)) {
                        userestrictDaoLicenceType = EUROPEANA;
                    } else if (userestrictDaoLicenceLink.startsWith(OUT_OF_COPYRIGHT_LINK)) {
                        userestrictDaoLicenceType = OUT_OF_COPYRIGHT;
                    } else {
                        userestrictDaoLicenceType = "";
                    }
                    if (this.inArchdesc && !this.inC && userestrictDaoLicenceType != "") {
                        archdescLicenceLink = userestrictDaoLicenceLink;
                        archdescLicenceType = userestrictDaoLicenceType;
                        licensesOnParent = true;
                    }
                }
            } else if (qName.equals("unittitle")) {
                this.inUnittitle = true;
            } else if (qName.equals("filedesc")) {
                this.inFiledesc = true;
            } else if (qName.equals("titlestmt")) {
                this.inTitlestmt = true;
            } else if (qName.equals("titleproper")) {
                this.inTitleproper = true;
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName)
                throws SAXException {
            if (qName.equals("langusage")) {
                this.inLangusage = false;
            } else if (qName.equals("archdesc")) {
                this.inArchdesc = false;
            } else if (qName.equals("c")) {
                this.inC = false;
                if (!this.metsLicenceRetrieved){
                    licensesOnAllCLevels = false;
                    LOG.debug("No license present in c level.");
                }
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
            } else if (qName.equals("unittitle")) {
                this.inUnittitle = false;
            } else if (qName.equals("filedesc")) {
                this.inFiledesc = false;
            } else if (qName.equals("titlestmt")) {
                this.inTitlestmt = false;
            } else if (qName.equals("titleproper")) {
                this.inTitleproper = false;
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
                if (this.inArchdesc && !this.inC) {
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
            if (this.inArchdesc && !this.inC && this.inDid && this.inUnittitle) {
                archdescUnittitle = new String(ch, start, length);
            }
            if (this.inTitlestmt && this.inTitleproper) {
                titlestmtTitleproper = new String(ch, start, length);
            }
        }
    }
}
