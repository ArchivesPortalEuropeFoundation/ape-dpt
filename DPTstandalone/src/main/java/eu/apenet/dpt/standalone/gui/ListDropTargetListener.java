/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
