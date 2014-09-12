package eu.apenet.dpt.standalone.gui.ead2edm;

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
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.dpt.standalone.gui.APETabbedPane;
import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.SummaryWorking;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.progress.ApexActionListener;
import eu.apenet.dpt.standalone.gui.progress.ProgressFrame;
import eu.apenet.dpt.utils.ead2edm.XMLUtil;
import eu.apenet.dpt.utils.ead2edm.EdmConfig;
import eu.apenet.dpt.utils.util.Ead2EseInformation;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * User: Yoann Moranville Date: 17/11/2011
 *
 * @author Yoann Moranville
 * @author Stefan Papp
 */
public class EdmOptionsPanel extends JPanel {

    /**
     * Serializable.
     */
    private static final long serialVersionUID = -832102808180688015L;
    private static final Logger LOG = Logger.getLogger(EdmOptionsPanel.class);
    private static final String MINIMAL = "minimal";
    private static final String FULL = "full";
    private static final String TEXT = "TEXT";
    private static final String IMAGE = "IMAGE";
    private static final String VIDEO = "VIDEO";
    private static final String SOUND = "SOUND";
    private static final String THREE_D = "3D";
    private static final String YES = "yes";
    private static final String NO = "no";
    private static final String PROVIDE = "provide";
    private static final String CREATIVE_COMMONS = "creative_commons";
    private static final String CREATIVE_COMMONS_CC0 = "creative_commons_cc0";
    private static final String CREATIVE_COMMONS_PUBLIC_DOMAIN_MARK = "creative_commons_public_domain_mark";
    private static final String EUROPEANA_RIGHTS_STATEMENTS = "europeana_rights_statements";
    private static final String OUT_OF_COPYRIGHT = "Out_of_copyright";
    private static final String EMPTY_PANEL = "empty_panel";
    private String archdescRepository = null;
    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private Map<String, FileInstance> fileInstances;
    private List<File> selectedIndices;
    private JFrame parent;
    private APETabbedPane apeTabbedPane;
    private ButtonGroup conversionModeGroup;
    private ButtonGroup typeGroup;
    private ButtonGroup inheritParentGroup;
    private ButtonGroup inheritOriginationGroup;
    private ButtonGroup inheritLanguageGroup;
    private ButtonGroup licenseGroup;
    private ButtonGroup creativeCommonsBtnGrp;
    private JComboBox creativeCommonsComboBox;
    private JComboBox europeanaRightsComboBox;
    private JTextArea contextTextArea;
    private JTextArea dataProviderTextArea;
    private JList languageList;
    private JTextArea additionalRightsTextArea;
    private Map<String, String> languages;
    private static final Border BLACK_LINE = BorderFactory.createLineBorder(Color.BLACK);
    private static final Border GREY_LINE = BorderFactory.createLineBorder(Color.GRAY);
    private DataPreparationToolGUI dataPreparationToolGUI;
    private boolean batch;
    private Ead2EseInformation ead2EseInformation;
    private JCheckBox useExistingRepoCheckbox;
    private JCheckBox useExistingDaoRoleCheckbox;
    private JCheckBox useExistingLanguageCheckbox;
    private JPanel languageBoxPanel = new LanguageBoxPanel();
    private String conversionMode = MINIMAL;
    private JPanel hierarchyPrefixPanel;
    private JPanel inheritParentPanel;
    private JPanel inheritOriginationPanel;
    private JPanel inheritLanguagePanel;
    private JCheckBox hierarchyPrefixCheckbox;
    private JCheckBox inheritParentCheckbox;
    private JCheckBox inheritOriginationCheckbox;
    private JRadioButton inhParYesRadioButton;
    private JRadioButton inhParNoRadioButton;
    private JRadioButton inhOriYesRadioButton;
    private JRadioButton inhOriNoRadioButton;
    private JRadioButton inhLanYesRadioButton;
    private JRadioButton inhLanNoRadioButton;
    private JRadioButton inhLanProvideRadioButton;

    public EdmOptionsPanel(ResourceBundle labels, DataPreparationToolGUI dataPreparationToolGUI, JFrame parent, APETabbedPane apeTabbedPane, boolean batch) {
        super(new BorderLayout());
        this.labels = labels;
        this.retrieveFromDb = new RetrieveFromDb();
        this.parent = parent;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.selectedIndices = setIndices(dataPreparationToolGUI.getXmlEadList().getSelectedValues());
        this.apeTabbedPane = apeTabbedPane;
        this.fileInstances = dataPreparationToolGUI.getFileInstances();
        this.batch = batch;
        createOptionPanel();
    }

