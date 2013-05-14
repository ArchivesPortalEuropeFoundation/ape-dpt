package eu.apenet.dpt.standalone.gui.ead2ese;

import eu.apenet.dpt.standalone.gui.APEPanel;
import eu.apenet.dpt.standalone.gui.APETabbedPane;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.SummaryWorking;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.ese2edm.ConvertEdmActionListener;
import eu.apenet.dpt.standalone.gui.ese2edm.TransformEdm;
import eu.apenet.dpt.standalone.gui.progress.ApexActionListener;
import eu.apenet.dpt.standalone.gui.progress.ProgressFrame;
import eu.apenet.dpt.utils.ese2edm.EdmConfig;
import eu.apenet.dpt.utils.util.XmlChecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * User: Yoann Moranville Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class ConvertEseActionListener extends ApexActionListener {

    private final static Logger LOG = Logger.getLogger(ConvertEseActionListener.class);
    private DataPreparationToolGUI dataPreparationToolGUI;
    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private APEPanel apePanel;

    public ConvertEseActionListener(ResourceBundle labels, DataPreparationToolGUI dataPreparationToolGUI, APEPanel apePanel) {
        this.labels = labels;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.retrieveFromDb = new RetrieveFromDb();
        this.apePanel = apePanel;
    }

    public void actionPerformed(ActionEvent e) {
        continueLoop = true;
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.disableRadioButtons();
        dataPreparationToolGUI.disableAllBatchBtns();
        dataPreparationToolGUI.getAPEPanel().setFilename("");
        if (!dataPreparationToolGUI.getXmlEadList().isSelectionEmpty() && !dataPreparationToolGUI.getEseList().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(dataPreparationToolGUI, "You have selected several files in different processing status. Please make sure that you have selected either all apeEAD files only or all ESE files only.");
        } else {
            // Conversion from EAD to ESE in the if-statement, conversion from ESE to EDM in the else-statement
            if (!dataPreparationToolGUI.getXmlEadList().isSelectionEmpty()) {
                JFrame eseOptionFrame = new JFrame(labels.getString("ese.eseOptionFrame"));
                if(dataPreparationToolGUI.getXmlEadList().getSelectedValues().length > 1)
                    eseOptionFrame.add(new EseOptionsPanel(labels, dataPreparationToolGUI, eseOptionFrame, apePanel.getApeTabbedPane(), true));
                else
                    eseOptionFrame.add(new EseOptionsPanel(labels, dataPreparationToolGUI, eseOptionFrame, apePanel.getApeTabbedPane(), false));
                eseOptionFrame.setPreferredSize(new Dimension(dataPreparationToolGUI.getContentPane().getWidth() * 3 / 4, dataPreparationToolGUI.getContentPane().getHeight() * 3 / 4));
                eseOptionFrame.pack();
                eseOptionFrame.setVisible(true);
            } else if (!dataPreparationToolGUI.getEseList().isSelectionEmpty()) {
                final Object[] objects = dataPreparationToolGUI.getEseList().getSelectedValues();
                final ApexActionListener apexActionListener = this;
                new Thread(new Runnable() {
                    public void run() {
                        int numberOfFiles = objects.length;
                        int currentFileNumberBatch = 0;
                        ProgressFrame progressFrame = new ProgressFrame(labels, apePanel, true, true, apexActionListener);
                        ProgressFrame.ApeProgressBar progressBar = progressFrame.getProgressBarBatch();

                        for (Object oneFile : objects) {
                            if (!continueLoop) {
                                break;
                            }

                            File file = (File) oneFile;
                            dataPreparationToolGUI.setResultAreaText(labels.getString("converting") + " " + file.getName() + " (" + (++currentFileNumberBatch) + "/" + numberOfFiles + ")");
                            SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea(), progressBar);
                            summaryWorking.setTotalNumberFiles(numberOfFiles);
                            summaryWorking.setCurrentFileNumberBatch(currentFileNumberBatch);
                            Thread threadRunner = new Thread(summaryWorking);
                            threadRunner.setName(SummaryWorking.class.toString());
                            threadRunner.start();
                            dataPreparationToolGUI.getEseList().setEnabled(false);

                            FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(file.getName());

                            if (!fileInstance.isXml()) {
                                fileInstance.setXml(XmlChecker.isXmlParseable(file) == null);
                                if (!fileInstance.isXml()) {
                                    fileInstance.setConversionErrors(labels.getString("conversion.error.fileNotXml"));
                                    dataPreparationToolGUI.enableSaveBtn();
                                    dataPreparationToolGUI.enableRadioButtons();
                                }
                            }

                            if (fileInstance.isXml()) {
                                ConvertEdmActionListener edmListener = new ConvertEdmActionListener(apePanel, dataPreparationToolGUI, apePanel);
                                EdmConfig edmConfig = edmListener.fillConfig();
                                try {
                                    SwingUtilities.invokeLater(new TransformEdm(edmConfig, file, dataPreparationToolGUI));
                                    apePanel.getApeTabbedPane().appendEseConversionErrorText(MessageFormat.format(labels.getString("edm.convertedAndSaved"), file.getAbsolutePath(), retrieveFromDb.retrieveDefaultSaveFolder()) + "\n");
                                    apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_GREEN_COLOR);
                                } catch (Exception ex) {
                                    LOG.error(ex);
                                    apePanel.getApeTabbedPane().checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_RED_COLOR);
                                    dataPreparationToolGUI.enableEdmConversionBtn();
                                    dataPreparationToolGUI.enableRadioButtons();
                                } finally {
                                    summaryWorking.stop();
                                    threadRunner.interrupt();
                                    dataPreparationToolGUI.getEseList().repaint();
                                }
                            }
                        }
                        if (progressFrame != null) {
                            progressFrame.stop();
                        }
                        dataPreparationToolGUI.getFinalAct().run();
                        dataPreparationToolGUI.getEseList().clearSelection();
                        if (continueLoop) {
                            dataPreparationToolGUI.setResultAreaText(labels.getString("finished"));
                        } else {
                            dataPreparationToolGUI.setResultAreaText(labels.getString("aborted"));
                        }
                        dataPreparationToolGUI.enableSaveBtn();
                        dataPreparationToolGUI.enableRadioButtons();
                        dataPreparationToolGUI.getEseList().setEnabled(true);
                    }
                }).start();
            }
        }
    }

    public void stopLoop() {
        continueLoop = false;
    }
}