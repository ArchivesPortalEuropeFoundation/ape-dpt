package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.conversion.ConvertActionListener;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.ead2ese.ConvertEseActionListener;
import eu.apenet.dpt.standalone.gui.edition.ActionNamesEnum;
import eu.apenet.dpt.standalone.gui.edition.NodeAppendable;
import eu.apenet.dpt.standalone.gui.edition.XMLTreeTableModel;
import eu.apenet.dpt.standalone.gui.edition.XMLTreeTableRenderer;
import eu.apenet.dpt.standalone.gui.ese2edm.ConvertEdmActionListener;
import eu.apenet.dpt.standalone.gui.validation.DownloadReportActionListener;
import eu.apenet.dpt.standalone.gui.validation.ValidateActionListener;
import eu.apenet.dpt.standalone.gui.xsdAddition.XsdObject;
import eu.apenet.dpt.utils.util.DOMUtil;
import eu.apenet.dpt.utils.util.Xsd_enum;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 * User: Yoann Moranville
 * Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class APETabbedPane extends JTabbedPane {
    private static final Logger LOG = Logger.getLogger(APETabbedPane.class);

    public static final int TAB_SUMMARY = 0;
    public static final int TAB_VALIDATION = 1;
    public static final int TAB_CONVERSION = 2;
    public static final int TAB_ESE = 3;
    public static final int TAB_EDITION = 4;

    private static final Dimension DEFAULT_BTN_DIMENSION = new Dimension(100, 40);

    private JTextArea validationErrors;
    private JTextArea conversionErrors;
    private JTextArea eseConversionErrors;

    private JScrollPane validationPane;
    private JScrollPane conversionPane;
    private JScrollPane eseConversionPane;
    private JScrollPane editionPane;

    private JButton convertBtn;
    private JButton validateBtn;
    private JButton convertEseBtn;
    private JButton convertEdmBtn;
    private JButton reportBtn;

    private JPanel xsltPanel;
    private JPanel xsdPanel;

    private ResourceBundle labels;
    private APEPanel apePanel;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private RetrieveFromDb retrieveFromDb;
    private Component parent;

    private JXTreeTable tree;

    public APETabbedPane(ResourceBundle labels, APEPanel apePanel, Component parent, DataPreparationToolGUI dataPreparationToolGUI, RetrieveFromDb retrieveFromDb) {
        super();
        this.labels = labels;
        this.apePanel = apePanel;
        this.parent = parent;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.tree = dataPreparationToolGUI.getTree();
        this.retrieveFromDb = retrieveFromDb;

        create();
        initialize();

        addTab(labels.getString("summary"), makeTextPanelSummary());
        addTab(labels.getString("validation"), makeTextPanel(TAB_VALIDATION));
        addTab(labels.getString("conversion"), makeTextPanel(TAB_CONVERSION));
        addTab(labels.getString("eseConversion"), makeTextPanel(TAB_ESE));
        addTab(labels.getString("edition"), makeTextPanel(TAB_EDITION));
        addMouseListener(new APETabbedPaneMouseAdapter(this));

        changeLanguage(labels);
    }

    private void create() {
        validationErrors = new JTextArea();
        reportBtn = new JButton();
        JPanel validationPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(reportBtn);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        validationPanel.add(buttonPanel, BorderLayout.NORTH);
        validationPanel.add(validationErrors, BorderLayout.CENTER);
        conversionErrors = new JTextArea();
        eseConversionErrors = new JTextArea();
        validationPane = new JScrollPane(validationPanel);
        conversionPane = new JScrollPane(conversionErrors);
        eseConversionPane = new JScrollPane(eseConversionErrors);
        editionPane = new JScrollPane();

        convertBtn = new JButton();
        validateBtn = new JButton();
        convertEseBtn = new JButton();
        convertEdmBtn = new JButton();

        xsltPanel = new JPanel();
        xsdPanel = new JPanel();
    }

    private void initialize() {
        editionPane.setViewportView(createEditionTree(null));

        validationErrors.setEditable(false);
        validationErrors.setLineWrap(true);
        validationErrors.setWrapStyleWord(true);
        conversionErrors.setEditable(false);
        conversionErrors.setLineWrap(true);
        conversionErrors.setWrapStyleWord(true);
        eseConversionErrors.setEditable(false);
        eseConversionErrors.setLineWrap(true);
        eseConversionErrors.setWrapStyleWord(true);

        convertBtn.addActionListener(new ConvertActionListener(parent, dataPreparationToolGUI, apePanel));
        convertBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        convertBtn.setEnabled(false);

        convertEseBtn.addActionListener(new ConvertEseActionListener(labels, dataPreparationToolGUI, apePanel));
        convertEseBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        convertEseBtn.setEnabled(false);

        convertEdmBtn.addActionListener(new ConvertEdmActionListener(parent, dataPreparationToolGUI, apePanel));
        convertEdmBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        convertEdmBtn.setEnabled(false);

        validateBtn.addActionListener(new ValidateActionListener(dataPreparationToolGUI, this));
        validateBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        validateBtn.setEnabled(false);

        reportBtn.addActionListener(new DownloadReportActionListener(dataPreparationToolGUI));
        reportBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        reportBtn.setEnabled(false);
    }

    public void changeBackgroundColor(int tab, Color color) {
        setBackgroundAt(tab, color);
    }

    public void checkFlashingTab(int tabIndex, Color color) {
        if(getSelectedIndex() != tabIndex)
            changeBackgroundColor(tabIndex, color);
    }

    public void addToXsltPanel(Component component) {
        xsltPanel.add(component);
        this.repaint();
    }

    public void addToXsdPanel(Component component) {
        xsdPanel.add(component);
        this.repaint();
    }

    public DataPreparationToolGUI getDataPreparationToolGUI() {
        return dataPreparationToolGUI;
    }

    public void appendEseConversionErrorText(String text) {
        eseConversionErrors.append(text);
    }
    public void setConversionErrorText(String text) {
        conversionErrors.setText(text);
    }
    public void setValidationErrorText(String text) {
        validationErrors.setText(text);
    }
    public void setEseConversionErrorText(String text) {
        eseConversionErrors.setText(text);
    }

    public void enableConversionBtn() {
        convertBtn.setEnabled(true);
    }
    public void disableConversionBtn() {
        convertBtn.setEnabled(false);
    }
    public void enableValidationBtn() {
        validateBtn.setEnabled(true);
    }
    public void disableValidationBtn() {
        validateBtn.setEnabled(false);
    }
    public void enableConversionEseBtn() {
        convertEseBtn.setEnabled(true);
    }
    public void disableConversionEseBtn() {
        convertEseBtn.setEnabled(false);
    }
    public void enableConversionEdmBtn() {
        convertEdmBtn.setEnabled(true);
    }
    public void disableConversionEdmBtn() {
        convertEdmBtn.setEnabled(false);
    }
    public void enableReportBtn() {
        reportBtn.setEnabled(true);
    }
    public void disableReportBtn() {
        reportBtn.setEnabled(false);
    }

    public void changeLanguage(ResourceBundle labels) {
        setTitleAt(TAB_CONVERSION, labels.getString("conversion"));
        setTitleAt(TAB_EDITION, labels.getString("edition"));
        setTitleAt(TAB_SUMMARY, labels.getString("summary"));
        setTitleAt(TAB_VALIDATION, labels.getString("validation"));
        setTitleAt(TAB_ESE, labels.getString("eseConversion"));

        convertBtn.setText(labels.getString("convert"));
        validateBtn.setText(labels.getString("validate"));
        convertEseBtn.setText(labels.getString("summary.convertEse.button"));
        convertEdmBtn.setText(labels.getString("summary.convertEdm.button"));
        reportBtn.setText("Download report of XML QUALITY");
    }

    public void setValidationBtnText(String text) {
        validateBtn.setText(text);
    }

    private JComponent makeTextPanel(int tab){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        if(tab == TAB_VALIDATION){
            p.add(validationPane);
        } else if(tab == TAB_CONVERSION){
            p.add(conversionPane);
        } else if(tab == TAB_ESE){
            p.add(eseConversionPane);
        } else if(tab == TAB_EDITION) {
            p.add(editionPane);
        }
        return p;
    }

    private JComponent makeTextPanelSummary(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

        JPanel summaryItems = new JPanel();
        summaryItems.setLayout(new BoxLayout(summaryItems, BoxLayout.PAGE_AXIS));
        summaryItems.add(makeButtons());
        summaryItems.add(makeCheckboxes());
        summaryItems.add(makeCheckboxesXsd());
        summaryItems.add(makeResultsPanel());

        JScrollPane summaryPane = new JScrollPane(summaryItems);
        p.add(summaryPane);
        return p;
    }

    private JComponent makeButtons(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.add(convertBtn);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(validateBtn);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(convertEseBtn);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(convertEdmBtn);
        return p;
    }

    private JComponent makeCheckboxes() {
        xsltPanel.setLayout(new BoxLayout(xsltPanel, BoxLayout.PAGE_AXIS));
        xsltPanel.add(new JLabel(labels.getString("summary.stylesheets.label") + ":"));
        JRadioButton radioButton;
        for(File xsltFile : Utilities.getXsltFiles()){
            radioButton = new JRadioButton(xsltFile.getName());
            if(xsltFile.getName().equals(Utilities.XSL_DEFAULT_NAME))
                radioButton.setSelected(true);
            radioButton.addActionListener(new XsltSelectorListener(dataPreparationToolGUI));
            dataPreparationToolGUI.getGroupXslt().add(radioButton);
            xsltPanel.add(radioButton);
            xsltPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        return xsltPanel;
    }

    private JComponent makeCheckboxesXsd() {
        xsdPanel.setLayout(new BoxLayout(xsdPanel, BoxLayout.PAGE_AXIS));
        xsdPanel.add(new JLabel(labels.getString("summary.schemas.label") + ":"));
        JRadioButton radioButton;
        for(Xsd_enum xsdEnum : Xsd_enum.values()){
            radioButton = new JRadioButton(xsdEnum.getReadableName());
            if(xsdEnum.equals(Xsd_enum.XSD_APE_SCHEMA))
                radioButton.setSelected(true);
            radioButton.addActionListener(new XsdSelectorListener(dataPreparationToolGUI));
            dataPreparationToolGUI.getGroupXsd().add(radioButton);
            xsdPanel.add(radioButton);
            xsdPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        for (XsdObject additionalXsd : retrieveFromDb.retrieveAdditionalXsds()) {
            radioButton = new JRadioButton(additionalXsd.getName());
            radioButton.addActionListener(new XsdSelectorListener(dataPreparationToolGUI));
            dataPreparationToolGUI.getGroupXsd().add(radioButton);
            xsdPanel.add(radioButton);
            xsdPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        return xsdPanel;
    }

    private JComponent makeResultsPanel(){
        JPanel resultsView = new JPanel();
        resultsView.setLayout(new BoxLayout(resultsView, BoxLayout.PAGE_AXIS));
        dataPreparationToolGUI.getResultArea().setForeground(new Color(255, 100, 100));
        resultsView.add(dataPreparationToolGUI.getResultArea());
        return resultsView;
    }

    public JComponent createMsgEditionTree(String msg){
        JEditorPane waitMessagePane = new JEditorPane();
        waitMessagePane.setEditable(false);
        waitMessagePane.setContentType("text/plain");
        waitMessagePane.setText(msg);
        waitMessagePane.setCaretPosition(0);
        return waitMessagePane;
    }

    /**
     * Creates the XML tree of the file or of the converted file (if file already converted)
     * @param file The file represented in the list
     * @return A JComponent containing the tree if it exists, or an error message if not
     */
    public JComponent createEditionTree(File file) {
        labels = dataPreparationToolGUI.getLabels();
        if(file == null){
            return createMsgEditionTree(labels.getString("noTreeBuild") + "...");
        }
        FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(file.getName());
        if(tree != null && ((XMLTreeTableModel)tree.getTreeTableModel()).getName().equals(file.getName()) && fileInstance.getLastOperation().equals(FileInstance.Operation.CREATE_TREE))
            return null;

        fileInstance.setLastOperation(FileInstance.Operation.CREATE_TREE);
        try {
            InputSource is;
            if(fileInstance.getCurrentLocation() == null || fileInstance.getCurrentLocation().equals("")){
                is = new InputSource(file.getAbsolutePath());
            } else {
                is = new InputSource(fileInstance.getCurrentLocation());
            }
            Document doc = DOMUtil.createDocument(is);
            tree = new JXTreeTable();
            tree.setTreeTableModel(new XMLTreeTableModel(doc, dataPreparationToolGUI.getDateNormalization(), dataPreparationToolGUI.getLevelList(), labels));
            tree.setTreeCellRenderer(new XMLTreeTableRenderer());

            tree.addTreeExpansionListener(new TreeExpansionListener() {
                public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
                    int row = tree.getRowForPath(treeExpansionEvent.getPath());
                    tree.scrollRowToVisible(row);
                    tree.scrollRowToVisible(row + 1);
                    tree.scrollRowToVisible(row + 2);
                    tree.scrollRowToVisible(row + 3);
                    tree.scrollRowToVisible(row + 4);
                }
                public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {}
            });

            expandFirstLevel();
//            tree.expandAll();
//            tree.setHighlighters(HighlighterFactory.createSimpleStriping (HighlighterFactory.QUICKSILVER));
            JScrollPane paneTest = new JScrollPane(tree);
            editionPane.setViewportView(paneTest);
            tree.setEditable(true);
            dataPreparationToolGUI.enableSaveBtn();
            ((XMLTreeTableModel)tree.getTreeTableModel()).setName(file.getName());
        } catch (Exception e){
            tree = null;
            editionPane.setViewportView(createMsgEditionTree(labels.getString("edition.error.fileNoXmlOrCorrupted")));
        }
        return tree;
    }

    private void expandFirstLevel(){
        TreeModel model = tree.getTreeTableModel();
        TreePath root = new TreePath(model.getRoot());
        tree.expandPath(root);

        for (int i = 0; i < tree.getRowCount(); i++) {
            if(i > 20)
                break;
            tree.expandRow(i);
        }
    }

    public class APETabbedPaneMouseAdapter extends MouseAdapter {
        private APETabbedPane apeTabbedPane;

        public APETabbedPaneMouseAdapter(APETabbedPane apeTabbedPane) {
            this.apeTabbedPane = apeTabbedPane;
        }

        @Override
        public void mouseClicked(MouseEvent e){
            if(apeTabbedPane.getSelectedIndex() == TAB_VALIDATION)
                changeBackgroundColor(TAB_VALIDATION, Utilities.TAB_COLOR);
            else if(apeTabbedPane.getSelectedIndex() == TAB_CONVERSION)
                changeBackgroundColor(TAB_CONVERSION, Utilities.TAB_COLOR);
            else if(apeTabbedPane.getSelectedIndex() == TAB_ESE)
                changeBackgroundColor(TAB_ESE, Utilities.TAB_COLOR);
            else if(apeTabbedPane.getSelectedIndex() == TAB_EDITION){
                if(dataPreparationToolGUI.getXmlEadList().getSelectedValue() != null){
                    createEditionTree((File)dataPreparationToolGUI.getXmlEadList().getSelectedValue());
                    if(tree != null)
                        tree.addMouseListener(new MouseListener(){
                            public void mouseReleased(MouseEvent e){
                                checkAndLaunchPopUp(e);
                            }
                            public void mousePressed(MouseEvent e){
                                checkAndLaunchPopUp(e);
                            }
                            public void mouseExited(MouseEvent e) {}
                            public void mouseEntered(MouseEvent e) {}
                            public void mouseClicked(MouseEvent e) {}
                        });
                }
            }
        }

        private void checkAndLaunchPopUp(MouseEvent e){
            if(e.isPopupTrigger()){
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selPath != null){
                    tree.getTreeSelectionModel().clearSelection();
                    tree.getTreeSelectionModel().addSelectionPath(selPath);
                }

                JPopupMenu popupMenu = buildPopup(tree.getTreeSelectionModel().getSelectionPath());

                // Only show popup if we have any menu items in it
                if(popupMenu.getComponentCount() > 0)
                    popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
            }
        }

        private JPopupMenu buildPopup(final TreePath path) {
            JPopupMenu pm = new JPopupMenu();
            Object node = path.getLastPathComponent();
            if(node instanceof Node){
                JMenuItem j1 = new JMenuItem(((Node) node).getNodeName());
                j1.setEnabled(false);
                pm.add(j1);
                pm.addSeparator();
                java.util.List<JMenuItem> listActions = getPossibleActions((Node) node, path);
                for(JMenuItem menuItem : listActions)
                    pm.add(menuItem);
            }
            return pm;
        }

        private List<JMenuItem> getPossibleActions(final Node node, final TreePath path){
            List<JMenuItem> actions = new ArrayList<JMenuItem>();
            List<String> actionNames = NodeAppendable.getActions(node, labels);
//        Element childNode = node.getOwnerDocument().createElementNS("urn:isbn:1-931666-22-9", "testElement");
//        node.appendChild(childNode);
            if(actionNames.isEmpty()){
                JMenuItem j1 = new JMenuItem(labels.getString("noActionsAvailable"));
                j1.setEnabled(false);
                actions.add(j1);
                return actions;
            }

            for(String actionName : actionNames){
                JMenuItem menuItem = new JMenuItem(actionName);
                menuItem.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        doAction(node, path, e.getActionCommand());
                    }
                });
                actions.add(menuItem);
            }
            return actions;
        }

        private void doAction(Node node, TreePath path, String actionCommand){
            LOG.info(actionCommand + ": " + node.getNodeName());
            TreeModelSupport modelSupport = ((XMLTreeTableModel)tree.getTreeTableModel()).getModelSupport();
            if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_COUNTRYCODE.getBundleCode()))){
                Attr attr = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "countrycode");
                attr.setValue(dataPreparationToolGUI.getCountryCode());
                ((Element)node).setAttributeNode(attr);
                modelSupport.fireChildAdded(path, node.getAttributes().getLength()-1, attr);
            } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_MAINAGENCYCODE.getBundleCode()))){
                Attr attr = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "mainagencycode");
                attr.setValue(dataPreparationToolGUI.getRepositoryCodeIdentifier());
                ((Element)node).setAttributeNode(attr);
                modelSupport.fireChildAdded(path, node.getAttributes().getLength()-1, attr);
            } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_TEXT.getBundleCode()))){
                Text textNode = node.getOwnerDocument().createTextNode(dataPreparationToolGUI.getCountryCode() + "_" + dataPreparationToolGUI.getRepositoryCodeIdentifier());
                node.appendChild(textNode);
                modelSupport.fireTreeStructureChanged(path);
            } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_LANGUAGETAG.getBundleCode()))){
                String result = createPopupQuestion(0, labels.getString("chooseLanguage"), labels.getString("enterLanguageCode") + ":");
                if(result != null) {
                    Element languageTag = node.getOwnerDocument().createElementNS(NodeAppendable.XMLNS_EAD, "language");
                    Attr attr_langcode = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "langcode");
                    attr_langcode.setValue(result);
                    languageTag.setAttributeNode(attr_langcode);
                    Attr attr_scriptcode = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "scriptcode");
                    attr_scriptcode.setValue("Latn");
                    languageTag.setAttributeNode(attr_scriptcode);
                    Attr attr_encodinganalog = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "encodinganalog");
                    attr_encodinganalog.setValue("041");
                    languageTag.setAttributeNode(attr_encodinganalog);
                    Text textNode = node.getOwnerDocument().createTextNode(result);
                    languageTag.appendChild(textNode);
                    node.appendChild(languageTag);
                    modelSupport.fireTreeStructureChanged(path);
                }
            } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_NORMALATTR.getBundleCode()))){
                String result = createPopupQuestion(1, labels.getString("provideNormalAttr"), labels.getString("enterNormalAttr") + ":");
                if(result != null) {
                    Attr attr = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "normal");
                    attr.setValue(result);
                    ((Element)node).setAttributeNode(attr);
                    modelSupport.fireChildAdded(path, node.getAttributes().getLength()-1, attr);
                }
            }

            if(!tree.isExpanded(path))
                tree.expandPath(path);
        }

        private String createPopupQuestion(int type, String title, String text){
            String res;
            boolean error = false;
            do {
                res = (String)JOptionPane.showInputDialog(parent, text, title, JOptionPane.QUESTION_MESSAGE, Utilities.icon, null, null);
                if(res == null){
                    return null;
                }
                if(type == 0 && !dataPreparationToolGUI.getLangList().contains(res)){
                    res = null;
                    if(!error)
                        text += "\n" + labels.getString("enterCorrectLanguage") + ".";
                    error = true;
                }
                if(type == 1 && dataPreparationToolGUI.getDateNormalization().checkForNormalAttribute(res) == null){
                    res = null;
                    if(!error)
                        text += "\n" + labels.getString("enterCorrectNormalAttr") + ".";
                    error = true;
                }
            } while (res == null);

            return res;
        }
    }

    public JButton getConvertBtn() {
        return convertBtn;
    }

    public JButton getValidateBtn() {
        return validateBtn;
    }

    public JButton getConvertEseBtn() {
        return convertEseBtn;
    }

}
