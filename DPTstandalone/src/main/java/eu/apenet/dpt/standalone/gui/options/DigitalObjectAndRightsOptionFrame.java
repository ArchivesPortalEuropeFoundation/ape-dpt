package eu.apenet.dpt.standalone.gui.options;

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

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 16/02/2012
 *
 * @author Yoann Moranville
 */
public class DigitalObjectAndRightsOptionFrame extends JFrame {
    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private static boolean inUse = false;

    public DigitalObjectAndRightsOptionFrame(ResourceBundle labels, RetrieveFromDb retrieveFromDb){
        inUse = true;
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
        createRoleTypeForm();
    }

    private void createRoleTypeForm(){
        super.setTitle(labels.getString("ead.conversion.option.title"));

        JPanel daoPane = new JPanel();
        daoPane.setLayout(new BoxLayout(daoPane, BoxLayout.PAGE_AXIS));
        String[] daos = {"TEXT", "IMAGE", "VIDEO", "SOUND", "3D", "UNSPECIFIED"};
        final JComboBox comboBox = new JComboBox<String>(daos);
        String defaultRoleType = retrieveFromDb.retrieveRoleType();
        if(defaultRoleType == null) {
            comboBox.setSelectedIndex(5);
        } else {
            comboBox.setSelectedItem(defaultRoleType);
        }
        final JCheckBox useExistingCheckBox = new JCheckBox(labels.getString("ead.conversion.option.useExistingIfFound"));
        if(retrieveFromDb.retrieveUseExistingRoleType())
            useExistingCheckBox.setSelected(true);

        daoPane.add(new JLabel(labels.getString("ead.conversion.option.dao")));
        daoPane.add(comboBox);
        daoPane.add(useExistingCheckBox);


        JPanel rightsDaoPane = new JPanel();
        rightsDaoPane.setLayout(new BoxLayout(rightsDaoPane, BoxLayout.PAGE_AXIS));
        String[] rights = {"---", "Public Domain Mark", "Out of copyright - no commercial re-use", "Creative Commons CC0 Public Domain Dedication",
                "Creative Commons Attribution", "Creative Commons Attribution, ShareAlike", "Creative Commons Attribution, No Derivatives",
                "Creative Commons Attribution, Non-Commercial", "Creative Commons Attribution, Non-Commercial, ShareAlike",
                "Creative Commons Attribution, Non-Commercial, No Derivatives", "Free access – no re-use",
                "Paid access – no re-use", "Orphan works", "Unknown"};
        JComboBox comboBoxRightsDao = new JComboBox<String>(rights);
        comboBoxRightsDao.setSelectedIndex(0);
        JTextArea descriptionRightsDao = new JTextArea();
        descriptionRightsDao.setRows(4);
        JTextField rightsholderRightsDao = new JTextField();

        rightsDaoPane.add(new JLabel(labels.getString("ead.conversion.option.rights.digital")));
        rightsDaoPane.add(comboBoxRightsDao);
        rightsDaoPane.add(new JLabel(labels.getString("ead.conversion.option.rights.description")));
        rightsDaoPane.add(descriptionRightsDao);
        rightsDaoPane.add(new JLabel(labels.getString("ead.conversion.option.rights.rightsholder")));
        rightsDaoPane.add(rightsholderRightsDao);


        JPanel rightsEadPane = new JPanel();
        rightsEadPane.setLayout(new BoxLayout(rightsEadPane, BoxLayout.PAGE_AXIS));
        JComboBox comboBoxRightsEad = new JComboBox<String>(rights);
        comboBoxRightsEad.setSelectedIndex(0);
        JTextArea descriptionRightsEad = new JTextArea();
        descriptionRightsEad.setRows(4);
        JTextField rightsholderRightsEad = new JTextField();

        rightsEadPane.add(new JLabel(labels.getString("ead.conversion.option.rights.ead")));
        rightsEadPane.add(comboBoxRightsEad);
        rightsEadPane.add(new JLabel(labels.getString("ead.conversion.option.rights.description")));
        rightsEadPane.add(descriptionRightsEad);
        rightsEadPane.add(new JLabel(labels.getString("ead.conversion.option.rights.rightsholder")));
        rightsEadPane.add(rightsholderRightsEad);

        JButton showItButton = new JButton(labels.getString("ok"));
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String daoType = (String)comboBox.getSelectedItem();
                retrieveFromDb.saveOrUpdateRoleType(daoType, useExistingCheckBox.isSelected());
                closeFrame();
            }
        });
        JButton cancelButton = new JButton(labels.getString("cancelBtn"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                closeFrame();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(showItButton);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(cancelButton);
        buttonPanel.add(new JLabel(""));

        add(createPane(daoPane, rightsDaoPane, rightsEadPane, buttonPanel));

        addWindowListener(new WindowAdapter() {
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

    private JPanel createPane(JPanel daoPane, JPanel daoRightsPane, JPanel eadRightsPane, Component buttonPanel) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));

        box.add(daoPane);
        box.add(daoRightsPane);
        box.add(eadRightsPane);

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.PAGE_START);
        pane.add(buttonPanel, BorderLayout.PAGE_END);
        return pane;
    }
}
