package eu.apenet.dpt.utils.ead2ese;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.junit.Ignore;
import org.junit.Test;

public class XMLTransformerTest {

	@Test @Ignore
	public void testTransformWithNoLanguage() throws TransformerException {
		Map<String, Object> parameters = getParameters();
		XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
		File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_LANGUAGES.ead.xml");
		File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"no-language"));		
		xmlTransformer.transform("asdf", inputFile, outputFile);	
	}
	@Test @Ignore
	public void testTransformWithInheritLanguage() throws TransformerException {
		Map<String, Object> parameters = getParameters();
		parameters.put("inheritLanguage", Boolean.TRUE.toString());
		XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
		File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_LANGUAGES.ead.xml");
		File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"inherit-language"));		
		xmlTransformer.transform("asdf", inputFile, outputFile);	
	}
	@Test @Ignore
	public void testTransformWithGivenLanguage() throws TransformerException {
		Map<String, Object> parameters = getParameters();
		parameters.put("language", "nl");
		XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
		File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_LANGUAGES.ead.xml");
		File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"given-language"));		
		xmlTransformer.transform("asdf", inputFile, outputFile);	
	}	
	@Test @Ignore
	public void testTransformWithNoLanguage1() throws TransformerException {
		Map<String, Object> parameters = getParameters();
		XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
		File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_NO_LANGUAGES.ead.xml");
		File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"no-language"));		
		xmlTransformer.transform("asdf", inputFile, outputFile);	
	}
	@Test @Ignore
	public void testTransformWithInheritLanguage1() throws TransformerException {
		Map<String, Object> parameters = getParameters();
		parameters.put("inheritLanguage", Boolean.TRUE.toString());
		XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
		File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_NO_LANGUAGES.ead.xml");
		File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"inherit-language"));		
		xmlTransformer.transform("asdf", inputFile, outputFile);	
	}
	@Test @Ignore
	public void testTransformWithGivenLanguage1() throws TransformerException {
		Map<String, Object> parameters = getParameters();
		parameters.put("language", "nl");
		XMLTransformer xmlTransformer = new XMLTransformer("/ead2ese/ead2ese.xslt", parameters);
		File inputFile = new File ("C:/apenet/work/final/workspace/DPT-project/DPTutils/src/test/resources/IMAGES_NO_LANGUAGES.ead.xml");
		File outputFile = new File (inputFile.getParentFile(), getOutputName(inputFile.getName(),"given-language"));		
		xmlTransformer.transform("asdf", inputFile, outputFile);	
	}	
	private String getOutputName(String name, String suffix){
		int lastIndex  = name.lastIndexOf(".ead.xml");
		return name.substring(0,lastIndex) + "-" + suffix + ".ese.xml";
	}
	private Map<String, Object> getParameters(){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("europeana_provider", "Archives Portal Europe");
		parameters.put("europeana_rights", "http://www.europeana.eu/rights/rr-f/");
		parameters.put("dc_rights", "Difficult to choose this rights");
		parameters.put("europeana_type", "IMAGE");
		parameters.put("useISODates", "false");
		//parameters.put("language", "nl");
		parameters.put("inheritElementsFromFileLevel", Boolean.FALSE.toString());
		parameters.put("inheritOrigination", Boolean.FALSE.toString());
		//parameters.put("inheritLanguage", Boolean.TRUE.toString());
		parameters.put("contextInformationPrefix", "Context: ");
		return parameters;
	}
}
