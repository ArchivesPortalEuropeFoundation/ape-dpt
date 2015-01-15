package eu.apenet.dpt.standalone.gui.controlaccessanalyzer;

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.utils.util.ListControlaccessTerms;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by yoannmoranville on 15/01/15.
 */
public class AnalyzeControlaccessListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(AnalyzeControlaccessListener.class);
    private ResourceBundle labels;
    private Map<String, FileInstance> fileInstances;
    private Component parent;
    private DataPreparationToolGUI dataPreparationToolGUI;

    public AnalyzeControlaccessListener(ResourceBundle labels, Component parent, Map<String, FileInstance> fileInstances, DataPreparationToolGUI dataPreparationToolGUI) {
        this.labels = labels;
        this.parent = parent;
        this.fileInstances = fileInstances;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
    }

    public void changeLanguage(ResourceBundle labels) {
        this.labels = labels;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("title");
        frame.setPreferredSize(new Dimension(parent.getWidth(), parent.getHeight()));
        frame.setMinimumSize(new Dimension(parent.getWidth(), parent.getHeight()));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        frame.add(panel);
        textArea.setText("Analyzing...");
        frame.setVisible(true);

        List<File> files = new ArrayList<File>();
        for(String key : fileInstances.keySet()) {
            FileInstance fileInstance = fileInstances.get(key);
            files.add(fileInstance.getFile());
        }
        ListControlaccessTerms listControlaccessTerms = new ListControlaccessTerms(files);
        listControlaccessTerms.countTerms();
        String results = listControlaccessTerms.retrieveResults();

        textArea.setText(results);
        textArea.setCaretPosition(0);
    }
}
