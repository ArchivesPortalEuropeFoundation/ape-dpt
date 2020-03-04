package eu.apenet.dpt.utils.ead2edm;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

public class EdmConfig implements Serializable {

    private static final long serialVersionUID = -3232731426711003838L;
    private XMLTransformer transformerXML2XML;
    private XMLTransformer transformerXML2HTML;
    private boolean validateLinks;
    private boolean removeInvalidLinks;
    private String provider;
    private String type;
    private String idSource;
    private boolean generateHtml;
    private String sourceFilename;
    private String language;
    private File htmlSourcesDir;
//    private boolean inheritElementsFromFileLevel;
//    private boolean inheritOrigination;
    private boolean inheritUnittitle;
//    private boolean inheritLanguage;
//    private boolean inheritRightsInfo;
    private String rights;
    private String rightsAdditionalInformation;
    private String dataProvider;
    private boolean useExistingDaoRole;
    private boolean useExistingLanguage;
    private boolean useExistingRepository;
    private boolean useExistingRightsInfo;
//    private boolean minimalConversion;
    private String edmIdentifier;
    private String host;
    private String repositoryCode;
    private String xmlTypeName;
    private String landingPage;
    private boolean useArchUnittitle;
    private String outputBaseDirectory;
    private Properties properties;

    public EdmConfig() {
    }

    public EdmConfig(Properties properties) {
        this.properties = properties;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isGenerateHtml() {
        return generateHtml;
    }

    /**
     * @return the idSource
     */
    public String getIdSource() {
        return idSource;
    }

    /**
     * @param idSource the idSource to set
     */
    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }

    public void setGenerateHtml(boolean generateHtml) {
        this.generateHtml = generateHtml;
    }

    public void setValidateLinks(boolean validateLinks) {
        this.validateLinks = validateLinks;
    }

//    public boolean isInheritElementsFromFileLevel() {
//        return inheritElementsFromFileLevel;
//    }
//
//    public void setInheritElementsFromFileLevel(boolean inheritElementsFromFileLevel) {
//        this.inheritElementsFromFileLevel = inheritElementsFromFileLevel;
//    }
//
//    public boolean isInheritOrigination() {
//        return inheritOrigination;
//    }
//
//    public void setInheritOrigination(boolean inheritOrigination) {
//        this.inheritOrigination = inheritOrigination;
//    }
//
//    public boolean isInheritLanguage() {
//        return inheritLanguage;
//    }
//
//    public void setInheritLanguage(boolean inheritLanguage) {
//        this.inheritLanguage = inheritLanguage;
//    }
//
    public boolean isInheritUnittitle() {
        return inheritUnittitle;
    }

