package eu.apenet.dpt.standalone.gui.validation;

import eu.apenet.dpt.standalone.gui.APETabbedPane;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.Utilities;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 05/03/2013
 *
 * @author Yoann Moranville
 */
public class DownloadReportActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(ValidateActionListener.class);

    private DataPreparationToolGUI dataPreparationToolGUI;

    public DownloadReportActionListener(DataPreparationToolGUI dataPreparationToolGUI) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
    }

    public void actionPerformed(ActionEvent e) {
        String defaultSaveLocation = dataPreparationToolGUI.getDefaultSaveLocation();
        File file = new File(defaultSaveLocation + "report.txt");
        try {
            FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(((File) dataPreparationToolGUI.getXmlEadList().getSelectedValue()).getName());
            FileUtils.writeStringToFile(file, getStringFromMap(fileInstance.getXmlQualityErrors()));
            JOptionPane.showMessageDialog(dataPreparationToolGUI.getContentPane(), MessageFormat.format(dataPreparationToolGUI.getLabels().getString("dataquality.reportSaved"), defaultSaveLocation), dataPreparationToolGUI.getLabels().getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
        } catch (IOException e1) {
            LOG.error("Could not save the report.txt file", e1);
            JOptionPane.showMessageDialog(dataPreparationToolGUI.getContentPane(), dataPreparationToolGUI.getLabels().getString("dataquality.reportSavedError"), dataPreparationToolGUI.getLabels().getString("fileSaved"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
        }
    }

    private String getStringFromMap(Map<String, Map<String, Boolean>> map) {
        ResourceBundle labels = dataPreparationToolGUI.getLabels();
        StringBuilder builder = new StringBuilder();

        builder.append("----- ");
        builder.append(labels.getString("dataquality.title"));
        builder.append(" -----");
        builder.append("\n");
        builder.append(MessageFormat.format(labels.getString("dataquality.missing.unittitle"), "(unittitle)"));
        builder.append(" ");
        builder.append(Integer.toString(map.get("unittitle").size()));
        builder.append("\n");
        builder.append(MessageFormat.format(labels.getString("dataquality.missing.unitdate"), "(unitdate@normal)"));
        builder.append(" ");
        builder.append(Integer.toString(map.get("unitdate").size()));
        builder.append("\n");
        builder.append(MessageFormat.format(labels.getString("dataquality.missing.dao"), "(dao@xlink:role)"));
        builder.append(" ");
        builder.append(Integer.toString(map.get("dao").size()));

        builder.append("\n");
        builder.append("\n");

        for(String key : map.keySet()) {
            if(map.get(key).size() > 0) {
                if(key.equals("unittitle")) {
                    builder.append("--- ").append(MessageFormat.format(labels.getString("dataquality.report.unittitle"), "(unittitle)")).append(" ---");
                } else if(key.equals("unitdate")) {
                    builder.append("--- ").append(MessageFormat.format(labels.getString("dataquality.report.unitdate"), "(unitdate@normal)")).append(" ---");
                } else if(key.equals("dao")) {
                    builder.append("--- ").append(MessageFormat.format(labels.getString("dataquality.report.dao"), "(dao@xlink:role)")).append(" ---");
                }
                builder.append("\n");
                for(String id : map.get(key).keySet()) {
                    builder.append(id);
                    if(!map.get(key).get(id)) {
                        builder.append(" (");
                        builder.append(labels.getString("dataquality.report.idxml"));
                        builder.append(")");
                    }
                    builder.append("\n");
                }
            }
        }
        return builder.toString();
    }
}
