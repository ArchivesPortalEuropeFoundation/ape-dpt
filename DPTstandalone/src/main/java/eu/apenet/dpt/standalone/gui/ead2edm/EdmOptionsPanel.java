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
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.EAD2002Utils;
import eu.apenet.dpt.utils.util.Ead2EdmInformation;
import eu.apenet.dpt.utils.util.extendxsl.EdmQualityCheckerCall;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.FileUtils;
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
    private static final String UNITID = "unitid";
    private static final String CID = "cid";
    private static final String ARCHDESC_UNITTITLE = "archdescUnittitle";
    private static final String TITLESTMT_TITLEPROPER = "titlestmtTitleproper";
    private static final String APE = "ape";
    private static final String OTHER = "other";
    private static final String APE_BASE = "http://www.archivesportaleurope.net";
    private static final String TEXT = "TEXT";
    private static final String IMAGE = "IMAGE";
    private static final String VIDEO = "VIDEO";
    private static final String SOUND = "SOUND";
    private static final String THREE_D = "3D";
    private static final String YES = "yes";
    private static final String NO = "no";
    private static final String PROVIDE = "provide";
    private static final String CREATIVE_COMMONS = "creativecommons";
    private static final String CREATIVE_COMMONS_CC0 = "cc0";
    private static final String CREATIVE_COMMONS_PUBLIC_DOMAIN_MARK = "cpdm";
    private static final String EUROPEANA_RIGHTS_STATEMENTS = "europeana";
    private static final String EMPTY_PANEL = "empty_panel";
    private String archdescRepository = null;
    private String archdescUnittitle = null;
    private String titlestmtTitleproper = null;
    private ResourceBundle labels;
    private RetrieveFromDb retrieveFromDb;
    private Map<String, FileInstance> fileInstances;
    private List<File> selectedIndices;
    private JFrame parent;
    private APETabbedPane apeTabbedPane;
    private ButtonGroup cLevelIdSourceButtonGroup;
    private ButtonGroup sourceOfFondsTitleButtonGroup;
    private ButtonGroup landingPageButtonGroup;
    private JTextArea landingPageTextArea;
    private ButtonGroup typeGroup;
    private ButtonGroup concatUnittitleButtonGroup;
    private ButtonGroup licenseGroup;
    private JComboBox creativeCommonsComboBox;
    private JComboBox europeanaRightsComboBox;
    private JTextArea dataProviderTextArea;
    private JList languageList;
    private JTextArea additionalRightsTextArea;
    private Map<String, String> languages;
    private static final Border BLACK_LINE = BorderFactory.createLineBorder(Color.BLACK);
    private static final Border GREY_LINE = BorderFactory.createLineBorder(Color.GRAY);
    private DataPreparationToolGUI dataPreparationToolGUI;
    private boolean batch;
    private Ead2EdmInformation ead2EdmInformation;
    private JCheckBox useExistingRepoCheckbox;
    private JCheckBox useExistingDaoRoleCheckbox;
    private JCheckBox useExistingLanguageMaterialCheckbox;
    private JCheckBox sameLanguageAsMaterialCheckbox;
    private JCheckBox useExistingRightsInfoCheckbox;
    private JPanel languageBoxPanelMaterial = new LanguageBoxPanel();
    private JPanel languageBoxPanelDescription = new LanguageBoxPanel();
    private String sourceOfFondsTitle = ARCHDESC_UNITTITLE;
    private JPanel languageOfMaterialPanel;
    private JPanel languageOfDescriptionPanel;
    private JCheckBox ccRemixingCheckBox;
    private JCheckBox ccProhibitCheckBox;
    private JCheckBox ccShareCheckBox;
    private JCheckBox useExistingLanguageDescriptionCheckbox;

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

        JPanel formPanel = new JPanel(new GridLayout(11, 1));

        JPanel extraLicenseCardLayoutPanel = new JPanel(new CardLayout());
        extraLicenseCardLayoutPanel.add(creativeCommonsPanel, CREATIVE_COMMONS);
        extraLicenseCardLayoutPanel.add(europeanaRightsPanel, EUROPEANA_RIGHTS_STATEMENTS);
        extraLicenseCardLayoutPanel.add(emptyPanel, EMPTY_PANEL);
        CardLayout cardLayout = (CardLayout) extraLicenseCardLayoutPanel.getLayout();
        cardLayout.show(extraLicenseCardLayoutPanel, EMPTY_PANEL);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JRadioButton radioButton;

        cLevelIdSourceButtonGroup = new ButtonGroup();

        panel.add(new Label(this.labels.getString("edm.generalOptionsForm.identifierSource.header")));
        radioButton = new JRadioButton(this.labels.getString("edm.generalOptionsForm.identifierSource.unitid"));
        radioButton.setActionCommand(UNITID);
        if (retrieveFromDb.retrieveCIdentifierSource().equals(radioButton.getActionCommand())) {
            radioButton.setSelected(true);
        }
        cLevelIdSourceButtonGroup.add(radioButton);
        panel.add(radioButton);
        radioButton = new JRadioButton(this.labels.getString("edm.generalOptionsForm.identifierSource.cid"));
        radioButton.setActionCommand(CID);
        if (retrieveFromDb.retrieveCIdentifierSource().equals(radioButton.getActionCommand())) {
            radioButton.setSelected(true);
        }
        cLevelIdSourceButtonGroup.add(radioButton);
        panel.add(radioButton);

        formPanel.add(panel);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sourceOfFondsTitleButtonGroup = new ButtonGroup();

        determineDaoInformation();
        panel.add(new Label(this.labels.getString("edm.generalOptionsForm.sourceOfFondsTitle")));
        if (!this.batch && StringUtils.isBlank(ead2EdmInformation.getArchdescUnittitle()) && StringUtils.isBlank(ead2EdmInformation.getTitlestmtTitleproper())) {
            panel.add(new Label(this.labels.getString("edm.generalOptionsForm.sourceOfFondsTitle.noSourceAvailable")));
        } else {
            radioButton = new JRadioButton(this.labels.getString("edm.generalOptionsForm.sourceOfFondsTitle.archdescUnittitle"));
            radioButton.setActionCommand(ARCHDESC_UNITTITLE);
            radioButton.setSelected(true);
            sourceOfFondsTitleButtonGroup.add(radioButton);
            panel.add(radioButton);
            radioButton = new JRadioButton(this.labels.getString("edm.generalOptionsForm.sourceOfFondsTitle.titlestmtTitleproper"));
            radioButton.setActionCommand(TITLESTMT_TITLEPROPER);
            sourceOfFondsTitleButtonGroup.add(radioButton);
            panel.add(radioButton);
        }
        formPanel.add(panel);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        concatUnittitleButtonGroup = new ButtonGroup();

        panel.add(new Label(this.labels.getString("edm.generalOptionsForm.concatUnittitles.header")));
        radioButton = new JRadioButton(this.labels.getString("ese.yes"));
        radioButton.setActionCommand(YES);
        concatUnittitleButtonGroup.add(radioButton);
        panel.add(radioButton);
        radioButton = new JRadioButton(this.labels.getString("ese.no"));
        radioButton.setActionCommand(NO);
        radioButton.setSelected(true);
        concatUnittitleButtonGroup.add(radioButton);
        panel.add(radioButton);

        formPanel.add(panel);

        panel = new JPanel(new GridLayout(2, 2));
        landingPageButtonGroup = new ButtonGroup();

        panel.add(new Label(this.labels.getString("edm.generalOptionsForm.landingPages.header")));
        radioButton = new JRadioButton(this.labels.getString("edm.generalOptionsForm.landingPages.ape"));
        radioButton.setActionCommand(APE);
        if (retrieveFromDb.retrieveLandingPageBase().equals(APE_BASE)) {
            radioButton.setSelected(true);
        }
        landingPageButtonGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new Label());
        JPanel otherPanel = new JPanel(new GridLayout(1, 2));
        radioButton = new JRadioButton(this.labels.getString("edm.generalOptionsForm.landingPages.other"));
        radioButton.setActionCommand(OTHER);
        landingPageButtonGroup.add(radioButton);
        otherPanel.add(radioButton);
        landingPageTextArea = new JTextArea();
        landingPageTextArea.setLineWrap(true);
        landingPageTextArea.setWrapStyleWord(true);
        if (!retrieveFromDb.retrieveLandingPageBase().equals(APE_BASE)) {
            radioButton.setSelected(true);
            landingPageTextArea.setText(retrieveFromDb.retrieveLandingPageBase());
        }
        JScrollPane lptaScrollPane = new JScrollPane(landingPageTextArea);
        lptaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        otherPanel.add(lptaScrollPane);
        panel.add(otherPanel);

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
        if (!batch) {
            String repository = ead2EdmInformation.getRepository();
            if (repository != null && !repository.equals("")) {
                dataProviderTextArea.setText(repository);
            } else {
                if (archdescRepository != null) {
                    dataProviderTextArea.setText(archdescRepository);
                } else {
                    useExistingRepoCheckbox.setSelected(false);
                }
            }
        }
        panel.add(panel2);
        panel.setBorder(BLACK_LINE);
        formPanel.add(panel);

        panel = new JPanel(new GridLayout(5, 3));
        typeGroup = new ButtonGroup();

        panel.add(new Label(labels.getString("ese.type") + ":" + "*"));
        String currentRoleType;
        if (batch) {
            currentRoleType = "";
        } else {
            currentRoleType = ead2EdmInformation.getRoleType();
        }
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.text"));
        if (currentRoleType.equals(EdmOptionsPanel.TEXT)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(TEXT);
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.image"));
        if (currentRoleType.equals(EdmOptionsPanel.IMAGE)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(IMAGE);
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.video"));
        if (currentRoleType.equals(EdmOptionsPanel.VIDEO)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(VIDEO);
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
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        radioButton = new JRadioButton(this.labels.getString("edm.panel.dao.role.threed"));
        if (currentRoleType.equals(EdmOptionsPanel.THREE_D)) {
            radioButton.setSelected(true);
        }
        radioButton.setActionCommand(THREE_D);
        typeGroup.add(radioButton);
        panel.add(radioButton);
        panel.add(new JLabel(""));

        panel.setBorder(GREY_LINE);
        formPanel.add(panel);

        if (this.batch) {
            panel = new JPanel(new GridLayout(1, 3));
            panel.add(new Label(labels.getString("ese.selectLanguageMaterial") + ":" + "*"));
            panel.add(languageBoxPanelMaterial);
            useExistingLanguageMaterialCheckbox = new JCheckBox(labels.getString("ese.takeFromFileLanguage"));
            useExistingLanguageMaterialCheckbox.setSelected(true);
            useExistingLanguageMaterialCheckbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    //empty method on purpose
                }
            });
            panel.add(useExistingLanguageMaterialCheckbox);
            panel.setBorder(BLACK_LINE);
            formPanel.add(panel);
        } else {
            languageOfMaterialPanel = new JPanel(new GridLayout(1, 3));
            languageOfMaterialPanel.add(new Label(labels.getString("ese.selectLanguageMaterial") + ":" + "*"));

            JPanel rbPanel = new JPanel(new GridLayout(1, 1));
            languageBoxPanelMaterial.setVisible(true);
            languageOfMaterialPanel.add(languageBoxPanelMaterial, BorderLayout.WEST);

            useExistingLanguageMaterialCheckbox = new JCheckBox(labels.getString("ese.takeFromFileLanguage"));
            useExistingLanguageMaterialCheckbox.setSelected(true);
            useExistingLanguageMaterialCheckbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    //empty method on purpose
                }
            });
            rbPanel.add(useExistingLanguageMaterialCheckbox);
            languageOfMaterialPanel.add(rbPanel, BorderLayout.EAST);

            languageOfMaterialPanel.setBorder(BLACK_LINE);
            languageOfMaterialPanel.setVisible(true);
            formPanel.add(languageOfMaterialPanel);
        }

        languageOfDescriptionPanel = new JPanel(new GridLayout(1, 3));
        languageOfDescriptionPanel.add(new Label(labels.getString("ese.selectLanguageDescription") + ":" + "*"));

        JPanel rbPanel = new JPanel(new GridLayout(1, 1));
        languageBoxPanelDescription.setVisible(true);
        languageOfDescriptionPanel.add(languageBoxPanelDescription, BorderLayout.WEST);

        JPanel checkboxPanel = new JPanel(new GridLayout(2, 1));
        sameLanguageAsMaterialCheckbox = new JCheckBox(labels.getString("ese.sameLanguageAsMaterial"));
        sameLanguageAsMaterialCheckbox.setSelected(true);
        sameLanguageAsMaterialCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //empty method on purpose
            }
        });
        checkboxPanel.add(sameLanguageAsMaterialCheckbox);
        useExistingLanguageDescriptionCheckbox = new JCheckBox(labels.getString("ese.takeFromFileLanguage"));
        useExistingLanguageDescriptionCheckbox.setSelected(false);
        useExistingLanguageDescriptionCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //empty method on purpose
            }
        });
        checkboxPanel.add(sameLanguageAsMaterialCheckbox);
        checkboxPanel.add(useExistingLanguageDescriptionCheckbox);
        rbPanel.add(checkboxPanel);
        languageOfDescriptionPanel.add(rbPanel, BorderLayout.EAST);

        languageOfDescriptionPanel.setBorder(BLACK_LINE);
        languageOfDescriptionPanel.setVisible(true);
        formPanel.add(languageOfDescriptionPanel);

        JPanel mainLicensePanel = new JPanel(new GridLayout(1, 4));
        mainLicensePanel.add(new Label(labels.getString("ese.specifyLicense") + ":" + "*"));

        panel = new JPanel(new GridLayout(4, 1));
        licenseGroup = new ButtonGroup();

        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.cc"));
        radioButton.setActionCommand(CREATIVE_COMMONS);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);

        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.cc.zero"));
        radioButton.setActionCommand(CREATIVE_COMMONS_CC0);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);

        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.cc.public"));
        radioButton.setActionCommand(CREATIVE_COMMONS_PUBLIC_DOMAIN_MARK);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);

        radioButton = new JRadioButton(this.labels.getString("edm.panel.label.europeana.rights"));
        radioButton.setActionCommand(EUROPEANA_RIGHTS_STATEMENTS);
        radioButton.addActionListener(new ChangePanelActionListener(extraLicenseCardLayoutPanel));
        licenseGroup.add(radioButton);
        panel.add(radioButton);
        mainLicensePanel.add(panel, BorderLayout.WEST);

        mainLicensePanel.add(extraLicenseCardLayoutPanel, BorderLayout.EAST);

        useExistingRightsInfoCheckbox = new JCheckBox(labels.getString("ese.takeFromFileLicense"));
        useExistingRightsInfoCheckbox.setSelected(true);
        useExistingRightsInfoCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //empty method on purpose
            }
        });
        mainLicensePanel.add(useExistingRightsInfoCheckbox);

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

        JButton createEdmBtn = new JButton(labels.getString("ese.createEseBtn"));
        JButton cancelBtn = new JButton(labels.getString("ese.cancelBtn"));

        createEdmBtn.addActionListener(new CreateEdmActionListener());
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Map.Entry<String, FileInstance> entry : fileInstances.entrySet()) {
                    FileInstance fileInstance = entry.getValue();
                    fileInstance.setEdm(false);
                }
                dataPreparationToolGUI.enableEdmConversionBtn();
                if (batch) {
                    dataPreparationToolGUI.enableAllBatchBtns();
                }
                dataPreparationToolGUI.enableRadioButtons();
                close();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));

        buttonPanel.add(new JLabel(""));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(createEdmBtn);
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

        Enumeration<AbstractButton> enumeration = cLevelIdSourceButtonGroup.getElements();
        boolean found = false;
        while (!found && enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (btn.isSelected()) {
                if (EdmOptionsPanel.UNITID.equals(btn.getActionCommand())) {
                    config.setIdSource(EdmOptionsPanel.UNITID);
                } else if (EdmOptionsPanel.CID.equals(btn.getActionCommand())) {
                    config.setIdSource(EdmOptionsPanel.CID);
                }
                found = true;
            }
        }

        enumeration = sourceOfFondsTitleButtonGroup.getElements();
        found = false;
        while (!found && enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (btn.isSelected()) {
                if (ARCHDESC_UNITTITLE.equals(btn.getActionCommand())) {
                    config.setUseArchUnittitle(true);
                } else {
                    config.setUseArchUnittitle(false);
                }
                found = true;
            }
        }

        enumeration = concatUnittitleButtonGroup.getElements();
        found = false;
        while (!found && enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (btn.isSelected()) {
                if (EdmOptionsPanel.YES.equals(btn.getActionCommand())) {
                    config.setInheritUnittitle(true);
                } else if (EdmOptionsPanel.NO.equals(btn.getActionCommand())) {
                    config.setInheritUnittitle(false);
                }
                found = true;
            }
        }

        enumeration = landingPageButtonGroup.getElements();
        found = false;
        while (!found && enumeration.hasMoreElements()) {
            AbstractButton btn = enumeration.nextElement();
            if (btn.isSelected()) {
                if (EdmOptionsPanel.APE.equals(btn.getActionCommand())) {
                    config.setLandingPage(EdmOptionsPanel.APE);
                } else if (EdmOptionsPanel.OTHER.equals(btn.getActionCommand())) {
                    config.setLandingPage(landingPageTextArea.getText());
                }
                found = true;
            }
        }

        config.setUseExistingRepository(false);
        if (useExistingRepoCheckbox.isSelected()) {
            config.setUseExistingRepository(true);
        }

        config.setDataProvider(dataProviderTextArea.getText());
        config.setProvider("Archives Portal Europe");

        enumeration = typeGroup.getElements();
        found = false;
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

        if (this.batch) {
            if (!useExistingLanguageMaterialCheckbox.isSelected()) {
            }
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
            if (!useExistingLanguageMaterialCheckbox.isSelected()) {
            }
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
        if (useExistingLanguageMaterialCheckbox.isSelected()) {
            config.setUseExistingLanguage(true);
        }

        Enumeration<AbstractButton> licenseEnumeration = licenseGroup.getElements();
        while (licenseEnumeration.hasMoreElements()) {
            AbstractButton btn = licenseEnumeration.nextElement();
            if (btn.isSelected()) {
                config.setRights(getCorrectRights(btn.getActionCommand()));
            }
        }
        if (useExistingRightsInfoCheckbox.isSelected()) {
            config.setUseExistingRightsInfo(true);
        } else {
            config.setUseExistingRightsInfo(false);
        }

        if (additionalRightsTextArea != null && StringUtils.isNotEmpty(additionalRightsTextArea.getText())) {
            config.setRightsAdditionalInformation(additionalRightsTextArea.getText());
        }

        config.setOutputBaseDirectory(retrieveFromDb.retrieveDefaultSaveFolder());

        //EDM identifier used for OAI-PMH; not needed for DPT purposes, so set to empty string
        config.setEdmIdentifier("");

        //prefixUrl, repositoryCode and xmlTypeName used for EDM element id generation;
        //repositoryCode is taken from the tool while the other two have fixed values.
        config.setHost("www.archivesportaleurope.net");
        config.setRepositoryCode(dataPreparationToolGUI.getRepositoryCodeIdentifier());
        config.setXmlTypeName("fa");

        return config;
    }

    private String getCorrectRights(String type) {
        if (type.equals(CREATIVE_COMMONS)) {
            CreativeCommonsType creativeCommonsType = new CreativeCommonsType();

            Set<AbstractButton> ccCheckBoxes = new LinkedHashSet<AbstractButton>();
            ccCheckBoxes.add(ccShareCheckBox);
            ccCheckBoxes.add(ccRemixingCheckBox);
            ccCheckBoxes.add(ccProhibitCheckBox);
            for (AbstractButton ccCheckBox : ccCheckBoxes) {
                if (ccCheckBox.isSelected()) {
                    creativeCommonsType.setBtnChecked(ccCheckBox.getActionCommand());
                }
            }
            String urlType = creativeCommonsType.getUrlType();
            CreativeCommons creativeCommons = CreativeCommons.getCreativeCommonsByCountryName(creativeCommonsComboBox.getSelectedItem().toString());
            String url = CreativeCommons.constructUrl(creativeCommons);
            return MessageFormat.format(url, urlType);
        } else if (type.equals(EUROPEANA_RIGHTS_STATEMENTS)) {
            String europeanaRights = europeanaRightsComboBox.getSelectedItem().toString();
            if (europeanaRights.equals(this.labels.getString("edm.panel.license.europeana.incopyright"))) {
                return "http://rightsstatements.org/vocab/InC/1.0/";
            } else if (europeanaRights.equals(this.labels.getString("edm.panel.license.europeana.incopyright.eduuse"))) {
                return "http://rightsstatements.org/vocab/InC-EDU/1.0/";
            } else if (europeanaRights.equals(this.labels.getString("edm.panel.license.europeana.incopyright.euorphan"))) {
                return "http://rightsstatements.org/vocab/InC-OW-EU/1.0/";
            } else if (europeanaRights.equals(this.labels.getString("edm.panel.license.europeana.nocopyright.noncommercial"))) {
                return "http://rightsstatements.org/vocab/NoC-NC/1.0/";
            } else if (europeanaRights.equals(this.labels.getString("edm.panel.license.europeana.nocopyright.otherlegal"))) {
                return "http://rightsstatements.org/vocab/NoC-OKLR/1.0/";
            } else {
                return "http://rightsstatements.org/vocab/CNE/1.0/";
            }
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
        if (cLevelIdSourceButtonGroup == null) {
            throw new Exception("cLevelIdSource is null");
        } else {
            if (!isRadioBtnChecked(cLevelIdSourceButtonGroup)) {
                throw new Exception("cLevelIdSource is not checked");
            }
        }

        if (!this.batch) {
            if (sourceOfFondsTitleButtonGroup.getSelection() == null) {
                sourceOfFondsTitle = TITLESTMT_TITLEPROPER;
            } else {
                if (sourceOfFondsTitleButtonGroup.getSelection().getActionCommand().equals(ARCHDESC_UNITTITLE)) {
                    if (StringUtils.isBlank(ead2EdmInformation.getArchdescUnittitle())) {
                        if (StringUtils.isNotBlank(ead2EdmInformation.getTitlestmtTitleproper())) {
                            throw new Exception("no content available from archdesc/did/unittile");
                        }
                    }
                }
                if (sourceOfFondsTitleButtonGroup.getSelection().getActionCommand().equals(TITLESTMT_TITLEPROPER)) {
                    if (StringUtils.isBlank(ead2EdmInformation.getTitlestmtTitleproper())) {
                        if (StringUtils.isNotBlank(ead2EdmInformation.getArchdescUnittitle())) {
                            throw new Exception("no content available from titlestmt/titleproper");
                        }
                    }
                }
            }
        }
        if (landingPageButtonGroup == null) {
            throw new Exception("landingPage is null");
        } else {
            if (!isRadioBtnChecked(landingPageButtonGroup)) {
                throw new Exception("landingPage is not checked");
            }
        }
        if (landingPageButtonGroup.getSelection().getActionCommand().equals(OTHER)) {
            if (landingPageTextArea.getText().isEmpty()) {
                throw new Exception("alternative landing page field is empty");
            }
            if (!landingPageTextArea.getText().startsWith("http://")) {
                throw new Exception("alternative landing page does not start with http://");
            }
        }

        if (typeGroup == null) {
            throw new Exception("typeGroup is null");
        } else {
            if (!isRadioBtnChecked(typeGroup)) {
                throw new Exception("typeGroup is not checked");
            }
        }

        if (this.languageBoxPanelMaterial == null) {
            throw new Exception("language is null");
        } else if (!((LanguageBoxPanel) this.languageBoxPanelMaterial).hasSelections()) {
            throw new Exception("no language selected");
        }

        if (StringUtils.isEmpty(dataProviderTextArea.getText())) {
            if (!useExistingRepoCheckbox.isSelected()) {
                throw new Exception("dataProviderTextField is empty");
            }
        }

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
                ead2EdmInformation = new Ead2EdmInformation();
            } else {
                ead2EdmInformation = new Ead2EdmInformation(new File(fileInstance.getCurrentLocation()), retrieveFromDb.retrieveRoleType(), archdescRepository);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EdmOptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(EdmOptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(EdmOptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ead2EdmInformation.getArchdescRepository() != null) {
            archdescRepository = ead2EdmInformation.getArchdescRepository();
        }
        if (ead2EdmInformation.getArchdescUnittitle() != null) {
            archdescUnittitle = ead2EdmInformation.getArchdescUnittitle();
        }
        if (ead2EdmInformation.getTitlestmtTitleproper() != null) {
            titlestmtTitleproper = ead2EdmInformation.getTitlestmtTitleproper();
        }
    }

    public JPanel getLanguageOfMaterialPanel() {
        return languageOfMaterialPanel;
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
                        || actionEvent.getActionCommand().equals(CREATIVE_COMMONS_PUBLIC_DOMAIN_MARK)) {
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
                            apeTabbedPane.setEdmConversionErrorText(labels.getString("edm.conversionEdmStarted") + "\n");
                            SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea(), progressBar);
                            summaryWorking.setTotalNumberFiles(numberOfFiles);
                            summaryWorking.setCurrentFileNumberBatch(currentFileNumberBatch);
                            Thread threadRunner = new Thread(summaryWorking);
                            threadRunner.setName(SummaryWorking.class.toString());
                            threadRunner.start();
                            ExecutorService executor = Executors.newCachedThreadPool();

                            try {
                                EdmConfig edmConfig = fillEdmConfig();
                                boolean hasError = false;
                                for (File selectedIndexFile : selectedIndices) {
                                    if (!continueLoop) {
                                        break;
                                    }
                                    FileInstance fileInstance = fileInstances.get(selectedIndexFile.getName());
                                    Future<?> future = executor.submit(new TransformEdm(edmConfig, selectedIndexFile, fileInstance));
                                    try {
                                        future.get();
                                        File edmFolder = new File(fileInstance.getEdmLocation());
                                        apeTabbedPane.appendEdmConversionErrorText(MessageFormat.format(labels.getString("edm.convertedAndSaved"), selectedIndexFile.getAbsolutePath(), edmFolder.toString()) + "\n");
                                        writer.append(MessageFormat.format(labels.getString("edm.convertedAndSaved"), selectedIndexFile.getAbsolutePath(), edmFolder.toString()) + "\n");

                                        //Do a XML Quality check
                                        EdmQualityCheckerCall edmQualityCheckerCall = new EdmQualityCheckerCall();
                                        File xslFile = Utilities.EDM_QUALITY_FILE;
                                        ArrayList<File> edmFiles = new ArrayList<File>(Arrays.asList(edmFolder.listFiles()));
                                        for (File edmFile : edmFiles) {
                                            TransformationTool.createTransformation(FileUtils.openInputStream(edmFile), null, xslFile, null, true, true, null, false, edmQualityCheckerCall);
                                        }
                                        /* TODO: Update quality report
                                        int duplicateElements = 0;
                                        StringWriter duplicates = new StringWriter();
                                        Map<String, Integer> unitids = edmQualityCheckerCall.getIdentifiers();
                                        for (Map.Entry<String, Integer> unitid : unitids.entrySet()) {
                                            if (unitid.getValue() > 1) {
                                                if (duplicates.getBuffer().length() > 0) {
                                                    duplicates.append(", ");
                                                }
                                                duplicates.append(unitid.getKey());
                                                duplicateElements += unitid.getValue();
                                            }
                                        }

                                        writer.append("\r\n");
                                        writer.append("\r\n");
                                        writer.append("----- ");
                                        writer.append(labels.getString("edm.report.header"));
                                        writer.append(" -----");
                                        writer.append("\r\n");
                                        writer.append(labels.getString("edm.report.note"));
                                        writer.append("\r\n");
                                        writer.append("\r\n");
                                        writer.append(labels.getString("edm.report.identifier.missing"));
                                        writer.append(": ");
                                        writer.append(Integer.toString(edmQualityCheckerCall.getCounterNoUnitid()));
                                        writer.append("\r\n");
                                        writer.append(labels.getString("edm.report.identifier.duplicated.number"));
                                        writer.append(": ");
                                        writer.append(Integer.toString(duplicateElements));
                                        writer.append("\r\n");
                                        writer.append(labels.getString("edm.report.identifier.duplicated.values"));
                                        writer.append(": ");
                                        writer.append(duplicates.toString());
                                        writer.append("\r\n");
                                         */

                                        fileInstance.setEuropeanaConversionErrors(writer.toString());
                                        writer.getBuffer().setLength(0);
                                    } catch (ExecutionException e) {
                                        Throwable yourException = e.getCause();
                                        LOG.info(yourException.getMessage());
                                        apeTabbedPane.appendEdmConversionErrorText(MessageFormat.format(labels.getString("edm.errorOccurred"), selectedIndexFile.getAbsolutePath()) + ": " + yourException.getMessage() + "\n");
                                        writer.append(MessageFormat.format(labels.getString("edm.errorOccurred"), selectedIndexFile.getAbsolutePath()) + ": " + yourException.getMessage() + "\n");
                                        fileInstance.setEuropeanaConversionErrors(writer.toString());
                                        hasError = true;
                                        writer.getBuffer().setLength(0);
                                    } catch (Exception e) {
                                        LOG.error(e);
                                    }
                                }
                                if (hasError) {
                                    apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_EDM, Utilities.FLASHING_RED_COLOR);
                                } else {
                                    apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_EDM, Utilities.FLASHING_GREEN_COLOR);
                                }
                                close();
                            } catch (Exception ex) {
                                apeTabbedPane.checkFlashingTab(APETabbedPane.TAB_EDM, Utilities.FLASHING_RED_COLOR);
                            } finally {
                                summaryWorking.stop();
                                threadRunner.interrupt();
                                executor.shutdownNow();
                                writer.close();
                            }
                        } catch (Exception e) {
                            LOG.error(e);
                        }
                        progressFrame.stop();
                    } catch (Exception ex1) {
                        if (ex1.getMessage().equals("At least one DAO does not have an associated language. Please inherit the language or provide it manually")) {
                            JOptionPane.showMessageDialog(parent, ex1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                        } else if (ex1.getMessage().equals("At least one DAO does not have an associated licence. Please inherit the licence or provide it manually")) {
                            JOptionPane.showMessageDialog(parent, ex1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
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
                    apeTabbedPane.setEdmConversionErrorText(writer.toString());
                    dataPreparationToolGUI.enableSaveBtn();
                    dataPreparationToolGUI.enableRadioButtons();
                }
            }
            ).start();
        }
    }

    private class TransformEdm implements Callable<Void> {

        private EdmConfig config;
        private File selectedIndex;
        private FileInstance fileInstance;

        TransformEdm(EdmConfig config, File selectedIndex, FileInstance fileInstance) {
            this.config = config;
            this.selectedIndex = selectedIndex;
            this.fileInstance = fileInstance;
        }

        @Override
        public Void call() throws Exception {
            try {
                RetrieveFromDb retrieveFromDb = new RetrieveFromDb();
                String eadid = EAD2002Utils.retrieveEadid(selectedIndex.getAbsoluteFile());
                String xmlOutputFolder = retrieveFromDb.retrieveDefaultSaveFolder() + convertToFilename(eadid) + "/";
                String loc;
                if (fileInstance.isConverted() || fileInstance.getLastOperation().equals(FileInstance.Operation.SAVE)) {
                    loc = fileInstance.getCurrentLocation();
                } else {
                    loc = fileInstance.getOriginalPath();
                }
                config.getTransformerXML2XML().transform(new File(loc));
                File outputFolder = new File(xmlOutputFolder);
                if (XMLUtil.countFilesInEDMfolder(outputFolder) <= 1) {
                    apeTabbedPane.appendEdmConversionErrorText(labels.getString("ese.fileEmpty"));
                } else {
                    fileInstance.setEdm(true);
                    fileInstance.setEdmLocation(outputFolder.getAbsolutePath());
                    fileInstance.setEuropeanaConversionErrors(MessageFormat.format(labels.getString("edm.convertedAndSaved"), outputFolder.getAbsolutePath(), retrieveFromDb.retrieveDefaultSaveFolder()) + "\n");
                    fileInstance.setLastOperation(FileInstance.Operation.CONVERT_EDM);
                }
            } catch (TransformerException e) {
                LOG.error("TransformerException when converting file into EDM");
                fileInstance.setEuropeanaConversionErrors(MessageFormat.format(labels.getString("edm.errorOccurred"), selectedIndex.getAbsolutePath()) + ": " + e.getMessage() + "\n");
                throw e;
            } catch (XMLStreamException e) {
                LOG.error("XMLStreamException when converting file into EDM");
                fileInstance.setEuropeanaConversionErrors(MessageFormat.format(labels.getString("edm.errorOccurred"), selectedIndex.getAbsolutePath()) + ": " + e.getMessage() + "\n");
                return null;
            } catch (IOException e) {
                LOG.error("IOException when converting file into EDM");
                fileInstance.setEuropeanaConversionErrors(MessageFormat.format(labels.getString("edm.errorOccurred"), selectedIndex.getAbsolutePath()) + ": " + e.getMessage() + "\n");
                return null;
            }
            return null;
        }
    }

    private class CreativeCommonsPanel extends JPanel {

        CreativeCommonsPanel() {
            super(new GridLayout(4, 1));
            ccRemixingCheckBox = new JCheckBox(labels.getString("edm.panel.license.cc.remixing"));
            ccRemixingCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    if (ccRemixingCheckBox.isSelected()) {
                        ccShareCheckBox.setEnabled(true);
                    } else {
                        ccShareCheckBox.setSelected(false);
                        ccShareCheckBox.setEnabled(false);
                    }
                }
            });
            add(ccRemixingCheckBox);
            ccProhibitCheckBox = new JCheckBox(labels.getString("edm.panel.license.cc.prohibit"));
            add(ccProhibitCheckBox);
            ccShareCheckBox = new JCheckBox(labels.getString("edm.panel.license.cc.share"));
            ccShareCheckBox.setEnabled(false);
            add(ccShareCheckBox);
            creativeCommonsComboBox = new JComboBox(CreativeCommons.getCountryNames().toArray());
            add(creativeCommonsComboBox);
        }

        public boolean isCcRemixingChecked() {
            return ccRemixingCheckBox.isSelected();
        }

        public boolean isCcProhibitChecked() {
            return ccProhibitCheckBox.isSelected();
        }

        public boolean isCcShareChecked() {
            return ccShareCheckBox.isSelected();
        }
    }

    private class EuropeanaRightsPanel extends JPanel {

        EuropeanaRightsPanel() {
            super(new GridLayout(1, 1));
            String[] rights = {labels.getString("edm.panel.license.europeana.incopyright"), labels.getString("edm.panel.license.europeana.incopyright.eduuse"), labels.getString("edm.panel.license.europeana.incopyright.euorphan"), labels.getString("edm.panel.license.europeana.nocopyright.noncommercial"), labels.getString("edm.panel.license.europeana.nocopyright.otherlegal"), labels.getString("edm.panel.license.europeana.copyrightnotevaluated")};
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

    private String convertToFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\-\\.]", "_");
    }
}
