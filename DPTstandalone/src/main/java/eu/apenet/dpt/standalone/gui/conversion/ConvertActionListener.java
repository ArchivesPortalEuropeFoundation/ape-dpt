package eu.apenet.dpt.standalone.gui.conversion;

import eu.apenet.dpt.standalone.gui.*;
import eu.apenet.dpt.standalone.gui.adhoc.EadidQueryComponent;
import eu.apenet.dpt.standalone.gui.progress.ProgressFrame;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.service.stax.CheckXslEadidVariable;
import eu.apenet.dpt.utils.service.stax.StaxTransformationTool;
import eu.apenet.dpt.utils.util.CountCLevels;
import eu.apenet.dpt.utils.util.XmlChecker;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 16/01/2012
 *
 * @author Yoann Moranville
 */
public class ConvertActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(ConvertActionListener.class);
    private Component parent;
    private ResourceBundle labels;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private APEPanel apePanel;

    public ConvertActionListener(Component parent, DataPreparationToolGUI dataPreparationToolGUI, APEPanel apePanel) {
        this.parent = parent;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.apePanel = apePanel;
        this.labels = dataPreparationToolGUI.getLabels();
    }

    public void actionPerformed(ActionEvent e) {
        Map<String, FileInstance> fileInstances = dataPreparationToolGUI.getFileInstances();
        JList list = dataPreparationToolGUI.getXmlEadList();
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.disableEditionTab();
        dataPreparationToolGUI.disableRadioButtons();
        File file = (File)list.getSelectedValue();
        FileInstance fileInstance = fileInstances.get(file.getName());
        if(!fileInstance.isXml()) {
            fileInstance.setXml(XmlChecker.isXmlParseable(file) == null);
            if(!fileInstance.isXml()) {
                apePanel.getApeTabbedPane().setConversionErrorText(labels.getString("conversion.error.fileNotXml"));
                apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_CONVERSION, Utilities.FLASHING_RED_COLOR);
                dataPreparationToolGUI.enableSaveBtn();
                dataPreparationToolGUI.enableRadioButtons();
                dataPreparationToolGUI.enableEditionTab();
                return;
            }
        }

        boolean isDefaultTransformation = false;
        if(fileInstance.getConversionScriptName().equals(Utilities.XSL_DEFAULT_NAME))
            isDefaultTransformation = true;

        String eadid = "";
        boolean doTransformation = true;
        if(fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_SCHEMA.getPath())) || fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD1_0_APE_SCHEMA.getPath())) || fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAD_SCHEMA.getPath()))){
            StaxTransformationTool staxTransformationTool = new StaxTransformationTool(file);
            staxTransformationTool.run();
            LOG.debug("file has eadid? " + staxTransformationTool.isFileWithEadid());
            if(!staxTransformationTool.isFileWithEadid()) {
                boolean showEadidQueryComponent = true;
                if(!isDefaultTransformation){
                    CheckXslEadidVariable checkXslEadidVariable = new CheckXslEadidVariable(new File(fileInstance.getConversionScriptPath()));
                    checkXslEadidVariable.run();
                    if(checkXslEadidVariable.isFileWithDefaultEadid())
                        showEadidQueryComponent = false;
                }

                if(showEadidQueryComponent) {
                    EadidQueryComponent eadidQueryComponent;
                    if(staxTransformationTool.getUnitid() != null && !staxTransformationTool.getUnitid().equals("")){
                        eadidQueryComponent = new EadidQueryComponent(staxTransformationTool.getUnitid());
                    } else {
                        eadidQueryComponent = new EadidQueryComponent(labels);
                    }
                    int result = JOptionPane.showConfirmDialog(parent, eadidQueryComponent.getMainPanel(), labels.getString("enterEADID"), JOptionPane.OK_CANCEL_OPTION);
                    while (eadidQueryComponent.getEntryEadid() == null || eadidQueryComponent.getEntryEadid().equals(""))
                        result = JOptionPane.showConfirmDialog(parent, eadidQueryComponent.getMainPanel(), labels.getString("enterEADID"), JOptionPane.OK_CANCEL_OPTION);
                    if(result == JOptionPane.OK_OPTION)
                        eadid = eadidQueryComponent.getEntryEadid();
                    if(result == JOptionPane.CANCEL_OPTION)
                        doTransformation = false;
                }
            }
        }

        if(doTransformation) {
//            SwingUtilities.invokeLater(new ConvertRunner(file, fileInstance, eadid, isDefaultTransformation));
            apePanel.getApeTabbedPane().setConversionErrorText(labels.getString("conversionBegun"));
            fileInstance.setLastOperation(FileInstance.Operation.CONVERT);
            new Thread(new ConvertRunner(file, fileInstance, eadid, isDefaultTransformation)).start();
        } else {
            dataPreparationToolGUI.enableSaveBtn();
            dataPreparationToolGUI.enableRadioButtons();
            dataPreparationToolGUI.enableValidationBtns();
            dataPreparationToolGUI.enableConversionBtns();
            dataPreparationToolGUI.enableEditionTab();
        }
    }

    private class ConvertRunner implements Runnable {
        private File file;
        private FileInstance fileInstance;
        private String eadid;
        private boolean isDefaultTransformation;

        ConvertRunner(File file, FileInstance fileInstance, String eadid, boolean isDefaultTransformation) {
            this.file = file;
            this.fileInstance = fileInstance;
            this.eadid = eadid;
            this.isDefaultTransformation = isDefaultTransformation;
        }

        public void run() {
            labels = dataPreparationToolGUI.getLabels();
            CounterCLevelCall counterCLevelCall = null;
            Thread threadProgress = null;
            CounterThread counterThread = null;
            ProgressFrame progressFrame = null;
            if(isDefaultTransformation) {
                progressFrame = new ProgressFrame(labels, parent, false, false, null);
                ProgressFrame.ApeProgressBar progressBar = progressFrame.getProgressBarSingle();
                CountCLevels countCLevels = new CountCLevels();
                int counterMax = countCLevels.countOneFile(file);
                if(counterMax > 0){
                    counterCLevelCall = new CounterCLevelCall();
                    counterCLevelCall.initializeCounter(counterMax);

                    counterThread = new CounterThread(counterCLevelCall, progressBar, counterMax);
                    threadProgress = new Thread(counterThread);
                    threadProgress.setName(CounterThread.class.toString());
                    threadProgress.start();
                }
            }
            SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea());
            Thread threadRunner = new Thread(summaryWorking);
            StringWriter xslMessages = new StringWriter();
            try {
                dataPreparationToolGUI.setResultAreaText(labels.getString("converting"));
                threadRunner.setName(SummaryWorking.class.toString());
                threadRunner.start();
                Map<String, String> paramMap = dataPreparationToolGUI.getParams();
                paramMap.put("eadidmissing", eadid);
                File xslFile = new File(fileInstance.getConversionScriptPath());
                File outputFile = new File(Utilities.TEMP_DIR + "temp_" + file.getName());
                outputFile.deleteOnExit();
                if(fileInstance.getConversionScriptName().equals(Utilities.XSL_DEFAULT_NAME)){
                    File outputFile_temp = new File(Utilities.TEMP_DIR + ".temp_"+file.getName());
                    TransformationTool.createTransformation(FileUtils.openInputStream(file), outputFile_temp, FileUtils.openInputStream(Utilities.BEFORE_XSL_FILE), paramMap, true, true, null, true, null);
                    xslMessages = TransformationTool.createTransformation(FileUtils.openInputStream(outputFile_temp), outputFile, FileUtils.openInputStream(xslFile), paramMap, true, true, null, true, counterCLevelCall);
                    outputFile_temp.delete();
                } else {
                    xslMessages = TransformationTool.createTransformation(FileUtils.openInputStream(file), outputFile, FileUtils.openInputStream(xslFile), paramMap, true, true, null, true, null);
                }
                apePanel.getApeTabbedPane().setConversionErrorText(xslMessages.toString());
                fileInstance.setConversionErrors(xslMessages.toString());
                fileInstance.setCurrentLocation(Utilities.TEMP_DIR + "temp_" + file.getName());
                fileInstance.setConverted();

                if(xslMessages.toString().equals("")) {
                    if(fileInstance.getConversionScriptName().equals(Utilities.XSL_DEFAULT_NAME))
                        fileInstance.setConversionErrors(labels.getString("conversion.noExcludedElements"));
                    else
                        fileInstance.setConversionErrors(labels.getString("conversion.finished"));
                    apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_CONVERSION, Utilities.FLASHING_GREEN_COLOR);
                    apePanel.getApeTabbedPane().setConversionErrorText(fileInstance.getConversionErrors());
                } else
                    apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_CONVERSION, Utilities.FLASHING_RED_COLOR);
                dataPreparationToolGUI.enableValidationBtns();
                dataPreparationToolGUI.enableSaveBtn();
            } catch (Exception ex){
                if(ex instanceof SaxonApiException)
                    LOG.info(xslMessages.toString());
                apePanel.getApeTabbedPane().setConversionErrorText(labels.getString("conversionException") + "\n\n-------------\n" + ex.getMessage());
                fileInstance.setConversionErrors(labels.getString("conversionException") + "\n\n-------------\n" + ex.getMessage());
                apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_CONVERSION, Utilities.FLASHING_RED_COLOR);
                LOG.error("Error when converting a file", ex);
            } finally {
                dataPreparationToolGUI.enableRadioButtons();
                dataPreparationToolGUI.enableEditionTab();
                summaryWorking.stop();
                if(counterThread != null)
                    counterThread.stop();
                if(threadProgress != null)
                    threadProgress.interrupt();
                if(threadRunner != null)
                    threadRunner.interrupt();
                if(progressFrame != null){
                    progressFrame.setVisible(false);
                    progressFrame.dispose();
                }
                dataPreparationToolGUI.setResultAreaText(labels.getString("conversionFinished"));
            }
        }
    }
}
