package eu.apenet.dpt.standalone.gui.ese2edm;

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.utils.ese2edm.EdmConfig;
import java.io.File;
import java.util.Map;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
//import org.w3c.dom.Document;

/**
 *
 * @author Stefan Papp
 */
public class TransformEdm implements Runnable {

    private EdmConfig edmConfig;
    private File selectedIndex;
    private Map<String, FileInstance> fileInstances;
    private static final Logger LOG = Logger.getLogger(TransformEdm.class);

    public TransformEdm(EdmConfig edmConfig, File file, DataPreparationToolGUI dataPreparationToolGUI) {
        this.edmConfig = edmConfig;
        this.selectedIndex = file;
        this.fileInstances = dataPreparationToolGUI.getFileInstances();
    }

    public void run() {
        try {
            RetrieveFromDb retrieveFromDb = new RetrieveFromDb();
            int lastIndex = selectedIndex.getName().lastIndexOf('.');
            String xmlOutputFilename = retrieveFromDb.retrieveDefaultSaveFolder() + selectedIndex.getName().substring(0, lastIndex) + "-edm" + selectedIndex.getName().substring(lastIndex);
            FileInstance fileInstance = fileInstances.get(selectedIndex.getName());
            File outputFile = new File(xmlOutputFilename);
            edmConfig.getTransformerXML2XML().transform(new File(fileInstance.getEseLocation()), outputFile);
            fileInstance.setEdm(true);
            File eseFile = new File(retrieveFromDb.retrieveDefaultSaveFolder() + selectedIndex.getName().substring(0, lastIndex) + "-ese" + selectedIndex.getName().substring(lastIndex));
            eseFile.delete();
        } catch (Exception e) {
            LOG.error("Error when converting file " + selectedIndex.getName() + " into EDM", e);
        }
    }
}
