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

import eu.apenet.dpt.standalone.gui.adhoc.DirectoryPermission;
import eu.apenet.dpt.standalone.gui.adhoc.FileNameComparator;
import eu.apenet.dpt.standalone.gui.batch.ConvertAndValidateActionListener;
import eu.apenet.dpt.standalone.gui.controlaccessanalyzer.AnalyzeControlaccessListener;
import eu.apenet.dpt.standalone.gui.databasechecker.DatabaseCheckerActionListener;
import eu.apenet.dpt.standalone.gui.dateconversion.DateConversionRulesDialog;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.eaccpf.EacCpfFrame;
import eu.apenet.dpt.standalone.gui.eag2012.Eag2012Frame;
import eu.apenet.dpt.standalone.gui.edition.CheckList;
import eu.apenet.dpt.standalone.gui.edition.PopupMouseListener;
import eu.apenet.dpt.standalone.gui.hgcreation.*;
import eu.apenet.dpt.standalone.gui.options.DigitalObjectAndRightsOptionFrame;
import eu.apenet.dpt.standalone.gui.xsdaddition.XsdObject;
import eu.apenet.dpt.utils.service.ResourceBundlesWrapper;
import eu.apenet.dpt.utils.util.*;
import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * User: Yoann Date: Apr 19, 2010 Time: 8:07:25 PM
 */
public class DataPreparationToolGUI extends JFrame {

    private final static Logger LOG = Logger.getLogger(DataPreparationToolGUI.class);
    public static final String VERSION_NB;

    static {
        if (StringUtils.isEmpty(DataPreparationToolGUI.class.getPackage().getImplementationVersion())) {
            VERSION_NB = "DEV-VERSION";
        } else {
            VERSION_NB = DataPreparationToolGUI.class.getPackage().getImplementationVersion();
        }
    }
    private static final String[] LANGUAGES_OF_TOOL = {"en", "fr", "de", "el", "nl", "hu", "xx"};
    private static final String[] I18N_BASENAMES = {"i18n/apeBundle","i18n/eag2012","i18n/eac-cpf/eac-cpf"};
    private ResourceBundle labels;

    /**
     * Button and titles to be used in the GUI
     */
//    private JButton convertAndValidateBtn = new JButton();
//    private JButton validateSelectionBtn = new JButton();
//    private JButton convertEdmSelectionBtn = new JButton();
    private JButton createHGBtn = new JButton();
    private JButton analyzeControlaccessBtn = new JButton();

