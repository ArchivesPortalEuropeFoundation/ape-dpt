package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public RoleTypeFrame(ResourceBundle labels, RetrieveFromDb retrieveFromDb){
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
                setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(showItButton);
        buttonPanel.add(new JLabel(""));

        add(createPane(labels.getString("chooseType"), radioButtons, useExistingCheckBox, buttonPanel));
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
