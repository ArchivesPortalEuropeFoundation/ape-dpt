/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

import java.awt.Point;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import javax.swing.JList;

/**
 *
 * @author papp
 */
public class ListDropTargetListener implements DropTargetListener {

    private JList list;
    private Point from;

    public ListDropTargetListener(JList list, Point from) {
        this.list = list;
        this.from = from;
    }

    private void updateLine(Point pt) {
        if (list.locationToIndex(pt) < 0)
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

                m.insertElementAt((File) m.getElementAt(srcRow), dstRow);
                if (dstRow <= srcRow) {
                    m.removeElementAt(srcRow + 1);
                    list.setSelectedIndex(dstRow);
                } else {
                    m.removeElementAt(srcRow);
                    list.setSelectedIndex(dstRow - 1);
                }
            }
}
