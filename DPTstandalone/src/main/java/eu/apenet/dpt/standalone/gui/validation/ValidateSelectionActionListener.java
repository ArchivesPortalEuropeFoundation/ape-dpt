package eu.apenet.dpt.standalone.gui.validation;

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.SummaryWorking;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.progress.ProgressFrame;
import eu.apenet.dpt.utils.service.DocumentValidation;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ValidateSelectionActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(ValidateSelectionActionListener.class);
    private ResourceBundle labels;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private Component parent;
    private boolean continueLoop;

    public ValidateSelectionActionListener(ResourceBundle labels, DataPreparationToolGUI dataPreparationToolGUI, Component parent) {
        this.labels = labels;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e){
        continueLoop = true;
//        dataPreparationToolGUI.enableAbortBtn();
        dataPreparationToolGUI.disableAllBtnAndItems();
        dataPreparationToolGUI.getAPEPanel().setFilename("");
        final Object[] objects = dataPreparationToolGUI.getList().getSelectedValues();
        new Thread(new Runnable(){
            public void run(){
                int numberOfFiles = objects.length;
                int currentFileNumberBatch = 0;
                ProgressFrame progressFrame = new ProgressFrame(labels, parent);

                for(Object oneFile : objects){
                    if(!continueLoop)
                        break;

                    ProgressFrame.ApeProgressBar progressBar = progressFrame.addProgressBarToPanel();

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
                    dataPreparationToolGUI.getList().setEnabled(false);

                    Map<String, FileInstance> fileInstances = dataPreparationToolGUI.getFileInstances();
                    FileInstance fileInstance = fileInstances.get(file.getName());
                    try {
                        List<SAXParseException> exceptions;
                        if(fileInstance.isConverted()){
                            InputStream is = FileUtils.openInputStream(new File(fileInstance.getCurrentLocation()));
                            exceptions = DocumentValidation.xmlValidation(is, fileInstance.getValidationSchema());
                        } else {
                            exceptions = DocumentValidation.xmlValidation(FileUtils.openInputStream(file), fileInstance.getValidationSchema());
                        }
                        if (exceptions == null || exceptions.isEmpty()){
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
                        dataPreparationToolGUI.getList().repaint();
                        if(progressBar != null)
                            progressBar.setVisible(false);
                    }
                }
                if(progressFrame != null){
                    progressFrame.setVisible(false);
                    progressFrame.dispose();
                }
                dataPreparationToolGUI.getFinalAct().run();
                dataPreparationToolGUI.getList().clearSelection();
                if(continueLoop)
                    dataPreparationToolGUI.setResultAreaText(labels.getString("finished"));
                else
                    dataPreparationToolGUI.setResultAreaText(labels.getString("aborted"));
                dataPreparationToolGUI.enableSaveAllBtn();
            }
        }).start();
    }

    public void stopLoop() {
        continueLoop = false;
    }
}