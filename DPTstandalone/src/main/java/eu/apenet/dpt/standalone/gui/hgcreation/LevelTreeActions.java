package eu.apenet.dpt.standalone.gui.hgcreation;

import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.utils.util.DOMUtil;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.FileUtil;
import eu.apenet.dpt.utils.util.HoldingsGuideCreationUtils;
import org.apache.commons.io.FileUtils;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
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

    public LevelTreeActions(FileUtil fileUtil){
        this.fileUtil = fileUtil;
    }

    public File createXML(TreeModel model, String title, String id, HashMap<String, String> paramMap, String countryCode, String globalIdentifier){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation domImplementation = builder.getDOMImplementation();
            Document doc = domImplementation.createDocument(null, null, null);

            Element root = createArchdesc(doc, model, model.getRoot(), paramMap, id, title);

            doc.appendChild(root);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer output = tf.newTransformer();
            output.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            output.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            output.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
            output.transform(new DOMSource(doc.getFirstChild()), new StreamResult(new File("/tmp/test.xml")));

            File outputFile = new File(Utilities.TEMP_DIR + "temp_HG.xml");
            File finalFile = new File(Utilities.TEMP_DIR + "Holdings_Guide_" + globalIdentifier + "_" + id + ".xml");
            finalFile.deleteOnExit();

            FileUtils.writeStringToFile(outputFile, HoldingsGuideCreationUtils.eadDeclaration(title, id, countryCode, globalIdentifier, DataPreparationToolGUI.VERSION_NB), "UTF-8");
            fileUtil.writeToFile(fileUtil.readFileAsString_linebreak("/tmp/test.xml"), Utilities.TEMP_DIR + outputFile.getName(), true);
            fileUtil.writeToFile(HoldingsGuideCreationUtils.endDeclaration(), Utilities.TEMP_DIR + outputFile.getName(), true);

            TransformationTool.createTransformation(fileUtil.readFileAsInputStream(outputFile), finalFile, FileUtils.openInputStream(Utilities.BEFORE_XSL_FILE), null, true, true, null, true, null);
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

    private Element createTree(Document doc, TreeModel model, Object node, HashMap<String, String> paramMap) {
        CLevelTreeObject obj = (CLevelTreeObject)((DefaultMutableTreeNode)node).getUserObject();
        Element el = doc.createElement("c");
        el.setAttribute("level", "series");

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
        for(int i=0;i<model.getChildCount(node);i++){
            Object child = model.getChild(node, i);
            if(((CLevelTreeObject)((DefaultMutableTreeNode)child).getUserObject()).isFile()) {
                File file = ((CLevelTreeObject)((DefaultMutableTreeNode)child).getUserObject()).getFile();
                try {
                    File outputFileTmp = new File(Utilities.TEMP_DIR + ".temp_HG.xml");

                    StringWriter xslMessages = TransformationTool.createTransformation(fileUtil.readFileAsInputStream(file), outputFileTmp, TransformationTool.class.getResourceAsStream("/xsl/fa2hg.xsl"), paramMap, true, true, null, true, null);
                    List<String> filesWithoutEadid = new ArrayList<String>();
                    if(xslMessages != null && xslMessages.toString().contains("NO_EADID_IN_FILE")){
                        filesWithoutEadid.add(file.getName());
                    } else {
                        Node fileLevel = stringToNode(doc, fileUtil.readFileAsInputStream(outputFileTmp));
                        el.appendChild(fileLevel);
                    }

                    outputFileTmp.delete();

                } catch (Exception e){
                    LOG.error("Could not create HG part for file: " + file.getName(), e);
//                    createErrorOrWarningPanel(e, true, "Could not create HG");
                }
            } else {
                el.appendChild(createTree(doc,model,child, paramMap));
            }
        }
        return el;
    }

    private Node stringToNode(Document originalDoc, InputStream inputStream) throws Exception {
        InputSource inputSource = new InputSource(inputStream);
        Document doc = DOMUtil.createDocument(inputSource);
        return originalDoc.adoptNode(doc.getFirstChild());
    }

}
