package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.utils.util.Xsd_enum;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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
    private final static String XML_LANGUAGES_NAME = "languages.xml";
    private final static String SER_HG_TREE_NAME = "myHGtreeSaved.ser";
    private final static String DIR_SEPARATOR = "/";

    private final static String CONFIG_DIR_SIMPLE = "xsl";
    private final static String SYSTEM_DIR_SIMPLE = "system";
    private final static String LOG_DIR_SIMPLE = "output";
    private final static String TEMP_DIR_SIMPLE = "temp";

    public final static String XSL_DEFAULT_NAME = "default.xsl";
    public final static String XSL_BEFORE_NAME = "before.xsl";

    public final static int XSLT_GROUP = 0;
    public final static int XSD_GROUP = 1;

    public final static ImageIcon icon = new ImageIcon(Utilities.class.getResource("/ape_logo.png"), "APE Logo");
    
    public final static String[] SYSTEM_FILES = {XSL_BEFORE_NAME, XSL_DEFAULT_NAME, XML_LANGUAGES_NAME, "changeNS.xsl", "frontmatter.xsl", "import.xsl", "levels.xsl"};
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
    public final static String HG_TREE_SER_FILE_PATH = TEMP_DIR + SER_HG_TREE_NAME;

    public final static File BEFORE_XSL_FILE = new File(BEFORE_XSL_FILE_PATH);

    public static boolean isSystemFile(String filename) {
        return Arrays.asList(SYSTEM_FILES).contains(filename);
    }
    
    public static String stringFromList(List<SAXParseException> exceptions) {
        StringBuilder result = new StringBuilder();
        for(SAXParseException exception : exceptions)
            result.append("l.").append(exception.getLineNumber()).append(", ").append("c.").append(exception.getColumnNumber()).append(": ").append(exception.getMessage()).append("\n");
        return result.toString();
    }
    
    private static String defaultXsl;
    public static void setDefaultXsl(String defaultXsl){
        Utilities.defaultXsl = defaultXsl;
    }
    public static String getDefaultXsl(){
        if(StringUtils.isBlank(defaultXsl))
            return XSL_DEFAULT_NAME;
        return defaultXsl;
    }

    private static Xsd_enum defaultXsd;
    public static void setDefaultXsd(String defaultXsdString){
        for (Xsd_enum xsdEnum : Xsd_enum.values()){
            if(xsdEnum.getReadableName().equals(defaultXsdString)){
                Utilities.defaultXsd = xsdEnum;
                break;
            }
        }
    }
    public static Xsd_enum getDefaultXsd(){
        if(defaultXsd == null)
            return Xsd_enum.XSD_APE_SCHEMA;
        return defaultXsd;
    }

    public static List<File> getXsltFiles(){
        List<File> list = new ArrayList<File>();
        List<String> xsltNotShown = Arrays.asList(Utilities.FILES_NOT_SHOWN);
        for(File file : new File(Utilities.SYSTEM_DIR).listFiles()) {
            if((file.getName().endsWith("xsl") || file.getName().endsWith("xslt")) && !xsltNotShown.contains(file.getName()))
                list.add(file);
        }
        for(File file : new File(Utilities.CONFIG_DIR).listFiles()) {
            if((file.getName().endsWith("xsl") || file.getName().endsWith("xslt")))
                list.add(file);
        }
        return list;
    }
}
