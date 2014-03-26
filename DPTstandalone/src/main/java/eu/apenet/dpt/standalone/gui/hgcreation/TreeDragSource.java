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

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.dnd.*;

/**
 * User: Yoann Moranville
 * Date: Feb 15, 2011
 *
 * @author Yoann Moranville
 */
public class TreeDragSource implements DragSourceListener, DragGestureListener {
    private final static Logger LOG = Logger.getLogger(TreeDragSource.class);

  DragSource source;

  DragGestureRecognizer recognizer;

  TransferableTreeNode transferable;

  DefaultMutableTreeNode oldNode;

  JTree sourceTree;

  public TreeDragSource(JTree tree, int actions) {
    sourceTree = tree;
    source = new DragSource();
    recognizer = source.createDefaultDragGestureRecognizer(sourceTree,
        actions, this);
  }

  /*
   * Drag Gesture Handler
   */
  public void dragGestureRecognized(DragGestureEvent dge) {
    TreePath path = sourceTree.getSelectionPath();
    if ((path == null) || (path.getPathCount() <= 1)) {
        LOG.info("empty?");
      // We can't move the root node or an empty selection
      return;
    }
    oldNode = (DefaultMutableTreeNode) path.getLastPathComponent();
    transferable = new TransferableTreeNode(path);
    source.startDrag(dge, DragSource.DefaultMoveDrop, transferable, this);

    // If you support dropping the node anywhere, you should probably
    // start with a valid move cursor:
    //source.startDrag(dge, DragSource.DefaultMoveDrop, transferable,
    // this);
  }

  /*
   * Drag Event Handlers
   */
  public void dragEnter(DragSourceDragEvent dsde) {
  }

  public void dragExit(DragSourceEvent dse) {
  }

  public void dragOver(DragSourceDragEvent dsde) {
  }

  public void dropActionChanged(DragSourceDragEvent dsde) {
    LOG.info("Action: " + dsde.getDropAction());
    LOG.info("Target Action: " + dsde.getTargetActions());
    LOG.info("User Action: " + dsde.getUserAction());
  }

  public void dragDropEnd(DragSourceDropEvent dsde) {
    /*
     * to support move or copy, we have to check which occurred:
     */
    LOG.info("Drop Action List: " + DnDConstants.ACTION_MOVE + " DnDConstants.ACTION_MOVE");
    LOG.info("Drop Action List: " + DnDConstants.ACTION_NONE + " DnDConstants.ACTION_NONE");
    LOG.info("Drop Action: " + dsde.getDropAction());
    if (dsde.getDropSuccess() && (dsde.getDropAction() == DnDConstants.ACTION_MOVE)) {
      ((DefaultTreeModel) sourceTree.getModel()).removeNodeFromParent(oldNode);
    }

    /*
     * to support move only... if (dsde.getDropSuccess()) {
     * ((DefaultTreeModel)sourceTree.getModel()).removeNodeFromParent(oldNode); }
     */
  }
}
