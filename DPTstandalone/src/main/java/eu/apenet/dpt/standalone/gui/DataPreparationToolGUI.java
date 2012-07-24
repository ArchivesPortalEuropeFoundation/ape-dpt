package eu.apenet.dpt.standalone.gui;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import eu.apenet.dpt.standalone.gui.conversion.CounterThread;
import eu.apenet.dpt.standalone.gui.ead2ese.ConvertEseActionListener;
import eu.apenet.dpt.standalone.gui.hgcreation.*;
import eu.apenet.dpt.standalone.gui.validation.ValidateActionListener;

import eu.apenet.dpt.standalone.gui.validation.ValidateSelectionActionListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;

import eu.apenet.dpt.standalone.gui.adhoc.EadidQueryComponent;
import eu.apenet.dpt.standalone.gui.adhoc.FileNameComparator;
import eu.apenet.dpt.standalone.gui.db.DBUtil;
import eu.apenet.dpt.standalone.gui.eagcreation.MyTableCellRenderer;
import eu.apenet.dpt.standalone.gui.eagcreation.MyTableModel;
import eu.apenet.dpt.standalone.gui.edition.CheckList;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.service.stax.StaxTransformationTool;
import eu.apenet.dpt.utils.util.CountCLevels;
import eu.apenet.dpt.utils.util.FileUtil;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;

/**
 * User: Yoann
 * Date: Apr 19, 2010
 * Time: 8:07:25 PM
 */

public class DataPreparationToolGUI extends JFrame {
    private final static Logger LOG = Logger.getLogger(DataPreparationToolGUI.class);

    public static final String VERSION_NB = "1.2.0-SNAPSHOT";

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
    private JMenuItem saveItem = new JMenuItem();
    private JMenuItem saveAllItem = new JMenuItem();
    private JMenuItem quitItem = new JMenuItem();
    private JMenuItem identifierItem = new JMenuItem();
    private JMenuItem countryCodeItem = new JMenuItem();
    private JMenuItem xsltItem = new JMenuItem();
    private JMenuItem createEagItem = new JMenuItem();
    private JMenuItem optionalRoleTypesItem = new JMenuItem();
    private JMenu defaultXslSelectionSubmenu = new JMenu();
    private JMenu defaultXsdSelectionSubmenu = new JMenu();
    private JMenuItem validateItem = new JMenuItem();
    private JMenuItem convertItem = new JMenuItem();

    private JMenuItem summaryWindowItem = new JMenuItem();
    private JMenuItem validationWindowItem = new JMenuItem();
    private JMenuItem conversionWindowItem = new JMenuItem();
    private JMenuItem eseConversionWindowItem = new JMenuItem();
    private JMenuItem editionWindowItem = new JMenuItem();

    private JMenuItem internetHelpItem = new JMenuItem();
    private JMenuItem internetApenetItem = new JMenuItem();
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
    private FileUtil fileUtil;
    private DBUtil dbUtil;
    private DateNormalization dateNormalization;

    private JMenuItem menuItem = new JMenuItem();

    private JFrame eagCreationFrame;
//    private JFrame roleTypeFrame;

    /**
     * Locations
     */
    public String globalIdentifier;
    public String countrycode;
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
        fileUtil = new FileUtil();
        Locale currentLocale = Locale.getDefault();
        labels = ResourceBundle.getBundle("i18n/apeBundle", currentLocale);
        apePanel = new APEPanel(labels, getContentPane(), this);

        CheckList checkList = new CheckList();
        langList = checkList.getLangList();
        levelList = checkList.getLevelList();

        dateNormalization = new DateNormalization();

        super.setTitle(labels.getString("title"));
        nameComponents();
        Image topLeftIcon = Utilities.icon.getImage();
        setIconImage(topLeftIcon);

        doChecks();

