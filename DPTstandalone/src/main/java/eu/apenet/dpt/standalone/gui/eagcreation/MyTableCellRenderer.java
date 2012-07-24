package eu.apenet.dpt.standalone.gui.eagcreation;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * User: Yoann Moranville
 * Date: Nov 19, 2010
 *
 * @author Yoann Moranville
 */
public class MyTableCellRenderer extends JLabel implements TableCellRenderer {
    private Border unselectedBorder = null;
    private Border selectedBorder = null;

    public MyTableCellRenderer(){
        setOpaque(false);
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            if (selectedBorder == null)
                selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
            setBorder(selectedBorder);
        } else {
            if (unselectedBorder == null)
                unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
            setBorder(unselectedBorder);
        }
        return this;
    }
}
