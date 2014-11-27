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
import java.util.ArrayList;
import java.util.List;
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
//        test();
        createRoleTypeForm();
    }

    private void test(){
        super.setTitle(labels.getString("ead.conversion.option.title"));

        JPanel daoPane = new JPanel();
        daoPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
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
        rightsDaoPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        rightsDaoPane.setLayout(new BoxLayout(rightsDaoPane, BoxLayout.PAGE_AXIS));
        final JComboBox comboBoxRightsDao = new JComboBox<String>(RightsInformation.toArray());
        String name = RightsInformation.getRightsInformationFromUrl(retrieveFromDb.retrieveDaoRights()).getName();
        comboBoxRightsDao.setSelectedItem(name);
        final JTextArea descriptionRightsDao = new JTextArea();
        descriptionRightsDao.setText(retrieveFromDb.retrieveDaoRightsDesc());
        descriptionRightsDao.setRows(4);
        final JTextField rightsholderRightsDao = new JTextField();
        rightsholderRightsDao.setText(retrieveFromDb.retrieveDaoRightsHolder());

        rightsDaoPane.add(new JLabel(labels.getString("ead.conversion.option.rights.digital")));
        rightsDaoPane.add(comboBoxRightsDao);
        rightsDaoPane.add(new JLabel(labels.getString("ead.conversion.option.rights.description")));
        rightsDaoPane.add(descriptionRightsDao);
        rightsDaoPane.add(new JLabel(labels.getString("ead.conversion.option.rights.rightsholder")));
        rightsDaoPane.add(rightsholderRightsDao);


        JPanel rightsEadPane = new JPanel();
        rightsEadPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        rightsEadPane.setLayout(new BoxLayout(rightsEadPane, BoxLayout.PAGE_AXIS));
        final JComboBox comboBoxRightsEad = new JComboBox<String>(RightsInformation.toArray());
        name = RightsInformation.getRightsInformationFromUrl(retrieveFromDb.retrieveEadRights()).getName();
        comboBoxRightsEad.setSelectedItem(name);
        final JTextArea descriptionRightsEad = new JTextArea();
        descriptionRightsEad.setText(retrieveFromDb.retrieveEadRightsDesc());
        descriptionRightsEad.setRows(4);
        final JTextField rightsholderRightsEad = new JTextField();
        rightsholderRightsEad.setText(retrieveFromDb.retrieveEadRightsHolder());

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
                retrieveFromDb.saveEadRights(RightsInformation.getRightsInformationFromName((String)comboBoxRightsEad.getSelectedItem()).getUrl(), descriptionRightsEad.getText(), rightsholderRightsEad.getText());
                retrieveFromDb.saveDaoRights(RightsInformation.getRightsInformationFromName((String) comboBoxRightsDao.getSelectedItem()).getUrl(), descriptionRightsDao.getText(), rightsholderRightsDao.getText());
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

//        add(createPane(daoPane, rightsDaoPane, rightsEadPane, buttonPanel));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }
        });
    }

    public void createRoleTypeForm() {
        JPanel result = new JPanel();
        result.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
        GroupLayout layout = new GroupLayout( result );
        result.setLayout( layout );
        layout.setAutoCreateGaps( true );

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
        JLabel label1 = new JLabel("<html><font size='5'>" + labels.getString("ead.conversion.option.dao") + "</font></html>");
        JSeparator separator1 = new JSeparator();

        final JComboBox comboBoxRightsDao = new JComboBox<String>(RightsInformation.toArray());
        String name = RightsInformation.getRightsInformationFromUrl(retrieveFromDb.retrieveDaoRights()).getName();
        comboBoxRightsDao.setSelectedItem(name);
        final JTextArea descriptionRightsDao = new JTextArea();
        descriptionRightsDao.setText(retrieveFromDb.retrieveDaoRightsDesc());
        descriptionRightsDao.setRows(4);
        final JTextField rightsholderRightsDao = new JTextField();
        rightsholderRightsDao.setText(retrieveFromDb.retrieveDaoRightsHolder());
        JLabel label2 = new JLabel("<html><font size='5'>" + labels.getString("ead.conversion.option.rights.digital") + "</font></html>");
        JLabel label3 = new JLabel(labels.getString("ead.conversion.option.rights.description"));
        JLabel label4 = new JLabel(labels.getString("ead.conversion.option.rights.rightsholder"));
        JSeparator separator2 = new JSeparator();

        final JComboBox comboBoxRightsEad = new JComboBox<String>(RightsInformation.toArray());
        name = RightsInformation.getRightsInformationFromUrl(retrieveFromDb.retrieveEadRights()).getName();
        comboBoxRightsEad.setSelectedItem(name);
        final JTextArea descriptionRightsEad = new JTextArea();
        descriptionRightsEad.setText(retrieveFromDb.retrieveEadRightsDesc());
        descriptionRightsEad.setRows(4);
        final JTextField rightsholderRightsEad = new JTextField();
        rightsholderRightsEad.setText(retrieveFromDb.retrieveEadRightsHolder());
        JLabel label5 = new JLabel("<html><font size='5'>" + labels.getString("ead.conversion.option.rights.ead") + "</font></html>");
        JLabel label6 = new JLabel(labels.getString("ead.conversion.option.rights.description"));
        JLabel label7 = new JLabel(labels.getString("ead.conversion.option.rights.rightsholder"));

        layout.setHorizontalGroup( layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(label1)
                                .addComponent(comboBox)
                                .addComponent(useExistingCheckBox)
                                .addComponent(separator1)
                                .addGap(5)
                                .addComponent(label2)
                                .addComponent(comboBoxRightsDao)
                                .addComponent(label3)
                                .addComponent(descriptionRightsDao)
                                .addComponent(label4)
                                .addComponent(rightsholderRightsDao)
                                .addComponent(separator2)
                                .addGap(5)
                                .addComponent(label5)
                                .addComponent(comboBoxRightsEad)
                                .addComponent(label6)
                                .addComponent(descriptionRightsEad)
                                .addComponent(label7)
                                .addComponent(rightsholderRightsEad))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label1))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(comboBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(useExistingCheckBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(separator1))
                        .addGap(5)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label2))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(comboBoxRightsDao))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(descriptionRightsDao))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label4))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(rightsholderRightsDao))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(separator2))
                        .addGap(5)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label5))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(comboBoxRightsEad))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label6))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(descriptionRightsEad))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label7))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(rightsholderRightsEad))
        );


        JButton showItButton = new JButton(labels.getString("ok"));
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String daoType = (String)comboBox.getSelectedItem();
                retrieveFromDb.saveOrUpdateRoleType(daoType, useExistingCheckBox.isSelected());
                retrieveFromDb.saveEadRights(RightsInformation.getRightsInformationFromName((String)comboBoxRightsEad.getSelectedItem()).getUrl(), descriptionRightsEad.getText(), rightsholderRightsEad.getText());
                retrieveFromDb.saveDaoRights(RightsInformation.getRightsInformationFromName((String) comboBoxRightsDao.getSelectedItem()).getUrl(), descriptionRightsDao.getText(), rightsholderRightsDao.getText());
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

        add(createPane(result, buttonPanel));

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

    private JPanel createPane(JPanel result, Component buttonPanel) {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        pane.add(result, BorderLayout.PAGE_START);
        pane.add(buttonPanel, BorderLayout.PAGE_END);
        return pane;
    }

    public enum RightsInformation {
        NO_SELECTION("", "---"),
        PUBLIC_DOMAIN_MARK("http://creativecommons.org/publicdomain/mark/1.0/", "Public Domain Mark"),
        OUT_OF_COPYRIGHT("http://www.europeana.eu/rights/out-of-copyright-non-commercial/", "Out of copyright - no commercial re-use"),
        CREATIVECOMMONS_CC0_PUBLIC("http://creativecommons.org/publicdomain/zero/1.0/", "Creative Commons CC0 Public Domain Dedication"),
        CREATIVECOMMONS_ATTRIBUTION("http://creativecommons.org/licenses/by/4.0/", "Creative Commons Attribution"),
        CREATIVECOMMONS_ATTRIBUTION_SHARE("http://creativecommons.org/licenses/by-sa/4.0/", "Creative Commons Attribution, ShareAlike"),
        CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATES("http://creativecommons.org/licenses/by-nd/4.0/", "Creative Commons Attribution, No Derivatives"),
        CREATIVECOMMONS_ATTRIBUTION_NON_COMERCIAL("http://creativecommons.org/licenses/by-nc/4.0/", "Creative Commons Attribution, Non-Commercial"),
        CREATIVECOMMONS_ATTRIBUTION_NC_SHARE("http://creativecommons.org/licenses/by-nc-sa/4.0/", "Creative Commons Attribution, Non-Commercial, ShareAlike"),
        CREATIVECOMMONS_ATTRIBUTION_NC_NO_DERIVATES("http://creativecommons.org/licenses/by-nc-nd/4.0/", "Creative Commons Attribution, Non-Commercial, No Derivatives"),
        FREE_ACCESS_NO_REUSE("http://www.europeana.eu/rights/rr-f/", "Free access – no re-use"),
        PAID_ACCESS_NO_REUSE("http://www.europeana.eu/rights/rr-p/", "Paid access – no re-use"),
        ORPHAN_WORKS("http://www.europeana.eu/rights/orphan-work-eu/", "Orphan works"),
        UNKNOWN("http://www.europeana.eu/rights/unknown/", "Unknown");

        String url;
        String name;
        RightsInformation(String url, String name) {
            this.url = url;
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public static String[] toArray() {
            List<String> rightsInformationList = new ArrayList<String>();
            for(RightsInformation rightsInformation : RightsInformation.values()) {
                rightsInformationList.add(rightsInformation.getName());
            }
            return rightsInformationList.toArray(new String[rightsInformationList.size()]);
        }

        public static RightsInformation getRightsInformationFromName(String name) {
            for(RightsInformation rightsInformation : RightsInformation.values()) {
                if(rightsInformation.getName().equals(name)) {
                    return rightsInformation;
                }
            }
            return null;
        }

        public static RightsInformation getRightsInformationFromUrl(String url) {
            for(RightsInformation rightsInformation : RightsInformation.values()) {
                if(rightsInformation.getUrl().equals(url)) {
                    return rightsInformation;
                }
            }
            return null;
        }
    }
}
