package eu.apenet.dpt.utils.ead2edm;

import org.junit.Ignore;
import org.junit.Test;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.Properties;

/**
 * Created by yoannmoranville on 25/08/15.
 */
public class XMLTransformerTest {

    @Test @Ignore
    public void testTransformWithNoLanguage() throws TransformerException {
        Properties parameters = getParameters();
        XMLTransformer xmlTransformer = new XMLTransformer("/Users/yoannmoranville/Documents/Work/APE/dpt/branches/DPT-project-2.1/DPTutils/src/main/resources/ead2edm/ead2edm.xsl", parameters);
        File inputFile = new File ("/Users/yoannmoranville/Documents/Work/APE/data/1974.xml");
        File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"no-language"));
        xmlTransformer.transformForTest(inputFile, outputFile);
    }
//    @Test @Ignore
//    public void testTransformWithInheritLanguage() throws TransformerException {
//        Properties parameters = getParameters();
//        parameters.put("inheritLanguage", Boolean.TRUE.toString());
//        XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
//        File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_LANGUAGES.ead.xml");
//        File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"inherit-language"));
//        xmlTransformer.transform("asdf", inputFile, outputFile);
//    }
//    @Test @Ignore
//    public void testTransformWithGivenLanguage() throws TransformerException {
//        Properties parameters = getParameters();
//        parameters.put("language", "nl");
//        XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
//        File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_LANGUAGES.ead.xml");
//        File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"given-language"));
//        xmlTransformer.transform("asdf", inputFile, outputFile);
//    }
//    @Test @Ignore
//    public void testTransformWithNoLanguage1() throws TransformerException {
//        Properties parameters = getParameters();
//        XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
//        File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_NO_LANGUAGES.ead.xml");
//        File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"no-language"));
//        xmlTransformer.transform("asdf", inputFile, outputFile);
//    }
//    @Test @Ignore
//    public void testTransformWithInheritLanguage1() throws TransformerException {
//        Properties parameters = getParameters();
//        parameters.put("inheritLanguage", Boolean.TRUE.toString());
//        XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
//        File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_NO_LANGUAGES.ead.xml");
//        File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"inherit-language"));
//        xmlTransformer.transform("asdf", inputFile, outputFile);
//    }
//    @Test @Ignore
//    public void testTransformWithGivenLanguage1() throws TransformerException {
//        Properties parameters = getParameters();
//        parameters.put("language", "nl");
//        XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
//        File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_NO_LANGUAGES.ead.xml");
//        File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"given-language"));
//        xmlTransformer.transform("asdf", inputFile, outputFile);
//    }
    private String getOutputName(String name, String suffix){
        int lastIndex  = name.lastIndexOf(".xml");
        return name.substring(0,lastIndex) + "-" + suffix + ".edm.xml";
    }
    private Properties getParameters(){
        Properties properties = new Properties();
        properties.put("europeana_provider", "europeana_provider");
        properties.put("europeana_dataprovider", "europeana_dataprovider");
        properties.put("europeana_rights", "europeana_rights");
        properties.put("dc_rights", "dc_rights");
        properties.put("europeana_type", "VIDEO");
        properties.put("idSource", "unitid");
        properties.put("useISODates", "false");
        properties.put("language", "en");
        properties.put("inheritElementsFromFileLevel", Boolean.TRUE.toString());
        properties.put("inheritOrigination", Boolean.TRUE.toString());
        properties.put("inheritUnittitle", Boolean.TRUE.toString());
        properties.put("inheritLanguage", Boolean.TRUE.toString());
        properties.put("inheritRightsInfo", Boolean.TRUE.toString());
        properties.put("useExistingDaoRole", Boolean.TRUE.toString());
        properties.put("useExistingLanguage", Boolean.TRUE.toString());
        properties.put("useExistingRepository", Boolean.TRUE.toString());
        properties.put("useExistingRightsInfo", Boolean.TRUE.toString());
        properties.put("minimalConversion", Boolean.FALSE.toString());
        properties.put("edm_identifier", "edm_identifier");
        properties.put("host", "localhost");
        properties.put("repository_code", "FR-SIAF");
        properties.put("xml_type_name", "fa");
        properties.put("landingPage", "ape");
        properties.put("useArchUnittitle", Boolean.TRUE.toString());


        return properties;
    }
}