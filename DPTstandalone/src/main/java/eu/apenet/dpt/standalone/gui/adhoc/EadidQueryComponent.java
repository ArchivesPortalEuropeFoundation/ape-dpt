package eu.apenet.dpt.standalone.gui.adhoc;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: Dec 1, 2010
 *
 * @author Yoann Moranville
 */
public class EadidQueryComponent {
    private JPanel panel = new JPanel();
    private JComboBox boxEadid = new JComboBox();
    private ResourceBundle labels;

    public EadidQueryComponent(ResourceBundle labels){
        this.labels = labels;
        boxEadid.setModel(new DefaultComboBoxModel(new String[]{labels.getString("eadidQuery")}));
        boxEadid.setEditable(true);

        panel.setLayout(new GridLayout(2,2,2,5));
        panel.add(new JLabel("EADID"));
        panel.add(boxEadid);
    }

    public EadidQueryComponent(String unitid){

        boxEadid.setModel(new DefaultComboBoxModel(new String[]{unitid}));
        boxEadid.setEditable(true);

        panel.setLayout(new GridLayout(2,2,2,5));
        panel.add(new JLabel("EADID"));
        panel.add(boxEadid);
    }

    public String getEntryEadid(){
        return (String) boxEadid.getSelectedItem();
    }

    public JComponent getMainPanel(){
        return panel;
    }
}