    public void setInheritUnittitle(boolean inheritUnittitle) {
        this.inheritUnittitle = inheritUnittitle;
    }
//
//    public boolean isInheritRightsInfo() {
//        return inheritRightsInfo;
//    }
//
//    public void setInheritRightsInfo(boolean inheritRightsInfo) {
//        this.inheritRightsInfo = inheritRightsInfo;
//    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public File getHtmlSourcesDir() {
        return htmlSourcesDir;
    }

    public void setHtmlSourcesDir(File htmlSourcesDir) {
        this.htmlSourcesDir = htmlSourcesDir;
    }

    public boolean isValidateLinks() {
        return validateLinks;
    }

    public boolean isRemoveInvalidLinks() {
        return removeInvalidLinks;
    }

    public void setRemoveInvalidLinks(boolean removeInvalidLinks) {
        this.removeInvalidLinks = removeInvalidLinks;
    }

    public String getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(String dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String getRightsAdditionalInformation() {
        return rightsAdditionalInformation;
    }

    public void setRightsAdditionalInformation(String rightsAdditionalInformation) {
        this.rightsAdditionalInformation = rightsAdditionalInformation;
    }

    public boolean isUseExistingDaoRole() {
        return useExistingDaoRole;
    }

    public void setUseExistingDaoRole(boolean useExistingDaoRole) {
        this.useExistingDaoRole = useExistingDaoRole;
    }

    public boolean isUseExistingLanguage() {
        return useExistingLanguage;
    }

    public void setUseExistingLanguage(boolean useExistingLanguage) {
        this.useExistingLanguage = useExistingLanguage;
    }

    public boolean isUseExistingRepository() {
        return useExistingRepository;
    }

    public void setUseExistingRepository(boolean useExistingRepository) {
        this.useExistingRepository = useExistingRepository;
    }

    public boolean isUseExistingRightsInfo() {
        return useExistingRightsInfo;
    }

    public void setUseExistingRightsInfo(boolean useExistingRightsInfo) {
        this.useExistingRightsInfo = useExistingRightsInfo;
    }

    public XMLTransformer getTransformerXML2XML() {
        if (transformerXML2XML == null) {
            transformerXML2XML = new XMLTransformer("/ead2edm/ead2edm.xsl", getProperties());
        }
        return transformerXML2XML;
    }

//    /**
//     * @return the minimalConversion
//     */
//    public boolean isMinimalConversion() {
//        return this.minimalConversion;
//    }
//
//    /**
//     * @param minimalConversion the minimalConversion to set
//     */
//    public void setMinimalConversion(boolean minimalConversion) {
//        this.minimalConversion = minimalConversion;
//    }
    public String getEdmIdentifier() {
        return edmIdentifier;
    }

    public void setEdmIdentifier(String edmIdentifier) {
        this.edmIdentifier = edmIdentifier;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getXmlTypeName() {
        return xmlTypeName;
    }

    public void setXmlTypeName(String xmlTypeName) {
        this.xmlTypeName = xmlTypeName;
    }

    public String getLandingPage() {
        if (landingPage == null) {
            landingPage = "ape";
        }
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    public boolean isUseArchUnittitle() {
        return useArchUnittitle;
    }

    public void setUseArchUnittitle(boolean fondsTitleSource) {
        this.useArchUnittitle = fondsTitleSource;
    }

    public String getOutputBaseDirectory() {
        return outputBaseDirectory;
    }

    public void setOutputBaseDirectory(String outputBaseDirectory) {
        this.outputBaseDirectory = outputBaseDirectory;
    }

    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        properties.put("europeana_provider", getString(getProvider()));
        properties.put("europeana_dataprovider", getString(getDataProvider()));
        properties.put("europeana_rights", getString(getRights()));
        properties.put("dc_rights", getString(getRightsAdditionalInformation()));
        properties.put("europeana_type", getString(getType()));
        properties.put("idSource", getString(getIdSource()));
        properties.put("useISODates", getString("false"));
        properties.put("language", getString(getLanguage()));
//            properties.put("inheritElementsFromFileLevel",
//                    getString(new Boolean(isInheritElementsFromFileLevel()).toString()));
//            properties.put("inheritOrigination", getString(new Boolean(isInheritOrigination()).toString()));
        properties.put("inheritUnittitle", getString(new Boolean(isInheritUnittitle()).toString()));
//            properties.put("inheritLanguage", getString(new Boolean(isInheritLanguage()).toString()));
//            properties.put("inheritRightsInfo", getString(new Boolean(isInheritRightsInfo()).toString()));
        properties.put("useExistingDaoRole", getString(new Boolean(isUseExistingDaoRole()).toString()));
        properties.put("useExistingLanguage", getString(new Boolean(isUseExistingLanguage()).toString()));
        properties.put("useExistingRepository", getString(new Boolean(isUseExistingRepository()).toString()));
        properties.put("useExistingRightsInfo", getString(new Boolean(isUseExistingRightsInfo()).toString()));
//            properties.put("minimalConversion", getString(new Boolean(isMinimalConversion()).toString()));
        properties.put("edm_identifier", getString(getEdmIdentifier()));
        properties.put("host", getString(getHost()));
        String repCodeAfterReplacement = getString(getRepositoryCode()).replace('/', '_');
        properties.put("repository_code", getString(repCodeAfterReplacement));
        properties.put("xml_type_name", getString(getXmlTypeName()));
        properties.put("landingPage", getString(getLandingPage()));
        properties.put("useArchUnittitle", getString(new Boolean(isUseArchUnittitle()).toString()));
        properties.put("outputBaseDirectory", getString(getOutputBaseDirectory()));
        }
        return properties;
    }

    public XMLTransformer getTransformerXML2HTML() {
        if (transformerXML2HTML == null) {
            transformerXML2HTML = new XMLTransformer("/ead2edm/edm2html.xslt", getProperties());
        }
        return transformerXML2HTML;
    }

    private static String getString(String string) {
        if (string == null) {
            return "";
        } else {
            return string;
        }
    }

    public static File getQualityReportXSLTFile() {
        return new File("/ead2edm/edmQuality.xsl");
    }
}
