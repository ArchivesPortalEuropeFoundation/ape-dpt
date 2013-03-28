/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

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
