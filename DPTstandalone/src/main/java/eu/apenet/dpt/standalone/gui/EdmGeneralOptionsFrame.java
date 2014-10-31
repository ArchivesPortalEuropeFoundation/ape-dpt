/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Stefan Papp
 */
public class EdmGeneralOptionsFrame extends JFrame {

    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private static boolean inUse = false;

    public EdmGeneralOptionsFrame(ResourceBundle labels, RetrieveFromDb retrieveFromDb) {
        inUse = true;
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
        createEdmGeneralOptionsForm();
    }

    private void createEdmGeneralOptionsForm() {
        super.setTitle(labels.getString("edm.generalOptionsForm.title"));

        JRadioButton[] sourceButtons = new JRadioButton[2];
        final ButtonGroup sourceButtonGroup = new ButtonGroup();
        JRadioButton[] landingPageButtons = new JRadioButton[2];
        final ButtonGroup landingPageButtonGroup = new ButtonGroup();
        final JTextField landingPageTf = new JTextField();

        final String unitidOption = "UNITID";
        final String cidOption = "CID";
        final String apeOption = "APE";
        final String otherOption = "OTHER";

        sourceButtons[0] = new JRadioButton(labels.getString("edm.generalOptionsForm.identifierSource.unitid"));
        sourceButtons[0].setActionCommand(unitidOption);
        sourceButtons[1] = new JRadioButton(labels.getString("edm.generalOptionsForm.identifierSource.cid"));
        sourceButtons[1].setActionCommand(cidOption);

        landingPageButtons[0] = new JRadioButton(labels.getString("edm.generalOptionsForm.landingPages.ape"));
        landingPageButtons[0].setActionCommand(apeOption);
        landingPageButtons[1] = new JRadioButton(labels.getString("edm.generalOptionsForm.landingPages.other"));
        landingPageButtons[1].setActionCommand(otherOption);

        String defaultSource = retrieveFromDb.retrieveCIdentifierSource();
        String defaultLandingPage = retrieveFromDb.retrieveLandingPageBase();

        for (JRadioButton radioButton : sourceButtons) {
            sourceButtonGroup.add(radioButton);
            if (defaultSource != null && defaultSource.equals(radioButton.getActionCommand())) {
                radioButton.setSelected(true);
            }
        }
        if (defaultSource == null) {
            sourceButtons[0].setSelected(true);
        }

        for (JRadioButton radioButton : landingPageButtons) {
            landingPageButtonGroup.add(radioButton);
            if (defaultLandingPage != null && defaultLandingPage.equals(radioButton.getActionCommand())) {
                radioButton.setSelected(true);
            }
        }
        if (defaultLandingPage == null) {
            landingPageButtons[0].setSelected(true);
        }

        landingPageButtons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (otherOption.equals(landingPageButtonGroup.getSelection().getActionCommand())) {
                    landingPageTf.setEnabled(true);
                } else {
                    landingPageTf.setEnabled(false);
                }

            }
        });

        JButton OkButton = new JButton(labels.getString("ok"));
        OkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String identifierSource = sourceButtonGroup.getSelection().getActionCommand();
                retrieveFromDb.saveCIdentifierSource(identifierSource);
                String landingPage = landingPageButtonGroup.getSelection().getActionCommand();
                if (landingPage.equals(apeOption)) {
                    retrieveFromDb.saveLandingPageBase("http://www.archivesportaleurope.net");
                }
                if (landingPage.equals(otherOption) && !landingPageTf.getText().isEmpty()) {
                    retrieveFromDb.saveLandingPageBase(landingPageTf.getText());
                }
                closeFrame();
            }
        });
        JButton cancelButton = new JButton(labels.getString("cancelBtn"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeFrame();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(OkButton);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(cancelButton);

        add(createPane(sourceButtons, landingPageButtons, landingPageTf, buttonPanel));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }
        });
    }

    protected void closeFrame() {
        inUse = false;
        this.dispose();
        this.setVisible(false);
    }

    public static boolean isInUse() {
        return inUse;
    }

    private JPanel createPane(JRadioButton[] sourceButtons, JRadioButton[] landingPageButtons, JTextField landingPageTf, Component buttonPanel) {
        JLabel sourceLabel = new JLabel(labels.getString("edm.generalOptionsForm.identifierSource.header"));
        JLabel landingPageLabel = new JLabel(labels.getString("edm.generalOptionsForm.landingPages.header"));

        JPanel sourceBox = new JPanel();
        sourceBox.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        sourceBox.add(sourceLabel, constraints);
        constraints.gridy = 1;
        sourceBox.add(sourceButtons[0], constraints);
        constraints.gridy = 2;
        sourceBox.add(sourceButtons[1], constraints);

        JPanel landingPageBox = new JPanel();
        landingPageBox.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        landingPageBox.add(landingPageLabel, constraints);
        constraints.gridy = 1;
        landingPageBox.add(landingPageButtons[0], constraints);
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        landingPageBox.add(landingPageButtons[1], constraints);
        constraints.gridx = 1;
        landingPageBox.add(landingPageTf, constraints);

        JPanel pane = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(sourceBox, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridy = 2;
        pane.add(landingPageBox, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridy = 4;
        pane.add(buttonPanel, constraints);
        return pane;
    }
}