    private void createOptionPanel() {
        labels = dataPreparationToolGUI.getLabels();
        JPanel creativeCommonsPanel = new CreativeCommonsPanel();
        JPanel europeanaRightsPanel = new EuropeanaRightsPanel();
        JPanel emptyPanel = new JPanel();

        JPanel formPanel = new JPanel(new GridLayout(10, 1));

        JPanel extraLicenseCardLayoutPanel = new JPanel(new CardLayout());
        extraLicenseCardLayoutPanel.add(creativeCommonsPanel, CREATIVE_COMMONS);
        extraLicenseCardLayoutPanel.add(europeanaRightsPanel, EUROPEANA_RIGHTS_STATEMENTS);
        extraLicenseCardLayoutPanel.add(emptyPanel, EMPTY_PANEL);
        CardLayout cardLayout = (CardLayout) extraLicenseCardLayoutPanel.getLayout();
        cardLayout.show(extraLicenseCardLayoutPanel, EMPTY_PANEL);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        conversionModeGroup = new ButtonGroup();
        JRadioButton radioButton;

        panel.add(new Label(this.labels.getString("edm.panel.label.choose.mode")));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.mode.minimal"));
        radioButton.setActionCommand(MINIMAL);
        radioButton.setSelected(true);
        radioButton.addActionListener(new ConversionModeListener());
        conversionModeGroup.add(radioButton);
        panel.add(radioButton);
        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.mode.full"));
        radioButton.setActionCommand(FULL);
        radioButton.addActionListener(new ConversionModeListener());
        conversionModeGroup.add(radioButton);
        panel.add(radioButton);

        panel.setBorder(BLACK_LINE);
        formPanel.add(panel);

        panel = new JPanel(new GridLayout(1, 1));
        panel.add(new Label(labels.getString("ese.mandatoryFieldsInfo")));
        panel.add(new Label(""));
        panel.setBorder(BLACK_LINE);
        formPanel.add(panel);

        panel = new JPanel(new GridLayout(1, 3));
        panel.add(new Label(labels.getString("ese.dataProvider") + ":" + "*"));
        dataProviderTextArea = new JTextArea();
        dataProviderTextArea.setLineWrap(true);
        dataProviderTextArea.setWrapStyleWord(true);
        JScrollPane dptaScrollPane = new JScrollPane(dataProviderTextArea);
        dptaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(dptaScrollPane);
        useExistingRepoCheckbox = new JCheckBox(labels.getString("ese.takeFromFileRepository"));
        useExistingRepoCheckbox.setSelected(true);
        useExistingRepoCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //empty method on purpose
            }
        });
        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        panel2.add(useExistingRepoCheckbox);
        if (batch) {
            panel.add(panel2);
        } else {
            determineDaoInformation();
            String repository = ead2EseInformation.getRepository();
            if (repository != null && !repository.equals("")) {
                dataProviderTextArea.setText(repository);
                panel.add(panel2);
            } else {
                if (archdescRepository != null) {
                    dataProviderTextArea.setText(archdescRepository);
                    panel.add(panel2);
                } else {
                    useExistingRepoCheckbox.setSelected(false);
                    panel.add(new JLabel());
                }
            }
        }
        panel.setBorder(BLACK_LINE);
        formPanel.add(panel);

        /*        panel = new JPanel(new GridLayout(1, 3));
         panel.add(new Label(labels.getString("ese.provider") + ":" + "*"));
         providerTextArea = new JTextArea();
         providerTextArea.setLineWrap(true);
         providerTextArea.setWrapStyleWord(true);
         JScrollPane ptaScrollPane = new JScrollPane(providerTextArea);
         ptaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         panel.add(ptaScrollPane);
         panel.add(new Label(""));
         panel.setBorder(BLACK_LINE);
         formPanel.add(panel);
         */
        panel = new JPanel(new GridLayout(5, 3));
        typeGroup = new ButtonGroup();

        panel.add(new Label(labels.getString("ese.type") + ":" + "*"));
        String currentRoleType;
        if (batch) {
            currentRoleType = "";
        } else {
            currentRoleType = ead2EseInformation.getRoleType();
        }
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.text"));
        if (currentRoleType.equals(EdmOptionsPanel.TEXT)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(TEXT);
        radioButton.addActionListener(new ConversionModeListener());
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.image"));
        if (currentRoleType.equals(EdmOptionsPanel.IMAGE)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(IMAGE);
        radioButton.addActionListener(new ConversionModeListener());
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.video"));
        if (currentRoleType.equals(EdmOptionsPanel.VIDEO)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(VIDEO);
        radioButton.addActionListener(new ConversionModeListener());
        typeGroup.add(radioButton);
        panel.add(radioButton);

        useExistingDaoRoleCheckbox = new JCheckBox(labels.getString("ese.takeFromFileDaoRole"));
        useExistingDaoRoleCheckbox.setSelected(true);
        useExistingDaoRoleCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            }
        });
        panel.add(useExistingDaoRoleCheckbox);

        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.sound"));
        if (currentRoleType.equals(EdmOptionsPanel.SOUND)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(SOUND);
        radioButton.addActionListener(new ConversionModeListener());
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.threed"));
        if (currentRoleType.equals(EdmOptionsPanel.THREE_D)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(THREE_D);
        radioButton.addActionListener(new ConversionModeListener());
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.setBorder(GREY_LINE);
        formPanel.add(panel);

        if (this.batch) {
            panel = new JPanel(new GridLayout(1, 3));
            panel.add(new Label(labels.getString("ese.selectLanguage") + ":" + "*"));
            panel.add(languageBoxPanel);
            useExistingLanguageCheckbox = new JCheckBox(labels.getString("ese.takeFromFileLanguage"));
            useExistingLanguageCheckbox.setSelected(true);
            useExistingLanguageCheckbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    //empty method on purpose
                }
            });
            panel.add(useExistingLanguageCheckbox);
            panel.setBorder(BLACK_LINE);
            formPanel.add(panel);
        } else {
            inheritLanguagePanel = new JPanel(new GridLayout(1, 3));
            inheritLanguagePanel.add(new Label(labels.getString("ese.inheritLanguage") + ":" + "*"));

            JPanel rbPanel = new JPanel(new GridLayout(4, 1));
            inheritLanguageGroup = new ButtonGroup();
            inhLanYesRadioButton = new JRadioButton(labels.getString("ese.yes"));
            inhLanYesRadioButton.setActionCommand(YES);
            inhLanYesRadioButton.addActionListener(new ChangePanelActionListener(languageBoxPanel));
            inheritLanguageGroup.add(inhLanYesRadioButton);
            rbPanel.add(inhLanYesRadioButton);
            inhLanNoRadioButton = new JRadioButton(labels.getString("ese.no"), true);
            inhLanNoRadioButton.setActionCommand(NO);
            inhLanNoRadioButton.addActionListener(new ChangePanelActionListener(languageBoxPanel));
            inheritLanguageGroup.add(inhLanNoRadioButton);
            rbPanel.add(inhLanNoRadioButton);
            inhLanProvideRadioButton = new JRadioButton(labels.getString("ese.provide"));
            inhLanProvideRadioButton.setActionCommand(PROVIDE);
            inhLanProvideRadioButton.addActionListener(new ChangePanelActionListener(languageBoxPanel));
            inheritLanguageGroup.add(inhLanProvideRadioButton);
            rbPanel.add(inhLanProvideRadioButton);
            useExistingLanguageCheckbox = new JCheckBox(labels.getString("ese.takeFromFileLanguage"));
            useExistingLanguageCheckbox.setSelected(true);
            useExistingLanguageCheckbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    //empty method on purpose
                }
            });
            rbPanel.add(useExistingLanguageCheckbox);
            inheritLanguagePanel.add(rbPanel, BorderLayout.WEST);

            languageBoxPanel.setVisible(true);
            inheritLanguagePanel.add(languageBoxPanel, BorderLayout.EAST);
            inheritLanguagePanel.setBorder(BLACK_LINE);
            inheritLanguagePanel.setVisible(true);
            formPanel.add(inheritLanguagePanel);
        }

        JPanel mainLicensePanel = new JPanel(new BorderLayout());
        panel = new JPanel(new GridLayout(5, 2));
        panel.add(new Label(labels.getString("ese.specifyLicense") + ":" + "*"));
        licenseGroup = new ButtonGroup();
        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.cc"));
        radioButton.setActionCommand(CREATIVE_COMMONS);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.cc.zero"));
        radioButton.setActionCommand(CREATIVE_COMMONS_CC0);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.cc.public"));
        radioButton.setActionCommand(CREATIVE_COMMONS_PUBLIC_DOMAIN_MARK);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.europeana.rights"));
        radioButton.setActionCommand(EUROPEANA_RIGHTS_STATEMENTS);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.out.copyright"));
        radioButton.setActionCommand(EdmOptionsPanel.OUT_OF_COPYRIGHT);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);
        mainLicensePanel.add(panel, BorderLayout.WEST);

        mainLicensePanel.add(extraLicenseCardLayoutPanel, BorderLayout.EAST);
        mainLicensePanel.setBorder(BLACK_LINE);
        formPanel.add(mainLicensePanel);

        panel = new JPanel(new GridLayout(1, 1));
        panel.add(new Label(labels.getString("ese.specifyAdditionalRightsInfo") + ":"));
        additionalRightsTextArea = new JTextArea();
        additionalRightsTextArea.setLineWrap(true);
        additionalRightsTextArea.setWrapStyleWord(true);
        JScrollPane artaScrollPane = new JScrollPane(additionalRightsTextArea);
        artaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(artaScrollPane);
        panel.setBorder(GREY_LINE);
        formPanel.add(panel);

        hierarchyPrefixPanel = new JPanel(new GridLayout(1, 3));
        hierarchyPrefixCheckbox = new JCheckBox(labels.getString("ese.hierarchyPrefix") + ":");
        hierarchyPrefixCheckbox.setSelected(true);
        hierarchyPrefixCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    contextTextArea.setEnabled(true);
                }
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    contextTextArea.setEnabled(false);
                }
            }
        });
        hierarchyPrefixPanel.add(hierarchyPrefixCheckbox);
        contextTextArea = new JTextArea(labels.getString("ese.hierarchyPrefixDefault") + ":");
        contextTextArea.setLineWrap(true);
        contextTextArea.setWrapStyleWord(true);
        JScrollPane ctaScrollPane = new JScrollPane(contextTextArea);
        ctaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        hierarchyPrefixPanel.add(ctaScrollPane);
        hierarchyPrefixPanel.add(new Label(""));
        hierarchyPrefixPanel.setBorder(BLACK_LINE);
        hierarchyPrefixPanel.setVisible(false);
        formPanel.add(hierarchyPrefixPanel);

        inheritParentPanel = new JPanel(new GridLayout(2, 3));
        inheritParentCheckbox = new JCheckBox(labels.getString("ese.inheritParent") + ":" + "*");
        inheritParentCheckbox.setSelected(true);
        inheritParentCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    inhParYesRadioButton.setEnabled(true);
                    inhParNoRadioButton.setEnabled(true);
                }
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    inhParYesRadioButton.setEnabled(false);
                    inhParNoRadioButton.setEnabled(false);
                }
            }
        });
        inheritParentPanel.add(inheritParentCheckbox);
        inheritParentGroup = new ButtonGroup();
        inhParYesRadioButton = new JRadioButton(labels.getString("ese.yes"));
        inhParYesRadioButton.setActionCommand(YES);
        inheritParentGroup.add(inhParYesRadioButton);
        inheritParentPanel.add(inhParYesRadioButton);
        inheritParentPanel.add(new JLabel(""));
        inhParNoRadioButton = new JRadioButton(labels.getString("ese.no"), true);
        inhParNoRadioButton.setActionCommand(NO);
        inheritParentGroup.add(inhParNoRadioButton);
        inheritParentPanel.add(inhParNoRadioButton);
        inheritParentPanel.setBorder(GREY_LINE);
        inheritParentPanel.setVisible(false);
        formPanel.add(inheritParentPanel);

        inheritOriginationPanel = new JPanel(new GridLayout(2, 3));
        inheritOriginationCheckbox = new JCheckBox(labels.getString("ese.inheritOrigination") + ":" + "*");
        inheritOriginationCheckbox.setSelected(true);
        inheritOriginationCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    inhOriYesRadioButton.setEnabled(true);
                    inhOriNoRadioButton.setEnabled(true);
                }
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    inhOriYesRadioButton.setEnabled(false);
                    inhOriNoRadioButton.setEnabled(false);
                }
            }
        });
        inheritOriginationPanel.add(inheritOriginationCheckbox);
        inheritOriginationGroup = new ButtonGroup();
        inhOriYesRadioButton = new JRadioButton(labels.getString("ese.yes"));
        inhOriYesRadioButton.setActionCommand(YES);
        inheritOriginationGroup.add(inhOriYesRadioButton);
        inheritOriginationPanel.add(inhOriYesRadioButton);
        inheritOriginationPanel.add(new JLabel(""));
        inhOriNoRadioButton = new JRadioButton(labels.getString("ese.no"), true);
        inhOriNoRadioButton.setActionCommand(NO);
        inheritOriginationGroup.add(inhOriNoRadioButton);
        inheritOriginationPanel.add(inhOriNoRadioButton);
        inheritOriginationPanel.setBorder(BLACK_LINE);
        inheritOriginationPanel.setVisible(false);
        formPanel.add(inheritOriginationPanel);

        JButton createEseBtn = new JButton(labels.getString("ese.createEseBtn"));
        JButton cancelBtn = new JButton(labels.getString("ese.cancelBtn"));

        createEseBtn.addActionListener(new CreateEdmActionListener());
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Map.Entry<String, FileInstance> entry : fileInstances.entrySet()) {
                    FileInstance fileInstance = entry.getValue();
                    fileInstance.setEse(false);
                }
                if (batch) {
                    dataPreparationToolGUI.enableAllBatchBtns();
                } else {
                    dataPreparationToolGUI.enableEdmConversionBtn();
                }
                dataPreparationToolGUI.enableRadioButtons();
                close();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));

        buttonPanel.add(new JLabel(""));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(createEseBtn);
        buttonPanel.add(new JLabel(""));

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private List<File> setIndices(Object[] indices) {
        List<File> files = new ArrayList<File>();
        for (Object index : indices) {
            if (index instanceof File) {
                files.add((File) index);
            }
        }
        return files;
    }

    public void close() {
        parent.setVisible(false);
        parent.dispose();
    }

    public String[] getAllLanguages() {
        String[] isoLanguages = Locale.getISOLanguages();
        Map<String, String> languagesTemp = new LinkedHashMap<String, String>(isoLanguages.length);
        languages = new LinkedHashMap<String, String>(isoLanguages.length);
        for (String isoLanguage : isoLanguages) {
            languagesTemp.put(new Locale(isoLanguage).getDisplayLanguage(Locale.ENGLISH), isoLanguage);
        }

        List<String> tempList = new LinkedList<String>(languagesTemp.keySet());
        Collections.sort(tempList, String.CASE_INSENSITIVE_ORDER);

        for (String tempLanguage : tempList) {
            languages.put(tempLanguage, languagesTemp.get(tempLanguage));
        }

        return languages.keySet().toArray(new String[]{});
    }

    private EdmConfig fillEdmConfig() {
        EdmConfig config = new EdmConfig();

        config.setUseExistingRepository(false);
        if (useExistingRepoCheckbox.isSelected()) {
            config.setUseExistingRepository(true);
        }

        config.setDataProvider(dataProviderTextArea.getText());
        config.setProvider("Archives Portal Europe");

        Enumeration<AbstractButton> enumeration = typeGroup.getElements();
        boolean found = false;
        while (!found && enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (btn.isSelected()) {
                if (EdmOptionsPanel.TEXT.equals(btn.getActionCommand())) {
                    config.setType(EdmOptionsPanel.TEXT);
                } else if (EdmOptionsPanel.IMAGE.equals(btn.getActionCommand())) {
                    config.setType(EdmOptionsPanel.IMAGE);
                } else if (EdmOptionsPanel.VIDEO.equals(btn.getActionCommand())) {
                    config.setType(EdmOptionsPanel.VIDEO);
                } else if (EdmOptionsPanel.SOUND.equals(btn.getActionCommand())) {
                    config.setType(EdmOptionsPanel.SOUND);
                } else {
                    config.setType(EdmOptionsPanel.THREE_D);
                }
                found = true;
            }
        }

        config.setUseExistingDaoRole(false);
        if (useExistingDaoRoleCheckbox.isSelected()) {
            config.setUseExistingDaoRole(true);
        }

        config.setInheritElementsFromFileLevel(false);
        enumeration = inheritParentGroup.getElements();
        while (enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (FULL.equals(conversionMode) && inheritParentCheckbox.isSelected() && btn.isSelected() && btn.getActionCommand().equals(YES)) {
                config.setInheritElementsFromFileLevel(true);
            }
        }

        config.setInheritOrigination(false);
        enumeration = inheritOriginationGroup.getElements();
        while (enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (FULL.equals(conversionMode) && inheritOriginationCheckbox.isSelected() && btn.isSelected() && btn.getActionCommand().equals(YES)) {
                config.setInheritOrigination(true);
                config.setInheritCustodhist(true);
                config.setInheritAltformavail(true);
                config.setInheritControlaccess(true);
            }
        }

        config.setInheritLanguage(false);
        if (this.batch) {
            StringBuilder result = new StringBuilder();
            Object[] languageValues = languageList.getSelectedValues();
            for (int i = 0; i < languageValues.length; i++) {
                result.append(languages.get(languageValues[i].toString()));
                if (languageValues.length > 0 && i < (languageValues.length - 1)) {
                    result.append(" ");
                }
            }
            config.setLanguage(result.toString());
        } else {
            enumeration = inheritLanguageGroup.getElements();
            while (enumeration.hasMoreElements()) {
                AbstractButton btn = enumeration.nextElement();
                if (/*inheritLanguageCheckbox.isSelected() &&*/btn.isSelected()) {
                    if (btn.getActionCommand().equals(YES)) {
                        config.setInheritLanguage(true);
                    } else if (btn.getActionCommand().equals(PROVIDE)) {
                        StringBuilder result = new StringBuilder();
                        Object[] languageValues = languageList.getSelectedValues();
                        for (int i = 0; i < languageValues.length; i++) {
                            result.append(languages.get(languageValues[i].toString()));
                            if (languageValues.length > 0 && i < (languageValues.length - 1)) {
                                result.append(" ");
                            }
                        }
                        config.setLanguage(result.toString());
                    }
                }
            }
        }
        if (useExistingLanguageCheckbox.isSelected()) {
            config.setUseExistingLanguage(true);
        }

        config.setContextInformationPrefix(labels.getString("ese.hierarchyPrefixDefault") + ":");
        if (FULL.equals(conversionMode) && hierarchyPrefixCheckbox.isSelected() && contextTextArea != null && StringUtils.isNotEmpty(contextTextArea.getText())) {
            config.setContextInformationPrefix(contextTextArea.getText());
        }

        enumeration = licenseGroup.getElements();
        while (enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (btn.isSelected()) {
                config.setRights(getCorrectRights(btn.getActionCommand()));
            }
        }

        if (additionalRightsTextArea != null && StringUtils.isNotEmpty(additionalRightsTextArea.getText())) {
            config.setRightsAdditionalInformation(additionalRightsTextArea.getText());
        }

        if (MINIMAL.equals(conversionMode)) {
            config.setMinimalConversion(true);
        } else {
            config.setMinimalConversion(false);
        }

        //EDM identifier used for OAI-PMH; not needed for DPT purposes, so set to empty string
        config.setEdmIdentifier("");

        //prefixUrl, repositoryCode and xmlTypeName used for EDM element id generation;
        //repositoryCode is taken from the tool while the other two have fixed values.
        config.setHost("http://www.archivesportaleurope.net/web/guest/ead-display/-/ead/fp");
        config.setRepositoryCode(dataPreparationToolGUI.getRepositoryCodeIdentifier());
        config.setXmlTypeName("fa");

        return config;
    }

    private String getCorrectRights(String type) {
        if (type.equals(CREATIVE_COMMONS)) {
            CreativeCommonsType creativeCommonsType = new CreativeCommonsType();
            Enumeration<AbstractButton> enumeration = creativeCommonsBtnGrp.getElements();
            while (enumeration.hasMoreElements()) {
                AbstractButton btn = enumeration.nextElement();
                if (btn.isSelected()) {
                    creativeCommonsType.setBtnChecked(btn.getActionCommand());
                }
            }
            String urlType = creativeCommonsType.getUrlType();
            CreativeCommons creativeCommons = CreativeCommons.getCreativeCommonsByCountryName(creativeCommonsComboBox.getSelectedItem().toString());
            String url = CreativeCommons.constructUrl(creativeCommons);
            return MessageFormat.format(url, urlType);
        } else if (type.equals(EUROPEANA_RIGHTS_STATEMENTS)) {
            String europeanaRights = europeanaRightsComboBox.getSelectedItem().toString();
            if (europeanaRights.equals(this.labels.getString("edm.panel.license.europeana.free"))) {
                return "http://www.europeana.eu/rights/rr-f/";
            } else if (europeanaRights.equals(this.labels.getString("edm.panel.license.europeana.paid"))) {
                return "http://www.europeana.eu/rights/rr-p/";
            } else {
                return "http://www.europeana.eu/rights/orphan-work-eu/";
            }
        } else if (type.equalsIgnoreCase(EdmOptionsPanel.OUT_OF_COPYRIGHT)) {
            return "http://www.europeana.eu/rights/out-of-copyright-non-commercial/";
        } else if (type.equals(CREATIVE_COMMONS_CC0)) {
            return "http://creativecommons.org/publicdomain/zero/1.0/";
        } else {
            return "http://creativecommons.org/publicdomain/mark/1.0/";
        }
    }

    private boolean isRadioBtnChecked(ButtonGroup buttonGroup) {
        Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
        while (enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (btn.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void checkIfAllFilled() throws Exception {
        if (typeGroup == null) {
            throw new Exception("typeGroup is null");
        } else {
            if (!isRadioBtnChecked(typeGroup)) {
                throw new Exception("typeGroup is not checked");
            }
        }

        if (inheritOriginationGroup == null) {
            throw new Exception("inheritOriginationGroup is null");
        } else {
            if (!isRadioBtnChecked(inheritOriginationGroup)) {
                throw new Exception("inheritOriginationGroup is not checked");
            }
        }

        if (inheritParentGroup == null) {
            throw new Exception("inheritParentGroup is null");
        } else if (!isRadioBtnChecked(inheritParentGroup)) {
            throw new Exception("inheritParentGroup is not checked");
        }

        if (!this.batch) {
            if (inheritLanguageGroup == null) {
                throw new Exception("inheritLanguageGroup is null");
            } else if (!isRadioBtnChecked(inheritLanguageGroup)) {
                throw new Exception("inheritLanguageGroup is not checked");
            }

            if (this.inheritLanguageGroup.getSelection().getActionCommand().equalsIgnoreCase(EdmOptionsPanel.PROVIDE)) {
                if (this.languageBoxPanel == null) {
                    throw new Exception("Provided language is null");
                } else if (!((LanguageBoxPanel) this.languageBoxPanel).hasSelections()) {
                    throw new Exception("No provided language selected");
                }
            } else if (this.inheritLanguageGroup.getSelection().getActionCommand().equalsIgnoreCase(EdmOptionsPanel.NO)) {
                if (!this.ead2EseInformation.isLanguagesOnAllCLevels()) {
                    throw new Exception("At least one DAO does not have an associated language. Please inherit the language or provide it manually");
                }
            } else if (this.inheritLanguageGroup.getSelection().getActionCommand().equalsIgnoreCase(EdmOptionsPanel.YES)) {
                if (!this.ead2EseInformation.isLanguagesOnParent()) {
                    throw new Exception("The higher levels do not have an associated language");
                }
            }

        } else {
            if (this.languageBoxPanel == null) {
                throw new Exception("language is null");
            } else if (!((LanguageBoxPanel) this.languageBoxPanel).hasSelections()) {
                throw new Exception("no language selected");
            }
        }

        if (StringUtils.isEmpty(dataProviderTextArea.getText())) {
            if (!useExistingRepoCheckbox.isSelected()) {
                throw new Exception("dataProviderTextField is empty");
            }
        }

        /*        if (StringUtils.isEmpty(providerTextArea.getText())) {
         throw new Exception("providerTextField is empty");
         }
         */
        if (licenseGroup == null) {
            throw new Exception("licenseGroup is null");
        } else if (!isRadioBtnChecked(licenseGroup)) {
            throw new Exception("licenseGroup is not checked");
        }
    }

    public void determineDaoInformation() {
        File index = selectedIndices.get(0);
        FileInstance fileInstance = fileInstances.get(index.getName());
        try {
            if (batch) {
                ead2EseInformation = new Ead2EseInformation();
            } else {
                ead2EseInformation = new Ead2EseInformation(new File(fileInstance.getCurrentLocation()), retrieveFromDb.retrieveRoleType(), archdescRepository);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EdmOptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(EdmOptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(EdmOptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ead2EseInformation.getArchdescRepository() != null) {
            archdescRepository = ead2EseInformation.getArchdescRepository();
        }
    }

    public JPanel getHierarchyPrefixPanel() {
        return hierarchyPrefixPanel;
    }

    public JPanel getInheritParentPanel() {
        return inheritParentPanel;
    }

    public JPanel getInheritOriginationPanel() {
        return inheritOriginationPanel;
    }

    public JPanel getInheritLanguagePanel() {
        return inheritLanguagePanel;
    }

    public class ChangePanelActionListener implements ActionListener {

        private JPanel extraCardLayoutPanel;

        ChangePanelActionListener(JPanel extraCardLayoutPanel) {
            this.extraCardLayoutPanel = extraCardLayoutPanel;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (extraCardLayoutPanel.getLayout() instanceof CardLayout) {
                CardLayout cardLayout = (CardLayout) extraCardLayoutPanel.getLayout();
                if (actionEvent.getActionCommand().equals(CREATIVE_COMMONS_CC0)
                        || actionEvent.getActionCommand().equals(CREATIVE_COMMONS_PUBLIC_DOMAIN_MARK)
                        || actionEvent.getActionCommand().equals(EdmOptionsPanel.OUT_OF_COPYRIGHT)) {
                    cardLayout.show(extraCardLayoutPanel, EMPTY_PANEL);
                } else if (actionEvent.getActionCommand().equals(CREATIVE_COMMONS) || actionEvent.getActionCommand().equals(EUROPEANA_RIGHTS_STATEMENTS)) {
                    cardLayout.show(extraCardLayoutPanel, actionEvent.getActionCommand());
                }
            }
        }
    }

    public class CreateEdmActionListener extends ApexActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
//            dataPreparationToolGUI.disableAllBtnAndItems(); //todo: FIX!
            continueLoop = true;
            final ApexActionListener apexActionListener = this;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StringWriter writer = new StringWriter();

                    int numberOfFiles = selectedIndices.size();
                    int currentFileNumberBatch = 0;

                    try {
                        checkIfAllFilled();
                        ProgressFrame progressFrame = new ProgressFrame(labels, parent, true, true, apexActionListener);
                        ProgressFrame.ApeProgressBar progressBar = progressFrame.getProgressBarBatch();

                        try {
                            apeTabbedPane.setEseConversionErrorText(labels.getString("edm.conversionEseStarted") + "\n");
                            writer.append(labels.getString("edm.conversionEseStarted") + "\n");
                            SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea(), progressBar);
                            summaryWorking.setTotalNumberFiles(numberOfFiles);
                            summaryWorking.setCurrentFileNumberBatch(currentFileNumberBatch);
                            Thread threadRunner = new Thread(summaryWorking);
                            threadRunner.setName(SummaryWorking.class.toString());
                            threadRunner.start();

                            try {
                                EdmConfig edmConfig = fillEdmConfig();
                                for (File selectedIndexFile : selectedIndices) {
                                    if (!continueLoop) {
                                        break;
                                    }
                                    SwingUtilities.invokeLater(new TransformEdm(edmConfig, selectedIndexFile));
                                    apeTabbedPane.appendEseConversionErrorText(MessageFormat.format(labels.getString("edm.convertedAndSaved"), selectedIndexFile.getAbsolutePath(), retrieveFromDb.retrieveDefaultSaveFolder()) + "\n");
                                    writer.append(MessageFormat.format(labels.getString("edm.convertedAndSaved"), selectedIndexFile.getAbsolutePath(), retrieveFromDb.retrieveDefaultSaveFolder()) + "\n");
                                }
                                apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_GREEN_COLOR);
                                close();
                            } catch (Exception ex) {
                                apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_ESE, Utilities.FLASHING_RED_COLOR);
                            } finally {
                                summaryWorking.stop();
                                threadRunner.interrupt();
                            }
                        } catch (Exception e) {
                            LOG.error(e);
                        }
                        progressFrame.stop();
                    } catch (Exception ex1) {
                        if (ex1.getMessage().equals("At least one DAO does not have an associated language. Please inherit the language or provide it manually")) {
                            JOptionPane.showMessageDialog(parent, "At least one DAO does not have an associated language. Please inherit the language or provide it manually.", "Error", JOptionPane.WARNING_MESSAGE);
                        } else {
                            DataPreparationToolGUI.createErrorOrWarningPanel(ex1, false, labels.getString("ese.formNotFilledError"), parent);
                        }
                    }

                    dataPreparationToolGUI.getFinalAct().run();
                    dataPreparationToolGUI.getXmlEadList().clearSelection();
                    if (continueLoop) {
                        dataPreparationToolGUI.setResultAreaText(labels.getString("finished"));
                    } else {
                        dataPreparationToolGUI.setResultAreaText(labels.getString("aborted"));
                    }
                    apeTabbedPane.setEseConversionErrorText(writer.toString());
                    dataPreparationToolGUI.enableSaveBtn();
                    dataPreparationToolGUI.enableRadioButtons();
                }
            }).start();
        }
    }

    private class TransformEdm implements Runnable {

        private EdmConfig config;
        private File selectedIndex;

        TransformEdm(EdmConfig config, File selectedIndex) {
            this.config = config;
            this.selectedIndex = selectedIndex;
        }

        @Override
        public void run() {
            try {
                RetrieveFromDb retrieveFromDb = new RetrieveFromDb();
                int lastIndex = selectedIndex.getName().lastIndexOf('.');
                String xmlOutputFilename = retrieveFromDb.retrieveDefaultSaveFolder() + selectedIndex.getName().substring(0, lastIndex) + "-edm" + selectedIndex.getName().substring(lastIndex);
                FileInstance fileInstance = fileInstances.get(selectedIndex.getName());
                String loc;
                if (fileInstance.isConverted() || fileInstance.getLastOperation().equals(FileInstance.Operation.SAVE)) {
                    loc = fileInstance.getCurrentLocation();
                } else {
                    loc = fileInstance.getOriginalPath();
                }
                File outputFile = new File(xmlOutputFilename);
                config.getTransformerXML2XML().transform(new File(loc), outputFile);
                if (XMLUtil.analyzeESEXML(outputFile).getNumberOfProvidedCHO() <= 1) {
                    apeTabbedPane.appendEseConversionErrorText(labels.getString("ese.fileEmpty"));
                } else {
                    fileInstance.setEseLocation(outputFile.getAbsolutePath());
                    if (StringUtils.isNotEmpty(fileInstance.getEseLocation())) {
                        fileInstance.setMinimalConverted(config.isMinimalConversion());
                    }
                }
            } catch (Exception e) {
                LOG.error("Error when converting file into ESE", e);
            }
        }

    }

    private class CreativeCommonsPanel extends JPanel {

        CreativeCommonsPanel() {
            super(new GridLayout(4, 1));
            creativeCommonsBtnGrp = new ButtonGroup();
            JCheckBox checkBox = new JCheckBox(labels.getString("edm.panel.license.cc.remixing"));
            add(checkBox);
            creativeCommonsBtnGrp.add(checkBox);
            checkBox = new JCheckBox(labels.getString("edm.panel.license.cc.prohibit"));
            add(checkBox);
            creativeCommonsBtnGrp.add(checkBox);
            checkBox = new JCheckBox(labels.getString("edm.panel.license.cc.share"));
            add(checkBox);
            creativeCommonsBtnGrp.add(checkBox);
            creativeCommonsComboBox = new JComboBox(CreativeCommons.getCountryNames().toArray());
            add(creativeCommonsComboBox);
        }
    }

    private class EuropeanaRightsPanel extends JPanel {

        EuropeanaRightsPanel() {
            super(new GridLayout(1, 1));
            String[] rights = {labels.getString("edm.panel.license.europeana.free"), labels.getString("edm.panel.license.europeana.orphan"), labels.getString("edm.panel.license.europeana.paid")};
            europeanaRightsComboBox = new JComboBox(rights);
            add(europeanaRightsComboBox);
        }
    }

    private class LanguageBoxPanel extends JPanel {

        LanguageBoxPanel() {
            super(new GridLayout(1, 1));
            languageList = new JList(getAllLanguages());
            languageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            add(new JScrollPane(languageList));
        }

        public boolean hasSelections() {
            return !languageList.isSelectionEmpty();
        }
    }

    private enum CreativeCommons {

        UNPORTED("Unported", "", "3.0"),
        ARGENTINA("Argentina", "ar", "2.5"),
        AUSTRALIA("Australia", "au", "3.0"),
        AUSTRIA("Austria", "at", "3.0"),
        BELGIUM("Belgium", "be", "2.0"),
        BRAZIL("Brazil", "br", "3.0"),
        BULGARIA("Bulgaria", "bg", "2.5"),
        CANADA("Canada", "ca", "2.5"),
        CHILE("Chile", "cl", "3.0"),
        CHINA_MAINLAND("China Mainland", "cn", "3.0"),
        COLOMBIA("Colombia", "co", "2.5"),
        COSTA_RICA("Costa Rica", "cr", "3.0"),
        CROATIA("Croatia", "hr", "3.0"),
        CZECH_REPUBLIC("Czech Republic", "cz", "3.0"),
        DENMARK("Denmark", "dk", "2.5"),
        ECUADOR("Ecuador", "ec", "3.0"),
        ESTONIA("Estonia", "ee", "3.0"),
        FINLAND("Finland", "fi", "1.0"),
        FRANCE("France", "fr", "3.0"),
        GERMANY("Germany", "de", "3.0"),
        GREECE("Greece", "gr", "3.0"),
        GUATEMALA("Guatemala", "gt", "3.0"),
        HONG_KONG("Hong Kong", "hk", "3.0"),
        HUNGARY("Hungary", "hu", "2.5"),
        INDIA("India", "in", "2.5"),
        IRELAND("Ireland", "ie", "3.0"),
        ISRAEL("Israel", "il", "2.5"),
        ITALY("Italy", "it", "3.0"),
        JAPAN("Japan", "jp", "2.1"),
        LUXEMBOURG("Luxembourg", "lu", "3.0"),
        MACEDONIA("Macedonia", "mk", "2.5"),
        MALAYSIA("Malaysia", "my", "2.5"),
        MALTA("Malta", "mt", "2.5"),
        MEXICO("Mexico", "mx", "2.5"),
        NETHERLANDS("Netherlands", "nl", "3.0"),
        NEW_ZEALAND("New Zealand", "nz", "3.0"),
        NORWAY("Norway", "no", "3.0"),
        PERU("Peru", "pe", "2.5"),
        PHILIPPINES("Philippines", "ph", "3.0"),
        POLAND("Poland", "pl", "3.0"),
        PORTUGAL("Portugal", "pt", "3.0"),
        PUERTO_RICO("Puerto Rico", "pr", "3.0"),
        ROMANIA("Romania", "ro", "3.0"),
        SERBIA("Serbia", "rs", "3.0"),
        SINGAPORE("Singapore", "sg", "3.0"),
        SLOVENIA("Slovenia", "si", "2.5"),
        SOUTH_AFRICA("South Africa", "za", "2.5"),
        SOUTH_KOREA("Korea", "ko", "2.0"),
        SPAIN("Spain", "es", "3.0"),
        SWEDEN("Sweden", "se", "2.5"),
        SWITZERLAND("Switzerland", "ch", "3.0"),
        TAIWAN("Taiwan", "tw", "3.0"),
        THAILAND("Thailand", "th", "3.0"),
        UK_ENGLAND_WALES("UK: England & Wales", "uk", "2.0"),
        UK_SCOTLAND("UK: Scotland", "scotland", "2.5"),
        UGANDA("Uganda", "ug", "3.0"),
        UNITED_STATES("United States", "us", "3.0"),
        VIETNAM("Vietnam", "vn", "3.0");
        private static final String URL_START = "http://creativecommons.org/licenses/{0}/";
        private static final String URL_SEP = "/";
        private String countryName;
        private String countryCode;
        private String version;

        CreativeCommons(String countryName, String countryCode, String version) {
            this.countryName = countryName;
            this.countryCode = countryCode;
            this.version = version;
        }

        public static String constructUrl(CreativeCommons creativeCommons) {
            if (StringUtils.isNotEmpty(creativeCommons.countryCode)) {
                return URL_START + creativeCommons.version + URL_SEP + creativeCommons.countryCode + URL_SEP;
            }
            return URL_START + creativeCommons.version + URL_SEP;
        }

        public static List<String> getCountryNames() {
            List<String> countries = new ArrayList<String>();
            for (CreativeCommons creativeCommons : CreativeCommons.values()) {
                countries.add(creativeCommons.countryName);
            }
            return countries;
        }

        public static CreativeCommons getCreativeCommonsByCountryName(String countryName) {
            for (CreativeCommons creativeCommons : CreativeCommons.values()) {
                if (creativeCommons.countryName.equals(countryName)) {
                    return creativeCommons;
                }
            }
            return null;
        }
    }

    private class CreativeCommonsType {

        private boolean isAllowsRemixing;
        private boolean isProhibitCommercialUse;
        private boolean isRequireShareAlike;

        CreativeCommonsType() {
            isAllowsRemixing = false;
            isProhibitCommercialUse = false;
            isRequireShareAlike = false;
        }

        public void setAllowsRemixing(boolean AllowsRemixing) {
            isAllowsRemixing = AllowsRemixing;
        }

        public void setProhibitCommercialUse(boolean prohibitCommercialUse) {
            isProhibitCommercialUse = prohibitCommercialUse;
        }

        public void setRequireShareAlike(boolean requireShareAlike) {
            isRequireShareAlike = requireShareAlike;
        }

        public void setBtnChecked(String btnName) {
            if (btnName.equals(labels.getString("edm.panel.license.cc.remixing"))) {
                setAllowsRemixing(true);
            } else if (btnName.equals(labels.getString("edm.panel.license.cc.prohibit"))) {
                setProhibitCommercialUse(true);
            } else if (btnName.equals(labels.getString("edm.panel.license.cc.share"))) {
                setRequireShareAlike(true);
            }
        }

        public String getUrlType() {
            //No check: by-nd
            //remix: by
            //remix + prohibit: by-nc
            //remix + prohibit + require: by-nc-sa
            //remix + require: by-sa
            //prohibit: by-nc-nd
            if (isAllowsRemixing) {
                if (isProhibitCommercialUse && !isRequireShareAlike) {
                    return "by-nc";
                } else if (isProhibitCommercialUse && isRequireShareAlike) {
                    return "by-nc-sa";
                } else if (isRequireShareAlike) {
                    return "by-sa";
                } else {
                    return "by";
                }
            } else if (isProhibitCommercialUse) {
                return "by-nc-nd";
            } else {
                return "by-nd";
            }
        }
    }

    private class OptionalCheckboxItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                dataProviderTextArea.setText("");
            }
            if (e.getStateChange() == ItemEvent.SELECTED) {
                dataProviderTextArea.setText(archdescRepository);
            }
        }
    }

    private class ConversionModeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (MINIMAL.equals(e.getActionCommand())) {
                conversionMode = MINIMAL;
                hierarchyPrefixPanel.setVisible(false);
                inheritOriginationPanel.setVisible(false);
                inheritParentPanel.setVisible(false);
            }
            if (FULL.equals(e.getActionCommand())) {
                conversionMode = FULL;
                hierarchyPrefixPanel.setVisible(true);
                inheritOriginationPanel.setVisible(true);
                inheritParentPanel.setVisible(true);
            }
        }
    }
}
