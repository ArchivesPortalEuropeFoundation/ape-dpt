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
    private String languageMaterial;
    private String languageDescription;
    private File htmlSourcesDir;
    private boolean inheritUnittitle;
    private String rights;
    private String rightsAdditionalInformation;
    private String dataProvider;
    private boolean useExistingDaoRole;
    private boolean useExistingLanguageMaterial;
    private boolean useExistingLanguageDescription;
    private boolean languageDescriptionSameAsLanguageMaterial;
    private boolean useExistingRepository;
    private boolean useExistingRightsInfo;
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

    public boolean isInheritUnittitle() {
        return inheritUnittitle;
    }

    public void setInheritUnittitle(boolean inheritUnittitle) {
        this.inheritUnittitle = inheritUnittitle;
    }

    public String getLanguageMaterial() {
        return languageMaterial;
    }

    public void setLanguageMaterial(String languageMaterial) {
        this.languageMaterial = languageMaterial;
    }

    public String getLanguageDescription() {
        return languageDescription;
    }

    public void setLanguageDescription(String languageDescription) {
        this.languageDescription = languageDescription;
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

    public boolean isUseExistingLanguageMaterial() {
        return useExistingLanguageMaterial;
    }

    public void setUseExistingLanguageMaterial(boolean useExistingLanguageMaterial) {
        this.useExistingLanguageMaterial = useExistingLanguageMaterial;
    }

    public boolean isUseExistingLanguageDescription() {
        return useExistingLanguageDescription;
    }

    public void setUseExistingLanguageDescription(boolean useExistingLanguageDescription) {
        this.useExistingLanguageDescription = useExistingLanguageDescription;
    }

    public boolean isLanguageDescriptionSameAsLanguageMaterial() {
        return languageDescriptionSameAsLanguageMaterial;
    }

    public void setLanguageDescriptionSameAsLanguageMaterial(boolean languageDescriptionSameAsLanguageMaterial) {
        this.languageDescriptionSameAsLanguageMaterial = languageDescriptionSameAsLanguageMaterial;
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
            transformerXML2XML = new XMLTransformer("/ead2edm/ead2edmV2.xsl", getProperties());
        }
        return transformerXML2XML;
    }

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
        properties.put("languageMaterial", getString(getLanguageMaterial()));
        properties.put("languageDescription", getString(getLanguageDescription()));
        properties.put("inheritUnittitle", getString(new Boolean(isInheritUnittitle()).toString()));
        properties.put("useExistingDaoRole", getString(new Boolean(isUseExistingDaoRole()).toString()));
        properties.put("useExistingLanguageMaterial", getString(new Boolean(isUseExistingLanguageMaterial()).toString()));
        properties.put("useExistingLanguageDescription", getString(new Boolean(isUseExistingLanguageDescription()).toString()));
        properties.put("languageDescriptionSameAsLanguageMaterial", getString(new Boolean(isLanguageDescriptionSameAsLanguageMaterial()).toString()));
        properties.put("useExistingRepository", getString(new Boolean(isUseExistingRepository()).toString()));
        properties.put("useExistingRightsInfo", getString(new Boolean(isUseExistingRightsInfo()).toString()));
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
