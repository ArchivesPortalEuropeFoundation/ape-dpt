package eu.apenet.dpt.utils.ead2ese;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

public class EseConfig implements Serializable {

    private static final long serialVersionUID = -3232731426711003838L;
    private XMLTransformer transformerXML2XML;
    private XMLTransformer transformerXML2HTML;
    private boolean validateLinks;
    private boolean removeInvalidLinks;
    private String provider;
    private String type;
    private boolean generateHtml;
    private String sourceFilename;
    private String language;
    private File htmlSourcesDir;
    private boolean inheritElementsFromFileLevel;
    private boolean inheritOrigination;
    private boolean inheritLanguage;
    private boolean inheritCustodhist;
    private boolean inheritAltformavail;
    private boolean inheritControlaccess;
    private String contextInformationPrefix;
    private String rights;
    private String rightsAdditionalInformation;
    private String dataProvider;
    private boolean useExistingDaoRole;
    private boolean useExistingLanguage;
    private boolean useExistingRepository;
    private Properties properties;

    public EseConfig() {
    }

    public EseConfig(Properties properties) {
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

    public void setGenerateHtml(boolean generateHtml) {
        this.generateHtml = generateHtml;
    }

    public void setValidateLinks(boolean validateLinks) {
        this.validateLinks = validateLinks;
    }

    public boolean isInheritElementsFromFileLevel() {
        return inheritElementsFromFileLevel;
    }

    public void setInheritElementsFromFileLevel(boolean inheritElementsFromFileLevel) {
        this.inheritElementsFromFileLevel = inheritElementsFromFileLevel;
    }

    public boolean isInheritOrigination() {
        return inheritOrigination;
    }

    public void setInheritOrigination(boolean inheritOrigination) {
        this.inheritOrigination = inheritOrigination;
    }

    public boolean isInheritLanguage() {
        return inheritLanguage;
    }

    public void setInheritLanguage(boolean inheritLanguage) {
        this.inheritLanguage = inheritLanguage;
    }

    public boolean isInheritCustodhist() {
        return inheritCustodhist;
    }

    public void setInheritCustodhist(boolean inheritCustodhist) {
        this.inheritCustodhist = inheritCustodhist;
    }

    public boolean isInheritAltformavail() {
        return inheritAltformavail;
    }

    public void setInheritAltformavail(boolean inheritAltformavail) {
        this.inheritAltformavail = inheritAltformavail;
    }

    public boolean isInheritControlaccess() {
        return inheritControlaccess;
    }

    public void setInheritControlaccess(boolean inheritControlaccess) {
        this.inheritControlaccess = inheritControlaccess;
    }

    public String getContextInformationPrefix() {
        if (StringUtils.isNotBlank(contextInformationPrefix)) {
            if (StringUtils.endsWith(contextInformationPrefix, ":")) {
                return contextInformationPrefix + " ";
            } else if (StringUtils.endsWith(contextInformationPrefix, ": ")) {
                return contextInformationPrefix;
            } else {
                return contextInformationPrefix + ": ";
            }
        }
        return contextInformationPrefix;
    }

    public void setContextInformationPrefix(String contextInformationPrefix) {
        this.contextInformationPrefix = contextInformationPrefix;
    }

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

    public boolean isUseExistingRepository() {
        return useExistingRepository;
    }

    public void setUseExistingRepository(boolean useExistingRepository) {
        this.useExistingRepository = useExistingRepository;
    }

    public boolean isUseExistingLanguage() {
        return useExistingLanguage;
    }

    public void setUseExistingLanguage(boolean useExistingLanguage) {
        this.useExistingLanguage = useExistingLanguage;
    }

    public XMLTransformer getTransformerXML2XML() {
        if (transformerXML2XML == null) {
            transformerXML2XML = new XMLTransformer("/ead2ese/ead2ese.xslt", getProperties());
        }
        return transformerXML2XML;
    }

    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.put("europeana_provider", getString(getProvider()));
            properties.put("europeana_dataprovider", getString(getDataProvider()));
            properties.put("europeana_rights", getString(getRights()));
            properties.put("dc_rights", getString(getRightsAdditionalInformation()));
            properties.put("europeana_type", getString(getType()));
            properties.put("useISODates", getString("false"));
            properties.put("language", getString(getLanguage()));
            properties.put("inheritElementsFromFileLevel",
                    getString(new Boolean(isInheritElementsFromFileLevel()).toString()));
            properties.put("inheritOrigination", getString(new Boolean(isInheritOrigination()).toString()));
            properties.put("inheritLanguage", getString(new Boolean(isInheritLanguage()).toString()));
            properties.put("inheritCustodhist", getString(new Boolean(isInheritCustodhist()).toString()));
            properties.put("inheritAltformavailHead", getString(new Boolean(isInheritAltformavail()).toString()));
            properties.put("inheritControlaccess", getString(new Boolean(isInheritControlaccess()).toString()));
            properties.put("contextInformationPrefix", getString(getContextInformationPrefix()));
            properties.put("useExistingDaoRole", getString(new Boolean(isUseExistingDaoRole()).toString()));
            properties.put("useExistingLanguage", getString(new Boolean(isUseExistingLanguage()).toString()));
            properties.put("useExistingRepository", getString(new Boolean(isUseExistingRepository()).toString()));
        }
        return properties;
    }

    public XMLTransformer getTransformerXML2HTML() {
        if (transformerXML2HTML == null) {
            transformerXML2HTML = new XMLTransformer("/ead2ese/ese2html.xslt", null);
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
}
