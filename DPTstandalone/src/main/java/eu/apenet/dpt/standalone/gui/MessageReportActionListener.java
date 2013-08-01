/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Stefan Papp
 */
public class MessageReportActionListener implements ActionListener {

    private final static Logger LOG = Logger.getLogger(MessageReportActionListener.class);
    
    private RetrieveFromDb retrieveFromDb;
    private Map<String, FileInstance> fileInstances;
    private ResourceBundle labels;
    private final Component parent;
    private DataPreparationToolGUI dataPreparationToolGui;

    MessageReportActionListener(RetrieveFromDb retrieveFromDb, DataPreparationToolGUI dataPreparationToolGui, Map<String, FileInstance> fileInstances, ResourceBundle labels, Component parent) {
        this.retrieveFromDb = retrieveFromDb;
        this.dataPreparationToolGui = dataPreparationToolGui;
        this.fileInstances = fileInstances;
        this.labels = labels;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        JList xmlEadList = dataPreparationToolGui.getXmlEadList();
        String defaultOutputDirectory = retrieveFromDb.retrieveDefaultSaveFolder();
        StringBuffer stringBuffer = new StringBuffer();

        if(stringBuffer.toString() != null && stringBuffer.toString().equals(""))
            LOG.info(stringBuffer.toString());
        if (stringBuffer.toString() == null)
            LOG.info("StringBuffer is null");
        if (stringBuffer.toString().equals(""))
            LOG.info("StringBuffer is empty");
        for (Object selectedValue : xmlEadList.getSelectedValues()) {
            File selectedFile = (File) selectedValue;
            String filename = selectedFile.getName();
            FileInstance fileInstance = fileInstances.get(filename);

            //todo: do we really need this?
            filename = filename.startsWith("temp_") ? filename.replace("temp_", "") : filename;

            if (!fileInstance.getConversionErrors().isEmpty() || !fileInstance.getValidationErrors().isEmpty() || !fileInstance.getEuropeanaConversionErrors().isEmpty()) {
                stringBuffer.append("========================================\n");
                stringBuffer.append(filename);
                stringBuffer.append("\n");
                stringBuffer.append("========================================\n");
            }
            if (!fileInstance.getConversionErrors().isEmpty()) {
                stringBuffer.append(fileInstance.getConversionErrors());
                stringBuffer.append("\n");
            }
            if (!fileInstance.getValidationErrors().isEmpty()) {
                stringBuffer.append(fileInstance.getValidationErrors());
                stringBuffer.append("\n");
            }
            if (!fileInstance.getEuropeanaConversionErrors().isEmpty()) {
                stringBuffer.append(fileInstance.getEuropeanaConversionErrors());
                stringBuffer.append("\n");
            }
        }
        try {
            if (stringBuffer.toString().equals(""))
                JOptionPane.showMessageDialog(parent, labels.getString("noReportData"), labels.getString("noReportDataHeader"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
            else {
            FileUtils.writeStringToFile(new File(defaultOutputDirectory + "/report.txt"), stringBuffer.toString());
            JOptionPane.showMessageDialog(parent, MessageFormat.format(labels.getString("filesInOutput"), defaultOutputDirectory) + ".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MessageReportActionListener.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
