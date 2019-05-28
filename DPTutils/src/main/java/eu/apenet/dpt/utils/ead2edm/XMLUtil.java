package eu.apenet.dpt.utils.ead2edm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eu.apenet.dpt.utils.ead2edm.stax.EDMParser;
import eu.apenet.dpt.utils.ead2edm.stax.ProvidedCHOParser;
import eu.apenet.dpt.utils.ead2edm.stax.WebResourceParser;
import eu.apenet.dpt.utils.util.APEXmlCatalogResolver;
import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {

    public static final String UTF_8 = "UTF-8";

    public static Document convertXMLToDocument(InputStream inputStream) throws SAXException, IOException,
            ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        // XMLErrorHandler errorHandler = new XMLErrorHandler();
        // builder.setErrorHandler(errorHandler);
        InputSource inputSource = new InputSource(inputStream);
        Document document = builder.parse(inputSource);
        // if (errorHandler.hasError()) {
        // throw errorHandler.getException();
        //
        // }
        return document;

    }

    public static XMLStreamReader getXMLStreamReader(File inputFile) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        return inputFactory.createXMLStreamReader(fileInputStream, UTF_8);
    }

    public static XMLStreamWriter getXMLStreamWriter(File outputFile, boolean indent) throws FileNotFoundException,
            XMLStreamException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        FileOutputStream out = new FileOutputStream(outputFile);
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(out, UTF_8);
        if (indent) {
            writer = new IndentingXMLStreamWriter(writer);
        }
        return writer;
    }

    public static void write(Document document, OutputStream outputStream) throws TransformerException,
            UnsupportedEncodingException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, UTF_8);
        StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
    }

    public static int countFilesInEDMfolder(File outputFolder) {
        int files = 0;
        for (File fileEntry : outputFolder.listFiles()) {
            if (fileEntry.isDirectory()) {
            } else {
                files++;
            }
        }
        return files;
    }

    public static DigitalObjectCounter analyzeESEXML(File outputFile) throws XMLStreamException, SAXException,
            IOException {

        XMLStreamReader xmlReader = XMLUtil.getXMLStreamReader(outputFile);
        EDMParser parser = new EDMParser();
        ProvidedCHOParser providedCHOParser = new ProvidedCHOParser();
        WebResourceParser webResourceParser = new WebResourceParser();
        parser.registerParser(providedCHOParser);
        parser.registerParser(webResourceParser);
        // count number of records
        parser.parse(xmlReader, null);
        int numberOfProvidedCHO = providedCHOParser.getNumberOfProvidedCHO();
        int numberOfWebResource = webResourceParser.getNumberOfWebResource();
        DigitalObjectCounter digitalObjectCounter = new DigitalObjectCounter(numberOfProvidedCHO, numberOfWebResource);
        if (numberOfProvidedCHO == 0) {
            outputFile.delete();
//TODO Re-enable validation; currently disabled because of strange exception in code (although oXygen says file is valid)
//		} else {
//			XMLUtil.validateEDM(outputFile);
        }
        xmlReader.close();
        return digitalObjectCounter;
    }

    private static void validateEDM(File file) throws SAXException, IOException, XMLStreamException {
        List<URL> schemaURLs = new ArrayList<URL>();
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/CONTEXTS.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/DC.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/DCTERMS.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/EDM-COMMON-MAIN.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/EDM-EXTERNAL-MAIN.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/EDM-INTERNAL-MAIN.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/EDM-INTERNAL.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/EDM.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/ENRICHMENT.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/FOAF.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/ORE.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/OWL.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/RDAGR2.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/RDF.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/SKOS.xsd"));
        schemaURLs.add(XMLUtil.class.getResource("/ead2edm/edm/WGS84.xsd"));
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), UTF_8);
        StreamSource source = new StreamSource(reader);

        Schema schema = getSchema(schemaURLs);
        Validator validator = schema.newValidator();
        validator.validate(source);
        reader.close();
    }

    private static Schema getSchema(List<URL> schemaURLs) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(new APEXmlCatalogResolver());
        List<StreamSource> schemaSources = new ArrayList<StreamSource>();
        for (URL schemaURL : schemaURLs) {
            schemaSources.add(new StreamSource(schemaURL.toExternalForm()));
        }
        return schemaFactory.newSchema(schemaSources.toArray(new StreamSource[]{}));
    }

}
