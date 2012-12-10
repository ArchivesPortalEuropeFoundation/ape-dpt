package eu.apenet.dpt.standalone.gui;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.apenet.dpt.standalone.gui.batch.ConvertAndValidateActionListener;
import eu.apenet.dpt.standalone.gui.conversion.ConvertActionListener;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.ead2ese.ConvertEseActionListener;
import eu.apenet.dpt.standalone.gui.eag2012.Eag2012Frame;
import eu.apenet.dpt.standalone.gui.hgcreation.*;
import eu.apenet.dpt.standalone.gui.validation.ValidateActionListener;

import eu.apenet.dpt.standalone.gui.validation.ValidateSelectionActionListener;
import eu.apenet.dpt.utils.util.XmlChecker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.w3c.dom.*;

import eu.apenet.dpt.standalone.gui.adhoc.FileNameComparator;
import eu.apenet.dpt.standalone.gui.edition.CheckList;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;

/**
 * User: Yoann
 * Date: Apr 19, 2010
 * Time: 8:07:25 PM
 */

public class DataPreparationToolGUI extends JFrame {
    private final static Logger LOG = Logger.getLogger(DataPreparationToolGUI.class);

    public static final String VERSION_NB = "1.2.0-SNAPSHOT";

    private static final String[] LANGUAGES_OF_TOOL = {"en", "fr", "de", "hu", "xx"};

    private ResourceBundle labels;

    /**
     * Button and titles to be used in the GUI
     */
    private JButton convertAndValidateBtn = new JButton();
    private JButton validateSelectionBtn = new JButton();
    private JButton convertEseSelectionBtn = new JButton();
    private JButton createHGBtn = new JButton();

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
    private JMenuItem saveSelectedItem = new JMenuItem();
//    private JMenuItem sendFilesWebDAV = new JMenuItem();
    private JMenuItem quitItem = new JMenuItem();

    private JMenuItem createEag2012FromExistingEag02 = new JMenuItem();
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

    private JMenuItem validateItem = new JMenuItem();
    private JMenuItem convertItem = new JMenuItem();

    private JMenuItem summaryWindowItem = new JMenuItem();
    private JMenuItem validationWindowItem = new JMenuItem();
    private JMenuItem conversionWindowItem = new JMenuItem();
    private JMenuItem eseConversionWindowItem = new JMenuItem();
    private JMenuItem editionWindowItem = new JMenuItem();

    private JMenuItem internetApexItem = new JMenuItem();

    private JFileChooser fileChooser = new JFileChooser();
    private File currentLocation = null;

    /**
     * List of files (model)
     */
    private ProfileListModel model;

    private JLabel progressLabel = new JLabel("", JLabel.CENTER);
//    private JButton abort = new JButton("");

    private JList list;

    private JLabel resultArea = new JLabel();

    private JTable eagFormTable;

    private ButtonGroup groupXslt = new ButtonGroup();
    private ButtonGroup groupXsd = new ButtonGroup();

    /**
     * Utilities
     */
    private DateNormalization dateNormalization;

    private JMenuItem deleteFileItem = new JMenuItem();

    private JFrame eagCreationFrame;
//    private JFrame roleTypeFrame;

    /**
     * Locations
     */
    private RetrieveFromDb retrieveFromDb;

    public boolean useExistingRoleType;
    public String defaultRoleType;
    private boolean continueLoop = true;
    private Map<String, FileInstance> fileInstances = new HashMap<String, FileInstance>();
    private List<String> langList;
    private List<String> levelList;

    private Point from;

    /**
     * ActionListeners
     */
    private CreateHGListener createHgListener;

    /**
     * For edition
     */
//    private JTree tree;
//    private JTreeTable treeTable;
    private JXTreeTable tree;

    private DataPreparationToolGUI() {
        super("");
    }

