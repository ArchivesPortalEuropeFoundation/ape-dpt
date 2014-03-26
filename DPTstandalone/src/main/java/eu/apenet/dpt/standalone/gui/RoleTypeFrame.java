package eu.apenet.dpt.standalone.gui;

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
public class RoleTypeFrame extends JFrame {
    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private static boolean inUse = false;

    public RoleTypeFrame(ResourceBundle labels, RetrieveFromDb retrieveFromDb){
        inUse = true;
        this.labels = labels;
        this.retrieveFromDb = retrieveFromDb;
        createRoleTypeForm();
    }

    private void createRoleTypeForm(){
        super.setTitle(labels.getString("roleFrameTitle"));

        JRadioButton[] radioButtons = new JRadioButton[6];
        final ButtonGroup group = new ButtonGroup();

        final String textType = "TEXT";
        final String imageType = "IMAGE";
        final String videoType = "VIDEO";
        final String audioType = "SOUND";
        final String threeDType = "3D";
        final String unspecifiedType = "UNSPECIFIED";

        final JCheckBox useExistingCheckBox = new JCheckBox(labels.getString("useExistingIfFound"));
        if(retrieveFromDb.retrieveUseExistingRoleType())
            useExistingCheckBox.setSelected(true);

        radioButtons[0] = new JRadioButton(MessageFormat.format(labels.getString("type"), "TEXT"));
        radioButtons[0].setActionCommand(textType);

        radioButtons[1] = new JRadioButton(MessageFormat.format(labels.getString("type"), "IMAGE"));
        radioButtons[1].setActionCommand(imageType);

        radioButtons[2] = new JRadioButton(MessageFormat.format(labels.getString("type"), "VIDEO"));
        radioButtons[2].setActionCommand(videoType);

        radioButtons[3] = new JRadioButton(MessageFormat.format(labels.getString("type"), "SOUND"));
        radioButtons[3].setActionCommand(audioType);

        radioButtons[4] = new JRadioButton(MessageFormat.format(labels.getString("type"), "3D"));
        radioButtons[4].setActionCommand(threeDType);

        radioButtons[5] = new JRadioButton(MessageFormat.format(labels.getString("type"), "UNSPECIFIED"));
        radioButtons[5].setActionCommand(unspecifiedType);

        String defaultRoleType = retrieveFromDb.retrieveRoleType();

        for(JRadioButton radioButton : radioButtons) {
            group.add(radioButton);
            if(defaultRoleType != null && defaultRoleType.equals(radioButton.getActionCommand()))
                radioButton.setSelected(true);
        }
        if(defaultRoleType == null)
            radioButtons[5].setSelected(true);

        JButton showItButton = new JButton(labels.getString("ok"));
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = group.getSelection().getActionCommand();
                retrieveFromDb.saveOrUpdateRoleType(command, useExistingCheckBox.isSelected());
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

        add(createPane(labels.getString("chooseType"), radioButtons, useExistingCheckBox, buttonPanel));

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

    private JPanel createPane(String description, JRadioButton[] radioButtons, JCheckBox checkBox, Component buttonPanel) {
        int numChoices = radioButtons.length;
        JPanel box = new JPanel();
        JLabel label = new JLabel(description);

        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(label);

        box.add(checkBox);

        for (int i = 0; i < numChoices; i++) {
            box.add(radioButtons[i]);
        }

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.PAGE_START);
        pane.add(buttonPanel, BorderLayout.PAGE_END);
        return pane;
    }
}
