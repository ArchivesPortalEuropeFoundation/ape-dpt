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
import eu.apenet.dpt.utils.ese2edm.EdmConfig;
import eu.apenet.dpt.utils.util.XmlChecker;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class ConvertEdmActionListener implements ActionListener {

    private static final Logger LOG = Logger.getLogger(ConvertEdmActionListener.class);
    private Component parent;
    private ResourceBundle labels;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private RetrieveFromDb retrieveFromDb;
    private APEPanel apePanel;

    public ConvertEdmActionListener(Component parent, DataPreparationToolGUI dataPreparationToolGUI, APEPanel apePanel) {
        this.parent = parent;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.retrieveFromDb = new RetrieveFromDb();
        this.apePanel = apePanel;
        this.labels = dataPreparationToolGUI.getLabels();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        Map<String, FileInstance> fileInstances = dataPreparationToolGUI.getEseFileInstances();
//        JList list = dataPreparationToolGUI.getEseList();
//        dataPreparationToolGUI.disableAllBtnAndItems();
//        dataPreparationToolGUI.disableRadioButtons();
//        File file = (File) list.getSelectedValue();
//        FileInstance fileInstance = fileInstances.get(file.getName());
//        if (!fileInstance.isXml()) {
//            fileInstance.setXml(XmlChecker.isXmlParseable(file) == null);
//            if (!fileInstance.isXml()) {
//                apePanel.getApeTabbedPane().setConversionErrorText(labels.getString("conversion.error.fileNotXml"));
//                apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_CONVERSION, Utilities.FLASHING_RED_COLOR);
//                dataPreparationToolGUI.enableSaveBtn();
//                dataPreparationToolGUI.enableRadioButtons();
//                return;
//            }
//        }
//
//        apePanel.getApeTabbedPane().appendEseConversionErrorText(labels.getString("edm.conversionEdmStarted") + "\n");
//        EdmConfig config = fillConfig();
//        config.setMinimalConversion(fileInstance.isMinimalConverted());
//        try {
//            SwingUtilities.invokeLater(new TransformEdm(config, file, dataPreparationToolGUI));
//            apePanel.getApeTabbedPane().appendEseConversionErrorText(MessageFormat.format(labels.getString("edm.convertedAndSaved"), file.getAbsolutePath(), retrieveFromDb.retrieveDefaultSaveFolder()) + "\n");
//            apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_GREEN_COLOR);
//            JOptionPane.showMessageDialog(dataPreparationToolGUI.getContentPane(), MessageFormat.format(labels.getString("fileInOutput"), retrieveFromDb.retrieveDefaultSaveFolder()) + ".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
//        } catch (Exception ex) {
//            LOG.error(ex);
//            apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_RED_COLOR);
//            dataPreparationToolGUI.enableEdmConversionBtn();
//            dataPreparationToolGUI.enableRadioButtons();
//        }
//        dataPreparationToolGUI.enableRadioButtons();
    }

    public EdmConfig fillEdmConfig(){
        EdmConfig config = new EdmConfig(true);

        //EDM identifier used for OAI-PMH; not needed for DPT purposes, so set to empty string
        config.setEdmIdentifier("");

        //prefixUrl, repositoryCode and xmlTypeName used for EDM element id generation;
        //repositoryCode is taken from the tool while the other two have fixed values.

        config.setPrefixUrl("http://www.archivesportaleurope.net/web/guest/ead-display/-/ead/fp");
        config.setRepositoryCode(dataPreparationToolGUI.getRepositoryCodeIdentifier());
        config.setXmlTypeName("fa");

        return config;
    }
}
