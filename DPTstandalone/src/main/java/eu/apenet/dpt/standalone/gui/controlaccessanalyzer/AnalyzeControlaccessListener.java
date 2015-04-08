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
    private JTextArea textArea;
    private ListControlaccessTerms listControlaccessTerms;

    public AnalyzeControlaccessListener(ResourceBundle labels, Component parent, Map<String, FileInstance> fileInstances) {
        this.labels = labels;
        this.parent = parent;
        this.fileInstances = fileInstances;
    }

    public void changeLanguage(ResourceBundle labels) {
        this.labels = labels;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame(labels.getString("listControlaccess"));
        frame.setPreferredSize(new Dimension(parent.getWidth(), parent.getHeight()));
        frame.setMinimumSize(new Dimension(parent.getWidth(), parent.getHeight()));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        frame.add(panel);
        frame.setVisible(true);

        new Worker().execute();
    }

    public class Worker extends SwingWorker<String, String>{

        @Override
        protected String doInBackground() throws Exception {
            publish(labels.getString("analyzing"));
            List<File> files = new ArrayList<File>();
            for(String key : fileInstances.keySet()) {
                FileInstance fileInstance = fileInstances.get(key);
                files.add(fileInstance.getFile());
            }
            listControlaccessTerms = new ListControlaccessTerms(files);
            listControlaccessTerms.countTerms();
            publish(listControlaccessTerms.retrieveResults(labels));
            return null;
        }

        protected void process(List<String> item) {
            textArea.setText(item.get(0));
            textArea.setCaretPosition(0);
        }
    }
}
