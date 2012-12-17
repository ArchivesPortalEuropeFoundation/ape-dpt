package eu.apenet.dpt.standalone.gui.validation;

import eu.apenet.dpt.standalone.gui.*;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.XmlChecker;
import eu.apenet.dpt.utils.util.Xsd_enum;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 18/01/2012
 *
 * @author Yoann Moranville
 */
public class ValidateActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(ValidateActionListener.class);

    private ResourceBundle labels;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private APETabbedPane apeTabbedPane;

    public ValidateActionListener(ResourceBundle labels, DataPreparationToolGUI dataPreparationToolGUI, APETabbedPane apeTabbedPane) {
        this.labels = labels;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.apeTabbedPane = apeTabbedPane;
    }


    public void actionPerformed(ActionEvent e) {
        apeTabbedPane.setValidationErrorText(labels.getString("validationBegun"));
        dataPreparationToolGUI.disableAllBtnAndItems();

        File file = (File) dataPreparationToolGUI.getList().getSelectedValue();
//        SwingUtilities.invokeLater(new ValidateRunner(file));
        new Thread(new ValidateRunner(file)).start();
    }

    private class ValidateRunner implements Runnable {
        private File file;

        ValidateRunner(File file) {
            this.file = file;
        }

        public void run() {
            SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea());
            Thread threadRunner = new Thread(summaryWorking);
            FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(file.getName());
            if(!fileInstance.isXml()) {
                fileInstance.setXml(XmlChecker.isXmlParseable(file) == null);
                if(!fileInstance.isXml()) {
                    apeTabbedPane.setValidationErrorText(labels.getString("validation.error.fileNotXml"));
                    apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_VALIDATION, Utilities.FLASHING_RED_COLOR);
                    dataPreparationToolGUI.setResultAreaText(labels.getString("validation.error.fileNotXml"));
                    dataPreparationToolGUI.enableSaveBtn();
                    return;
                }
            }
            try{
                dataPreparationToolGUI.setResultAreaText(labels.getString("validating"));
                threadRunner.setName(SummaryWorking.class.toString());
                threadRunner.start();
                List<SAXParseException> exceptions;
                if(dataPreparationToolGUI.getTree() != null && dataPreparationToolGUI.getTree().getTreeTableModel() != null && fileInstance.getLastOperation().equals(FileInstance.Operation.CREATE_TREE)){
                    TreeTableModel treeTableModel = dataPreparationToolGUI.getTree().getTreeTableModel();
                    Document document = (Document)treeTableModel.getRoot();
                    try {
                        File file2 = new File(Utilities.TEMP_DIR + "temp_" + file.getName());
                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer output = tf.newTransformer();
                        output.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
                        output.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");

                        output.transform(new DOMSource(document.getFirstChild()), new StreamResult(file2));
                        exceptions = DocumentValidation.xmlValidation(FileUtils.openInputStream(file2), fileInstance.getValidationSchema());
                    } catch (Exception ex){
                        LOG.error("Error when taking the XML tree and validating it", ex);
                        throw new RuntimeException(ex);
                    }
                } else if(!fileInstance.isConverted())
                    exceptions = DocumentValidation.xmlValidation(FileUtils.openInputStream(file), fileInstance.getValidationSchema());
                else {
                    InputStream is = FileUtils.openInputStream(new File(fileInstance.getCurrentLocation()));
                    exceptions = DocumentValidation.xmlValidation(is, fileInstance.getValidationSchema());
                }
                if (exceptions == null || exceptions.isEmpty()){
                    apeTabbedPane.setValidationErrorText(labels.getString("validationSuccess"));
                    apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_VALIDATION, Utilities.FLASHING_GREEN_COLOR);
                    fileInstance.setValid(true);
                    if(fileInstance.getValidationSchema() == Xsd_enum.XSD_APE_SCHEMA || fileInstance.getValidationSchema() == Xsd_enum.XSD1_0_APE_SCHEMA)
                        dataPreparationToolGUI.enableEseConversionBtn();
                } else {
                    if(!fileInstance.isConverted())
                        dataPreparationToolGUI.enableConversionBtns();

                    String errors = Utilities.stringFromList(exceptions);
                    fileInstance.setValidationErrors(errors);
                    fileInstance.setValid(false);
                    apeTabbedPane.setValidationErrorText(errors);
                    apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_VALIDATION, Utilities.FLASHING_RED_COLOR);
                }
            } catch(Exception ex){
                fileInstance.setValidationErrors(labels.getString("validationException") + "\n\n-------------\n" + ex.getMessage());
                fileInstance.setValid(false);
                apeTabbedPane.setValidationErrorText(fileInstance.getValidationErrors());
                apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_VALIDATION, Utilities.FLASHING_RED_COLOR);
                LOG.error("Error when validating a file", ex);
            } finally {
                dataPreparationToolGUI.enableSaveBtn();
                summaryWorking.stop();
                threadRunner.interrupt();
                dataPreparationToolGUI.setResultAreaText(labels.getString("validationFinished"));
                fileInstance.setLastOperation(FileInstance.Operation.VALIDATE);
                dataPreparationToolGUI.getList().repaint();
            }
        }
    }
}
