package eu.apenet.dpt.standalone.gui;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import eu.apenet.dpt.standalone.gui.xsdaddition.XsdObject;
import eu.apenet.dpt.utils.util.XmlChecker;

import java.io.File;
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
    private boolean isEdm;
    private String validationErrors;
    private String conversionErrors;
    private String europeanaConversionErrors;
    private String currentLocation;
    private String edmLocation;
    private String conversionScriptName;
    private XsdObject validationSchema;
    private FileType fileType;
    private Operation lastOperation;
    private boolean isXml;
    Map<String, Map<String, Boolean>> xmlQualityErrors;
    private boolean isMinimalConverted;

    public FileInstance(File file, boolean checkXml, String defaultXsl, String defaultXsd) {
        if(checkXml && !isXml)
            this.isXml = XmlChecker.isXmlParseable(file) == null;
        this.name = file.getName();
        this.originalPath = file.getPath();
        this.isValid = false;
        this.isConverted = false;
        this.isEdm = false;
        this.validationErrors = "";
        this.conversionErrors = "";
        this.europeanaConversionErrors = "";
        this.currentLocation = file.getPath();
        this.edmLocation = "";
        this.conversionScriptName = defaultXsl;
        this.validationSchema = Utilities.getXsdObjectFromName(defaultXsd);
        this.fileType = FileType.EAD;
        this.lastOperation = Operation.NONE;
        this.isMinimalConverted = true;
    }

    @Override
    public String toString() {
        return name + ":\noriginalPath=" + originalPath + "\nisValid=" + isValid + "\nisConverted=" + isConverted + "\nisEdm=" + isEdm + "\ncurrentLocation=" + currentLocation + "\nvalidationSchema=" + validationSchema.getName() + "\nconversionScript=" + conversionScriptName;
    }

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

    public void setConverted() {
        isConverted = true;
    }

    public boolean isEdm() {
        return isEdm;
    }

    public void setEdm(boolean edm) {
        isEdm = edm;
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

    public String getEuropeanaConversionErrors() {
        return europeanaConversionErrors;
    }

    public void setEuropeanaConversionErrors(String europeanaConversionErrors) {
        this.europeanaConversionErrors = europeanaConversionErrors;
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

    public String getEdmLocation() {
        return edmLocation;
    }

    public void setEdmLocation(String edmLocation) {
        this.edmLocation = edmLocation;
    }

    public Map<String, Map<String, Boolean>> getXmlQualityErrors() {
        return xmlQualityErrors;
    }

    public void setXmlQualityErrors(Map<String, Map<String, Boolean>> xmlQualityErrors) {
        this.xmlQualityErrors = xmlQualityErrors;
    }

    public boolean isMinimalConverted() {
        return isMinimalConverted;
    }

    public void setMinimalConverted(boolean isMinimalConverted) {
        this.isMinimalConverted = isMinimalConverted;
    }

    public File getFile() {
        return new File(currentLocation);
    }

    public enum FileType{
        EAD("apeEAD"),
        EAG("EAG"),
        EAC_CPF("APE_CPF"),
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
        EDIT_TREE,
        SAVE,
        CONVERT_EDM
    }
}
