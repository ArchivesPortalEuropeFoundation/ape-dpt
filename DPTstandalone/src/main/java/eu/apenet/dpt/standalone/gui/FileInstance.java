package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.xsdAddition.XsdObject;
import eu.apenet.dpt.utils.util.XmlChecker;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: Jun 18, 2010
 *
 * @author Yoann Moranville
 */
public class FileInstance {
    private String name;
    private String originalPath;
    private boolean isValid;
    private boolean isConverted;
    private boolean isEse;
    private String validationErrors;
    private String conversionErrors;
    private String currentLocation;
    private String eseLocation;
    private String conversionScriptName;
    private XsdObject validationSchema;
    private FileType fileType;
    private Operation lastOperation;
    private boolean isXml;
    Map<String, Map<String, Boolean>> xmlQualityErrors;

    public FileInstance(File file, boolean checkXml, String defaultXsl, String defaultXsd) {
        if(checkXml && !isXml)
            this.isXml = XmlChecker.isXmlParseable(file) == null;
        this.name = file.getName();
        this.originalPath = file.getPath();
        this.isValid = false;
        this.isConverted = false;
        this.isEse = false;
        this.validationErrors = "";
        this.conversionErrors = "";
        this.currentLocation = "";
        this.eseLocation = "";
        this.conversionScriptName = defaultXsl;
        this.validationSchema = Utilities.getXsdObjectFromName(defaultXsd);
        this.fileType = FileType.EAD;
        this.lastOperation = Operation.NONE;
    }

//    public FileInstance(File file) {
//        this(file, false);
//    }

    public FileInstance(File file, String defaultXsl, String defaultXsd) {
        this(file, false, defaultXsl, defaultXsd);
    }

    public String getName() {
        return name;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isConverted() {
        return isConverted;
    }

    public void setIsConverted() {
        isConverted = true;
    }

    public boolean isEse() {
        return isEse;
    }

    public void setIsEse() {
        isEse = true;
    }

    public String getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(String validationErrors) {
        this.validationErrors = validationErrors;
    }

    public String getConversionErrors() {
        return conversionErrors;
    }

    public void setConversionErrors(String conversionErrors) {
        this.conversionErrors = conversionErrors;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getConversionScriptName() {
        return conversionScriptName;
    }

    public String getConversionScriptPath() {
        if(Utilities.isSystemFile(conversionScriptName))
            return Utilities.SYSTEM_DIR + conversionScriptName;
        return Utilities.CONFIG_DIR + conversionScriptName;
    }

    public void setConversionScriptName(String conversionScriptName) {
        this.conversionScriptName = conversionScriptName;
    }

    public String getValidationSchemaName() {
        return validationSchema.getName();
    }

    public XsdObject getValidationSchema() {
        return validationSchema;
    }

    public void setValidationSchema(String validationSchema) {
        for(XsdObject xsd : Utilities.getXsdList()) {
            if(xsd.getName().equals(validationSchema)) {
                this.validationSchema = xsd;
            }
        }
    }
    public void setValidationSchema(XsdObject validationSchema) {
        this.validationSchema = validationSchema;
    }

    public void setFileType(FileType fileType){
        this.fileType = fileType;
    }

    public FileType getFileType(){
        return fileType;
    }

    public void setLastOperation(Operation operation){
        this.lastOperation = operation;
    }

    public Operation getLastOperation(){
        return lastOperation;
    }

    public boolean isXml() {
        return isXml;
    }

    public void setXml(boolean xml) {
        isXml = xml;
    }

    public String getEseLocation() {
        return eseLocation;
    }

    public void setEseLocation(String eseLocation) {
        this.eseLocation = eseLocation;
    }

    public Map<String, Map<String, Boolean>> getXmlQualityErrors() {
        return xmlQualityErrors;
    }

    public void setXmlQualityErrors(Map<String, Map<String, Boolean>> xmlQualityErrors) {
        this.xmlQualityErrors = xmlQualityErrors;
    }

    public enum FileType{
        EAD("apeEAD"),
        EAG("EAG"),
        EAC_CPF("APE_CPF"),
        ESE("apeESE"),
        OTHER("Other");

        private String filePrefix;
        FileType(String filePrefix){
            this.filePrefix = filePrefix;
        }
        public String getFilePrefix(){
            return filePrefix;
        }

        public static FileType getCorrectFileType(String filetypeStr) {
            for(FileType type : FileType.values()) {
                if(type.getFilePrefix().equals(filetypeStr))
                    return type;
            }
            return null;
        }
    }

    public enum Operation{
        NONE,
        CONVERT,
        VALIDATE,
        CREATE_TREE,
        SAVE,
        CONVERT_ESE,
        CONVERT_EDM
    }
}
