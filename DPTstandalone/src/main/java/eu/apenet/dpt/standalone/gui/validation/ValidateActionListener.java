package eu.apenet.dpt.standalone.gui.validation;

import eu.apenet.dpt.standalone.gui.*;
import eu.apenet.dpt.standalone.gui.xsdaddition.XsdObject;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.XmlChecker;
import eu.apenet.dpt.utils.util.extendxsl.XmlQualityCheckerCall;
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
import java.io.StringWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
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

    public ValidateActionListener(DataPreparationToolGUI dataPreparationToolGUI, APETabbedPane apeTabbedPane) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.apeTabbedPane = apeTabbedPane;
        this.labels = dataPreparationToolGUI.getLabels();
    }


    public void actionPerformed(ActionEvent e) {
        apeTabbedPane.setValidationErrorText(labels.getString("validationBegun"));
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.disableEditionTab();
        dataPreparationToolGUI.disableRadioButtons();

        File file = (File) dataPreparationToolGUI.getXmlEadList().getSelectedValue();
        new Thread(new ValidateRunner(file)).start();
    }

    private class ValidateRunner implements Runnable {
        private File file;

        ValidateRunner(File file) {
            this.file = file;
        }

        public void run() {
            labels = dataPreparationToolGUI.getLabels();
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
                    dataPreparationToolGUI.enableRadioButtons();
                    dataPreparationToolGUI.enableEditionTab();
                    return;
                }
            }
            try{
                dataPreparationToolGUI.setResultAreaText(labels.getString("validating"));
                threadRunner.setName(SummaryWorking.class.toString());
                threadRunner.start();
                List<SAXParseException> exceptions;
                XsdObject xsdObject = fileInstance.getValidationSchema();
                URL schemaPath = Utilities.getUrlPathXsd(xsdObject);
                InputStream is2;
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
                        is2 = FileUtils.openInputStream(file2);
                        exceptions = DocumentValidation.xmlValidation(FileUtils.openInputStream(file2), schemaPath, xsdObject.isXsd11());
                    } catch (Exception ex){
                        LOG.error("Error when taking the XML tree and validating it", ex);
                        throw new RuntimeException(ex);
                    }
                } else if(!fileInstance.isConverted()) {
                    is2 = FileUtils.openInputStream(file);
                    exceptions = DocumentValidation.xmlValidation(FileUtils.openInputStream(file), schemaPath, xsdObject.isXsd11());
                } else {
                    is2 = FileUtils.openInputStream(new File(fileInstance.getCurrentLocation()));
                    exceptions = DocumentValidation.xmlValidation(FileUtils.openInputStream(new File(fileInstance.getCurrentLocation())), schemaPath, xsdObject.isXsd11());
                }
                if (exceptions == null || exceptions.isEmpty()){
                    if(fileInstance.getCurrentLocation() == null || fileInstance.getCurrentLocation().equals(""))
                        fileInstance.setCurrentLocation(file.getAbsolutePath());
                    fileInstance.setValid(true);
                    fileInstance.setValidationErrors(labels.getString("validationSuccess"));
                    if(xsdObject.getFileType().equals(FileInstance.FileType.EAD)) {
                        dataPreparationToolGUI.enableEseConversionBtn();
                        //Do a XML Quality check
                        InputStream xslIs = ValidateActionListener.class.getResourceAsStream("/xmlQuality/xmlQuality.xsl");
                        XmlQualityCheckerCall xmlQualityCheckerCall = new XmlQualityCheckerCall();
                        TransformationTool.createTransformation(is2, null, xslIs, null, true, true, null, false, xmlQualityCheckerCall);

                        StringWriter writer = new StringWriter();
                        writer.append("\n");
                        writer.append("\n");
                        writer.append("----- ");
                        writer.append(labels.getString("dataquality.title"));
                        writer.append(" -----");
                        writer.append("\n");
                        writer.append(MessageFormat.format(labels.getString("dataquality.missing.unittitle"), "(unittitle)"));
                        writer.append(" ");
                        writer.append(Integer.toString(xmlQualityCheckerCall.getCounterUnittitle()));
                        writer.append("\n");
                        writer.append(MessageFormat.format(labels.getString("dataquality.missing.unitdate"), "(unitdate@normal)"));
                        writer.append(" ");
                        writer.append(Integer.toString(xmlQualityCheckerCall.getCounterUnitdate()));
                        writer.append("\n");
                        writer.append(MessageFormat.format(labels.getString("dataquality.missing.dao"), "(dao@xlink:role)"));
                        writer.append(" ");
                        writer.append(Integer.toString(xmlQualityCheckerCall.getCounterDao()));
                        fileInstance.setValidationErrors(fileInstance.getValidationErrors() + writer.toString());

                        fileInstance.setXmlQualityErrors(createXmlQualityErrors(xmlQualityCheckerCall));
                        apeTabbedPane.enableValidationReportBtn();
                    }
                    apeTabbedPane.setValidationErrorText(fileInstance.getValidationErrors());
                    apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_VALIDATION, Utilities.FLASHING_GREEN_COLOR);
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
                dataPreparationToolGUI.getXmlEadList().repaint();
                dataPreparationToolGUI.enableRadioButtons();
                dataPreparationToolGUI.enableEditionTab();
            }
        }

        private Map<String, Map<String, Boolean>> createXmlQualityErrors(XmlQualityCheckerCall xmlQualityCheckerCall) {
            Map<String, Map<String, Boolean>> map = new HashMap<String, Map<String, Boolean>>();
            map.put("unittitle", xmlQualityCheckerCall.getIdsUnittitle());
            map.put("unitdate", xmlQualityCheckerCall.getIdsUnitdate());
            map.put("dao", xmlQualityCheckerCall.getIdsDao());
            return map;
        }
    }
}
