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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Stefan Papp
 */
public class ListMouseAdapter extends MouseAdapter {

    private JList filelist;
    private ProfileListModel filelistModel;
    private JMenuItem deleteFileItem;
    private DataPreparationToolGUI dataPreparationToolGUI;

    public ListMouseAdapter(JList filelist, ProfileListModel filelistModel, JMenuItem deleteFileItem, DataPreparationToolGUI dataPreparationToolGUI) {
        this.filelist = filelist;
        this.filelistModel = filelistModel;
        this.deleteFileItem = deleteFileItem;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (filelist.getSelectedValues().length == 1) {
                final int indexToErase = filelist.locationToIndex(e.getPoint());
                filelist.setSelectedIndex(indexToErase);
            }
            JPopupMenu popup = new JPopupMenu();
            deleteFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (filelist.getSelectedValues().length > 1) {
                            filelistModel.removeFiles(filelist.getSelectedValues());
                        } else {
                            filelistModel.removeFile((File) filelist.getSelectedValue());
                            dataPreparationToolGUI.disableTabFlashing();
                        }
                    } catch (Exception ex) {
                        DataPreparationToolGUI.createErrorOrWarningPanel(ex, true, dataPreparationToolGUI.getLabels().getString("errorRemovingFileFromList"), dataPreparationToolGUI.getContentPane());
                    } finally {
                        deleteFileItem.removeActionListener(this);
                    }
                }
            });
            popup.add(deleteFileItem);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
