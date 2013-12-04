package eu.apenet.dpt.standalone.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: 04/12/2013
 *
 * @author Yoann Moranville
 */
public class ChangeNameListCellRenderer extends IconListCellRenderer {

    public ChangeNameListCellRenderer(Map<String, FileInstance> fileInstances) {
        super(fileInstances);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        File file = (File) value;
        if(file.getName().startsWith("temp_"))
            setText(file.getName().replace("temp_", ""));

        return component;
    }
}
