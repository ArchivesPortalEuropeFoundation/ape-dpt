package eu.apenet.dpt.standalone.gui.batch;

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
import eu.apenet.dpt.standalone.gui.*;
import eu.apenet.dpt.standalone.gui.xsdaddition.XsdObject;
import eu.apenet.dpt.standalone.gui.adhoc.EadidQueryComponent;
import eu.apenet.dpt.standalone.gui.conversion.CounterThread;
import eu.apenet.dpt.standalone.gui.progress.ApexActionListener;
import eu.apenet.dpt.standalone.gui.progress.ProgressFrame;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.service.stax.StaxTransformationTool;
import eu.apenet.dpt.utils.util.CountCLevels;
import eu.apenet.dpt.utils.util.XmlChecker;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville Date: 17/04/2012
 *
 * @author Yoann Moranville
 */
public class ConvertAndValidateActionListener extends ApexActionListener {

    private static final Logger LOG = Logger.getLogger(ConvertAndValidateActionListener.class);
    private DataPreparationToolGUI dataPreparationToolGUI;
    private ResourceBundle labels;
    private Component parent;

    public ConvertAndValidateActionListener(DataPreparationToolGUI dataPreparationToolGUI, Component parent) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent event) {
        labels = dataPreparationToolGUI.getLabels();
        continueLoop = true;
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.disableEditionTab();
        dataPreparationToolGUI.disableRadioButtons();
        dataPreparationToolGUI.disableAllBatchBtns();
        dataPreparationToolGUI.getAPEPanel().setFilename("");
        final Object[] objects = dataPreparationToolGUI.getXmlEadList().getSelectedValues();
        final ApexActionListener apexActionListener = this;
        new Thread(new Runnable() {
            public void run() {
                int numberOfFiles = objects.length;
                int currentFileNumberBatch = 0;
                ProgressFrame progressFrame = new ProgressFrame(labels, parent, true, false, apexActionListener);
                JProgressBar batchProgressBar = progressFrame.getProgressBarBatch();

                for (Object oneFile : objects) {
                    if (!continueLoop) {
                        break;
                    }

                    File file = (File) oneFile;
                    dataPreparationToolGUI.setResultAreaText(labels.getString("converting") + " " + file.getName() + " (" + (++currentFileNumberBatch) + "/" + numberOfFiles + ")");

                    SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea(), batchProgressBar);
                    summaryWorking.setTotalNumberFiles(numberOfFiles);
                    summaryWorking.setCurrentFileNumberBatch(currentFileNumberBatch);
                    Thread threadRunner = new Thread(summaryWorking);
                    threadRunner.setName(SummaryWorking.class.toString());
                    threadRunner.start();
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().disableConversionBtn();
                    dataPreparationToolGUI.getAPEPanel().getApeTabbedPane().disableValidationBtn();
                    dataPreparationToolGUI.getXmlEadList().setEnabled(false);

                    FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(file.getName());

                    if (!fileInstance.isXml()) {
                        fileInstance.setXml(XmlChecker.isXmlParseable(file) == null);
                        if (!fileInstance.isXml()) {
                            fileInstance.setConversionErrors(labels.getString("conversion.error.fileNotXml"));
                            dataPreparationToolGUI.enableSaveBtn();
                            dataPreparationToolGUI.enableRadioButtons();
                            dataPreparationToolGUI.enableEditionTab();
                        }
                    }

                    if (fileInstance.isXml()) {
                        JProgressBar progressBar = null;

                        String eadid = "";
                        boolean doTransformation = true;
                        if (fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_SCHEMA.getPath())) || fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAD_SCHEMA.getPath()))) {
                            StaxTransformationTool staxTransformationTool = new StaxTransformationTool(file);
                            staxTransformationTool.run();
                            LOG.debug("file has eadid? " + staxTransformationTool.isFileWithEadid());
                            if (!staxTransformationTool.isFileWithEadid()) {
                                EadidQueryComponent eadidQueryComponent;
                                if (staxTransformationTool.getUnitid() != null && !staxTransformationTool.getUnitid().equals("")) {
                                    eadidQueryComponent = new EadidQueryComponent(staxTransformationTool.getUnitid());
                                } else {
                                    eadidQueryComponent = new EadidQueryComponent(labels);
                                }
                                int result = JOptionPane.showConfirmDialog(parent, eadidQueryComponent.getMainPanel(), labels.getString("enterEADID"), JOptionPane.OK_CANCEL_OPTION);
                                while (StringUtils.isEmpty(eadidQueryComponent.getEntryEadid()) && result != JOptionPane.CANCEL_OPTION) {
                                    result = JOptionPane.showConfirmDialog(parent, eadidQueryComponent.getMainPanel(), labels.getString("enterEADID"), JOptionPane.OK_CANCEL_OPTION);
                                }
                                if (result == JOptionPane.OK_OPTION) {
                                    eadid = eadidQueryComponent.getEntryEadid();
                                } else if (result == JOptionPane.CANCEL_OPTION) {
                                    doTransformation = false;
                                }
                            }
                        }
                        if (doTransformation) {
                            Thread threadProgress = null;
                            CounterThread counterThread = null;
                            CounterCLevelCall counterCLevelCall = null;
                            int counterMax = 0;
                            if (fileInstance.getConversionScriptName().equals(Utilities.XSL_DEFAULT_APEEAD_NAME)) {
                                progressBar = progressFrame.getProgressBarSingle();
                                progressBar.setVisible(true);
                                progressFrame.setTitle(labels.getString("progressTrans") + " - " + file.getName());
                                CountCLevels countCLevels = new CountCLevels();
                                counterMax = countCLevels.countOneFile(file);
                                if (counterMax > 0) {
                                    counterCLevelCall = new CounterCLevelCall();
                                    counterCLevelCall.initializeCounter(counterMax);
                                    counterThread = new CounterThread(counterCLevelCall, progressBar, counterMax);
                                    threadProgress = new Thread(counterThread);
                                    threadProgress.setName(CounterThread.class.toString());
                                    threadProgress.start();
                                }
                            }
                            try {
                                try {
                                    File xslFile = new File(fileInstance.getConversionScriptPath());

                                    File outputFile = new File(Utilities.TEMP_DIR + "temp_" + file.getName());
                                    outputFile.deleteOnExit();
                                    StringWriter xslMessages;
                                    HashMap<String, String> parameters = dataPreparationToolGUI.getParams();
                                    parameters.put("eadidmissing", eadid);

                                    if (fileInstance.getConversionScriptName().equals(Utilities.XSL_DEFAULT_APEEAD_NAME)) {
                                        File outputFile_temp = new File(Utilities.TEMP_DIR + ".temp_" + file.getName());
                                        TransformationTool.createTransformation(FileUtils.openInputStream(file), outputFile_temp, Utilities.BEFORE_XSL_FILE, null, true, true, null, true, null);

                                        xslMessages = TransformationTool.createTransformation(FileUtils.openInputStream(outputFile_temp), outputFile, xslFile, parameters, true, true, null, true, counterCLevelCall);
                                        outputFile_temp.delete();
                                    } else {
                                        xslMessages = TransformationTool.createTransformation(FileUtils.openInputStream(file), outputFile, xslFile, parameters, true, true, null, true, null);
                                    }
                                    fileInstance.setConversionErrors(xslMessages.toString());
                                    fileInstance.setCurrentLocation(Utilities.TEMP_DIR + "temp_" + file.getName());
                                    fileInstance.setConverted();
                                    fileInstance.setLastOperation(FileInstance.Operation.CONVERT);
                                    if (xslMessages.toString().equals("")) {
                                        if (fileInstance.getConversionScriptName().equals(Utilities.XSL_DEFAULT_APEEAD_NAME)) {
                                            fileInstance.setConversionErrors(labels.getString("conversion.noExcludedElements"));
                                        } else {
                                            fileInstance.setConversionErrors(labels.getString("conversion.finished"));
                                        }
                                    }

                                    if (!continueLoop) {
                                        break;
                                    }

                                } catch (Exception e) {
                                    fileInstance.setConversionErrors(labels.getString("conversionException") + "\r\n\r\n-------------\r\n" + e.getMessage());
                                    throw new Exception("Error when converting " + file.getName(), e);
                                }

                                if (threadProgress != null) {
                                    counterThread.stop();
                                    threadProgress.interrupt();
                                }
                                if (progressBar != null) {
                                    if (counterMax > 0) {
                                        progressBar.setValue(counterMax);
                                    }
                                    progressBar.setIndeterminate(true);
                                }

                                try {
                                    InputStream is = FileUtils.openInputStream(new File(fileInstance.getCurrentLocation()));
                                    dataPreparationToolGUI.setResultAreaText(labels.getString("validating") + " " + file.getName() + " (" + currentFileNumberBatch + "/" + numberOfFiles + ")");
                                    XsdObject xsdObject = fileInstance.getValidationSchema();

                                    List<SAXParseException> exceptions = DocumentValidation.xmlValidation(is, Utilities.getUrlPathXsd(xsdObject), xsdObject.isXsd11());
                                    if (exceptions == null || exceptions.isEmpty()) {
                                        fileInstance.setValid(true);
                                        fileInstance.setValidationErrors(labels.getString("validationSuccess"));
                                    } else {
                                        String errors = Utilities.stringFromList(exceptions);
                                        fileInstance.setValidationErrors(errors);
                                        fileInstance.setValid(false);
                                    }
                                    fileInstance.setLastOperation(FileInstance.Operation.VALIDATE);
                                } catch (Exception ex) {
                                    fileInstance.setValid(false);
                                    fileInstance.setValidationErrors(labels.getString("validationException") + "\r\n\r\n-------------\r\n" + ex.getMessage());
                                    throw new Exception("Error when validating", ex);
                                }
                            } catch (Exception e) {
                                LOG.error("Error when converting and validating", e);
                            } finally {
                                summaryWorking.stop();
                                threadRunner.interrupt();
                                dataPreparationToolGUI.getXmlEadListLabel().repaint();
                                dataPreparationToolGUI.getXmlEadList().repaint();
                                if (progressBar != null) {
                                    progressBar.setVisible(false);
                                }
                            }
                        }
                    }
                }
                Toolkit.getDefaultToolkit().beep();
                if (progressFrame != null) {
                    progressFrame.stop();
                }
                dataPreparationToolGUI.getFinalAct().run();
                dataPreparationToolGUI.getXmlEadList().clearSelection();
                if (continueLoop) {
                    dataPreparationToolGUI.setResultAreaText(labels.getString("finished"));
                } else {
                    dataPreparationToolGUI.setResultAreaText(labels.getString("aborted"));
                }
                dataPreparationToolGUI.enableSaveBtn();
                dataPreparationToolGUI.enableRadioButtons();
                dataPreparationToolGUI.enableEditionTab();
            }
        }).start();
    }
}
