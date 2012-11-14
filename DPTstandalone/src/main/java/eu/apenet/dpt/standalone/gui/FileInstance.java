package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.utils.util.XmlChecker;
import eu.apenet.dpt.utils.util.Xsd_enum;

import java.io.File;

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
    private String validationErrors;
    private String conversionErrors;
    private String currentLocation;
    private String conversionScriptName;
    private Xsd_enum validationSchema;
    private FileType fileType;
    private Operation lastOperation;
    private boolean isXml;

    public FileInstance(File file, boolean checkXml) {
        if(checkXml && !isXml) //not sure about this !isXml
            this.isXml = XmlChecker.isXmlParseable(file) == null;
        this.name = file.getName();
        this.originalPath = file.getPath();
        this.isValid = false;
        this.isConverted = false;
        this.validationErrors = "";
        this.conversionErrors = "";
        this.currentLocation = "";
        this.conversionScriptName = Utilities.getDefaultXsl();
        this.validationSchema = Utilities.getDefaultXsd();
        this.fileType = FileType.EAD;
        this.lastOperation = Operation.NONE;
    }

    public FileInstance(File file) {
        this(file, false);
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
        return validationSchema.getReadableName();
    }

    public Xsd_enum getValidationSchema() {
        return validationSchema;
    }

    public void setValidationSchema(String validationSchema) {
        for (Xsd_enum xsdEnum : Xsd_enum.values()){
            if(xsdEnum.getReadableName().equals(validationSchema)){
                this.validationSchema = xsdEnum;
                break;
            }
        }
    }
    public void setValidationSchema(Xsd_enum validationSchema) {
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

    public enum FileType{
        EAD("apeEAD"),
        EAG("EAG"),
        EAC_CPF("APE_CPF");

        private String filePrefix;
        FileType(String filePrefix){
            this.filePrefix = filePrefix;
        }
        public String getFilePrefix(){
            return filePrefix;
        }
    }

    public enum Operation{
        NONE,
        CONVERT,
        VALIDATE,
        CREATE_TREE,
        SAVE
    }
}
