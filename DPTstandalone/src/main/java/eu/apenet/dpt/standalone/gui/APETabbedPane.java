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

import eu.apenet.dpt.standalone.gui.conversion.ConvertActionListener;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.edition.*;
import eu.apenet.dpt.standalone.gui.ead2edm.ConvertEdmActionListener;
import eu.apenet.dpt.standalone.gui.validation.DownloadReportActionListener;
import eu.apenet.dpt.standalone.gui.validation.ValidateActionListener;
import eu.apenet.dpt.standalone.gui.xsdaddition.XsdObject;
import eu.apenet.dpt.utils.util.DOMUtil;
import eu.apenet.dpt.utils.util.Xsd_enum;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
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
    public static final int TAB_EDM = 3;
    public static final int TAB_EDITION = 4;

    private static final Dimension DEFAULT_BTN_DIMENSION = new Dimension(100, 40);

    private JTextArea validationErrors;
    private JTextArea conversionErrors;
    private JTextArea edmConversionErrors;

    private JScrollPane validationPane;
    private JScrollPane conversionPane;
    private JScrollPane edmConversionPane;
    private JScrollPane editionPane;

    private JButton convertBtn;
    private JButton validateBtn;
    private JButton convertEdmBtn;
    private JButton validationReportBtn;
    private JButton messageReportBtn;

    private JLabel stylesheetLabel;
    private JLabel schemaLabel;

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
        addTab(labels.getString("eseConversion"), makeTextPanel(TAB_EDM));
        addTab(labels.getString("edition"), makeTextPanel(TAB_EDITION));
        addMouseListener(new APETabbedPaneMouseAdapter(this));

        changeLanguage(labels);
    }

    private void create() {
        validationErrors = new JTextArea();
        validationReportBtn = new JButton();
        JPanel validationPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(validationReportBtn);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));
        validationPanel.add(buttonPanel, BorderLayout.NORTH);
        validationPanel.add(validationErrors, BorderLayout.CENTER);
        conversionErrors = new JTextArea();
        edmConversionErrors = new JTextArea();
        validationPane = new JScrollPane(validationPanel);
        conversionPane = new JScrollPane(conversionErrors);
        edmConversionPane = new JScrollPane(edmConversionErrors);
        editionPane = new JScrollPane();

        convertBtn = new JButton();
        validateBtn = new JButton();
        convertEdmBtn = new JButton();
        messageReportBtn = new JButton();

        stylesheetLabel = new JLabel(labels.getString("summary.stylesheets.label") + ":");
        schemaLabel = new JLabel(labels.getString("summary.schemas.label") + ":");

        xsltPanel = new JPanel();
        xsdPanel = new JPanel();
    }

    private void initialize() {
        createEditionTree(null);

        validationErrors.setEditable(false);
        validationErrors.setLineWrap(true);
        validationErrors.setWrapStyleWord(true);
        conversionErrors.setEditable(false);
        conversionErrors.setLineWrap(true);
        conversionErrors.setWrapStyleWord(true);
        edmConversionErrors.setEditable(false);
        edmConversionErrors.setLineWrap(true);
        edmConversionErrors.setWrapStyleWord(true);

        convertBtn.addActionListener(new ConvertActionListener(parent, dataPreparationToolGUI, apePanel));
        convertBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        convertBtn.setEnabled(false);

        convertEdmBtn.addActionListener(new ConvertEdmActionListener(labels, dataPreparationToolGUI, apePanel));
        convertEdmBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        convertEdmBtn.setEnabled(false);

        messageReportBtn.addActionListener(new MessageReportActionListener(retrieveFromDb, dataPreparationToolGUI, dataPreparationToolGUI.getFileInstances(), labels, this));
        messageReportBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        messageReportBtn.setEnabled(false);

        validateBtn.addActionListener(new ValidateActionListener(dataPreparationToolGUI, this));
        validateBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        validateBtn.setEnabled(false);

        validationReportBtn.addActionListener(new DownloadReportActionListener(dataPreparationToolGUI));
        validationReportBtn.setPreferredSize(DEFAULT_BTN_DIMENSION);
        validationReportBtn.setEnabled(false);
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

    public void appendEdmConversionErrorText(String text) {
        edmConversionErrors.append(text);
    }
    public void setConversionErrorText(String text) {
        conversionErrors.setText(text);
    }
    public void setValidationErrorText(String text) {
        validationErrors.setText(text);
    }
    public void setEdmConversionErrorText(String text) {
        edmConversionErrors.setText(text);
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
    public void enableConversionEdmBtn() {
        convertEdmBtn.setEnabled(true);
    }
    public void disableConversionEdmBtn() {
        convertEdmBtn.setEnabled(false);
    }
    public void enableMessageReportBtn() {
        messageReportBtn.setEnabled(true);
    }
    public void disableMessageReportBtn() {
        messageReportBtn.setEnabled(false);
    }
    public void enableValidationReportBtn() {
        validationReportBtn.setEnabled(true);
    }
    public void disableValidationReportBtn() {
        validationReportBtn.setEnabled(false);
    }

    public void changeLanguage(ResourceBundle labels) {
        setTitleAt(TAB_CONVERSION, labels.getString("conversion"));
        setTitleAt(TAB_EDITION, labels.getString("edition"));
        setTitleAt(TAB_SUMMARY, labels.getString("summary"));
        setTitleAt(TAB_VALIDATION, labels.getString("validation"));
        setTitleAt(TAB_EDM, labels.getString("eseConversion"));

        convertBtn.setText(labels.getString("convert"));
        validateBtn.setText(labels.getString("validate"));
        convertEdmBtn.setText(labels.getString("summary.convertEdm.button"));
        messageReportBtn.setText(labels.getString("summary.messageReport.button"));
        validationReportBtn.setText(labels.getString("dataquality.report.download"));

        stylesheetLabel.setText(labels.getString("summary.stylesheets.label") + ":");
        schemaLabel.setText(labels.getString("summary.schemas.label") + ":");
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
        } else if(tab == TAB_EDM){
            p.add(edmConversionPane);
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
        p.add(convertEdmBtn);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(messageReportBtn);
        return p;
    }

    private JComponent makeCheckboxes() {
        xsltPanel.setLayout(new BoxLayout(xsltPanel, BoxLayout.PAGE_AXIS));
        xsltPanel.add(stylesheetLabel);
        JRadioButton radioButton;
        for(File xsltFile : Utilities.getXsltFiles()){
            radioButton = new JRadioButton(xsltFile.getName());
            if(xsltFile.getName().equals(retrieveFromDb.retrieveDefaultXsl()))
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
        xsdPanel.add(schemaLabel);
        JRadioButton radioButton;
        for(Xsd_enum xsdEnum : Xsd_enum.values()){
            radioButton = new JRadioButton(xsdEnum.getReadableName());
            if(xsdEnum.getReadableName().equals(retrieveFromDb.retrieveDefaultXsd()))
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
            editionPane.setViewportView(createMsgEditionTree(labels.getString("noTreeBuild") + "..."));
            return null;
        }
        FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(file.getName());
        if(tree != null && ((XMLTreeTableModel)tree.getTreeTableModel()).getName().equals(file.getName()) && fileInstance.getLastOperation().equals(FileInstance.Operation.EDIT_TREE))
            return null;

//        fileInstance.setLastOperation(FileInstance.Operation.CREATE_TREE);
        try {
            InputSource is;
            if(fileInstance.getCurrentLocation() == null || fileInstance.getCurrentLocation().equals("")){
                is = new InputSource(file.getAbsolutePath());
            } else {
                is = new InputSource(fileInstance.getCurrentLocation());
            }
            Document doc = DOMUtil.createDocument(is);
            tree = new JXTreeTable();
            tree.setTreeTableModel(new XMLTreeTableModel(doc, dataPreparationToolGUI.getDateNormalization(), dataPreparationToolGUI.getLevelList(), labels, fileInstance, dataPreparationToolGUI));
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
            dataPreparationToolGUI.setTree(tree);
            ((XMLTreeTableModel)tree.getTreeTableModel()).setName(file.getName());
        } catch (Exception e){
            LOG.info("Error creating tree", e);
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
            else if(apeTabbedPane.getSelectedIndex() == TAB_EDM)
                changeBackgroundColor(TAB_EDM, Utilities.TAB_COLOR);
            else if(apeTabbedPane.getSelectedIndex() == TAB_EDITION){
                if(dataPreparationToolGUI.getXmlEadList().getSelectedValue() != null){
                    createEditionTree((File)dataPreparationToolGUI.getXmlEadList().getSelectedValue());
                    if(tree != null) {
                        FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(((File) dataPreparationToolGUI.getXmlEadList().getSelectedValue()).getName());
                        tree.addMouseListener(new PopupMouseListener(tree, dataPreparationToolGUI, parent, fileInstance));
                    }
                }
            }
        }
    }

    public JButton getConvertBtn() {
        return convertBtn;
    }

    public JButton getValidateBtn() {
        return validateBtn;
    }

    public JButton getMessageReportBtn() {
        return messageReportBtn;
    }
}
