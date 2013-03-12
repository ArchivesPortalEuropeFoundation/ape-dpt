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

    private String getStringFromMap(Map<String, List<String>> map) {
        StringBuilder builder = new StringBuilder();
        for(String key : map.keySet()) {
            if(map.get(key).size() > 0) {
                builder.append("--- IDs with ");
                builder.append(key);
                builder.append(" problems ---");
                builder.append("\n");
                for(String value : map.get(key)) {
                    builder.append(value);
                    builder.append("\n");
                }
            }
        }
        return builder.toString();
    }
}
