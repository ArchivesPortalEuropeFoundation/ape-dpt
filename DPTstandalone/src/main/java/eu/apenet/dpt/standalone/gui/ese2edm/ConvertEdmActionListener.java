/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui.ese2edm;

import eu.apenet.dpt.standalone.gui.APEPanel;
import eu.apenet.dpt.standalone.gui.APETabbedPane;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.ead2ese.EseOptionsPanel;
import eu.apenet.dpt.utils.ead2ese.XMLTransformer;
import eu.apenet.dpt.utils.ead2ese.XMLUtil;
import eu.apenet.dpt.utils.ead2ese.stax.ESEParser;
import eu.apenet.dpt.utils.ead2ese.stax.RecordParser;
import eu.apenet.dpt.utils.ese2edm.EdmConfig;
import eu.apenet.dpt.utils.util.XmlChecker;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author papp
 */
public class ConvertEdmActionListener implements ActionListener {

    private static final Logger LOG = Logger.getLogger(ConvertEdmActionListener.class);
    private Component parent;
    private ResourceBundle labels;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private APEPanel apePanel;

    public ConvertEdmActionListener(Component parent, DataPreparationToolGUI dataPreparationToolGUI, APEPanel apePanel) {
        this.parent = parent;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.apePanel = apePanel;
        this.labels = dataPreparationToolGUI.getLabels();
    }

    public void actionPerformed(ActionEvent e) {
        dataPreparationToolGUI.disableAllBtnAndItems();

        Map<String, FileInstance> fileInstances = dataPreparationToolGUI.getFileInstances();
        JList list = dataPreparationToolGUI.getEseList();
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.disableRadioButtons();
        File file = (File) list.getSelectedValue();
        FileInstance fileInstance = fileInstances.get(file.getName());
        if (!fileInstance.isXml()) {
            fileInstance.setXml(XmlChecker.isXmlParseable(file) == null);
            if (!fileInstance.isXml()) {
                apePanel.getApeTabbedPane().setConversionErrorText(labels.getString("conversion.error.fileNotXml"));
                apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_CONVERSION, Utilities.FLASHING_RED_COLOR);
                dataPreparationToolGUI.enableSaveBtn();
                dataPreparationToolGUI.enableRadioButtons();
                return;
            }
        }

        apePanel.getApeTabbedPane().appendEseConversionErrorText(labels.getString("edm.conversionEdmStarted") + "\n");
        try {
            SwingUtilities.invokeLater(new TransformEdm(file));
            apePanel.getApeTabbedPane().appendEseConversionErrorText(MessageFormat.format(labels.getString("edm.convertedAndSaved"), file.getAbsolutePath()) + "\n");
            apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_GREEN_COLOR);
        } catch (Exception ex) {
            LOG.error(ex);
            apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_RED_COLOR);
            dataPreparationToolGUI.enableEdmConversionBtn();
            dataPreparationToolGUI.enableRadioButtons();
        }
        dataPreparationToolGUI.enableRadioButtons();
    }

    private class TransformEdm implements Runnable {

        private File selectedIndex;
        private Map<String, FileInstance> fileInstances;

        private TransformEdm(File file) {
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
                File outputFile = new File(xmlOutputFilename);
                EdmConfig config = new EdmConfig();
                config.getTransformerXML2XML().transform(new File(loc), outputFile);
            } catch (Exception e) {
                LOG.error("Error when converting file into EDM", e);
            }
        }
    }
}
