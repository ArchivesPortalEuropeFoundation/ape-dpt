package eu.apenet.dpt.standalone.gui;

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.Component;
import java.awt.Color;
import java.io.File;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: 16/01/2012
 *
 * @author Yoann Moranville
 */
public class IconListCellRenderer extends DefaultListCellRenderer {
    private Map<String, FileInstance> fileInstances;

    public IconListCellRenderer(Map<String, FileInstance> fileInstances){
        this.fileInstances = fileInstances;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        File file = (File) value;
        super.getListCellRendererComponent(list, file.getName(), index, isSelected, cellHasFocus);
        FileInstance fileInstance = fileInstances.get(file.getName());
        if(fileInstance != null) {
            if(fileInstance.isValid()){
                if(isSelected){
                    setBackground(new Color(100, 180, 100));
                    setBorder(BorderFactory.createLineBorder(getBackground()));
                } else
                    setForeground(new Color(100, 180, 100));
            } else if(fileInstance.getValidationErrors() != null && !fileInstance.getValidationErrors().equals("")){
                if(isSelected){
                    setBackground(new Color(200, 90, 90));
                    setBorder(BorderFactory.createLineBorder(getBackground()));
                } else
                    setForeground(new Color(200, 90, 90));
            }

            if(fileInstance.getLastOperation().equals(FileInstance.Operation.SAVE))
                setIcon(MetalIconFactory.getTreeFloppyDriveIcon());
        }
        return this;
    }
}
