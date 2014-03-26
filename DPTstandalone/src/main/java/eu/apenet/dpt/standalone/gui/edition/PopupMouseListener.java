package eu.apenet.dpt.standalone.gui.edition;

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

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * User: Yoann Moranville
 * Date: 19/03/2013
 *
 * @author Yoann Moranville
 */
public class PopupMouseListener implements MouseListener {
    private static final Logger LOG = Logger.getLogger(PopupMouseListener.class);
    private JXTreeTable tree;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private ResourceBundle labels;
    private Component parent;
    private FileInstance fileInstance;

    public PopupMouseListener(JXTreeTable tree, DataPreparationToolGUI dataPreparationToolGUI, Component parent, FileInstance fileInstance) {
        this.tree = tree;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        this.labels = dataPreparationToolGUI.getLabels();
        this.parent = parent;
        this.fileInstance = fileInstance;
    }

    public void mouseReleased(MouseEvent e){
        checkAndLaunchPopUp(e);
    }
    public void mousePressed(MouseEvent e){
        checkAndLaunchPopUp(e);
    }
    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}

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

    private java.util.List<JMenuItem> getPossibleActions(final Node node, final TreePath path){
        java.util.List<JMenuItem> actions = new ArrayList<JMenuItem>();
        java.util.List<String> actionNames = NodeAppendable.getActions(node, labels);
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
            fileInstance.setLastOperation(FileInstance.Operation.EDIT_TREE);
        } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_MAINAGENCYCODE.getBundleCode()))){
            Attr attr = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "mainagencycode");
            attr.setValue(dataPreparationToolGUI.getRepositoryCodeIdentifier());
            ((Element)node).setAttributeNode(attr);
            modelSupport.fireChildAdded(path, node.getAttributes().getLength()-1, attr);
            fileInstance.setLastOperation(FileInstance.Operation.EDIT_TREE);
        } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_TEXT.getBundleCode()))){
            Text textNode = node.getOwnerDocument().createTextNode(dataPreparationToolGUI.getCountryCode() + "_" + dataPreparationToolGUI.getRepositoryCodeIdentifier());
            node.appendChild(textNode);
            modelSupport.fireTreeStructureChanged(path);
            fileInstance.setLastOperation(FileInstance.Operation.EDIT_TREE);
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
                fileInstance.setLastOperation(FileInstance.Operation.EDIT_TREE);
            }
        } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_NORMALATTR.getBundleCode()))){
            String result = createPopupQuestion(1, labels.getString("provideNormalAttr"), labels.getString("enterNormalAttr") + ":");
            if(result != null) {
                Attr attr = node.getOwnerDocument().createAttributeNS(NodeAppendable.XMLNS_EAD, "normal");
                attr.setValue(result);
                ((Element)node).setAttributeNode(attr);
                modelSupport.fireChildAdded(path, node.getAttributes().getLength()-1, attr);
                fileInstance.setLastOperation(FileInstance.Operation.EDIT_TREE);
            }
        } else if(actionCommand.equals(labels.getString(ActionNamesEnum.INSERT_EADID.getBundleCode()))) {
            String result = createPopupQuestion(2, labels.getString("eadidQuery"), labels.getString("eadidQuery") + ":");
            if(result != null) {
                Element eadidTag = node.getOwnerDocument().createElementNS(NodeAppendable.XMLNS_EAD, "eadid");
                Text textNode = node.getOwnerDocument().createTextNode(result);
                eadidTag.appendChild(textNode);
                node.appendChild(eadidTag);
                modelSupport.fireTreeStructureChanged(path);
                fileInstance.setLastOperation(FileInstance.Operation.EDIT_TREE);
            }
        }
        ((ProfileListModel)dataPreparationToolGUI.getXmlEadList().getModel()).fireContentsChanged((ProfileListModel)dataPreparationToolGUI.getXmlEadList().getModel(), 0, 1);

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
