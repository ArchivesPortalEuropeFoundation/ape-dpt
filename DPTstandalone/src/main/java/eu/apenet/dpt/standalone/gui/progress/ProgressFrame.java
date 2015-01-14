package eu.apenet.dpt.standalone.gui.progress;

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
    private ApeProgressBar progressBarBatch;
    private ApeProgressBar progressBarSingle;


    public ProgressFrame(ResourceBundle labels, Component parent, boolean isBatch, boolean isValidationOnly, ApexActionListener actionListener) {
        super(labels.getString("progressTrans"));
        setLayout(new GridLayout(1, 1));

        JPanel progressPanel = new JPanel(new GridLayout(3, 1));
        progressPanel.setBorder(BorderFactory.createBevelBorder(1));

        apeProgressBars = new ArrayList<ApeProgressBar>();

        JButton abort = null;
        if(isBatch) {
            final ApexActionListener apexActionListener = actionListener;
            abort = new JButton(labels.getString("abort"));
            abort.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    apexActionListener.abort();
                }
            });

            progressBarBatch = new ApeProgressBar(progressPanel.getWidth());
            progressPanel.add(progressBarBatch);
            apeProgressBars.add(progressBarBatch);
        }

        if(!isValidationOnly) {
            progressBarSingle = new ApeProgressBar(progressPanel.getWidth());
            progressPanel.add(progressBarSingle);
        }

        if(isBatch) {
            JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
            buttonPanel.setBorder(BorderFactory.createBevelBorder(1));
            buttonPanel.add(new JLabel(""));
            buttonPanel.add(abort);
            buttonPanel.add(new JLabel(""));
            progressPanel.add(buttonPanel);
        }

        int width = parent.getWidth();
        int height = parent.getHeight();
        setPreferredSize(new Dimension(width / 2, 100));
        setLocation((width - width/2) / 2, (height - getHeight()) / 2);

        getContentPane().add(progressPanel);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public List<ApeProgressBar> getApeProgressBars() {
        return apeProgressBars;
    }

    public void stop() throws Exception {
        for(JProgressBar progressBar : getApeProgressBars()) {
            progressBar.setVisible(false);
        }
        if(isVisible())
            setVisible(false);
        dispose();
    }

    public ApeProgressBar getProgressBarBatch() {
        return progressBarBatch;
    }

    public ApeProgressBar getProgressBarSingle() {
        return progressBarSingle;
    }

    public class ApeProgressBar extends JProgressBar {
        public ApeProgressBar(int widthParent) {
            setPreferredSize(new Dimension(widthParent *9/10, 30));
            setIndeterminate(true);
            setStringPainted(true);
        }
    }
}