    private void setupTool() {
        Locale currentLocale = Locale.getDefault();
        labels = ResourceBundle.getBundle("i18n/apeBundle", currentLocale);
        apePanel = new APEPanel(labels, getContentPane(), this);

        retrieveFromDb = new RetrieveFromDb();

        CheckList checkList = new CheckList();
        langList = checkList.getLangList();
        levelList = checkList.getLevelList();

        dateNormalization = new DateNormalization();

        super.setTitle(labels.getString("title"));
        nameComponents();
        Image topLeftIcon = Utilities.icon.getImage();
        setIconImage(topLeftIcon);

        doChecks();

        if(isFileMissing(Utilities.LOG_DIR))
            new File(Utilities.LOG_DIR).mkdir();

        File tempDir = new File(Utilities.TEMP_DIR);
        //In case it didn't deleteOnExit at the previous closing of the program, we clean up.
        if(tempDir.exists()){
            LOG.warn("Probably a problem when deleting the temp files at closure, so we clean up");
            eraseOldTempFiles(tempDir);
            try {
                FileUtils.deleteDirectory(tempDir);
            } catch (IOException e){
                LOG.error("Could not delete the temp directory. Attempt to delete the directory once more: " + (tempDir.delete()?"Successful.":"Unsuccessful."));
            }
        }
        tempDir.mkdir();
        tempDir.deleteOnExit();

//        CountCLevels countCLevels = new CountCLevels("/Users/yoannmoranville/Work/APEnet/Projects/data/Ready_APEnet/READY/Finland/HeNAF/FA_EAD/");
//        CountCLevels countCLevels = new CountCLevels("/Users/yoannmoranville/Work/APEnet/Projects/data/BORA/ALL/");
//        countCLevels.setCopyInAppropriateDirs(true);
//        countCLevels.changeMainagencycodeForSweden(false);
//        countCLevels.countLevels();

//        SeparateFinnishFiles separateFinnishFiles = new SeparateFinnishFiles(new File("/Users/yoannmoranville/Desktop/files_fi"), TEMP_DIR);
        makeDefaultXsdMenuItems();
        makeDefaultXslMenuItems();

        getContentPane().add(apePanel);

        model = new ProfileListModel(fileInstances, this);
        list = new JList(model);
        list.setCellRenderer(new IconListCellRenderer(fileInstances));
        list.setDragEnabled(true);

        list.setTransferHandler(new ListTransferHandler());

        list.setDropTarget(new DropTarget(list, new DropTargetListener() {
            private void updateLine(Point pt) {
                if(list.locationToIndex(pt) < 0)
                    list.clearSelection();
                list.repaint();
            }

            public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
                Point location = dropTargetDragEvent.getLocation();
                from = location;
                updateLine(location);
            }

            public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
                Point location = dropTargetDragEvent.getLocation();
                updateLine(location);
            }

            public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
            }

            private void resetGlassPane() {
                list.repaint();
            }

            public void dragExit(DropTargetEvent dropTargetEvent) {
                resetGlassPane();
            }

            public void drop(DropTargetDropEvent dropTargetDropEvent) {
                resetGlassPane();

                Point p = list.getMousePosition();

                int dstRow = list.locationToIndex(p);

                int srcRow = list.locationToIndex(from);
                ProfileListModel m = (ProfileListModel) list.getModel();

                if (dstRow < 0) {
                    dstRow = 0;
                }
                if (dstRow > m.getSize() - 1) {
                    dstRow = m.getSize() - 1;
                }

                m.insertElementAt((File)m.getElementAt(srcRow), dstRow);
                if(dstRow <= srcRow){
                    m.removeElementAt(srcRow+1);
                    list.setSelectedIndex(dstRow);
                }
                else{
                    m.removeElementAt(srcRow);
                    list.setSelectedIndex(dstRow-1);
                }
            }
        }));
        model.setList(list);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(optionMenu);
        menuBar.add(actionMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        fileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(fileItem);

        createEag2012Item.add(createEag2012FromExistingEag02);
        createEag2012Item.add(createEag2012FromExistingEag2012);
        createEag2012Item.add(createEag2012FromScratch);

        fileMenu.add(createEag2012Item);
        saveSelectedItem.setEnabled(false);
        saveSelectedItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(saveSelectedItem);
//        fileMenu.add(sendFilesWebDAV); //todo: Future
        fileMenu.addSeparator();
        fileMenu.add(quitItem);

        optionMenu.add(countryCodeItem);
        optionMenu.add(repositoryCodeItem);
        optionMenu.add(checksLoadingFilesItem);
        optionMenu.add(digitalObjectTypeItem);
        optionMenu.add(defaultSaveFolderItem);
        optionMenu.add(xsltItem);
        optionMenu.add(xsdItem);
        optionMenu.add(defaultXslSelectionSubmenu);
        optionMenu.add(defaultXsdSelectionSubmenu);
        optionMenu.add(languageMenu);
        optionMenu.add(listDateConversionRulesItem);

        validateItem.setEnabled(false);
        validateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        actionMenu.add(validateItem);
        convertItem.setEnabled(false);
        convertItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        actionMenu.add(convertItem);
        summaryWindowItem.setEnabled(true);
        summaryWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(summaryWindowItem);
        validationWindowItem.setEnabled(true);
        validationWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(validationWindowItem);
        conversionWindowItem.setEnabled(true);
        conversionWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(conversionWindowItem);
        eseConversionWindowItem.setEnabled(true);
        eseConversionWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(eseConversionWindowItem);
        editionWindowItem.setEnabled(true);
        editionWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        windowMenu.add(editionWindowItem);
        helpMenu.add(internetApexItem);
        helpMenu.addSeparator();
        JMenuItem versionItem = new JMenuItem("APE DPT v"+ VERSION_NB);
        versionItem.setEnabled(false);
        helpMenu.add(versionItem);
        createLanguageMenu();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);

        getContentPane().add(menuBar, BorderLayout.NORTH);

        apePanel.setFilename("");

        createHgListener = new CreateHGListener(retrieveFromDb, labels, getContentPane(), fileInstances, list, this);
        createHGBtn.addActionListener(createHgListener);
        createHGBtn.setEnabled(false);

        validateItem.addActionListener(new ValidateActionListener(labels, this, apePanel.getApeTabbedPane()));
        convertItem.addActionListener(new ConvertActionListener(getContentPane(), labels, this, apePanel));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(createWest(), BorderLayout.WEST);

        convertAndValidateBtn.addActionListener(new ConvertAndValidateActionListener(labels, this, getContentPane()));
        validateSelectionBtn.addActionListener(new ValidateSelectionActionListener(labels, this, getContentPane()));
        convertEseSelectionBtn.addActionListener(new ConvertEseActionListener(labels, this, apePanel));

        wireUp();
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    private void nameComponents(){
        fileMenu.setText(labels.getString("file"));
        optionMenu.setText(labels.getString("options"));
        actionMenu.setText(labels.getString("actions"));

        fileItem.setText(labels.getString("selectFile"));
        createEag2012Item.setText(labels.getString("createEag2012"));
        createEag2012FromExistingEag02.setText(labels.getString("menu.createEag2012FromEag02"));
        createEag2012FromExistingEag2012.setText(labels.getString("menu.createEag2012FromEag2012"));
        createEag2012FromScratch.setText(labels.getString("menu.createEag2012FromScratch"));
        saveSelectedItem.setText(labels.getString("saveSelectedFile"));
//        sendFilesWebDAV.setText(labels.getString("sendFilesWebDAV"));
        quitItem.setText(labels.getString("quit"));

        countryCodeItem.setText(labels.getString("countryCode"));
        repositoryCodeItem.setText(labels.getString("repositoryCode"));
        checksLoadingFilesItem.setText(labels.getString("checksLoadingFiles"));
        digitalObjectTypeItem.setText(labels.getString("digitalObjectType"));
        defaultSaveFolderItem.setText(labels.getString("defaultSaveFolder"));
        xsltItem.setText(labels.getString("ownXsl"));
        xsdItem.setText(labels.getString("ownXsd"));
        defaultXslSelectionSubmenu.setText(labels.getString("defaultXslSelectionSubmenu"));
        defaultXsdSelectionSubmenu.setText(labels.getString("defaultXsdSelectionSubmenu"));
        languageMenu.setText(labels.getString("languageMenu"));
        listDateConversionRulesItem.setText(labels.getString("listDateConversionRules"));

        validateItem.setText(labels.getString("validate"));
        convertItem.setText(labels.getString("convert"));

        convertAndValidateBtn.setText(labels.getString("convertAndValidate"));
        createHGBtn.setText(labels.getString("createHG"));
        progressLabel.setText(labels.getString("chooseFile"));
//        abort.setText(labels.getString("abort"));
        deleteFileItem.setText(labels.getString("removeFile"));

        windowMenu.setText(labels.getString("windows"));
        summaryWindowItem.setText(labels.getString("summary"));
        validationWindowItem.setText(labels.getString("validation"));
        conversionWindowItem.setText(labels.getString("conversion"));
        eseConversionWindowItem.setText(labels.getString("eseConversion"));
        editionWindowItem.setText(labels.getString("edition"));

        helpMenu.setText(labels.getString("help"));
        internetApexItem.setText(labels.getString("projectWebsite"));

        validateSelectionBtn.setText(labels.getString("validateSelected"));
        convertEseSelectionBtn.setText(labels.getString("convertEseSelectionBtn"));
    }

    private void changeAllTextLg(){
        nameComponents();

        createHgListener.changeLanguage(labels);
        apePanel.changeLanguage(labels);

        DataPreparationToolGUI.super.setTitle(labels.getString("title"));

        if(list.getSelectedValue() == null)
            apePanel.setFilename("");
        else
            apePanel.setFilename(((File) list.getSelectedValue()).getName());

        apePanel.getApeTabbedPane().setValidationBtnText(labels.getString("validate"));
    }

    private void createLanguageMenu(){
        Locale currentLocale = Locale.getDefault();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("English");
        if(!Arrays.asList(LANGUAGES_OF_TOOL).contains(currentLocale.getLanguage()) || currentLocale.getLanguage().equals("en")) {
            rbMenuItem.setSelected(true);
        }

        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Français");
        rbMenuItem.setActionCommand("fr");
        if(currentLocale.getLanguage().equals("fr")){
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Deutsch");
        rbMenuItem.setActionCommand("de");
        if(currentLocale.getLanguage().equals("de")){
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Magyar");
        rbMenuItem.setActionCommand("hu");
        if(currentLocale.getLanguage().equals("hu")){
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        if(Utilities.isDev) {
            languageMenu.addSeparator();
            rbMenuItem = new JRadioButtonMenuItem("XXXXXX");
            rbMenuItem.setActionCommand("xx");
            if(currentLocale.getLanguage().equals("xx")){
                rbMenuItem.setSelected(true);
            }
            rbMenuItem.addActionListener(new LanguageActionListener());
            group.add(rbMenuItem);
            languageMenu.add(rbMenuItem);
        }
    }

    int currentFileNumberBatch = 0;
    private void wireUp() {

//        abort.addActionListener(new ActionListener() {
//            //@Override
//            public void actionPerformed(ActionEvent e) {
//                continueLoop = false;
//                resultArea.setText(labels.getString("aborting"));
//            }
//        });
        fileItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                if(actionEvent.getSource() == fileItem) {
                    currentLocation = new File(retrieveFromDb.retrieveOpenLocation());
                    fileChooser.setCurrentDirectory(currentLocation);
                    int returnedVal = fileChooser.showOpenDialog(getParent());

                    if(returnedVal == JFileChooser.APPROVE_OPTION){
                        currentLocation = fileChooser.getCurrentDirectory();
                        retrieveFromDb.saveOpenLocation(currentLocation.getAbsolutePath());

                        File[] files = fileChooser.getSelectedFiles();
                        for(File file : files) {
                            if(file.isDirectory()){
                                File[] fileArray = file.listFiles();
                                Arrays.sort(fileArray, new FileNameComparator());
                                for(File children : fileArray){
                                    if(isCorrect(children))
                                        model.addFile(children);
                                }
                            } else {
                                if(isCorrect(file))
                                    model.addFile(file);
                            }
                        }
                    }
                }
            }
        });
        repositoryCodeItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                createOptionPaneForRepositoryCode();
            }
        });
        countryCodeItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                createOptionPaneForCountryCode();
            }
        });
        checksLoadingFilesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                createOptionPaneForChecksLoadingFiles();
            }
        });
        createEag2012FromExistingEag02.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser eagFileChooser = new JFileChooser();
                eagFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                eagFileChooser.setMultiSelectionEnabled(false);
                eagFileChooser.setCurrentDirectory(new File(retrieveFromDb.retrieveOpenLocation()));
                if(eagFileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File eagFile = eagFileChooser.getSelectedFile();
                    new Eag2012Frame(eagFile, false, getContentPane().getSize(), (ProfileListModel)getList().getModel());
                }
            }
        });
        createEag2012FromExistingEag2012.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser eagFileChooser = new JFileChooser();
                eagFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                eagFileChooser.setMultiSelectionEnabled(false);
                eagFileChooser.setCurrentDirectory(new File(retrieveFromDb.retrieveOpenLocation()));
                if(eagFileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File eagFile = eagFileChooser.getSelectedFile();
                    new Eag2012Frame(eagFile, true, getContentPane().getSize(), (ProfileListModel)getList().getModel());
                }
            }
        });
        createEag2012FromScratch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Eag2012Frame(getContentPane().getSize(), (ProfileListModel)getList().getModel());
            }
        });
        digitalObjectTypeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame roleTypeFrame = new RoleTypeFrame(labels, retrieveFromDb);

                roleTypeFrame.setPreferredSize(new Dimension(getContentPane().getWidth() *3/8, getContentPane().getHeight() *3/8));
                roleTypeFrame.setLocation(getContentPane().getWidth() / 8, getContentPane().getHeight() / 8);

                roleTypeFrame.pack();
                roleTypeFrame.setVisible(true);
            }
        });
        defaultSaveFolderItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser defaultSaveFolderChooser = new JFileChooser();
                defaultSaveFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                defaultSaveFolderChooser.setMultiSelectionEnabled(false);
                defaultSaveFolderChooser.setCurrentDirectory(new File(retrieveFromDb.retrieveDefaultSaveFolder()));
                if(defaultSaveFolderChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    File directory = defaultSaveFolderChooser.getSelectedFile();
                    retrieveFromDb.saveDefaultSaveFolder(directory + "/");
                }
            }
        });
        saveSelectedItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(String filename : fileInstances.keySet()) {
                    FileInstance fileInstance = fileInstances.get(filename);
                    String filePrefix = fileInstance.getFileType().getFilePrefix();
                    String defaultOutputDirectory = retrieveFromDb.retrieveDefaultSaveFolder();

                    //todo: do we really need this?
                    filename = filename.startsWith("temp_")?filename.replace("temp_", ""):filename;

                    if(!fileInstance.isValid())
                        filePrefix = "NOT_" + filePrefix;

                    if(tree != null && tree.getTreeTableModel() != null && !fileInstance.getLastOperation().equals(FileInstance.Operation.CONVERT)){
                        TreeTableModel treeTableModel = tree.getTreeTableModel();
                        Document document = (Document)treeTableModel.getRoot();
                        try {
                            File file2 = new File(defaultOutputDirectory + filePrefix + "_" + filename);
                            TransformerFactory tf = TransformerFactory.newInstance();
                            Transformer output = tf.newTransformer();
                            output.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
                            output.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");

                            output.transform(new DOMSource(document.getFirstChild()), new StreamResult(file2));

                            fileInstance.setLastOperation(FileInstance.Operation.SAVE);
                            fileInstance.setCurrentLocation(file2.getAbsolutePath());
                        } catch (Exception ex){
                            createErrorOrWarningPanel(ex, true, labels.getString("errorSavingTreeXML"), getContentPane());
                        }
                    } else if(fileInstance.isConverted()){
                        File newFile = new File(defaultOutputDirectory + filePrefix + "_" + filename);
                        (new File(fileInstance.getCurrentLocation())).renameTo(newFile);
                        fileInstance.setLastOperation(FileInstance.Operation.SAVE);
                        fileInstance.setCurrentLocation(newFile.getAbsolutePath());
                    } else {
                        try{
                            File newFile = new File(defaultOutputDirectory + filePrefix + "_" + filename);
                            FileUtils.copyFile((File) list.getSelectedValue(), new File(defaultOutputDirectory + filePrefix + "_" + filename));
                            fileInstance.setLastOperation(FileInstance.Operation.SAVE);
                            fileInstance.setCurrentLocation(newFile.getAbsolutePath());
                        } catch (IOException ioe){
                            LOG.error("Error when saving file", ioe);
                        }
                    }
                }
                JOptionPane.showMessageDialog(getContentPane(), labels.getString("fileInOutput")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
            }
        });
        quitItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        xsltItem.addActionListener(new XsltAdderActionListener(this, labels));
        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getButton() != MouseEvent.BUTTON3 && e.getClickCount() == 1 && list.getSelectedValues().length == 1){
                    changeInfoInGUI(((File)list.getSelectedValue()).getName());
                    if(apePanel.getApeTabbedPane().getSelectedIndex() == APETabbedPane.TAB_EDITION) {
                        try {
                            apePanel.getApeTabbedPane().createEditionTree(((File)list.getSelectedValue()));
                        } catch (Exception e1) {
                            //nothing
                        }
                    }
                    apePanel.getApeTabbedPane().changeBackgroundColor(APETabbedPane.TAB_CONVERSION, Utilities.TAB_COLOR);
                    apePanel.getApeTabbedPane().changeBackgroundColor(APETabbedPane.TAB_VALIDATION, Utilities.TAB_COLOR);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if(list.getSelectedValues().length == 1){
                        final int indexToErase = list.locationToIndex(e.getPoint());
                        list.setSelectedIndex(indexToErase);
                    }
                    JPopupMenu popup = new JPopupMenu();
                    deleteFileItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (list.getSelectedValues().length > 1) {
                                    model.removeFiles(list.getSelectedValues());
                                } else {
                                    model.removeFile((File) list.getSelectedValue());
                                }
                            } catch (Exception ex) {
                                createErrorOrWarningPanel(ex, true, labels.getString("errorRemovingFileFromList"), getContentPane());
                            } finally {
                                deleteFileItem.removeActionListener(this);
                            }
                        }
                    });
                    popup.add(deleteFileItem);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        list.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if(list.getSelectedValues() != null) {
                    if(list.getSelectedValues().length > 1) {
                        convertAndValidateBtn.setEnabled(true);
                        validateSelectionBtn.setEnabled(true);
                        convertEseSelectionBtn.setEnabled(true);
                        disableAllBtnAndItems();
                    } else {
                        convertAndValidateBtn.setEnabled(false);
                        validateSelectionBtn.setEnabled(false);
                        convertEseSelectionBtn.setEnabled(false);
                    }
                    checkHoldingsGuideButton();
                } else {
                    convertAndValidateBtn.setEnabled(false);
                    validateSelectionBtn.setEnabled(false);
                    convertEseSelectionBtn.setEnabled(false);
                    createHGBtn.setEnabled(false);
                }
            }
        });

        summaryWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_SUMMARY));
        validationWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_VALIDATION));
        conversionWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_CONVERSION));
        eseConversionWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_ESE));
        editionWindowItem.addActionListener(new TabItemActionListener(apePanel, APETabbedPane.TAB_EDITION));

        internetApexItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                BareBonesBrowserLaunch.openURL("http://www.apex-project.eu/");
            }
        });
    }

    private void checkHoldingsGuideButton() {
        for(int i = 0; i < list.getSelectedValues().length; i++) {
            FileInstance fileInstance = fileInstances.get(((File)list.getSelectedValues()[i]).getName());
            if((fileInstance.getValidationSchema() == Xsd_enum.XSD_APE_SCHEMA || fileInstance.getValidationSchema() == Xsd_enum.XSD1_0_APE_SCHEMA) && fileInstance.isValid()) {
                createHGBtn.setEnabled(true);
                return;
            }
        }
        createHGBtn.setEnabled(false);
    }

    public static void createErrorOrWarningPanel(Throwable e, boolean isError, String message, Component owner){
        java.util.logging.Level level = java.util.logging.Level.INFO;
        if(isError)
            LOG.error(message, e);
        else
            LOG.info(message, e);
        ErrorInfo errorInfo = new ErrorInfo("Error", message, null, "category", e, level, null);
        JXErrorPane.showDialog(owner, errorInfo);
        //todo: FIX below, should be uncommented
//        if(owner == eagCreationFrame && !owner.hasFocus())
//            eseOptionFrame.setAlwaysOnTop(true);
    }

    private JPanel createWest() {
        list.setCellRenderer(new IconListCellRenderer(fileInstances));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(200, 100));
        p.add(new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        p.add(createSouthWest(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel createSouthWest() {
        JPanel p = new JPanel(new GridLayout(0, 1));
        p.add(progressLabel);
        convertAndValidateBtn.setPreferredSize(new Dimension(-1, 40)); //width max
        convertAndValidateBtn.setEnabled(false);
        p.add(convertAndValidateBtn);
        validateSelectionBtn.setPreferredSize(new Dimension(-1, 40));
        validateSelectionBtn.setEnabled(false);
        p.add(validateSelectionBtn);
        convertEseSelectionBtn.setPreferredSize(new Dimension(-1, 40));
        convertEseSelectionBtn.setEnabled(false);
        p.add(convertEseSelectionBtn);
        createHGBtn.setPreferredSize(new Dimension(-1, 40));
        createHGBtn.setEnabled(false);
        p.add(createHGBtn);
//        abort.setEnabled(false);
//        p.add(abort);
        return p;
    }

    private boolean isCorrect(File file){
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
            convertAndValidateBtn.setEnabled(false);
            validateSelectionBtn.setEnabled(false);
            convertEseSelectionBtn.setEnabled(false);
            createHGBtn.setEnabled(false);
//            abort.setEnabled(false);
            list.setEnabled(true);
        }
    };
    public Runnable getFinalAct() {
        return finalAct;
    }

    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.INFO);
        DataPreparationToolGUI dataPreparationToolGUI = new DataPreparationToolGUI();
        if(args.length == 1 && args[0].equals("dev"))
            Utilities.isDev = true;
        dataPreparationToolGUI.setupTool();
        dataPreparationToolGUI.setVisible(true);
    }

    private void makeDefaultXsdMenuItems() {
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem jRadioButtonMenuItem;
        for(Xsd_enum xsdEnum : Xsd_enum.values()){
            jRadioButtonMenuItem = new JRadioButtonMenuItem(xsdEnum.getReadableName());
            if(xsdEnum.equals(Xsd_enum.XSD_APE_SCHEMA))
                jRadioButtonMenuItem.setSelected(true);
            jRadioButtonMenuItem.addActionListener(new XsdSelectActionListener());
            group.add(jRadioButtonMenuItem);
            defaultXsdSelectionSubmenu.add(jRadioButtonMenuItem);
        }
    }
    private class XsdSelectActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Utilities.setDefaultXsd(e.getActionCommand());
            for(String key : fileInstances.keySet()) {
                FileInstance fileInstance = fileInstances.get(key);
                fileInstance.setValidationSchema(e.getActionCommand());

                if(fileInstance.getValidationSchema().equals(Xsd_enum.XSD_EAG_SCHEMA) || fileInstance.getValidationSchema().equals(Xsd_enum.XSD_EAG_2012_SCHEMA))
                    fileInstance.setFileType(FileInstance.FileType.EAG);
                else if(fileInstance.getValidationSchema().equals(Xsd_enum.XSD_EAC_SCHEMA))
                    fileInstance.setFileType(FileInstance.FileType.EAC_CPF);

                changeInfoInGUI(key);
            }
        }
    }
    private void makeDefaultXslMenuItems() {
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem jRadioButtonMenuItem;
        for(File xsltFile : Utilities.getXsltFiles()) {
            jRadioButtonMenuItem = new JRadioButtonMenuItem(xsltFile.getName());
            if(xsltFile.getName().equals(Utilities.XSL_DEFAULT_NAME))
                jRadioButtonMenuItem.setSelected(true);
            jRadioButtonMenuItem.addActionListener(new XslSelectActionListener());
            group.add(jRadioButtonMenuItem);
            defaultXslSelectionSubmenu.add(jRadioButtonMenuItem);
        }
    }
    private class XslSelectActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Utilities.setDefaultXsl(e.getActionCommand());
            for(String key : fileInstances.keySet()) {
                FileInstance fileInstance = fileInstances.get(key);
                fileInstance.setConversionScriptName(e.getActionCommand());
                changeInfoInGUI(key);
            }
        }
    }

    private void createOptionPaneForCountryCode() {
        String currentResult = retrieveFromDb.retrieveCountryCode();
        String explanation = labels.getString("enterCountryCode") + "\n" + labels.getString("currentCountryCode") + ": '" + currentResult + "'";
        int i = 0;
        String result;
        do {
            if(i == 1)
                explanation += "\n" + labels.getString("options.pleaseFollowRules");
            result = (String)JOptionPane.showInputDialog(getContentPane(), explanation, labels.getString("chooseCountryCode"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            if(result == null)
                break;
            i++;
        } while (dateNormalization.checkForCountrycode(result) == null);
        if(result != null)
            retrieveFromDb.saveCountryCode(result);
    }

    private void createOptionPaneForRepositoryCode() {
        String currentResult = retrieveFromDb.retrieveRepositoryCode();
        String explanation = labels.getString("enterIdentifier") + "\n" + labels.getString("currentRepositoryCode") + ": '" + currentResult + "'";
        int i = 0;
        String result;
        do {
            if(i == 1)
                explanation += "\n" + labels.getString("options.pleaseFollowRules");
            result = (String)JOptionPane.showInputDialog(getContentPane(), explanation, labels.getString("chooseRepositoryCode"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            if(result == null)
                break;
            i++;
        } while (dateNormalization.checkForMainagencycode(result) == null);
        if(result != null)
            retrieveFromDb.saveRepositoryCode(result);
    }

    private void createOptionPaneForChecksLoadingFiles() {
        String currentResult = retrieveFromDb.retrieveCurrentLoadingChecks();
        String explanation = labels.getString("options.loadFilesExplanationYes") + "\n" + labels.getString("options.loadFilesExplanationNo") + "\n" + labels.getString("options.currentLoadFiles") + " '" + currentResult + "'";
        if(JOptionPane.showConfirmDialog(getContentPane(), explanation, labels.getString("options.howLoadNewFiles"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Utilities.icon) == JOptionPane.YES_OPTION) {
            retrieveFromDb.saveLoadingChecks("YES");
        } else {
            retrieveFromDb.saveLoadingChecks("NO");
        }
    }

    public boolean checkLoadingFiles() {
        return retrieveFromDb.retrieveCurrentLoadingChecks().equals("YES");
    }

    private void changeInfoInGUI(String text){
        APETabbedPane apeTabbedPane = apePanel.getApeTabbedPane();
        resultArea.setText("");
        apePanel.setFilename(text);
        FileInstance fileInstance = fileInstances.get(text);
        if(fileInstance.isValid())
            apeTabbedPane.setValidationErrorText(labels.getString("validationSuccess"));
        else
            apeTabbedPane.setValidationErrorText(fileInstance.getValidationErrors());
        apeTabbedPane.setConversionErrorText(fileInstance.getConversionErrors());
        if(fileInstance.isConverted()){
            convertItem.setEnabled(false);
            apeTabbedPane.disableConversionBtn();
            apeTabbedPane.setValidationBtnText(labels.getString("validate"));
        } else {
            convertItem.setEnabled(true);
            apeTabbedPane.enableConversionBtn();
            apeTabbedPane.setValidationBtnText(labels.getString("validate"));
        }
        if(fileInstance.isValid()){
            validateItem.setEnabled(false);
            apeTabbedPane.disableValidationBtn();
            saveSelectedItem.setEnabled(true);
            if(fileInstance.getValidationSchema() == Xsd_enum.XSD_APE_SCHEMA || fileInstance.getValidationSchema() == Xsd_enum.XSD1_0_APE_SCHEMA)
                apeTabbedPane.enableConversionEseBtn();
        } else {
            validateItem.setEnabled(true);
            apeTabbedPane.enableValidationBtn();
            apeTabbedPane.disableConversionEseBtn();
            saveSelectedItem.setEnabled(true);
        }

        refreshButtons(fileInstance, Utilities.XSLT_GROUP);
        refreshButtons(fileInstance, Utilities.XSD_GROUP);
    }

    public void refreshButtons(FileInstance fileInstance, int groupId){
        Enumeration buttons;
        if(groupId == Utilities.XSD_GROUP)
            buttons = groupXsd.getElements();
        else
            buttons = groupXslt.getElements();

        while(buttons.hasMoreElements()){
            JRadioButton jRadioButton = (JRadioButton)buttons.nextElement();
            if(jRadioButton.getText().equals(fileInstance.getConversionScriptName()) || jRadioButton.getText().equals(fileInstance.getValidationSchemaName())){
                jRadioButton.setSelected(true);
                break;
            }
        }
    }

    private void doChecks(){
        //2 Files MUST exist - xsl/default.xsl and xsl/languages.xml
        String errorMsg = "";
        if(isFileMissing(Utilities.DEFAULT_XSL_FILE_PATH))
            errorMsg = MessageFormat.format(labels.getString("fileNotFound"), Utilities.DEFAULT_XSL_FILE_PATH) + "\n";

        if(isFileMissing(Utilities.LANGUAGES_XML_FILE_PATH))
            errorMsg += MessageFormat.format(labels.getString("fileNotFound"), Utilities.LANGUAGES_XML_FILE_PATH) + "\n";

        if(isFileMissing(Utilities.BEFORE_XSL_FILE_PATH))
            errorMsg += MessageFormat.format(labels.getString("fileNotFound"), Utilities.BEFORE_XSL_FILE_PATH) + "\n";

        if(StringUtils.isNotEmpty(errorMsg)){
            errorMsg += "\n" + labels.getString("exitProgram");
            JOptionPane.showMessageDialog(getContentPane(), errorMsg, labels.getString("error"), JOptionPane.ERROR_MESSAGE, Utilities.icon);
            System.exit(0);
        }

        String repositoryCodeIdentifier = getRepositoryCodeIdentifier();
        if(repositoryCodeIdentifier == null){
            do {
                repositoryCodeIdentifier = (String)JOptionPane.showInputDialog(getContentPane(), labels.getString("enterIdentifier") + "\n" + labels.getString("modifyInOption") + "\n\n", labels.getString("chooseId"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            } while(repositoryCodeIdentifier == null || repositoryCodeIdentifier.equals(""));
            retrieveFromDb.saveRepositoryCode(repositoryCodeIdentifier);
        }

        String countrycode = getCountryCode();
        if(countrycode == null){
            do {
                countrycode = (String)JOptionPane.showInputDialog(getContentPane(), labels.getString("enterCountryCode") + "\n" + labels.getString("modifyInOption") + "\n\n", labels.getString("chooseCountryCode"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            } while(countrycode == null || countrycode.equals(""));
            retrieveFromDb.saveCountryCode(countrycode);
        }

        defaultRoleType = retrieveFromDb.retrieveRoleType();
        useExistingRoleType = retrieveFromDb.retrieveUseExistingRoleType();
        retrieveFromDb.checkForUpdates(getContentPane(), labels.getString("newVersionAvailable"), VERSION_NB);
    }

    /**
     * Erase old temp files (temp_* and .temp_*) from a directory
     * @param dir The directory where we want to erase temp files
     */
    private void eraseOldTempFiles(File dir){
        if(dir.isDirectory()){
            for(File file : dir.listFiles()){
                file.delete();
            }
        }
    }

    /**
     * Checks if a file or a directory exists
     * @param filePath Path of the file to be checked
     * @return True if the file/dir exists, False if not
     */
    private boolean isFileMissing(String filePath){
        return !(new File(filePath)).exists();
    }

    public String getCountryCode(){
        return retrieveFromDb.retrieveCountryCode();
    }

    public String getRepositoryCodeIdentifier(){
        return retrieveFromDb.retrieveRepositoryCode();
    }

    public HashMap<String, String> getParams(){
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("countrycode", getCountryCode());
        parameters.put("mainagencycode", getRepositoryCodeIdentifier());
        parameters.put("versionnb", VERSION_NB);

        parameters.put("defaultRoleType", retrieveFromDb.retrieveRoleType());

        parameters.put("useDefaultRoleType", Boolean.toString(!(retrieveFromDb.retrieveUseExistingRoleType())));
        //todo: Need one for languages.xml file?
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

    public JList getList() {
        return list;
    }
    public Map<String, FileInstance> getFileInstances() {
        return fileInstances;
    }

    public void disableAllBtnAndItems() {
        apePanel.getApeTabbedPane().disableConversionEseBtn();
        apePanel.getApeTabbedPane().disableConversionBtn();
        apePanel.getApeTabbedPane().disableValidationBtn();

        convertItem.setEnabled(false);
        validateItem.setEnabled(false);
    }

    public void enableValidationBtns() {
        apePanel.getApeTabbedPane().enableValidationBtn();
        validateItem.setEnabled(true);
    }

    public void enableConversionBtns() {
        apePanel.getApeTabbedPane().enableConversionBtn();
        convertItem.setEnabled(true);
    }

    public void enableEseConversionBtn() {
        apePanel.getApeTabbedPane().enableConversionEseBtn();
    }
    public void disableConversionEseBtn() {
        convertEseSelectionBtn.setEnabled(false);
    }

//    public void enableAbortBtn() {
//        abort.setEnabled(true);
//    }
//    public void disableAbortBtn() {
//        abort.setEnabled(false);
//    }

    public void enableSaveBtn() {
        saveSelectedItem.setEnabled(true);
    }

    private void enableAllBtnAndItems() {
        enableConversionBtns();
        enableValidationBtns();
        apePanel.getApeTabbedPane().enableConversionEseBtn();
    }

    private class LanguageActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("English")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("en"));
            } else if(e.getActionCommand().equals("fr")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("fr"));
            } else if(e.getActionCommand().equals("de")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("de"));
            } else if(e.getActionCommand().equals("hu")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("hu"));
            } else if(e.getActionCommand().equals("xx")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("xx"));
            }
            changeAllTextLg();
        }
    }
}