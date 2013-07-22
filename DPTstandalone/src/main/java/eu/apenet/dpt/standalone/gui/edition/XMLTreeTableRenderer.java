package eu.apenet.dpt.standalone.gui.edition;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.util.List;


/**
 * User: Yoann Moranville
 * Date: Sep 27, 2010
 *
 * @author Yoann Moranville
 */
public class XMLTreeTableRenderer extends DefaultTreeCellRenderer {
    private static final Color ELEMENT_COLOR = new Color(0, 0, 128);
    private static final Color ATTRIBUTE_COLOR = new Color(0, 128, 0);
    private static final Color NOT_EDITABLE_COLOR = new Color(128, 0, 0);
    private List<String> nodeEditable;

    public XMLTreeTableRenderer(){
        setOpenIcon(null);
        setClosedIcon(null);
        setLeafIcon(null);
        nodeEditable = new NodeEditable().getList();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){
        Node node = (Node)value;
        switch(node.getNodeType()){
            case Node.ELEMENT_NODE:
                value = '<'+node.getNodeName()+'>';
                break;
            case Node.ATTRIBUTE_NODE:
                value = '@'+node.getNodeName();
                break;
            case Node.TEXT_NODE:
                value = "# text";
                break;
            case Node.COMMENT_NODE:
                value = "# comment";
                break;
            case Node.DOCUMENT_TYPE_NODE:
                value = "# doctype";
                break;
            default:
                value = node.getNodeName();
        }
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if(!selected){
            if(node.getNodeType() == Node.ATTRIBUTE_NODE && nodeEditable.contains(node.getNodeName()))
                setForeground(ATTRIBUTE_COLOR);
            else if(node.getNodeType() == Node.TEXT_NODE && nodeEditable.contains(node.getParentNode().getNodeName()))
                setForeground(ELEMENT_COLOR);
            else
                setForeground(NOT_EDITABLE_COLOR);
        }
        return this;
    }
} 