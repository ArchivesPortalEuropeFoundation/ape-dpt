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

import java.util.List;
import java.util.ResourceBundle;

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;

import javax.swing.*;

/**
 * User: Yoann Moranville
 * Date: Sep 24, 2010
 *
 * @author Yoann Moranville
 */
public class XMLTreeTableModel extends AbstractTreeTableModel {
    private List<String> nodeEditable;
    private DateNormalization dateNormalization;
    private List<String> levelList;
    private String name;
    private ResourceBundle labels;
    private FileInstance fileInstance;
    private DataPreparationToolGUI dataPreparationToolGUI;

    public XMLTreeTableModel(Document doc) {
        super(doc);
        root = doc;
        modelSupport = new TreeModelSupport(this);
        nodeEditable = new NodeEditable().getList();
        name = "";
    }

    public XMLTreeTableModel(Document doc, DateNormalization dateNormalization, List<String> levelList, ResourceBundle labels, FileInstance fileInstance, DataPreparationToolGUI dataPreparationToolGUI) {
        super(doc);
        root = doc;
        modelSupport = new TreeModelSupport(this);
        nodeEditable = new NodeEditable().getList();
        name = "";
        this.dateNormalization = dateNormalization;
        this.levelList = levelList;
        this.labels = labels;
        this.fileInstance = fileInstance;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
    }

    public void setDateNormalization(DateNormalization dateNormalization){
        this.dateNormalization = dateNormalization;
    }

    public void setLevelList(List<String> levelList){
        this.levelList = levelList;
    }

    public Document getRoot() {
        return (Document) root;
    }

    public void setRoot(Document doc) {
        root = doc;
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int column){
        switch(column) {
            case 0:
                return labels.getString("edit.column.name");
            case 1:
                return labels.getString("edit.column.value");
            default:
                return "Column " + column;
}
    }

    public Object getValueAt(Object node, int column) {
        if (!(node instanceof Node)) {
            throw new IllegalArgumentException();
        }
        switch(column) {
            case 0:
                if(((Node) node).getNodeType() == Node.ATTRIBUTE_NODE)
                    return "@" + ((Node) node).getNodeName();
                return ((Node) node).getNodeName();
            case 1:
                return ((Node) node).getNodeValue();
            default:
                throw new IllegalArgumentException();
        }
    }

    public Object getChild(Object parent, int index) {
        if (!(parent instanceof Node)) {
            throw new IllegalArgumentException();
        }

        NamedNodeMap attributes = ((Node) parent).getAttributes();
        int attrCount = attributes!=null ? attributes.getLength() : 0;
        if(index<attrCount)
            return attributes.item(index);
        NodeList children = ((Node) parent).getChildNodes();
        return children.item(index - attrCount);
    }

    public int getChildCount(Object parent) {
        if (!(parent instanceof Node)) {
            throw new IllegalArgumentException();
        }
        NamedNodeMap attributes = ((Node) parent).getAttributes();
        int attrCount = attributes!=null ? attributes.getLength() : 0;
        NodeList children = ((Node) parent).getChildNodes();
        int childCount = children!=null ? children.getLength() : 0;
        if(childCount==1 && children.item(0).getNodeType() == Node.TEXT_NODE && ((Node) parent).getNodeType() == Node.ATTRIBUTE_NODE)
            return attrCount;
        else 
            return attrCount + childCount;
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (!(parent instanceof Node)) {
            throw new IllegalArgumentException();
        }
        if (!(child instanceof Node)) {
            throw new IllegalArgumentException();
        }

        NamedNodeMap attrs = ((Node) parent).getAttributes();
        int attrCount = attrs!=null ? attrs.getLength() : 0;
        if(((Node) child).getNodeType() == Node.ATTRIBUTE_NODE) {
            for(int i=0; i<attrCount; i++){
                if(attrs.item(i) == child)
                    return i;
            }
        } else {
            NodeList children = ((Node) parent).getChildNodes();
            int childCount = children!=null ? children.getLength() : 0;
            for(int i=0; i<childCount; i++){
                if(children.item(i) == child)
                    return attrCount + i;
            }
        }
        throw new RuntimeException("this should never happen!");
    }

    public boolean isCellEditable(Object node, int column){
        if (!(node instanceof Node)) {
            throw new IllegalArgumentException();
        }
        return column == 1 && ((((Node) node).getNodeType() == Node.TEXT_NODE && nodeEditable.contains(((Node) node).getParentNode().getNodeName())) || (((Node) node).getNodeType() == Node.ATTRIBUTE_NODE && nodeEditable.contains(((Node) node).getNodeName()))); 
    }

    public void setValueAt(Object value, Object node, int column){
        if (!(node instanceof Node)) {
            throw new IllegalArgumentException();
        }
        if (!(value instanceof String)) {
            throw new IllegalArgumentException();
        }

        if(check((String)value, (Node)node)) {
            ((Node) node).setNodeValue((String)value);
            fileInstance.setLastOperation(FileInstance.Operation.EDIT_TREE);
            ((ProfileListModel)dataPreparationToolGUI.getXmlEadList().getModel()).fireContentsChanged((ProfileListModel)dataPreparationToolGUI.getXmlEadList().getModel(), 0, 1);
        }
    }

    private boolean check(String value, Node node){
        if(node.getNodeType() == Node.TEXT_NODE){
            return true;
        } else if(node.getNodeName().equals("mainagencycode")){
            if(dateNormalization.checkForMainagencycode(value) != null)
                return true;
        } else if(node.getNodeName().equals("countrycode")){
            if(dateNormalization.checkForCountrycode(value) != null)
                return true;
        } else if(node.getNodeName().equals("normal")){
            if(dateNormalization.checkForNormalAttribute(value) != null)
                return true;
        } else if(node.getNodeName().equals("level")){
            if(levelList.contains(value))
                return true;
        }
        return false;
    }

    public TreeModelSupport getModelSupport(){
        return modelSupport;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
