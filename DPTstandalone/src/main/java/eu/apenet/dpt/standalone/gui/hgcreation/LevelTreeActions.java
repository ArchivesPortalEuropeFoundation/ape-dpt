package eu.apenet.dpt.standalone.gui.hgcreation;

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

import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.utils.util.DOMUtil;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.FileUtil;
import eu.apenet.dpt.utils.util.HoldingsGuideCreationUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Mar 1, 2011
 *
 * @author Yoann Moranville
 */
public class LevelTreeActions {
    private static final Logger LOG = Logger.getLogger(LevelTreeActions.class);
    private FileUtil fileUtil;

    public LevelTreeActions(){
        this.fileUtil = new FileUtil();
    }

    public File createXML(TreeModel model, HashMap<String, String> paramMap, String countryCode, String globalIdentifier){
        CLevelTreeObject obj = (CLevelTreeObject)((DefaultMutableTreeNode)model.getRoot()).getUserObject();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation domImplementation = builder.getDOMImplementation();
            Document doc = domImplementation.createDocument(null, null, null);

            Element root = createArchdesc(doc, model, model.getRoot(), paramMap, obj.getId(), obj.getName());

            doc.appendChild(root);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer output = tf.newTransformer();
            output.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            output.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            output.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
            output.transform(new DOMSource(doc.getFirstChild()), new StreamResult(new File(Utilities.TEMP_DIR + ".hg_creation.xml")));

            File outputFile = new File(Utilities.TEMP_DIR + "temp_HG.xml");
            File finalFile = new File(Utilities.TEMP_DIR + "Holdings_Guide_" + globalIdentifier + "_" + obj.getId() + ".xml");
            finalFile.deleteOnExit();

            FileUtils.writeStringToFile(outputFile, HoldingsGuideCreationUtils.eadDeclaration(obj.getName(), obj.getId(), countryCode, globalIdentifier, DataPreparationToolGUI.VERSION_NB), "UTF-8");
            fileUtil.writeToFile(fileUtil.readFileAsString_linebreak(Utilities.TEMP_DIR + ".hg_creation.xml"), Utilities.TEMP_DIR + outputFile.getName(), true);
            fileUtil.writeToFile(HoldingsGuideCreationUtils.endDeclaration(), Utilities.TEMP_DIR + outputFile.getName(), true);

            TransformationTool.createTransformation(fileUtil.readFileAsInputStream(outputFile), finalFile, Utilities.BEFORE_XSL_FILE, null, true, true, null, true, null);
            outputFile.delete();

            return finalFile;
        } catch (Exception e){
            LOG.error("Error", e);
        }
        return null;
    }

    private Element createArchdesc(Document doc, TreeModel model, Object node, HashMap<String, String> paramMap, String id, String title) {
        CLevelTreeObject obj = (CLevelTreeObject)((DefaultMutableTreeNode)node).getUserObject();

        Element archdesc = doc.createElement("archdesc");
        archdesc.setAttribute("level", "fonds");
        archdesc.setAttribute("type", "holdings_guide");
        archdesc.setAttribute("encodinganalog", "3.1.4");
        archdesc.setAttribute("relatedencoding", "ISAD(G)v2");

        Element did = doc.createElement("did");

        Element unitid = doc.createElement("unitid");
        unitid.setAttribute("encodinganalog", "3.1.1");
        unitid.setTextContent(id);

        Element unittitle = doc.createElement("unittitle");
        unittitle.setAttribute("encodinganalog", "3.1.2");
        unittitle.setTextContent(title);

        Element dsc = doc.createElement("dsc");

        did.appendChild(unitid);
        did.appendChild(unittitle);
        archdesc.appendChild(did);

        if(!obj.getDescription().equals("")){
            Element scopecontent = doc.createElement("scopecontent");
            scopecontent.setAttribute("encodinganalog", "summary");
            Element pElt = doc.createElement("p");
            pElt.setTextContent(obj.getDescription());
            scopecontent.appendChild(pElt);
            archdesc.appendChild(scopecontent);
        }

        archdesc.appendChild(dsc);

        for(int i=0; i<model.getChildCount(node); i++){
            dsc.appendChild(createTree(doc, model, model.getChild(node, i), paramMap));
        }

        return archdesc;
    }

    private Node createTree(Document doc, TreeModel model, Object node, HashMap<String, String> paramMap) {
        CLevelTreeObject obj = (CLevelTreeObject)((DefaultMutableTreeNode)node).getUserObject();

        if(obj.isFile()) {
            File file = obj.getFile();
            try {
                File outputFileTmp = new File(Utilities.TEMP_DIR + ".temp_HG.xml");
                FileWriter fileWriter = new FileWriter(outputFileTmp);
                InputStream xslIs = TransformationTool.class.getResourceAsStream("/xsl/fa2hg.xsl");
                Source xsltSource = new StreamSource(xslIs);
                StringWriter stringWriter = new StringWriter();
                StringWriter xslMessages = TransformationTool.createTransformation(fileUtil.readFileAsInputStream(file), stringWriter, xsltSource, paramMap);
                fileWriter.write(stringWriter.toString());
                fileWriter.close();
                List<String> filesWithoutEadid = new ArrayList<String>();
                if(xslMessages != null && xslMessages.toString().contains("NO_EADID_IN_FILE")){
                    filesWithoutEadid.add(file.getName());
                } else {
                    Reader reader = new FileReader(outputFileTmp);
                    ReaderInputStream readerInputStream = new ReaderInputStream(reader, "UTF-8");
                    Node fileLevel = stringToNode(doc, readerInputStream);
                    return fileLevel;
//                    el.getParentNode().appendChild(fileLevel);
                }

                outputFileTmp.delete();

            } catch (Exception e){
                LOG.error("Could not create HG part for file: " + file.getName(), e);
//                    createErrorOrWarningPanel(e, true, "Could not create HG");
            }
        } else {
            Element el = doc.createElement("c");

            Element did = doc.createElement("did");
            if(!obj.getId().equals("")){
                Element unitid = doc.createElement("unitid");
                unitid.setAttribute("encodinganalog", "3.1.1");
                unitid.setTextContent(obj.getId());
                did.appendChild(unitid);
            }
            Element title = doc.createElement("unittitle");
            title.setAttribute("encodinganalog", "3.1.2");
            title.setTextContent(obj.getName());
            did.appendChild(title);
            el.appendChild(did);

            if(!obj.getDescription().equals("")){
                Element scopecontent = doc.createElement("scopecontent");
                scopecontent.setAttribute("encodinganalog", "summary");
                Element pElt = doc.createElement("p");
                pElt.setTextContent(obj.getDescription());
                scopecontent.appendChild(pElt);
                el.appendChild(scopecontent);
            }
            for(int i=0;i<model.getChildCount(node);i++) {
                Object child = model.getChild(node, i);
                el.appendChild(createTree(doc,model,child, paramMap));
            }
            return el;
        }
        return null;
    }

    private Node stringToNode(Document originalDoc, ReaderInputStream readerInputStream) throws Exception {
        Document doc = DOMUtil.createDocument(readerInputStream);
        return originalDoc.adoptNode(doc.getFirstChild());
    }

}
