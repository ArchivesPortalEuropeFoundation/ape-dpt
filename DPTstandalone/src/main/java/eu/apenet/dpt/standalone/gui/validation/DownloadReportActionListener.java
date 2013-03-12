package eu.apenet.dpt.standalone.gui.validation;

import eu.apenet.dpt.standalone.gui.APETabbedPane;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.Utilities;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
        File file = new File(Utilities.LOG_DIR + "report.txt");
        try {
            FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(((File) dataPreparationToolGUI.getXmlEadList().getSelectedValue()).getName());
            FileUtils.writeStringToFile(file, getStringFromMap(fileInstance.getXmlQualityErrors()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private String getStringFromMap(Map<String, Map<String, Boolean>> map) {
        ResourceBundle labels = dataPreparationToolGUI.getLabels();
        StringBuilder builder = new StringBuilder();

        builder.append("----- ");
        builder.append(labels.getString("dataquality.title"));
        builder.append(" -----");
        builder.append("\n");
        builder.append(labels.getString("dataquality.missing.unittitle"));
        builder.append(" ");
        builder.append(Integer.toString(map.get("unittitle").size()));
        builder.append("\n");
        builder.append(labels.getString("dataquality.missing.unitdate"));
        builder.append(" ");
        builder.append(Integer.toString(map.get("unitdate").size()));
        builder.append("\n");
        builder.append(labels.getString("dataquality.missing.dao"));
        builder.append(" ");
        builder.append(Integer.toString(map.get("dao").size()));

        builder.append("\n");
        builder.append("\n");

        for(String key : map.keySet()) {
            if(map.get(key).size() > 0) {
                if(key.equals("unittitle")) {
                    builder.append("--- ").append(labels.getString("dataquality.report.unittitle")).append(" ---");
                } else if(key.equals("unitdate")) {
                    builder.append("--- ").append(labels.getString("dataquality.report.unitdate")).append(" ---");
                } else if(key.equals("dao")) {
                    builder.append("--- ").append(labels.getString("dataquality.report.dao")).append(" ---");
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
