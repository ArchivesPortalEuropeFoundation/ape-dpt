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
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.XsltChecker;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/01/2012
 *
 * @author Yoann Moranville
 */
public final class Utilities {
    public static boolean isDev = false;

    private final static String XML_LANGUAGES_NAME = "languages.xml";
    private final static String SER_HG_TREE_NAME = "myHGtreeSaved.ser";
    private final static String DIR_SEPARATOR = "/";

    private final static String CONFIG_DIR_SIMPLE = "xsl";
    private final static String SYSTEM_DIR_SIMPLE = "system";
    private final static String LOG_DIR_SIMPLE = "output";
    private final static String TEMP_DIR_SIMPLE = "temp";

    public final static String XSL_DEFAULT_NAME = "default-apeEAD.xsl";
    public final static String XSL_BEFORE_NAME = "before.xsl";

    public final static int XSLT_GROUP = 0;
    public final static int XSD_GROUP = 1;

    public final static ImageIcon icon = new ImageIcon(Utilities.class.getResource("/ape_logo.png"), "APE Logo");

    public final static String[] SYSTEM_FILES = {XSL_BEFORE_NAME, XSL_DEFAULT_NAME, XML_LANGUAGES_NAME, "changeNS.xsl", "frontmatter.xsl", "import.xsl", "levels.xsl", "eag2eag2012.xsl"};
    public final static String[] FILES_NOT_SHOWN = {XSL_BEFORE_NAME, "frontmatter.xsl", "import.xsl", "levels.xsl"};

//    public static final Color FLASHING_RED_COLOR = new Color(255, 30, 30);
    public static final Color FLASHING_RED_COLOR = new Color(200, 90, 90);
    public static final Color FLASHING_GREEN_COLOR = new Color(100, 180, 100);
    public static final Color TAB_COLOR = new Color(238, 238, 238);

    public final static String CONFIG_DIR = CONFIG_DIR_SIMPLE + DIR_SEPARATOR;
    public final static String LOG_DIR = LOG_DIR_SIMPLE + DIR_SEPARATOR;
    public final static String TEMP_DIR = TEMP_DIR_SIMPLE + DIR_SEPARATOR;
    public final static String SYSTEM_DIR = CONFIG_DIR + SYSTEM_DIR_SIMPLE + DIR_SEPARATOR;

    public final static String DEFAULT_XSL_FILE_PATH = SYSTEM_DIR + XSL_DEFAULT_NAME;
    public final static String BEFORE_XSL_FILE_PATH = SYSTEM_DIR + XSL_BEFORE_NAME;
    public final static String LANGUAGES_XML_FILE_PATH = SYSTEM_DIR + XML_LANGUAGES_NAME;
    public final static String HG_TREE_SER_FILE_PATH = LOG_DIR + SER_HG_TREE_NAME;

    public final static File BEFORE_XSL_FILE = new File(BEFORE_XSL_FILE_PATH);

    private static List<XsdObject> xsdList;

    public static boolean isSystemFile(String filename) {
        return Arrays.asList(SYSTEM_FILES).contains(filename);
    }

    public static String stringFromList(List<SAXParseException> exceptions) {
        StringBuilder result = new StringBuilder();
        for(SAXParseException exception : exceptions)
            result.append("l.").append(exception.getLineNumber()).append(", ").append("c.").append(exception.getColumnNumber()).append(": ").append(exception.getMessage()).append("\n");
        return result.toString();
    }

    public static XsdObject getXsdObjectFromPath(String xsdPath) {
        for(XsdObject xsdObject : getXsdList()) {
            if(xsdObject.getPath().equals(xsdPath)) {
                return xsdObject;
            }
        }
        return null;
    }

    public static XsdObject getXsdObjectFromName(String xsdName) {
        for(XsdObject xsdObject : getXsdList()) {
            if(xsdObject.getName().equals(xsdName)) {
                return xsdObject;
            }
        }
        return null;
    }

    public static URL getUrlPathXsd(XsdObject xsdObject) throws MalformedURLException {
        if(xsdObject.isSystem())
            return DocumentValidation.class.getResource("/" + xsdObject.getPath());
        else
            return new File(CONFIG_DIR + xsdObject.getPath()).toURI().toURL(); //to test on UNIX system
    }

    public static List<XsdObject> getXsdList() {
        return xsdList;
    }

    public static void setXsdList(List<XsdObject> xsdList) {
        Utilities.xsdList = xsdList;
    }

    public static void addToXsdList(XsdObject xsd) {
        Utilities.xsdList.add(xsd);
    }

    public static List<File> getXsltFiles(){
        List<File> list = new ArrayList<File>();
        List<String> xsltNotShown = Arrays.asList(Utilities.FILES_NOT_SHOWN);
        File temp = new File(Utilities.SYSTEM_DIR);
        for(File file : new File(Utilities.SYSTEM_DIR).listFiles()) {
            if((file.getName().endsWith("xsl") || file.getName().endsWith("xslt")) && !xsltNotShown.contains(file.getName()) && XsltChecker.isXsltFile(file)){
                list.add(file);
            }
        }
        for(File file : new File(Utilities.CONFIG_DIR).listFiles()) {
            if((file.getName().endsWith("xsl") || file.getName().endsWith("xslt")) && XsltChecker.isXsltFile(file))
                list.add(file);
        }
        return list;
    }
}
