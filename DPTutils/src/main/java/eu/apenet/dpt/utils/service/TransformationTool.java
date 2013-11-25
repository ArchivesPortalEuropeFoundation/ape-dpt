package eu.apenet.dpt.utils.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Controller;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import net.sf.saxon.serialize.Emitter;
import net.sf.saxon.serialize.MessageEmitter;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import eu.apenet.dpt.utils.util.DiscardDtdEntityResolver;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevel;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;
import eu.apenet.dpt.utils.util.extendxsl.FlagSet;
import eu.apenet.dpt.utils.util.extendxsl.LinkFormatChecker;
import eu.apenet.dpt.utils.util.extendxsl.Oai2EadNormalization;
import eu.apenet.dpt.utils.util.extendxsl.XmlQualityChecker;
import eu.apenet.dpt.utils.util.extendxsl.XmlQualityCheckerCall;

/**
 * User: yoann.moranville
 * Date: 4 dec. 2009
 */
public class TransformationTool {
    private static final String CONVERTED_APE_EAD_VERSION = "Converted_apeEAD_version_";
	private static final Logger LOG = Logger.getLogger(TransformationTool.class);
    private static final String CHARACTER_SET = "UTF-8";

    public static StringWriter createTransformation(InputStream inputStream, Writer writer, Source xsltSource, Map<String, String> parameters) throws SAXException {
        try {
            XMLReader saxParser =  XMLReaderFactory.createXMLReader();
            saxParser.setEntityResolver(new DiscardDtdEntityResolver());

            DateNormalization dateNormalization = new DateNormalization();
            Oai2EadNormalization oai2EadNormalization = new Oai2EadNormalization();
            FlagSet flagSet = new FlagSet();
            LinkFormatChecker linkFormatChecker = new LinkFormatChecker();

            InputSource is = new InputSource(inputStream);
            SAXSource xmlSource = new SAXSource(saxParser, is);

            Processor processor = new Processor(false);
            processor.registerExtensionFunction(dateNormalization);
            processor.registerExtensionFunction(oai2EadNormalization);
            processor.registerExtensionFunction(flagSet);
            processor.registerExtensionFunction(linkFormatChecker);

            XsltCompiler compiler = processor.newXsltCompiler();

            compiler.setErrorListener(
                    new ErrorListener() {
                        public void warning(TransformerException e) throws TransformerException {
                            logError(e);
                        }
                        public void error(TransformerException e) throws TransformerException {
                            logError(e);
                        }
                        public void fatalError(TransformerException e) throws TransformerException {
                            logError(e);
                        }
                        public void logError(TransformerException e) {
                            LOG.error("Error: " + e.getMessageAndLocation());
                        }
                    }
            );

            XsltExecutable executable = compiler.compile(xsltSource);
            XsltTransformer transformer = executable.load();

            transformer.setSource(xmlSource);
            Serializer serializer = new Serializer();
            serializer.setOutputWriter(writer);
            transformer.setDestination(serializer);

            if(parameters == null)
                parameters = new HashMap<String, String>();
            transformer = insertParameters(transformer, parameters);
            StringWriter xslMessages = new StringWriter();
            long start = System.currentTimeMillis();
            transformer.transform();
            long end = System.currentTimeMillis();
            if(inputStream != null)
            	inputStream.close();

            LOG.trace("Time elapsed for transformation: " + (end-start) + "ms");
            return xslMessages;
        } catch (SAXParseException e) {
            LOG.error("Problem validating file", e);
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static StringWriter createTransformation(InputStream inputFileStream, File outputFile, InputStream xslFile, Map<String, String> parameters, boolean outputIsFile, boolean forWebApp, String provider, boolean isXsd11, ExtensionFunctionCall extensionFunctionCall, String... baseURI) throws SAXException {
        Source xsltSource = new StreamSource(xslFile);
        return createTransformation(inputFileStream, outputFile, xsltSource, parameters, outputIsFile, forWebApp, provider, isXsd11, extensionFunctionCall, baseURI);
    }

    public static StringWriter createTransformation(InputStream inputFileStream, File outputFile, File xslFile, Map<String, String> parameters, boolean outputIsFile, boolean forWebApp, String provider, boolean isXsd11, ExtensionFunctionCall extensionFunctionCall, String... baseURI) throws SAXException {
        Source xsltSource = new StreamSource(xslFile);
        return createTransformation(inputFileStream, outputFile, xsltSource, parameters, outputIsFile, forWebApp, provider, isXsd11, extensionFunctionCall, baseURI);
    }

    public static StringWriter createTransformation(InputStream inputFileStream, File outputFile, Source xsltSource, Map<String, String> parameters, boolean outputIsFile, boolean forWebApp, String provider, boolean isXsd11, ExtensionFunctionCall extensionFunctionCall, String... baseURI) throws SAXException {
        try {
            XMLReader saxParser =  XMLReaderFactory.createXMLReader();
            saxParser.setEntityResolver(new DiscardDtdEntityResolver());

            DateNormalization dateNormalization = new DateNormalization(baseURI);
            Oai2EadNormalization oai2EadNormalization = new Oai2EadNormalization();
            FlagSet flagSet = new FlagSet();
            LinkFormatChecker linkFormatChecker = new LinkFormatChecker();

            InputSource is = new InputSource(inputFileStream);
            SAXSource xmlSource = new SAXSource(saxParser, is);

            Processor processor = new Processor(false);
            processor.registerExtensionFunction(dateNormalization);
            processor.registerExtensionFunction(oai2EadNormalization);
            processor.registerExtensionFunction(flagSet);
            processor.registerExtensionFunction(linkFormatChecker);

            if(extensionFunctionCall == null || extensionFunctionCall instanceof CounterCLevelCall) {
                if(extensionFunctionCall == null)
                    extensionFunctionCall = new CounterCLevelCall();
                CounterCLevel counterCLevel = new CounterCLevel((CounterCLevelCall)extensionFunctionCall);
                processor.registerExtensionFunction(counterCLevel);
            } else if(extensionFunctionCall instanceof XmlQualityCheckerCall) {
                XmlQualityChecker xmlQualityChecker = new XmlQualityChecker((XmlQualityCheckerCall)extensionFunctionCall);
                processor.registerExtensionFunction(xmlQualityChecker);
            }

            XsltCompiler compiler = processor.newXsltCompiler();

            compiler.setErrorListener(
                    new ErrorListener() {
                        public void warning(TransformerException e) throws TransformerException {
                            logError(e);
                        }
                        public void error(TransformerException e) throws TransformerException {
                            logError(e);
                        }
                        public void fatalError(TransformerException e) throws TransformerException {
                            logError(e);
                        }
                        public void logError(TransformerException e) {
                            LOG.error("Error: " + e.getMessageAndLocation());
                        }
                    }
            );

            XsltExecutable executable = compiler.compile(xsltSource);
            XsltTransformer transformer = executable.load();

            transformer.setSource(xmlSource);

            FileOutputStream outputStream = null;
            if(outputFile != null) {
                outputStream = new FileOutputStream(outputFile);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, CHARACTER_SET);
                Serializer serializer = new Serializer();
                serializer.setOutputWriter(writer);
                transformer.setDestination(serializer);
            } else {
                OutputStream writer = new NullOutputStream();
                Serializer serializer = new Serializer();
                serializer.setOutputStream(writer);
                transformer.setDestination(serializer);
            }

            if(parameters == null)
                parameters = new HashMap<String, String>();
            parameters.put("provider", provider);
            if(!isXsd11){
                parameters.put("useXSD10", "true");
            }
            transformer = insertParameters(transformer, parameters);
            StringWriter xslMessages = new StringWriter();
            if(outputIsFile){
                Controller controller = transformer.getUnderlyingController();
                Receiver receiver = new MessageEmitter();
                if(forWebApp){
                    ((Emitter)receiver).setWriter(xslMessages);
                } else {
                    FileWriter fstream = new FileWriter("xsl_messages.txt");
                    ((Emitter)receiver).setWriter(fstream);
                }
                controller.setMessageEmitter(receiver);
            }
            long start = System.currentTimeMillis();
            transformer.transform();
            long end = System.currentTimeMillis();
            if(outputStream != null)
                outputStream.close();
            if(inputFileStream != null)
                inputFileStream.close();

            LOG.trace("Time elapsed for transformation: " + (end-start) + "ms");
            return xslMessages;
        } catch (SAXParseException e) {
            LOG.error("Problem validating file", e);
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static XsltTransformer insertParameters(XsltTransformer transformer, Map<String, String> parameters){
        if(parameters != null){
            for(String parameter : parameters.keySet()){
                transformer.setParameter(new QName(parameter), new XdmAtomicValue(parameters.get(parameter)));
            }
        }
        transformer.setParameter(new QName("versionnb"), new XdmAtomicValue(getVersion()));
        return transformer;
    }
    public static String getVersion(){
    	String version =  TransformationTool.class.getPackage().getImplementationVersion();
        if (StringUtils.isEmpty(version)) {
        	version= "DEV-VERSION";
        }
        return version;
    }
    public static String getFullVersion(){
    	 return CONVERTED_APE_EAD_VERSION + getVersion();
    }
}
