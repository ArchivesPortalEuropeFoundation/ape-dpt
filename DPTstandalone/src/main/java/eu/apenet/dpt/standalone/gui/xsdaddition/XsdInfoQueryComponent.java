package eu.apenet.dpt.standalone.gui.xsdaddition;

import eu.apenet.dpt.standalone.gui.FileInstance;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 08/02/2013
 *
 * @author Yoann Moranville
 */
public class XsdInfoQueryComponent extends Panel {
    private JTextField textFieldPath;
    private JTextField textFieldName;
    private ButtonGroup groupXsdVersion;
    private JComboBox boxFileType;
    private ResourceBundle labels;

    public XsdInfoQueryComponent(ResourceBundle labels, String filename){
        this.labels = labels;

        setLayout(new BorderLayout());

        textFieldPath = new JTextField();
        textFieldPath.setEnabled(false);
        textFieldPath.setText(filename);

        textFieldName = new JTextField();
        textFieldName.setText(filename.replace(".xsd", ""));

        groupXsdVersion = new ButtonGroup();
        JRadioButton radioButtonV1 = new JRadioButton();
        radioButtonV1.setText("1");
        radioButtonV1.setSelected(true);
        groupXsdVersion.add(radioButtonV1);
        JRadioButton radioButtonV11 = new JRadioButton();
        radioButtonV11.setText("1.1");
        groupXsdVersion.add(radioButtonV11);

        boxFileType = new JComboBox();
        boxFileType.setModel(new DefaultComboBoxModel(new String[]{FileInstance.FileType.EAD.getFilePrefix(), FileInstance.FileType.EAG.getFilePrefix(), FileInstance.FileType.EAC_CPF.getFilePrefix(), FileInstance.FileType.OTHER.getFilePrefix()}));
        boxFileType.setEditable(false);


        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(4, 2, 2, 2));

        pane.add(new JLabel("XSD Filename:"));
        pane.add(textFieldPath);
        pane.add(new JLabel("Name:"));
        pane.add(textFieldName);
        pane.add(new JLabel("XSD version:"));
        JPanel child = new JPanel(new GridLayout(1, 2, 2, 2));
        child.add(radioButtonV1);
        child.add(radioButtonV11);
        pane.add(child);
        pane.add(new JLabel("File type:"));
        pane.add(boxFileType);

        add(new JLabel("Explanation"), BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
    }

    public String getName(){
        return textFieldName.getText();
    }

    public String getXsdVersion() {
        Enumeration buttons = groupXsdVersion.getElements();
        while (buttons.hasMoreElements()) {
            JRadioButton jRadioButton = (JRadioButton) buttons.nextElement();
            if (jRadioButton.isSelected())
                return jRadioButton.getText();
        }
        return "1";
    }

    public String getFileType() {
        return (String) boxFileType.getSelectedItem();
    }
}
