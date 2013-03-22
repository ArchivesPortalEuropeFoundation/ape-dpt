package eu.apenet.dpt.standalone.gui.ese2edm;

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.utils.ese2edm.EdmConfig;
import java.io.File;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 *
 * @author Stefan Papp
 */
public class TransformEdm implements Runnable {

    private File selectedIndex;
    private Map<String, FileInstance> fileInstances;
    
    private static final Logger LOG = Logger.getLogger(TransformEdm.class);


    public TransformEdm(File file, DataPreparationToolGUI dataPreparationToolGUI) {
        this.selectedIndex = file;
        this.fileInstances = dataPreparationToolGUI.getFileInstances();
    }

    public void run() {
        try {
            RetrieveFromDb retrieveFromDb = new RetrieveFromDb();
            int lastIndex = selectedIndex.getName().lastIndexOf('.');
            String xmlOutputFilename = retrieveFromDb.retrieveDefaultSaveFolder() + selectedIndex.getName().substring(0, lastIndex - 4) + "-edm" + selectedIndex.getName().substring(lastIndex);
            FileInstance fileInstance = fileInstances.get(selectedIndex.getName());
            String loc;
            if (fileInstance.isEse()) {
                loc = fileInstance.getCurrentLocation();
            } else {
                loc = fileInstance.getOriginalPath();
            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document intermediateDoc = docBuilder.newDocument();
            File outputFile = new File(xmlOutputFilename);
            EdmConfig config = new EdmConfig(true);
                config.getTransformerXML2XML().transform(new File(loc), outputFile);
//                File outputFile_temp = new File(Utilities.TEMP_DIR + ".temp_"+selectedIndex.getName());
//            config.getTransformerXML2XML().transform(new File(loc), intermediateDoc);
//                System.out.println("intermediateDoc root: " + intermediateDoc.getDocumentElement().getNodeName());
//            config.setTransferToFileOutput(true);
//            config.getTransformerXML2XML().transform(intermediateDoc, outputFile);
        } catch (Exception e) {
            LOG.error("Error when converting file "+ selectedIndex.getName() +" into EDM", e);
        }
    }
}