        if(isFileMissing(Utilities.OUTPUT_DIR))
            new File(Utilities.OUTPUT_DIR).mkdir();

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
        saveItem.setEnabled(false);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(saveItem);
        saveAllItem.setEnabled(false);
        fileMenu.add(saveAllItem);
        fileMenu.addSeparator();
        fileMenu.add(quitItem);
        optionMenu.add(identifierItem);
        optionMenu.add(countryCodeItem);
        optionMenu.add(xsltItem);
        optionMenu.add(languageMenu);
        optionMenu.add(createEagItem);
        optionMenu.add(optionalRoleTypesItem);
        optionMenu.addSeparator();
        optionMenu.add(defaultXslSelectionSubmenu);
        optionMenu.add(defaultXsdSelectionSubmenu);
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
        helpMenu.add(internetHelpItem);
        helpMenu.add(internetApenetItem);
        helpMenu.addSeparator();
        JMenuItem versionItem = new JMenuItem("APE DPT v"+ VERSION_NB);
        versionItem.setEnabled(false);
        helpMenu.add(versionItem);
        createLanguageMenu();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);

        getContentPane().add(menuBar, BorderLayout.NORTH);

        apePanel.setFilename("");

        createHgListener = new CreateHGListener(dbUtil, labels, getContentPane(), fileUtil, fileInstances, list, this);
        createHGBtn.addActionListener(createHgListener);
        createHGBtn.setEnabled(false);

        validateItem.addActionListener(new ValidateActionListener(labels, fileInstances, this, apePanel.getApeTabbedPane()));
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
        saveItem.setText(labels.getString("saveFile"));
        saveAllItem.setText(labels.getString("saveAllFile"));
        quitItem.setText(labels.getString("quit"));
        identifierItem.setText(labels.getString("globalId"));
        xsltItem.setText(labels.getString("ownXsl"));
        validateItem.setText(labels.getString("validate"));
        convertItem.setText(labels.getString("convert"));
        convertAndValidateBtn.setText(labels.getString("convertAndValidate"));
        createHGBtn.setText(labels.getString("createHG"));
        progressLabel.setText(labels.getString("chooseFile"));