    private Cursor WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    private Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);


    /**
     * Tabs to be visible in the GUI
     */
    private APEPanel apePanel;
    private JMenu fileMenu = new JMenu();
    private JMenu optionMenu = new JMenu();
    private JMenu actionMenu = new JMenu();
    private JMenu windowMenu = new JMenu();
    private JMenu languageMenu = new JMenu();
    private JMenu helpMenu = new JMenu();
    private JMenuItem fileItem = new JMenuItem();
    private JMenu createEag2012Item = new JMenu();
    private JMenuItem closeSelectedItem = new JMenuItem();
    private JMenuItem saveSelectedItem = new JMenuItem();
    private JMenuItem saveMessageReportItem = new JMenuItem();
    private JMenuItem sendFilesWebDAV = new JMenuItem();
    private JMenuItem quitItem = new JMenuItem();
    private JMenuItem createEag2012FromExistingEag2012 = new JMenuItem();
    private JMenuItem createEag2012FromScratch = new JMenuItem();
    private JMenuItem repositoryCodeItem = new JMenuItem();
    private JMenuItem countryCodeItem = new JMenuItem();
    private JMenuItem xsltItem = new JMenuItem();
    private JMenuItem xsdItem = new JMenuItem();
    private JMenuItem checksLoadingFilesItem = new JMenuItem();
    private JMenuItem digitalObjectTypeItem = new JMenuItem();
    private JMenuItem defaultSaveFolderItem = new JMenuItem();
    private JMenu defaultXslSelectionSubmenu = new JMenu();
    private JMenu defaultXsdSelectionSubmenu = new JMenu();
    private JMenuItem listDateConversionRulesItem = new JMenuItem();
    private JMenuItem edmGeneralOptionsItem = new JMenuItem();
    private JMenuItem databaseItem = new JMenuItem();
    private JMenuItem validateItem = new JMenuItem();
    private JMenuItem convertItem = new JMenuItem();
    private JMenuItem summaryWindowItem = new JMenuItem();
    private JMenuItem validationWindowItem = new JMenuItem();
    private JMenuItem conversionWindowItem = new JMenuItem();
    private JMenuItem edmConversionWindowItem = new JMenuItem();
    private JMenuItem editionWindowItem = new JMenuItem();
    private JMenuItem internetApexItem = new JMenuItem();
    private JFileChooser fileChooser = new JFileChooser();
    private File currentLocation = null;

    /**
     * EAC-CPF
     */
    private JMenu eacCpfItem = new JMenu();
    private JMenuItem editEacCpfFile = new JMenuItem();
    private JMenuItem createEacCpf = new JMenuItem();

    /**
     * List of files (model)
     */
    private ProfileListModel xmlEadListModel;
    private JList xmlEadList;
    private JLabel xmlEadListLabel;
    private JLabel progressLabel = new JLabel("", JLabel.CENTER);
    private JLabel resultArea = new JLabel();
    private ButtonGroup groupXslt = new ButtonGroup();
    private ButtonGroup groupXsd = new ButtonGroup();

    /**
     * Utilities
     */
    private DateNormalization dateNormalization;
    private JMenuItem deleteFileItem = new JMenuItem();

    /**
     * Locations
     */
    private RetrieveFromDb retrieveFromDb;
    public boolean useExistingRoleType;
    public String defaultRoleType;
    private Map<String, FileInstance> fileInstances = new HashMap<String, FileInstance>();
    private List<String> langList;
    private List<String> levelList;
    private Point from;

    /**
     * ActionListeners
     */
    private CreateHGListener createHgListener;
    private AnalyzeControlaccessListener analyzeControlaccessListener;

    /**
     * For edition
     */
    private JXTreeTable tree;

    private DataPreparationToolGUI() {
        super("");
    }

    private void setupTool() {
        LoggerJAXB.outputJaxpImplementationInfo();
        Locale currentLocale = Locale.getDefault();

        labels = new ResourceBundlesWrapper(I18N_BASENAMES, currentLocale);

        retrieveFromDb = new RetrieveFromDb();
        apePanel = new APEPanel(labels, getContentPane(), this, retrieveFromDb);

        Utilities.setXsdList(fillXsdList());

        CheckList checkList = new CheckList();
        langList = checkList.getLangList();
        levelList = checkList.getLevelList();

        dateNormalization = new DateNormalization();

        super.setTitle(labels.getString("title"));
        Image topLeftIcon = Utilities.icon.getImage();
        setIconImage(topLeftIcon);

        doChecks();

        if (isFileMissing(Utilities.LOG_DIR)) {
            new File(Utilities.LOG_DIR).mkdir();
        }

        File tempDir = new File(Utilities.TEMP_DIR);
        //In case it didn't deleteOnExit at the previous closing of the program, we clean up.
        if (tempDir.exists()) {
            LOG.warn("Probably a problem when deleting the temp files at closure, so we clean up");
            eraseOldTempFiles(tempDir);
            try {
                FileUtils.deleteDirectory(tempDir);
            } catch (IOException e) {
                LOG.error("Could not delete the temp directory. Attempt to delete the directory once more: " + (tempDir.delete() ? "Successful." : "Unsuccessful."));
            }
        }
        tempDir.mkdir();
        tempDir.deleteOnExit();
//        ListControlaccessTerms listControlaccessTerms = new ListControlaccessTerms("/Users/yoannmoranville/Documents/Work/APE/data/AD78/");
//        listControlaccessTerms.countTerms();
//        listControlaccessTerms.displayLogsResults();
//        CountCLevels countCLevels = new CountCLevels("/Users/yoannmoranville/Work/APEnet/Projects/data/Ready_APEnet/READY/Finland/HeNAF/FA_EAD/");
//        CountCLevels countCLevels = new CountCLevels("/Users/yoannmoranville/Work/APEnet/Projects/data/BORA/ALL/");
//        countCLevels.setCopyInAppropriateDirs(true);
//        countCLevels.changeMainagencycodeForSweden(false);
//        countCLevels.countLevels();

//        SeparateFinnishFiles separateFinnishFiles = new SeparateFinnishFiles(new File("/Users/yoannmoranville/Desktop/files_fi"), TEMP_DIR);
        makeDefaultXsdMenuItems();
        makeDefaultXslMenuItems();

        getContentPane().add(apePanel);

        xmlEadListModel = new ProfileListModel(fileInstances, this);
        xmlEadList = new JList(xmlEadListModel);
        xmlEadList.setCellRenderer(new IconListCellRenderer(fileInstances));
        xmlEadList.setDragEnabled(true);

        xmlEadList.setTransferHandler(new ListTransferHandler());

        xmlEadList.setDropTarget(new DropTarget(xmlEadList, new ListDropTargetListener(xmlEadList, from)));
        xmlEadListModel.setList(xmlEadList);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(optionMenu);
        menuBar.add(actionMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        fileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(fileItem);

        closeSelectedItem.setEnabled(false);
//        closeSelectedItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        closeSelectedItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(closeSelectedItem);
        saveSelectedItem.setEnabled(false);
        saveSelectedItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(saveSelectedItem);
        saveMessageReportItem.setEnabled(false);
        saveMessageReportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(saveMessageReportItem);
//        sendFilesWebDAV.setEnabled(false);
//        fileMenu.add(sendFilesWebDAV);
        fileMenu.addSeparator();
        fileMenu.add(quitItem);

        optionMenu.add(countryCodeItem);
        optionMenu.add(repositoryCodeItem);
        optionMenu.add(digitalObjectTypeItem);
        optionMenu.add(edmGeneralOptionsItem);
        optionMenu.add(listDateConversionRulesItem);
        optionMenu.addSeparator();
        optionMenu.add(xsltItem);
        optionMenu.add(xsdItem);
        optionMenu.add(defaultXslSelectionSubmenu);
        optionMenu.add(defaultXsdSelectionSubmenu);
        optionMenu.addSeparator();
        optionMenu.add(checksLoadingFilesItem);
        optionMenu.add(defaultSaveFolderItem);
        optionMenu.add(languageMenu);
        if (Utilities.isDev) {
            optionMenu.addSeparator();
            optionMenu.add(databaseItem);
        }

        validateItem.setEnabled(false);
        validateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        actionMenu.add(validateItem);
        convertItem.setEnabled(false);
        convertItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        actionMenu.add(convertItem);

        actionMenu.addSeparator();

        // TODO: Uncomment when edit will be available.
//        eacCpfItem.add(this.editEacCpfFile);     //add the different EAC-CPF options menu
        eacCpfItem.add(createEacCpf);
        actionMenu.add(eacCpfItem);

        createEag2012Item.add(createEag2012FromExistingEag2012);
        createEag2012Item.add(createEag2012FromScratch);
        actionMenu.add(createEag2012Item);


        summaryWindowItem.setEnabled(true);
        summaryWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(summaryWindowItem);
        validationWindowItem.setEnabled(true);
        validationWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(validationWindowItem);
        conversionWindowItem.setEnabled(true);
        conversionWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(conversionWindowItem);
        edmConversionWindowItem.setEnabled(true);
        edmConversionWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(edmConversionWindowItem);
        editionWindowItem.setEnabled(true);
        editionWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(editionWindowItem);
        helpMenu.add(internetApexItem);
        helpMenu.addSeparator();
        JMenuItem versionItem = new JMenuItem("APE DPT v" + VERSION_NB);
        versionItem.setEnabled(false);
        helpMenu.add(versionItem);
        createLanguageMenu();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);

        getContentPane().add(menuBar, BorderLayout.NORTH);

        apePanel.setFilename("");

        createHgListener = new CreateHGListener(retrieveFromDb, labels, getContentPane(), fileInstances, xmlEadList, this);
        createHGBtn.addActionListener(createHgListener);
        createHGBtn.setEnabled(false);

        analyzeControlaccessListener = new AnalyzeControlaccessListener(labels, getContentPane(), fileInstances);
        analyzeControlaccessBtn.addActionListener(analyzeControlaccessListener);
        analyzeControlaccessBtn.setEnabled(false);

        validateItem.addActionListener(new ConvertAndValidateActionListener(this, apePanel.getApeTabbedPane(), ConvertAndValidateActionListener.VALIDATE));
        convertItem.addActionListener(new ConvertAndValidateActionListener(this, apePanel.getApeTabbedPane(), ConvertAndValidateActionListener.CONVERT));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(createWest(), BorderLayout.WEST);

//        convertAndValidateBtn.addActionListener(new ConvertAndValidateActionListener(this, getContentPane()));
//        validateSelectionBtn.addActionListener(new ValidateSelectionActionListener(this, getContentPane()));
//        convertEdmSelectionBtn.addActionListener(new ConvertEdmActionListener(labels, this, apePanel));

        nameComponents();
        wireUp();
        setSize(1024,768);
//        setSize(Toolkit.getDefaultToolkit().getScreenSize());
//        setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void nameComponents() {
        fileMenu.setText(labels.getString("file"));
        optionMenu.setText(labels.getString("options"));
        actionMenu.setText(labels.getString("actions"));

        fileItem.setText(labels.getString("selectFile"));
        createEag2012Item.setText(labels.getString("createEag2012"));
        createEag2012FromExistingEag2012.setText(labels.getString("menu.createEag2012FromEag2012"));
        createEag2012FromScratch.setText(labels.getString("menu.createEag2012FromScratch"));
        closeSelectedItem.setText(labels.getString("menu.closeSelectedFile"));
        saveSelectedItem.setText(labels.getString("saveSelectedFile"));
        saveMessageReportItem.setText(labels.getString("saveReport"));
        sendFilesWebDAV.setText(labels.getString("sendFilesWebDAV"));
        quitItem.setText(labels.getString("quit"));

        countryCodeItem.setText(labels.getString("countryCode"));
        repositoryCodeItem.setText(labels.getString("repositoryCode"));
        checksLoadingFilesItem.setText(labels.getString("checksLoadingFiles"));
        digitalObjectTypeItem.setText(labels.getString("digitalObjectTypeRights"));
        defaultSaveFolderItem.setText(labels.getString("defaultSaveFolder"));
        xsltItem.setText(labels.getString("ownXsl"));
        xsdItem.setText(labels.getString("ownXsd"));
        databaseItem.setText("Development database");
        defaultXslSelectionSubmenu.setText(labels.getString("defaultXslSelectionSubmenu"));
        defaultXsdSelectionSubmenu.setText(labels.getString("defaultXsdSelectionSubmenu"));
        languageMenu.setText(labels.getString("languageMenu"));
        listDateConversionRulesItem.setText(labels.getString("listDateConversionRules"));
        edmGeneralOptionsItem.setText(labels.getString("edm.generalOptionsForm.menuItem"));

        validateItem.setText(labels.getString("validate"));
        convertItem.setText(labels.getString("convert"));

//        convertAndValidateBtn.setText(labels.getString("convertAndValidate"));
        createHGBtn.setText(labels.getString("createHG"));
        analyzeControlaccessBtn.setText(labels.getString("analyzeControlaccessBtn"));
        progressLabel.setText(labels.getString("chooseFile"));
        deleteFileItem.setText(labels.getString("removeFile"));

        windowMenu.setText(labels.getString("windows"));
        summaryWindowItem.setText(labels.getString("summary"));
        validationWindowItem.setText(labels.getString("validation"));
        conversionWindowItem.setText(labels.getString("conversion"));
        edmConversionWindowItem.setText(labels.getString("eseConversion"));
        editionWindowItem.setText(labels.getString("edition"));

        helpMenu.setText(labels.getString("about"));
        internetApexItem.setText(labels.getString("websiteManuals"));

//        validateSelectionBtn.setText(labels.getString("validateSelected"));
//        convertEdmSelectionBtn.setText(labels.getString("convertEseSelectionBtn"));

        xmlEadListLabel.setText(labels.getString("xmlEadFiles"));

        this.eacCpfItem.setText(labels.getString("eaccpf.eacCpfItem"));      //labels related to EAC-CPF in the menu
        this.editEacCpfFile.setText(labels.getString("eaccpf.menu.editEacCpfFile"));
        this.createEacCpf.setText(labels.getString("eaccpf.menu.createEacCpf"));
    }

    private void changeAllTextLg() {
        nameComponents();

        createHgListener.changeLanguage(labels);
        analyzeControlaccessListener.changeLanguage(labels);
        apePanel.changeLanguage(labels);

        DataPreparationToolGUI.super.setTitle(labels.getString("title"));

        if (xmlEadList.getSelectedValue() == null) {
            apePanel.setFilename("");
        } else {
            apePanel.setFilename(((File) xmlEadList.getSelectedValue()).getName());
        }

        apePanel.getApeTabbedPane().setValidationBtnText(labels.getString("validate"));
    }

    private void createLanguageMenu() {
        Locale currentLocale = Locale.getDefault();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("English");
        rbMenuItem.setActionCommand("en");
        if (!Arrays.asList(LANGUAGES_OF_TOOL).contains(currentLocale.getLanguage()) || currentLocale.getLanguage().equals("en")) {
            rbMenuItem.setSelected(true);
        }

        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Français");
        rbMenuItem.setActionCommand("fr");
        if (currentLocale.getLanguage().equals("fr")) {
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Deutsch");
        rbMenuItem.setActionCommand("de");
        if (currentLocale.getLanguage().equals("de")) {
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Ελληνικά");
        rbMenuItem.setActionCommand("el");
        if (currentLocale.getLanguage().equals("el")) {
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Nederlands");
        rbMenuItem.setActionCommand("nl");
        if (currentLocale.getLanguage().equals("nl")) {
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);


        rbMenuItem = new JRadioButtonMenuItem("Magyar");
        rbMenuItem.setActionCommand("hu");
        if (currentLocale.getLanguage().equals("hu")) {
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        if (Utilities.isDev) {
            languageMenu.addSeparator();
            rbMenuItem = new JRadioButtonMenuItem("XXXXXX");
            rbMenuItem.setActionCommand("xx");
            if (currentLocale.getLanguage().equals("xx")) {
                rbMenuItem.setSelected(true);
            }
            rbMenuItem.addActionListener(new LanguageActionListener());
            group.add(rbMenuItem);
            languageMenu.add(rbMenuItem);
        }
    }

    private void wireUp() {
        fileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == fileItem) {
                    currentLocation = new File(retrieveFromDb.retrieveOpenLocation());
                    fileChooser.setCurrentDirectory(currentLocation);
                    int returnedVal = fileChooser.showOpenDialog(getParent());

                    if (returnedVal == JFileChooser.APPROVE_OPTION) {
                        currentLocation = fileChooser.getCurrentDirectory();
                        retrieveFromDb.saveOpenLocation(currentLocation.getAbsolutePath());

                        RootPaneContainer root = (RootPaneContainer)getRootPane().getTopLevelAncestor();
                        root.getGlassPane().setCursor(WAIT_CURSOR);
                        root.getGlassPane().setVisible(true);

                        File[] files = fileChooser.getSelectedFiles();
                        for (File file : files) {
                            if (file.isDirectory()) {
                                File[] fileArray = file.listFiles();
                                Arrays.sort(fileArray, new FileNameComparator());
                                for (File children : fileArray) {
                                    if (isCorrect(children)) {
                                        xmlEadListModel.addFile(children);
                                    }
                                }
                            } else {
                                if (isCorrect(file)) {
                                    xmlEadListModel.addFile(file);
                                }
                            }
                        }

                        root.getGlassPane().setCursor(DEFAULT_CURSOR);
                        root.getGlassPane().setVisible(false);
                    }
                }
            }
        });
        repositoryCodeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createOptionPaneForRepositoryCode();
            }
        });
        countryCodeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createOptionPaneForCountryCode();
            }
        });
        checksLoadingFilesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                createOptionPaneForChecksLoadingFiles();
            }
        });
        createEag2012FromExistingEag2012.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser eagFileChooser = new JFileChooser();
                eagFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                eagFileChooser.setMultiSelectionEnabled(false);
                eagFileChooser.setCurrentDirectory(new File(retrieveFromDb.retrieveOpenLocation()));
                if (eagFileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    currentLocation = eagFileChooser.getCurrentDirectory();
                    retrieveFromDb.saveOpenLocation(currentLocation.getAbsolutePath());

                    File eagFile = eagFileChooser.getSelectedFile();
                    if (!Eag2012Frame.isUsed()) {
                        try {
                            if (ReadXml.isXmlFile(eagFile, "eag")) {
                                new Eag2012Frame(eagFile, getContentPane().getSize(), (ProfileListModel) getXmlEadList().getModel(), labels);
                            } else {
                                JOptionPane.showMessageDialog(rootPane, labels.getString("eag2012.errors.notAnEagFile"));
                            }
                        } catch (SAXException ex) {
                            if (ex instanceof SAXParseException) {
                                JOptionPane.showMessageDialog(rootPane, labels.getString("eag2012.errors.notAnEagFile"));
                            }
                            java.util.logging.Logger.getLogger(DataPreparationToolGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            java.util.logging.Logger.getLogger(DataPreparationToolGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        } catch (ParserConfigurationException ex) {
                            java.util.logging.Logger.getLogger(DataPreparationToolGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            try {
                                JOptionPane.showMessageDialog(rootPane, labels.getString(ex.getMessage()));
                            } catch (Exception ex1) {
                                JOptionPane.showMessageDialog(rootPane, "Error...");
                            }
                        }
                    }
                }
            }
        });
        createEag2012FromScratch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Eag2012Frame.isUsed()) {
                    new Eag2012Frame(getContentPane().getSize(), (ProfileListModel) getXmlEadList().getModel(), labels, retrieveFromDb.retrieveCountryCode(), retrieveFromDb.retrieveRepositoryCode());
                }
            }
        });
        digitalObjectTypeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!DigitalObjectAndRightsOptionFrame.isInUse()) {
                    JFrame DigitalObjectAndRightsOptionFrame = new DigitalObjectAndRightsOptionFrame(labels, retrieveFromDb);

                    DigitalObjectAndRightsOptionFrame.setPreferredSize(new Dimension(getContentPane().getWidth() * 3 / 8, getContentPane().getHeight() *3 / 4));
                    DigitalObjectAndRightsOptionFrame.setLocation(getContentPane().getWidth() / 8, getContentPane().getHeight() / 8);

                    DigitalObjectAndRightsOptionFrame.pack();
                    DigitalObjectAndRightsOptionFrame.setVisible(true);
                }
            }
        });
        defaultSaveFolderItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser defaultSaveFolderChooser = new JFileChooser();
                defaultSaveFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                defaultSaveFolderChooser.setMultiSelectionEnabled(false);
                defaultSaveFolderChooser.setCurrentDirectory(new File(retrieveFromDb.retrieveDefaultSaveFolder()));
                if (defaultSaveFolderChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File directory = defaultSaveFolderChooser.getSelectedFile();
                    if (directory.canWrite() && DirectoryPermission.canWrite(directory)) {
                        retrieveFromDb.saveDefaultSaveFolder(directory + "/");
                    } else {
                        createErrorOrWarningPanel(new Exception(labels.getString("error.directory.nowrites")), false, labels.getString("error.directory.nowrites"), getContentPane());
                    }
                }
            }
        });
        listDateConversionRulesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JDialog dateConversionRulesDialog = new DateConversionRulesDialog(labels, retrieveFromDb);

                dateConversionRulesDialog.setPreferredSize(new Dimension(getContentPane().getWidth() * 3 / 8, getContentPane().getHeight() * 7 / 8));
                dateConversionRulesDialog.setLocation(getContentPane().getWidth() / 8, getContentPane().getHeight() / 8);

                dateConversionRulesDialog.pack();
                dateConversionRulesDialog.setVisible(true);

            }
        });
        edmGeneralOptionsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!EdmGeneralOptionsFrame.isInUse()) {
                    JFrame edmGeneralOptionsFrame = new EdmGeneralOptionsFrame(labels, retrieveFromDb);

                    edmGeneralOptionsFrame.setPreferredSize(new Dimension(getContentPane().getWidth() * 3 / 8, getContentPane().getHeight() * 3 / 8));
                    edmGeneralOptionsFrame.setLocation(getContentPane().getWidth() / 8, getContentPane().getHeight() / 8);

                    edmGeneralOptionsFrame.pack();
                    edmGeneralOptionsFrame.setVisible(true);
                }
            }
        });
        closeSelectedItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xmlEadListModel.removeFiles(xmlEadList.getSelectedValues());
            }
        });
        saveSelectedItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String defaultOutputDirectory = retrieveFromDb.retrieveDefaultSaveFolder();
                boolean isMultipleFiles = xmlEadList.getSelectedIndices().length > 1;

                RootPaneContainer root = (RootPaneContainer)getRootPane().getTopLevelAncestor();
                root.getGlassPane().setCursor(WAIT_CURSOR);
                root.getGlassPane().setVisible(true);

                for (Object selectedValue : xmlEadList.getSelectedValues()) {
                    File selectedFile = (File) selectedValue;
                    String filename = selectedFile.getName();
                    FileInstance fileInstance = fileInstances.get(filename);
                    String filePrefix = fileInstance.getFileType().getFilePrefix();

                    //todo: do we really need this?
                    filename = filename.startsWith("temp_") ? filename.replace("temp_", "") : filename;

                    filename = !filename.endsWith(".xml") ? filename + ".xml" : filename;

                    if (!fileInstance.isValid()) {
                        filePrefix = "NOT_" + filePrefix;
                    }

                    if (fileInstance.getLastOperation().equals(FileInstance.Operation.EDIT_TREE)) {
                        TreeTableModel treeTableModel = tree.getTreeTableModel();
                        Document document = (Document) treeTableModel.getRoot();
                        try {
                            File file2 = new File(defaultOutputDirectory + filePrefix + "_" + filename);
                            File filetemp = new File(Utilities.TEMP_DIR + "temp_" + filename);
                            TransformerFactory tf = TransformerFactory.newInstance();
                            Transformer output = tf.newTransformer();
                            output.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
                            output.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                            DOMSource domSource = new DOMSource(document.getFirstChild());
                            output.transform(domSource, new StreamResult(filetemp));
                            output.transform(domSource, new StreamResult(file2));

                            fileInstance.setLastOperation(FileInstance.Operation.SAVE);
                            fileInstance.setCurrentLocation(filetemp.getAbsolutePath());
                        } catch (Exception ex) {
                            createErrorOrWarningPanel(ex, true, labels.getString("errorSavingTreeXML"), getContentPane());
                        }
                    } else if (fileInstance.isConverted()) {
                        try {
                            File newFile = new File(defaultOutputDirectory + filePrefix + "_" + filename);
                            FileUtils.copyFile(new File(fileInstance.getCurrentLocation()), newFile);
                            fileInstance.setLastOperation(FileInstance.Operation.SAVE);
//                            fileInstance.setCurrentLocation(newFile.getAbsolutePath());
                        } catch (IOException ioe) {
                            LOG.error("Error when saving file", ioe);
                        }
                    } else {
                        try {
                            File newFile = new File(defaultOutputDirectory + filePrefix + "_" + filename);
                            FileUtils.copyFile(selectedFile, newFile);
                            fileInstance.setLastOperation(FileInstance.Operation.SAVE);
//                            fileInstance.setCurrentLocation(newFile.getAbsolutePath());
                        } catch (IOException ioe) {
                            LOG.error("Error when saving file", ioe);
                        }
                    }
                }

                root.getGlassPane().setCursor(DEFAULT_CURSOR);
                root.getGlassPane().setVisible(false);

                if (isMultipleFiles) {
                    JOptionPane.showMessageDialog(getContentPane(), MessageFormat.format(labels.getString("filesInOutput"), defaultOutputDirectory) + ".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), MessageFormat.format(labels.getString("fileInOutput"), defaultOutputDirectory) + ".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                }
                xmlEadList.updateUI();
            }
        });
        saveMessageReportItem.addActionListener(new MessageReportActionListener(retrieveFromDb, this, fileInstances, labels, this));
        sendFilesWebDAV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        xsltItem.addActionListener(new XsltAdderActionListener(this, labels));
        xsdItem.addActionListener(new XsdAdderActionListener(this, labels, retrieveFromDb));
        if (Utilities.isDev) {
            databaseItem.addActionListener(new DatabaseCheckerActionListener(retrieveFromDb, getContentPane()));
        }
        xmlEadList.addMouseListener(new ListMouseAdapter(xmlEadList, xmlEadListModel, deleteFileItem, this));
        xmlEadList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (xmlEadList.getSelectedValues() != null && xmlEadList.getSelectedValues().length != 0) {
                        if (xmlEadList.getSelectedValues().length > 1) {
//                            convertAndValidateBtn.setEnabled(true);
//                            validateSelectionBtn.setEnabled(true);
//                            if (isValidated(xmlEadList)) {
//                                convertEdmSelectionBtn.setEnabled(true);
//                            } else {
//                                convertEdmSelectionBtn.setEnabled(false);
//                            }
//                            disableAllBtnAndItems();
                            saveMessageReportItem.setEnabled(true);
                            changeInfoInGUI("");
                        } else {
//                            convertAndValidateBtn.setEnabled(false);
//                            validateSelectionBtn.setEnabled(false);
//                            convertEdmSelectionBtn.setEnabled(false);
                            changeInfoInGUI(((File) xmlEadList.getSelectedValue()).getName());
                            if (apePanel.getApeTabbedPane().getSelectedIndex() == APETabbedPane.TAB_EDITION) {
                                apePanel.getApeTabbedPane().createEditionTree(((File) xmlEadList.getSelectedValue()));
                                if (tree != null) {
                                    FileInstance fileInstance = fileInstances.get(((File) getXmlEadList().getSelectedValue()).getName());
                                    tree.addMouseListener(new PopupMouseListener(tree, getDataPreparationToolGUI(), getContentPane(), fileInstance));
                                }
                            }
                            disableTabFlashing();
                        }
                        checkHoldingsGuideButton();
                    } else {
//                        convertAndValidateBtn.setEnabled(false);
//                        validateSelectionBtn.setEnabled(false);
//                        convertEdmSelectionBtn.setEnabled(false);
                        createHGBtn.setEnabled(false);
                        analyzeControlaccessBtn.setEnabled(false);
                        changeInfoInGUI("");
                    }
                }
            }

            private boolean isValidated(JList xmlEadList) {
                for (Object selectedValue : xmlEadList.getSelectedValues()) {
                    File selectedFile = (File) selectedValue;
                    String filename = selectedFile.getName();
                    FileInstance fileInstance = fileInstances.get(filename);
                    if (!fileInstance.isValid()) {
                        return false;
                    }
                }
                return true;
            }
        });

        summaryWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_SUMMARY));
        validationWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_VALIDATION));
        conversionWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_CONVERSION));
        edmConversionWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_EDM));
        editionWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_EDITION));

        internetApexItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                BareBonesBrowserLaunch.openURL("http://wiki.archivesportaleurope.net/index.php/Main_Page");
            }
        });

        /**
         * Option Edit apeEAC-CPF file in the menu
         */

        this.editEacCpfFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser eacFileChooser = new JFileChooser();
                eacFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                eacFileChooser.setMultiSelectionEnabled(false);
                eacFileChooser.setCurrentDirectory(new File(retrieveFromDb.retrieveOpenLocation()));
                if (eacFileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    currentLocation = eacFileChooser.getCurrentDirectory();
                    retrieveFromDb.saveOpenLocation(currentLocation.getAbsolutePath());

                    File eacFile = eacFileChooser.getSelectedFile();
                    if (!EacCpfFrame.isUsed()) {
                        try {
                            if (ReadXml.isXmlFile(eacFile, "eac-cpf")) {
                                new EacCpfFrame(eacFile, true, getContentPane().getSize(), (ProfileListModel) getXmlEadList().getModel(), labels);
                            } else {
                                JOptionPane.showMessageDialog(rootPane, labels.getString("eaccpf.error.notAnEacCpfFile"));
                            }
                        } catch (SAXException ex) {
                            if (ex instanceof SAXParseException) {
                                JOptionPane.showMessageDialog(rootPane, labels.getString("eaccpf.error.notAnEacCpfFile"));
                            }
                            java.util.logging.Logger.getLogger(DataPreparationToolGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            java.util.logging.Logger.getLogger(DataPreparationToolGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        } catch (ParserConfigurationException ex) {
                            java.util.logging.Logger.getLogger(DataPreparationToolGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            try {
                                JOptionPane.showMessageDialog(rootPane, labels.getString(ex.getMessage()));
                            } catch (Exception ex1) {
                                JOptionPane.showMessageDialog(rootPane, "Error...");
                            }
                        }
                    }
                }
            }
        });

        /**
         * Option Create apeEAC-CPF in the menu
         */
        this.createEacCpf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!EacCpfFrame.isUsed()) {
                    EacCpfFrame eacCpfFrame = new EacCpfFrame(getContentPane().getSize(), (ProfileListModel) getXmlEadList().getModel(), labels, retrieveFromDb.retrieveCountryCode(), retrieveFromDb.retrieveRepositoryCode(), null, null, null);
                }
            }
        });

    }

    protected void checkHoldingsGuideButton() {
        boolean isOnlyValidFiles = true;
        for (int i = 0; i < xmlEadList.getSelectedValues().length; i++) {
            FileInstance fileInstance = fileInstances.get(((File) xmlEadList.getSelectedValues()[i]).getName());
            if (fileInstance.getValidationSchema().getFileType().equals(FileInstance.FileType.EAD) && !fileInstance.isValid()) {
                isOnlyValidFiles = false;
                break;
            }
        }
        if (xmlEadList.getSelectedValues().length > 0) {
            if(isOnlyValidFiles) {
                createHGBtn.setEnabled(true);
            }
            analyzeControlaccessBtn.setEnabled(true);
        } else {
            createHGBtn.setEnabled(false);
            analyzeControlaccessBtn.setEnabled(false);
        }
    }

    public DataPreparationToolGUI getDataPreparationToolGUI() {
        return this;
    }

    public String getDefaultSaveLocation() {
        return retrieveFromDb.retrieveDefaultSaveFolder();
    }

    public static void createErrorOrWarningPanel(Throwable e, boolean isError, String message, Component owner) {
        java.util.logging.Level level = java.util.logging.Level.INFO;
        if (isError) {
            LOG.error(message, e);
        } else {
            LOG.info(message, e);
        }
        ErrorInfo errorInfo = new ErrorInfo("Error", message, null, "category", e, level, null);
        JXErrorPane.showDialog(owner, errorInfo);
        //todo: FIX below, should be uncommented
//        if(owner == eagCreationFrame && !owner.hasFocus())
//            eseOptionFrame.setAlwaysOnTop(true);
    }

    private JPanel createWest() {
        JPanel fileLists = new JPanel(new GridLayout(0, 1));
        JPanel xmlEadListPanel = new JPanel(new BorderLayout());
        xmlEadList.setCellRenderer(new IconListCellRenderer(fileInstances));
        xmlEadList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        xmlEadListLabel = new JLabel(labels.getString("xmlEadFiles"));
        xmlEadListPanel.add(new JScrollPane(xmlEadList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        xmlEadListPanel.add(xmlEadListLabel, BorderLayout.NORTH);
        fileLists.add(xmlEadListPanel);
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(200, 100));
        p.add(fileLists);
        p.add(createSouthWest(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel createSouthWest() {
        JPanel p = new JPanel(new GridLayout(0, 1));
        p.add(progressLabel);
//        convertAndValidateBtn.setPreferredSize(new Dimension(-1, 40)); //width max
//        convertAndValidateBtn.setEnabled(false);
//        p.add(convertAndValidateBtn);
//        validateSelectionBtn.setPreferredSize(new Dimension(-1, 40));
//        validateSelectionBtn.setEnabled(false);
//        p.add(validateSelectionBtn);
//        convertEdmSelectionBtn.setPreferredSize(new Dimension(-1, 40));
//        convertEdmSelectionBtn.setEnabled(false);
//        p.add(convertEdmSelectionBtn);
        createHGBtn.setPreferredSize(new Dimension(-1, 40));
        createHGBtn.setEnabled(false);
        p.add(createHGBtn);
        analyzeControlaccessBtn.setPreferredSize(new Dimension(-1, 40));
        analyzeControlaccessBtn.setEnabled(false);
        p.add(analyzeControlaccessBtn);
        return p;
    }

    private boolean isCorrect(File file) {
        if (fileInstances.containsKey(file.getName())) {
            if(JOptionPane.showConfirmDialog(getContentPane(), labels.getString("error.file.exists") + " - " + labels.getString("error.file.exists.overwrite"), labels.getString("options.howLoadNewFiles"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                ((ProfileListModel)getXmlEadList().getModel()).removeFile(file);
            }
        }
        if (checkLoadingFiles() && XmlChecker.isXmlParseable(file) != null) {
            createErrorOrWarningPanel(new Exception(labels.getString("error.file.notXml")), false, labels.getString("error.file.notXml"), this);
        }
        return !fileInstances.containsKey(file.getName()) && !file.isDirectory() && (!checkLoadingFiles() || checkLoadingFiles() && XmlChecker.isXmlParseable(file) == null);
    }

    public ButtonGroup getGroupXslt() {
        return groupXslt;
    }

    public ButtonGroup getGroupXsd() {
        return groupXsd;
    }

    public DateNormalization getDateNormalization() {
        return dateNormalization;
    }

    public ResourceBundle getLabels() {
        return labels;
    }

    public List<String> getLevelList() {
        return levelList;
    }

    public List<String> getLangList() {
        return langList;
    }

    public APEPanel getAPEPanel() {
        return apePanel;
    }
    private Runnable finalAct = new Runnable() {
        public void run() {
//            convertAndValidateBtn.setEnabled(false);
//            validateSelectionBtn.setEnabled(false);
//            convertEdmSelectionBtn.setEnabled(false);
            createHGBtn.setEnabled(false);
            analyzeControlaccessBtn.setEnabled(false);
            xmlEadList.setEnabled(true);
        }
    };

    public Runnable getFinalAct() {
        return finalAct;
    }

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.getRootLogger().setLevel(Level.INFO);
        DataPreparationToolGUI dataPreparationToolGUI = new DataPreparationToolGUI();
        if (args.length == 1 && args[0].equals("dev")) {
            Utilities.isDev = true;
        }
        dataPreparationToolGUI.setupTool();
        dataPreparationToolGUI.setVisible(true);
    }

    private void makeDefaultXsdMenuItems() {
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem jRadioButtonMenuItem;
        for (Xsd_enum xsdEnum : Xsd_enum.values()) {
            jRadioButtonMenuItem = new JRadioButtonMenuItem(xsdEnum.getReadableName());
            if (xsdEnum.getReadableName().equals(retrieveFromDb.retrieveDefaultXsd())) {
                jRadioButtonMenuItem.setSelected(true);
            }
            jRadioButtonMenuItem.addActionListener(new XsdSelectActionListener());
            group.add(jRadioButtonMenuItem);
            defaultXsdSelectionSubmenu.add(jRadioButtonMenuItem);
        }
        for (XsdObject additionalXsd : retrieveFromDb.retrieveAdditionalXsds()) {
            jRadioButtonMenuItem = new JRadioButtonMenuItem(additionalXsd.getName());
            if (additionalXsd.getName().equals(retrieveFromDb.retrieveDefaultXsd())) {
                jRadioButtonMenuItem.setSelected(true);
            }
            jRadioButtonMenuItem.addActionListener(new XsdSelectActionListener());
            group.add(jRadioButtonMenuItem);
            defaultXsdSelectionSubmenu.add(jRadioButtonMenuItem);
        }
    }

    private class XsdSelectActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            retrieveFromDb.saveDefaultXsd(e.getActionCommand());
            for (String key : fileInstances.keySet()) {
                FileInstance fileInstance = fileInstances.get(key);
                fileInstance.setValidationSchema(e.getActionCommand());

                if (fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_2012_SCHEMA.getPath()))) {
                    fileInstance.setFileType(FileInstance.FileType.EAG);
                } else if (fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAC_SCHEMA.getPath())) || fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_EAC_SCHEMA.getPath()))) {
                    fileInstance.setFileType(FileInstance.FileType.EAC_CPF);
                }

                changeInfoInGUI(key);
            }
        }
    }

    private void makeDefaultXslMenuItems() {
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem jRadioButtonMenuItem;
        for (File xsltFile : Utilities.getXsltFiles()) {
            jRadioButtonMenuItem = new JRadioButtonMenuItem(xsltFile.getName());
            if (xsltFile.getName().equals(retrieveFromDb.retrieveDefaultXsl())) {
                jRadioButtonMenuItem.setSelected(true);
            }
            jRadioButtonMenuItem.addActionListener(new XslSelectActionListener());
            group.add(jRadioButtonMenuItem);
            defaultXslSelectionSubmenu.add(jRadioButtonMenuItem);
        }
    }

    private class XslSelectActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            retrieveFromDb.saveDefaultXsl(e.getActionCommand());
            for (String key : fileInstances.keySet()) {
                FileInstance fileInstance = fileInstances.get(key);
                fileInstance.setConversionScriptName(e.getActionCommand());
                changeInfoInGUI(key);
            }
        }
    }

    private List<XsdObject> fillXsdList() {
        List<XsdObject> xsdObjects = new ArrayList<XsdObject>();
        for (Xsd_enum xsdEnum : Xsd_enum.values()) {
            String filetype;
            if (xsdEnum.getPath().equals("apeEAD.xsd") || xsdEnum.getPath().equals("ead_2002.xsd")) {
                filetype = FileInstance.FileType.EAD.getFilePrefix();
            } else if (xsdEnum.getPath().equals("eag_2012.xsd")) {
                filetype = FileInstance.FileType.EAG.getFilePrefix();
            } else if (xsdEnum.getPath().equals("cpf.xsd")) {
                filetype = FileInstance.FileType.EAC_CPF.getFilePrefix();
            } else {
                filetype = FileInstance.FileType.OTHER.getFilePrefix();
            }
            xsdObjects.add(new XsdObject(-1, xsdEnum.getReadableName(), xsdEnum.getPath(), xsdEnum.getPath().equals("apeEAD.xsd") ? 1 : 0, 1, filetype));
        }
        for (XsdObject additionalXsd : retrieveFromDb.retrieveAdditionalXsds()) {
            xsdObjects.add(additionalXsd);
        }
        return xsdObjects;
    }

    private void createOptionPaneForCountryCode() {
        String currentResult = retrieveFromDb.retrieveCountryCode();
        String explanation = labels.getString("enterCountryCode") + ((currentResult != null) ? "\n" + labels.getString("currentCountryCode") + ": '" + currentResult + "'" : "");
        int i = 0;
        String result;
        do {
            if (i == 1) {
                explanation += "\n" + labels.getString("options.pleaseFollowRules");
            }
            result = (String) JOptionPane.showInputDialog(getContentPane(), explanation, labels.getString("chooseCountryCode"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            if (result == null) {
                if(currentResult == null)
                    System.exit(0);
                break;
            }
            i++;
        } while (dateNormalization.checkForCountrycode(result) == null);
        if (result != null) {
            retrieveFromDb.saveCountryCode(result);
        }
    }

    private void createOptionPaneForRepositoryCode() {
        String currentResult = retrieveFromDb.retrieveRepositoryCode();
        String explanation = labels.getString("enterIdentifier") + ((currentResult != null) ? "\n" + labels.getString("currentRepositoryCode") + ": '" + currentResult + "'" : "");
        int i = 0;
        String result;
        do {
            if (i == 1) {
                explanation += "\n" + labels.getString("options.pleaseFollowRules");
            }
            result = (String) JOptionPane.showInputDialog(getContentPane(), explanation, labels.getString("chooseRepositoryCode"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            if (result == null) {
                if(currentResult == null)
                    System.exit(0);
                break;
            }
            i++;
        } while (dateNormalization.checkForMainagencycode(result) == null);
        if (result != null) {
            retrieveFromDb.saveRepositoryCode(result);
        }
    }

    private void createOptionPaneForChecksLoadingFiles() {
        String currentResult = retrieveFromDb.retrieveCurrentLoadingChecks();
        String explanation = labels.getString("options.loadFilesExplanationYes") + "\n" + labels.getString("options.loadFilesExplanationNo") + "\n" + labels.getString("options.currentLoadFiles") + " '" + currentResult + "'";
        if (JOptionPane.showConfirmDialog(getContentPane(), explanation, labels.getString("options.howLoadNewFiles"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Utilities.icon) == JOptionPane.YES_OPTION) {
            retrieveFromDb.saveLoadingChecks("YES");
        } else {
            retrieveFromDb.saveLoadingChecks("NO");
        }
    }

    public boolean checkLoadingFiles() {
        return retrieveFromDb.retrieveCurrentLoadingChecks().equals("YES");
    }

    protected void changeInfoInGUI(String text) {
        APETabbedPane apeTabbedPane = apePanel.getApeTabbedPane();
        resultArea.setText("");
        apePanel.setFilename(text);

        if (StringUtils.isNotBlank(text)) {
            FileInstance fileInstance = fileInstances.get(text);
            LOG.trace(fileInstance);
            if (fileInstance.isValid()) {
//                apeTabbedPane.setValidationErrorText(labels.getString("validationSuccess"));
                apeTabbedPane.setValidationErrorText(fileInstance.getValidationErrors());
                apeTabbedPane.enableValidationReportBtn();
            } else {
                apeTabbedPane.setValidationErrorText(fileInstance.getValidationErrors());
                apeTabbedPane.disableValidationReportBtn();
            }
            apeTabbedPane.setConversionErrorText(fileInstance.getConversionErrors());
            if (fileInstance.isConverted()) {
                convertItem.setEnabled(false);
                apeTabbedPane.disableConversionBtn();
                apeTabbedPane.disableConvertAndValidateBtn();
                saveMessageReportItem.setEnabled(true);
            } else {
                convertItem.setEnabled(true);
                apeTabbedPane.enableConversionBtn();
                saveMessageReportItem.setEnabled(false);
            }
            if (fileInstance.isValid()) {
                validateItem.setEnabled(false);
                convertItem.setEnabled(false);
                apeTabbedPane.disableValidationBtn();
                apeTabbedPane.disableConversionBtn();
                apeTabbedPane.disableConvertAndValidateBtn();
                apeTabbedPane.disableConversionEdmBtn();
                closeSelectedItem.setEnabled(true);
                saveSelectedItem.setEnabled(true);
                saveMessageReportItem.setEnabled(true);
                if (fileInstance.getValidationSchema().getFileType().equals(FileInstance.FileType.EAD)) {
                    apeTabbedPane.enableConversionEdmBtn();
                }
            } else {
                validateItem.setEnabled(true);
                apeTabbedPane.enableValidationBtn();
                apeTabbedPane.disableConversionEdmBtn();
                closeSelectedItem.setEnabled(true);
                saveSelectedItem.setEnabled(true);
                saveMessageReportItem.setEnabled(true);
            }
            if (fileInstance.isEdm()) {
                apeTabbedPane.setEdmConversionErrorText(fileInstance.getEuropeanaConversionErrors());
                validateItem.setEnabled(false);
                apeTabbedPane.disableConversionBtn();
                apeTabbedPane.disableValidationBtn();
                closeSelectedItem.setEnabled(true);
                saveSelectedItem.setEnabled(true);
                saveMessageReportItem.setEnabled(true);
                if (fileInstance.getValidationSchema().getFileType().equals(FileInstance.FileType.EAD)) {
                    apeTabbedPane.enableConversionEdmBtn();
                }
            }
            if(!fileInstance.isValid() && !fileInstance.isConverted()) {
                apeTabbedPane.enableConvertAndValidateBtn();
            }
            refreshButtons(fileInstance, Utilities.XSLT_GROUP);
            refreshButtons(fileInstance, Utilities.XSD_GROUP);
        } else {
            apeTabbedPane.setValidationErrorText("");
            apeTabbedPane.setConversionErrorText("");
            apeTabbedPane.setEdmConversionErrorText("");
            apeTabbedPane.createEditionTree(null);
        }
    }

    public void disableTabFlashing() {
        apePanel.getApeTabbedPane().changeBackgroundColor(APETabbedPane.TAB_CONVERSION, Utilities.TAB_COLOR);
        apePanel.getApeTabbedPane().changeBackgroundColor(APETabbedPane.TAB_VALIDATION, Utilities.TAB_COLOR);
        apePanel.getApeTabbedPane().changeBackgroundColor(APETabbedPane.TAB_EDM, Utilities.TAB_COLOR);
    }

    public void refreshButtons(FileInstance fileInstance, int groupId) {
        Enumeration buttons;
        if (groupId == Utilities.XSD_GROUP) {
            buttons = groupXsd.getElements();
        } else {
            buttons = groupXslt.getElements();
        }

        while (buttons.hasMoreElements()) {
            JRadioButton jRadioButton = (JRadioButton) buttons.nextElement();
            if (jRadioButton.getText().equals(fileInstance.getConversionScriptName()) || jRadioButton.getText().equals(fileInstance.getValidationSchemaName())) {
                jRadioButton.setSelected(true);
                break;
            }
        }
    }

    public void disableRadioButtons() {
        Enumeration buttons = groupXsd.getElements();
        while (buttons.hasMoreElements()) {
            JRadioButton jRadioButton = (JRadioButton) buttons.nextElement();
            jRadioButton.setEnabled(false);
        }
        buttons = groupXslt.getElements();
        while (buttons.hasMoreElements()) {
            JRadioButton jRadioButton = (JRadioButton) buttons.nextElement();
            jRadioButton.setEnabled(false);
        }
    }

    public void enableRadioButtons() {
        Enumeration buttons = groupXsd.getElements();
        while (buttons.hasMoreElements()) {
            JRadioButton jRadioButton = (JRadioButton) buttons.nextElement();
            jRadioButton.setEnabled(true);
        }
        buttons = groupXslt.getElements();
        while (buttons.hasMoreElements()) {
            JRadioButton jRadioButton = (JRadioButton) buttons.nextElement();
            jRadioButton.setEnabled(true);
        }
    }

    private void doChecks() {
        //2 Files MUST exist - xsl/default-apeEAD.xsl and xsl/languages.xml
        String errorMsg = "";
        if (isFileMissing(Utilities.DEFAULT_APEEAD_XSL_FILE_PATH)) {
            errorMsg = MessageFormat.format(labels.getString("fileNotFound"), Utilities.DEFAULT_APEEAD_XSL_FILE_PATH) + "\n";
        }

        if (isFileMissing(Utilities.LANGUAGES_XML_FILE_PATH)) {
            errorMsg += MessageFormat.format(labels.getString("fileNotFound"), Utilities.LANGUAGES_XML_FILE_PATH) + "\n";
        }

        if (isFileMissing(Utilities.BEFORE_XSL_FILE_PATH)) {
            errorMsg += MessageFormat.format(labels.getString("fileNotFound"), Utilities.BEFORE_XSL_FILE_PATH) + "\n";
        }

        if (StringUtils.isNotEmpty(errorMsg)) {
            errorMsg += "\n" + labels.getString("exitProgram");
            JOptionPane.showMessageDialog(getContentPane(), errorMsg, labels.getString("error"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
            System.exit(0);
        }
        if (getCountryCode() == null) {
            createOptionPaneForCountryCode();
        }
        if (getRepositoryCodeIdentifier() == null) {
            createOptionPaneForRepositoryCode();
        }

        defaultRoleType = retrieveFromDb.retrieveRoleType();
        useExistingRoleType = retrieveFromDb.retrieveUseExistingRoleType();
        retrieveFromDb.checkForUpdates(getContentPane(), labels.getString("newVersionAvailable"), VERSION_NB);
    }

    /**
     * Erase old temp files (temp_* and .temp_*) from a directory
     *
     * @param dir The directory where we want to erase temp files
     */
    private void eraseOldTempFiles(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }
    }

    /**
     * Checks if a file or a directory exists
     *
     * @param filePath Path of the file to be checked
     * @return True if the file/dir exists, False if not
     */
    private boolean isFileMissing(String filePath) {
        return !(new File(filePath)).exists();
    }

    public String getCountryCode() {
        return retrieveFromDb.retrieveCountryCode();
    }

    public String getRepositoryCodeIdentifier() {
        return retrieveFromDb.retrieveRepositoryCode();
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("countrycode", getCountryCode());
        parameters.put("mainagencycode", getRepositoryCodeIdentifier());
        parameters.put("versionnb", VERSION_NB);

        parameters.put("defaultRoleType", retrieveFromDb.retrieveRoleType());

        parameters.put("useDefaultRoleType", Boolean.toString(!(retrieveFromDb.retrieveUseExistingRoleType())));
        //todo: Need one for languages.xml file?
        String daoRightsUrl = retrieveFromDb.retrieveDaoRights();
        parameters.put("defaultRightsDigitalObject", daoRightsUrl);
        parameters.put("defaultRightsDigitalObjectText", DigitalObjectAndRightsOptionFrame.RightsInformation.getRightsInformationFromUrl(daoRightsUrl).getName());
        parameters.put("defaultRightsDigitalObjectDescription", retrieveFromDb.retrieveDaoRightsDesc());
        parameters.put("defaultRightsDigitalObjectHolder", retrieveFromDb.retrieveDaoRightsHolder());
        String eadRightsUrl = retrieveFromDb.retrieveEadRights();
        parameters.put("defaultRightsEadData", eadRightsUrl);
        parameters.put("defaultRightsEadDataText", DigitalObjectAndRightsOptionFrame.RightsInformation.getRightsInformationFromUrl(eadRightsUrl).getName());
        parameters.put("defaultRightsEadDataDescription", retrieveFromDb.retrieveEadRightsDesc());
        parameters.put("defaultRightsEadDataHolder", retrieveFromDb.retrieveEadRightsHolder());
        return parameters;
    }

    public void setResultAreaText(String text) {
        resultArea.setText(text);
    }

    public JXTreeTable getTree() {
        return tree;
    }

    public void setTree(JXTreeTable tree) {
        this.tree = tree;
    }

    public JLabel getResultArea() {
        return resultArea;
    }

    public JList getXmlEadList() {
        return xmlEadList;
    }

    public Map<String, FileInstance> getFileInstances() {
        return fileInstances;
    }

    public void disableAllBtnAndItems() {
        apePanel.getApeTabbedPane().disableConversionEdmBtn();
        apePanel.getApeTabbedPane().disableConversionBtn();
        apePanel.getApeTabbedPane().disableConvertAndValidateBtn();
        apePanel.getApeTabbedPane().disableValidationBtn();

        convertItem.setEnabled(false);
        validateItem.setEnabled(false);
        saveMessageReportItem.setEnabled(false);
    }

    public void disableEditionTab() {
        apePanel.getApeTabbedPane().setEnabledAt(4, false);
    }

    public void enableEditionTab() {
        apePanel.getApeTabbedPane().setEnabledAt(4, true);
    }

    public void disableAllBatchBtns() {
        createHGBtn.setEnabled(false);
        analyzeControlaccessBtn.setEnabled(false);
    }

    public void enableAllBatchBtns() {
        createHGBtn.setEnabled(true);
        analyzeControlaccessBtn.setEnabled(true);
    }

    public void enableValidationBtns() {
        apePanel.getApeTabbedPane().enableValidationBtn();
        validateItem.setEnabled(true);
    }

    public void enableConversionBtns() {
        apePanel.getApeTabbedPane().enableConversionBtn();
        convertItem.setEnabled(true);
    }

    public void enableMessageReportBtns() {
        saveMessageReportItem.setEnabled(true);
    }

//    public void disableEdmConversionBtn() {
//        convertEdmSelectionBtn.setEnabled(false);
//    }

    public void enableEdmConversionBtn() {
        apePanel.getApeTabbedPane().enableConversionEdmBtn();
    }

    public void enableSaveBtn() {
        saveSelectedItem.setEnabled(true);
    }

    public JLabel getXmlEadListLabel() {
        return xmlEadListLabel;
    }

    private class LanguageActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (Arrays.asList(LANGUAGES_OF_TOOL).contains(e.getActionCommand())) {
                labels = new ResourceBundlesWrapper(I18N_BASENAMES, new Locale(e.getActionCommand()));
            }
            changeAllTextLg();
        }
    }
}
