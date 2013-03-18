package eu.apenet.dpt.standalone.gui.validation;

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.SummaryWorking;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.xsdAddition.XsdObject;
import eu.apenet.dpt.standalone.gui.progress.ApexActionListener;
import eu.apenet.dpt.standalone.gui.progress.ProgressFrame;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.XmlChecker;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class ValidateSelectionActionListener extends ApexActionListener {
    private static final Logger LOG = Logger.getLogger(ValidateSelectionActionListener.class);
    private ResourceBundle labels;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private Component parent;
    private boolean continueLoop;

    public ValidateSelectionActionListener(DataPreparationToolGUI dataPreparationToolGUI, Component parent) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e){
        labels = dataPreparationToolGUI.getLabels();
        continueLoop = true;
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.disableRadioButtons();
        dataPreparationToolGUI.disableAllBatchBtns();
        dataPreparationToolGUI.getAPEPanel().setFilename("");
        final Object[] objects = dataPreparationToolGUI.getXmlEadList().getSelectedValues();
        final ApexActionListener apexActionListener = this;
        new Thread(new Runnable(){
            public void run(){
                int numberOfFiles = objects.length;
                int currentFileNumberBatch = 0;
                ProgressFrame progressFrame = new ProgressFrame(labels, parent, true, true, apexActionListener);

                ProgressFrame.ApeProgressBar progressBar = progressFrame.getProgressBarBatch();
                for(Object oneFile : objects){
                    if(!continueLoop)
                        break;

                    File file = (File)oneFile;
                    dataPreparationToolGUI.setResultAreaText(labels.getString("validating")+ " " + file.getName() + " (" + (++currentFileNumberBatch) + "/" + numberOfFiles + ")");
                    SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea(), progressBar);
                    summaryWorking.setTotalNumberFiles(numberOfFiles);
                    summaryWorking.setCurrentFileNumberBatch(currentFileNumberBatch);
                    Thread threadRunner = new Thread(summaryWorking);
                    threadRunner.setName(SummaryWorking.class.toString());
                    threadRunner.start();
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().disableConversionBtn();
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().disableValidationBtn();
                    dataPreparationToolGUI.getXmlEadList().setEnabled(false);

                    Map<String, FileInstance> fileInstances = dataPreparationToolGUI.getFileInstances();
                    FileInstance fileInstance = fileInstances.get(file.getName());
                    if(!fileInstance.isXml()) {
                        if(XmlChecker.isXmlParseable(file) == null)
                            fileInstance.setXml(true);
                        else {
                            fileInstance.setValid(false);
                            fileInstance.setValidationErrors(labels.getString("validation.error.fileNotXml"));
                        }
                    }
                    if(fileInstance.isXml()) {
                        try {
                            List<SAXParseException> exceptions;
                            XsdObject xsdObject = fileInstance.getValidationSchema();
                            if(fileInstance.isConverted()){
                                InputStream is = FileUtils.openInputStream(new File(fileInstance.getCurrentLocation()));
                                exceptions = DocumentValidation.xmlValidation(is, Utilities.getUrlPathXsd(xsdObject), xsdObject.isXsd11());
                            } else {
                                exceptions = DocumentValidation.xmlValidation(FileUtils.openInputStream(file), Utilities.getUrlPathXsd(xsdObject), xsdObject.isXsd11());
                            }
                            if (exceptions.isEmpty() || exceptions == null){
                                fileInstance.setValid(true);
                                fileInstance.setValidationErrors(labels.getString("validationSuccess"));
                            } else {
                                String errors = Utilities.stringFromList(exceptions);
                                fileInstance.setValidationErrors(errors);
                                fileInstance.setValid(false);
                            }
                            fileInstance.setLastOperation(FileInstance.Operation.VALIDATE);
                        } catch (Exception ex){
                            fileInstance.setValid(false);
                            fileInstance.setValidationErrors(labels.getString("validationException") + "\n\n-------------\n" + ex.getMessage());
                            LOG.error("Error when validating", ex);
                        } finally {
                            summaryWorking.stop();
                            threadRunner.interrupt();
                            dataPreparationToolGUI.getXmlEadList().repaint();
                        }
                    }
                }
                if(progressFrame != null){
                    progressFrame.stop();
                }
                dataPreparationToolGUI.getFinalAct().run();
                dataPreparationToolGUI.getXmlEadList().clearSelection();
                if(continueLoop)
                    dataPreparationToolGUI.setResultAreaText(labels.getString("finished"));
                else
                    dataPreparationToolGUI.setResultAreaText(labels.getString("aborted"));
                dataPreparationToolGUI.enableSaveBtn();
                dataPreparationToolGUI.enableRadioButtons();
            }
        }).start();
    }

    public void stopLoop() {
        continueLoop = false;
    }
}