//        abort.setText(labels.getString("abort"));
        languageMenu.setText(labels.getString("languageMenu"));
        menuItem.setText(labels.getString("removeFile"));

        windowMenu.setText(labels.getString("windows"));
        summaryWindowItem.setText(labels.getString("summary"));
        validationWindowItem.setText(labels.getString("validation"));
        conversionWindowItem.setText(labels.getString("conversion"));
        eseConversionWindowItem.setText(labels.getString("eseConversion"));
        editionWindowItem.setText(labels.getString("edition"));

        helpMenu.setText(labels.getString("help"));
        internetHelpItem.setText(labels.getString("xsltGuidelines"));
        internetApenetItem.setText(labels.getString("projectWebsite"));

        countryCodeItem.setText(labels.getString("countryCode"));
        createEagItem.setText(labels.getString("createEag"));
        optionalRoleTypesItem.setText(labels.getString("defaultRoleType"));
        validateSelectionBtn.setText(labels.getString("validateSelected"));
        convertEseSelectionBtn.setText(labels.getString("convertEseSelectionBtn"));

        defaultXsdSelectionSubmenu.setText(labels.getString("defaultXsdSelectionSubmenu"));
        defaultXslSelectionSubmenu.setText(labels.getString("defaultXslSelectionSubmenu"));
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

        if(list.getSelectedValue() != null && fileInstances.get(((File)list.getSelectedValue()).getName()).isConverted())
            apePanel.getApeTabbedPane().setValidationBtnText(labels.getString("validateBtn"));
        else
            apePanel.getApeTabbedPane().setValidationBtnText(labels.getString("validate"));
    }

    private void createLanguageMenu(){
        Locale currentLocale = Locale.getDefault();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("English");
        if(!currentLocale.getLanguage().equals("fr") && !currentLocale.getLanguage().equals("de") && !currentLocale.getLanguage().equals("xx")){
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Français");
        if(currentLocale.getLanguage().equals("fr")){
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Deutsch");
        if(currentLocale.getLanguage().equals("de")){
            rbMenuItem.setSelected(true);
        }
        rbMenuItem.addActionListener(new LanguageActionListener());
        group.add(rbMenuItem);
        languageMenu.add(rbMenuItem);


//        languageMenu.addSeparator();
//        rbMenuItem = new JRadioButtonMenuItem("XXXXXX");
//        if(currentLocale.getLanguage().equals("xx")){
//            rbMenuItem.setSelected(true);
//        }
//        rbMenuItem.addActionListener(new LanguageActionListener());
//        group.add(rbMenuItem);
//        languageMenu.add(rbMenuItem);
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
                if(actionEvent.getSource() == fileItem){
                    if (currentLocation != null)
                        fileChooser.setCurrentDirectory(currentLocation);
                    else //Todo: To erase this condition, only for tests with my computer
                        fileChooser.setCurrentDirectory(new File("/Users/yoannmoranville/Work/APEnet/Projects/data"));
                    int returnedVal = fileChooser.showOpenDialog(getParent());

                    if(returnedVal == JFileChooser.APPROVE_OPTION){
                        currentLocation = fileChooser.getCurrentDirectory();
                        File[] files = fileChooser.getSelectedFiles();
                        if(files == null || files.length == 0){
                            File file = fileChooser.getSelectedFile();
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
                        } else {
                            for(File file : files){
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
            }
        });
        identifierItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                globalIdentifier = createOptionPane(false);
                if(globalIdentifier != null && !"".equals(globalIdentifier))
                    saveOrUpdateIdentifier(globalIdentifier, null);
                else
                    globalIdentifier = retrieveIdentifier();
            }
        });
        countryCodeItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                countrycode = createOptionPane(true);
                if(countrycode != null && !"".equals(countrycode))
                    saveOrUpdateIdentifier(globalIdentifier, countrycode);
                else
                    countrycode = retrieveCountryCode();
            }
        });
        createEagItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                createEagForm();
            }
        });
        optionalRoleTypesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame roleTypeFrame = new RoleTypeFrame(labels, dbUtil);

                roleTypeFrame.setPreferredSize(new Dimension(getContentPane().getWidth() *3/8, getContentPane().getHeight() *3/8));
                roleTypeFrame.setLocation(getContentPane().getWidth() / 8, getContentPane().getHeight() / 8);

                roleTypeFrame.pack();
                roleTypeFrame.setVisible(true);
            }
        });
        saveItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String filename = ((File)list.getSelectedValue()).getName();
                FileInstance file = fileInstances.get(filename);
                String filePrefix = file.getFileType().getFilePrefix();

                if(!file.isValid())
                    filePrefix = "NOT_" + filePrefix;
                if(tree != null && tree.getTreeTableModel() != null && !file.getLastOperation().equals(FileInstance.Operation.CONVERT)){
                    TreeTableModel treeTableModel = tree.getTreeTableModel();
                    Document document = (Document)treeTableModel.getRoot();
                    try {
                        File file2 = new File(Utilities.OUTPUT_DIR + filePrefix + "_" + filename);
                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer output = tf.newTransformer();
                        output.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
                        output.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");

                        output.transform(new DOMSource(document.getFirstChild()),new StreamResult(file2));

                        file.setLastOperation(FileInstance.Operation.SAVE);
                        file.setCurrentLocation(file2.getAbsolutePath());
                        JOptionPane.showMessageDialog(getContentPane(), labels.getString("fileInOutput")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                    } catch (Exception ex){
                        createErrorOrWarningPanel(ex, true, labels.getString("errorSavingTreeXML"), getContentPane());
//                        throw new RuntimeException(ex);
                    }
                } else if(file.isConverted()){
                    File newFile = new File(Utilities.OUTPUT_DIR + filePrefix + "_" + filename);
                    (new File(file.getCurrentLocation())).renameTo(newFile);
                    file.setLastOperation(FileInstance.Operation.SAVE);
                    file.setCurrentLocation(newFile.getAbsolutePath());
                    JOptionPane.showMessageDialog(getContentPane(), labels.getString("fileInOutput")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                } else {
                    try{
                        File newFile = new File(Utilities.OUTPUT_DIR + filePrefix + "_" + filename);
                        fileUtil.copyFile((File)list.getSelectedValue(), new File(Utilities.OUTPUT_DIR + filePrefix + "_" + filename));
                        file.setLastOperation(FileInstance.Operation.SAVE);
                        file.setCurrentLocation(newFile.getAbsolutePath());
                        JOptionPane.showMessageDialog(getContentPane(), labels.getString("fileInOutput")+".", labels.getString("fileSaved"), JOptionPane.INFORMATION_MESSAGE, Utilities.icon);
                    } catch (IOException ioe){
                        LOG.error("Error when saving file", ioe);
                    }
                }
            }
        });
        saveAllItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for(String key : fileInstances.keySet()){
                    FileInstance fileInstance = fileInstances.get(key);

                    key = key.startsWith("temp_")?key.replace("temp_", ""):key;

                    File newFile = null;
                    if(fileInstance.isValid())
                        newFile = new File(Utilities.OUTPUT_DIR + fileInstance.getFileType().getFilePrefix() + "_" + key);
                    else if(!fileInstance.getLastOperation().equals(FileInstance.Operation.NONE))
                        newFile = new File(Utilities.OUTPUT_DIR + "NOT_" + fileInstance.getFileType().getFilePrefix() + "_" + key);

                    if(newFile != null){
                        try {
                            if(fileInstance.getCurrentLocation().equals(""))
                                FileUtils.copyFile(new File(fileInstance.getOriginalPath()), newFile);
                            else
                                new File(fileInstance.getCurrentLocation()).renameTo(newFile);
                            fileInstance.setCurrentLocation(newFile.getAbsolutePath());
                            fileInstance.setLastOperation(FileInstance.Operation.SAVE);
                        } catch (IOException ex){
                            LOG.error("Could not save file: " + key, ex);
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
                    if(apePanel.getApeTabbedPane().getSelectedIndex() == APETabbedPane.TAB_EDITION)
                        apePanel.getApeTabbedPane().createEditionTree(((File)list.getSelectedValue()));
                    apePanel.getApeTabbedPane().changeBackgroundColor(APETabbedPane.TAB_CONVERSION, Utilities.TAB_COLOR);
                    apePanel.getApeTabbedPane().changeBackgroundColor(APETabbedPane.TAB_VALIDATION, Utilities.TAB_COLOR);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if(list.getSelectedValues().length == 1){
                        final int indexToErase = list.locationToIndex(e.getPoint());
                        list.setSelectedIndex(indexToErase);
                    }
                    JPopupMenu popup = new JPopupMenu();
                    menuItem.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            try {
                                if(list.getSelectedValues().length > 1){
                                    model.removeFiles(list.getSelectedValues());
                                } else {
                                    model.removeFile((File)list.getSelectedValue());
                                }
                            } catch (Exception ex){
                                createErrorOrWarningPanel(ex, true, labels.getString("errorRemovingFileFromList"), getContentPane());
                            } finally {
                                menuItem.removeActionListener(this);
                            }
                        }
                    });
                    popup.add(menuItem);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        list.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if(list.getSelectedValue() != null && list.getSelectedValues().length > 1){
                    convertAndValidateBtn.setEnabled(true);
                    validateSelectionBtn.setEnabled(true);
                    convertEseSelectionBtn.setEnabled(true);
                    createHGBtn.setEnabled(true);
                    disableAllBtnAndItems();
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

        internetHelpItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                BareBonesBrowserLaunch.openURL("http://spacemomo.no-ip.org/APEnet_validator/guidelines_xslt.html");
            }
        });
        internetApenetItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                BareBonesBrowserLaunch.openURL("http://www.apenet.eu/");
            }
        });
    }

    private void createEagForm(){
        //3/4 size of contentPane - 1/8 from left and from top
        eagCreationFrame = new JFrame(labels.getString("eagFrameTitle"));
        int width = getContentPane().getWidth() * 3/4;
        int height = getContentPane().getHeight() /2;
        eagCreationFrame.setPreferredSize(new Dimension(width, height));
        eagCreationFrame.setLocation(getContentPane().getWidth() / 8, getContentPane().getHeight() / 8);
        eagCreationFrame.add(createEagFormComponents());
        eagCreationFrame.pack();
        eagCreationFrame.setVisible(true);
    }

    private Component createEagFormComponents(){
        JPanel eagPanel = new JPanel();
        eagFormTable = new JTable(new MyTableModel(labels, globalIdentifier));
        eagFormTable.setDefaultRenderer(String.class, new MyTableCellRenderer());
        eagFormTable.setGridColor(Color.BLACK);
        eagFormTable.setRowHeight(30);
        eagFormTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        eagFormTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        eagFormTable.getColumnModel().getColumn(1).setPreferredWidth(getContentPane().getWidth() * 3/4 - 200);
        eagPanel.setLayout(new BorderLayout());
        eagPanel.add(eagFormTable.getTableHeader(), BorderLayout.PAGE_START);
        eagPanel.add(eagFormTable, BorderLayout.CENTER);
        eagPanel.add(eagCreationButtons(), BorderLayout.PAGE_END);
        return eagPanel;
    }

    private Component eagCreationButtons(){
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        JButton buttonCancel = new JButton(labels.getString("cancelBtn"));
        buttonCancel.setPreferredSize(new Dimension(50, 30));
        buttonCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ((MyTableModel) eagFormTable.getModel()).eraseAllData();
                eagCreationFrame.setAlwaysOnTop(false);
                eagCreationFrame.setVisible(false);
            }
        });
        JButton buttonSave = new JButton(labels.getString("saveBtn"));
        buttonSave.setPreferredSize(new Dimension(50, 30));
        buttonSave.addActionListener(new SaveEagFileListener());
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(buttonCancel);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(buttonSave);
        buttonPanel.add(new JLabel(""));
        return buttonPanel;
    }

    private class SaveEagFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                eagFormTable.getCellEditor().stopCellEditing();
                HashMap<String, String> parameters = ((MyTableModel) eagFormTable.getModel()).getParamsEag();
                if(parameters == null)
                    throw new NullPointerException();
                parameters.put("countrycode", countrycode);
                parameters.put("repositorycode", globalIdentifier);
                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                parameters.put("date_creation", sdf.format(today));
                String identifier = parameters.get("eagid");
                InputStream in = getClass().getResourceAsStream("/EAG_XML_XSL/EAG_basic.xml");
                File outputFile = new File(Utilities.TEMP_DIR + "EAG_" + identifier + ".xml");
                TransformationTool.createTransformation(in, outputFile, getClass().getResourceAsStream("/EAG_XML_XSL/createEAG.xsl"), parameters, true, true, null, true, null);

                List<SAXParseException> exceptions = DocumentValidation.xmlValidation(new FileInputStream(outputFile), Xsd_enum.XSD_APE_EAG_SCHEMA);
                if(exceptions != null)
                    throw new Exception("There are validation exceptions in the newly created EAG.");
                model.addFile(outputFile);
                FileInstance eagFileInstance = fileInstances.get("EAG_" + identifier + ".xml");
                eagFileInstance.setFileType(FileInstance.FileType.EAG);
                eagFileInstance.setIsConverted();
                eagFileInstance.setLastOperation(FileInstance.Operation.CONVERT);
                eagFileInstance.setCurrentLocation(Utilities.TEMP_DIR + "EAG_" + identifier + ".xml");
                eagFileInstance.setValidationSchema(Xsd_enum.XSD_APE_EAG_SCHEMA);

            } catch (NullPointerException npe) {
                createErrorOrWarningPanel(npe, false, labels.getString("missingFields"), eagCreationFrame);
            } catch (Exception ex){
                createErrorOrWarningPanel(ex, true, labels.getString("eagCreationError"), eagCreationFrame);
            }
        }
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
        if(file.isDirectory())
            return false;
        //todo: Do more checks on files
        return true;
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
                //                refreshButtons(fileInstance, groupXsd);
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

    private String createOptionPane(boolean isCountryCode){
        String text = labels.getString("enter");
        if(!isCountryCode && globalIdentifier != null){
            text += "\n" + labels.getString("currentId") + ": '" + globalIdentifier + "'";
        }
        if(isCountryCode && countrycode != null){
            text += "\n" + labels.getString("currentCountryCode") + ": '" + countrycode + "'";
        }
        String result = (String)JOptionPane.showInputDialog(getContentPane(), text, labels.getString("chooseId"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
        if(result == null || result.length() == 0){
            return null;
        }
        return result;
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
            apeTabbedPane.setValidationBtnText(labels.getString("validateBtn"));
        } else {
            convertItem.setEnabled(true);
            apeTabbedPane.enableConversionBtn();
            apeTabbedPane.setValidationBtnText(labels.getString("validate"));
        }
        if(fileInstance.isValid()){
            validateItem.setEnabled(false);
            apeTabbedPane.disableValidationBtn();
            saveItem.setEnabled(true);
            if(fileInstance.getValidationSchema() == Xsd_enum.XSD_APE_SCHEMA || fileInstance.getValidationSchema() == Xsd_enum.XSD1_0_APE_SCHEMA)
                apeTabbedPane.enableConversionEseBtn();
        } else {
            validateItem.setEnabled(true);
            apeTabbedPane.enableValidationBtn();
            apeTabbedPane.disableConversionEseBtn();
            saveItem.setEnabled(false);
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

        connectDatabase();

        globalIdentifier = retrieveIdentifier();
        if(globalIdentifier == null){
            do {
                globalIdentifier = (String)JOptionPane.showInputDialog(getContentPane(), labels.getString("enterIdentifier") + "\n" + labels.getString("modifyInOption") + "\n\n", labels.getString("chooseId"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            } while(globalIdentifier == null || globalIdentifier.equals(""));
            saveOrUpdateIdentifier(globalIdentifier, null);
        }

        countrycode = retrieveCountryCode();
        if(countrycode == null){
            do {
                countrycode = (String)JOptionPane.showInputDialog(getContentPane(), labels.getString("enterCountryCode") + "\n" + labels.getString("modifyInOption") + "\n\n", labels.getString("chooseCountryCode"), JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
            } while(countrycode == null || countrycode.equals(""));
            saveOrUpdateIdentifier(globalIdentifier, countrycode);
        }

        defaultRoleType = dbUtil.retrieveRoleType();
        useExistingRoleType = dbUtil.retrieveUseExistingRoleType();
        checkForUpdates();
    }

    private void checkForUpdates(){
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();

        String date;
        String query = DBUtil.createSelectQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.OptionKeys.OPTION_UPDATEDATE.getName());
        String[] res = dbUtil.retrieveSqlListResult(query, DBUtil.DBNames.COLUMN_VALUE);
        if(res.length > 0){
            date = res[0];
        } else {
            query = DBUtil.createInsertQuery(DBUtil.DBNames.TABLE_OPTIONS.getName());
            date = dateFormatYYYYMMDD.format(today);
            dbUtil.doSqlQuery(query, Arrays.asList(DBUtil.OptionKeys.OPTION_UPDATEDATE.getName(), date));
        }

        boolean doCheckUpdate = false;
        try {
            Date lastUpdate = dateFormatYYYYMMDD.parse(date);
            //Milliseconds between both dates
            long difference = today.getTime() - lastUpdate.getTime();

            long twoWeeksInMilliseconds = 14 * 24 * 60 * 60 * 1000;

            if((twoWeeksInMilliseconds - difference) < 0){
                LOG.info("We do a check for updates - it has been more than 2 weeks since the last one.");
                doCheckUpdate = true;
                query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), dateFormatYYYYMMDD.format(today), DBUtil.OptionKeys.OPTION_UPDATEDATE.getName());
                dbUtil.doSqlQuery(query, null);
                LOG.info("We just updated the database with the date of today - next check in 2 weeks.");
            } else {
                LOG.info("We do not do a check for updates");
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        if(doCheckUpdate){
            try {
                URL url = new URL("http://www.archivesportaleurope.eu/Portal/dptupate/version.action?versionNb=" + VERSION_NB);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if(connection.getResponseCode() == 200){
                    //There is a new version
                    LOG.info("New version available...");
                    if(JOptionPane.showConfirmDialog(getContentPane(), labels.getString("newVersionAvailable")) == 0){
                        BareBonesBrowserLaunch.openURL("http://www.apenet.eu/index.php?option=com_content&view=article&id=94&Itemid=150&lang=en");
                        System.exit(0);
                    }
                }
//                LOG.info(connection.getResponseCode());
            } catch (Exception e) {
                LOG.error("Error to connect for checking the new version (probably no internet connection)", e);
            }
        }
    }

    private void connectDatabase(){
        dbUtil = new DBUtil();
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

    private void saveOrUpdateIdentifier(String identifier, String countryCode){
        if(countryCode == null){
            if(retrieveIdentifier() != null){
                String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), identifier, DBUtil.OptionKeys.OPTION_GLOBALID.getName());
                dbUtil.doSqlQuery(query, null);
            } else {
                String query = DBUtil.createInsertQuery(DBUtil.DBNames.TABLE_OPTIONS.getName());
                dbUtil.doSqlQuery(query, Arrays.asList(DBUtil.OptionKeys.OPTION_GLOBALID.getName(), identifier));
            }
        } else {
            if(retrieveCountryCode() != null){
                String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), countryCode, DBUtil.OptionKeys.OPTION_COUNTRYCODE.getName());
                dbUtil.doSqlQuery(query, null);
            } else {
                String query = DBUtil.createInsertQuery(DBUtil.DBNames.TABLE_OPTIONS.getName());
                dbUtil.doSqlQuery(query, Arrays.asList(DBUtil.OptionKeys.OPTION_COUNTRYCODE.getName(), countryCode));
            }
        }
    }

    private String retrieveIdentifier(){
        String query = DBUtil.createSelectQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.OptionKeys.OPTION_GLOBALID.getName());
        String[] res = dbUtil.retrieveSqlListResult(query, DBUtil.DBNames.COLUMN_VALUE);
        if(res.length > 0){
            return res[0];
        }
        return null;
    }

    private String retrieveCountryCode(){
        String query = DBUtil.createSelectQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.OptionKeys.OPTION_COUNTRYCODE.getName());
        String[] res = dbUtil.retrieveSqlListResult(query, DBUtil.DBNames.COLUMN_VALUE);
        if(res.length > 0){
            return res[0];
        }
        return null;
    }

    public String getCountrycode(){
        return countrycode;
    }

    public String getGlobalIdentifier(){
        return globalIdentifier;
    }

    public HashMap<String, String> getParams(){
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("countrycode", countrycode);
        parameters.put("mainagencycode", globalIdentifier);
        parameters.put("versionnb", VERSION_NB);

        parameters.put("defaultRoleType", dbUtil.retrieveRoleType());

        parameters.put("useDefaultRoleType", Boolean.toString(!(dbUtil.retrieveUseExistingRoleType())));
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

    public void enableSaveAllBtn() {
        saveAllItem.setEnabled(true);
    }

    public void enableSimpleSaveBtn() {
        saveItem.setEnabled(true);
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
            } else if(e.getActionCommand().equals("Français")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("fr"));
            } else if(e.getActionCommand().equals("Deutsch")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("de"));
            } else if(e.getActionCommand().equals("XXXXXX")){
                labels = ResourceBundle.getBundle("i18n/apeBundle", new Locale("xx"));
            }
            changeAllTextLg();
        }
    }
}