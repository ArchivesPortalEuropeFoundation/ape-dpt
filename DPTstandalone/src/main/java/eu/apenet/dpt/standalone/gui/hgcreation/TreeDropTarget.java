package eu.apenet.dpt.standalone.gui.hgcreation;

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

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * User: Yoann Moranville
 * Date: Feb 15, 2011
 *
 * @author Yoann Moranville
 */
public class TreeDropTarget implements DropTargetListener {
    private static final Logger LOG = Logger.getLogger(TreeDropTarget.class);

    DropTarget target;

    JTree targetTree;

    JList originalList;

    boolean doDropFromList = false;
//    Point fromPoint;

    public TreeDropTarget(JTree tree, JList list) {
        targetTree = tree;
        target = new DropTarget(targetTree, this);
        originalList = list;
    }

    /*
    * Drop Event Handlers
    */

    private DefaultMutableTreeNode getNodeForEvent(DropTargetDragEvent dtde) {
        Point p = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath path = tree.getClosestPathForLocation(p.x, p.y);
        return (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        CLevelTreeObject node = (CLevelTreeObject)getNodeForEvent(dtde).getUserObject();
        if(node.isFile())
            dtde.rejectDrag();
        dtde.acceptDrag(dtde.getDropAction());

        //Normally in other place:
        Point location = dtde.getLocation();
        doDropFromList = (targetTree.getPathForLocation(location.x, location.y) == null);
//        fromPoint = location;
    }

    public void dragOver(DropTargetDragEvent dtde) {
        CLevelTreeObject node = (CLevelTreeObject)getNodeForEvent(dtde).getUserObject();
        if(node.isFile())
            dtde.rejectDrag();
        dtde.acceptDrag(dtde.getDropAction());
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void drop(DropTargetDropEvent dtde) {
        if(doDropFromList){
            LOG.trace("Drop from List");

            Point p = targetTree.getMousePosition();
            TreePath dst = targetTree.getClosestPathForLocation(p.x, p.y);

            MutableTreeNode lastPathComp = ((MutableTreeNode)dst.getLastPathComponent());

//            LOG.info("fromPoint: " + fromPoint.toString());
            int[] srcRows = originalList.getSelectedIndices();
            ProfileListModel profileListModel = (ProfileListModel) originalList.getModel();

            for(int srcRow : srcRows){
                int indexToDrop = lastPathComp.getChildCount();
                if(lastPathComp.getParent() != null)
                    indexToDrop = lastPathComp.getParent().getIndex(lastPathComp) + 1;

                while(((CLevelTreeObject)((DefaultMutableTreeNode)lastPathComp).getUserObject()).isFile() && ((DefaultMutableTreeNode) lastPathComp).getParent() != null) {
                    lastPathComp = (MutableTreeNode) ((DefaultMutableTreeNode) lastPathComp).getParent();
                }
                if(!((CLevelTreeObject)((DefaultMutableTreeNode)lastPathComp).getUserObject()).isFile()){
                    CLevelTreeObject newCLevel = new CLevelTreeObject(((File)profileListModel.getElementAt(srcRow)).getName());
                    newCLevel.setFile((File)profileListModel.getElementAt(srcRow));

                    if(indexToDrop > lastPathComp.getChildCount())
                        indexToDrop = lastPathComp.getChildCount();
                    ((DefaultTreeModel)targetTree.getModel()).insertNodeInto(new DefaultMutableTreeNode(newCLevel), lastPathComp, indexToDrop);
                    LOG.debug("srcRow = " + srcRow + ", file name = " + ((File)profileListModel.getElementAt(srcRow)).getName() + ", lastPathComp = " + lastPathComp.toString());
                }
            }
            Arrays.sort(srcRows);
            for(int i = (srcRows.length-1); i >= 0; i--){
                LOG.debug("Remove: " + ((File)profileListModel.getElementAt(srcRows[i])).getName());
                profileListModel.removeElementAt(srcRows[i]);
            }
            originalList.clearSelection();
        } else {
            
            LOG.trace("Drop from Tree");
            Point pt = dtde.getLocation();
            DropTargetContext dtc = dtde.getDropTargetContext();
            JTree tree = (JTree) dtc.getComponent();

            TreePath realParent = tree.getPathForLocation(pt.x, pt.y);
            LOG.info(((CLevelTreeObject)((DefaultMutableTreeNode)realParent.getLastPathComponent()).getUserObject()).getName());

            if(((CLevelTreeObject)((DefaultMutableTreeNode)realParent.getLastPathComponent()).getUserObject()).isFile()){
                realParent = realParent.getParentPath();
            }

            TreePath[] parentPaths = {realParent};
//            TreePath[] parentPaths = tree.getSelectionPaths();
            for(TreePath parentpath : parentPaths){
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) parentpath.getLastPathComponent();
                if(((CLevelTreeObject)parent.getUserObject()).isFile()){
                    LOG.info("Reject drop");
                    dtde.rejectDrop();
                    return;
                }
//                if(((CLevelTreeObject)parent.getUserObject()) == parentpath)

                try {
                    Transferable tr = dtde.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        if (tr.isDataFlavorSupported(flavor)) {
                            LOG.trace("Checking if ok");
                            TreePath p = (TreePath) tr.getTransferData(flavor);
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
                            if(node.getUserObject().toString().equals(parent.getUserObject().toString())) { //bad hack for fix #706
                                LOG.info("Reject drop");
                                dtde.rejectDrop();
                                return;
                            }
                            if(isParentOf(node, parent)) { //bad hack for fix #706
                                LOG.info("Reject drop");
                                dtde.rejectDrop();
                                return;
                            }
                            dtde.acceptDrop(dtde.getDropAction());
                            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                            model.insertNodeInto(node, parent, parent.getChildCount());
                            dtde.dropComplete(true);
                            return;
                        }
                    }
                    dtde.rejectDrop();
                } catch (Exception e) {
                    LOG.error("Error when dropping file", e);
                    dtde.rejectDrop();
                }
            }
        }
    }

    public boolean isParentOf(DefaultMutableTreeNode supposedParent, DefaultMutableTreeNode supposedChild) {
        for(int i = 0; i < supposedParent.getChildCount(); i++) {
            TreeNode child = supposedParent.getChildAt(i);
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)child;
            if(childNode.toString().equals(supposedChild.toString())) {
                return true;
            } else {
                isParentOf(childNode, supposedChild);
            }
        }
        return false;
    }
}
