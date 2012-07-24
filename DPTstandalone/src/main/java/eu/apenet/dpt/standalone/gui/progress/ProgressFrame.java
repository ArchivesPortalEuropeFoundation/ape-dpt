package eu.apenet.dpt.standalone.gui.progress;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 17/04/2012
 *
 * @author Yoann Moranville
 */
public class ProgressFrame extends JFrame {
    private List<ApeProgressBar> apeProgressBars;
    private JPanel progressPanel;
    private JButton abort;

    public ProgressFrame(ResourceBundle labels, Component parent) {
        super(labels.getString("progressTrans"));
        setLayout(new GridLayout(2, 1));

        abort = new JButton(labels.getString("abort"));
        abort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //Abort the current action
            }
        });
        progressPanel = new JPanel(new BorderLayout());

        int width = parent.getWidth();
        int height = parent.getHeight();
        setPreferredSize(new Dimension(width / 2, 100));
        setLocation((width - width/2) / 2, (height - getHeight()) / 2);

        getContentPane().add(progressPanel);
        progressPanel.add(abort, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public List<ApeProgressBar> getApeProgressBars() {
        return apeProgressBars;
    }

    public void stop() {
        for(JProgressBar progressBar : getApeProgressBars()) {
            progressBar.setVisible(false);
        }
        setVisible(false);
        dispose();
    }

    public ApeProgressBar addProgressBarToPanel() {
        ApeProgressBar progressBar = new ApeProgressBar(progressPanel.getWidth());
        progressPanel.add(progressBar, BorderLayout.CENTER);
        if(apeProgressBars == null)
            apeProgressBars = new ArrayList<ApeProgressBar>();
        apeProgressBars.add(progressBar);
        return progressBar;
    }

    public class ApeProgressBar extends JProgressBar {
        public ApeProgressBar(int widthParent) {
            setPreferredSize(new Dimension(widthParent *9/10, 20));
            setIndeterminate(true);
            setStringPainted(true);
        }
    }
}
