package eu.apenet.dpt.standalone.gui.eag2012;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 13/03/2013
 *
 * @author Yoann Moranville
 */
public class TextFieldWithCheckbox {
    private JTextField textField;
    private JComboBox isilOrNotCombo;

    private static final String[] ISIL_NOT_ISIL = {"ISIL", "notISIL"};

    public TextFieldWithCheckbox(String text, String isilOrNot) {
        textField = new JTextField(text);
        isilOrNotCombo = new JComboBox(ISIL_NOT_ISIL);
        if(Arrays.asList(ISIL_NOT_ISIL).contains(isilOrNot))
            isilOrNotCombo.setSelectedItem(isilOrNot);
        else
            isilOrNotCombo.setSelectedItem("notISIL");
    }

    public JTextField getTextField() {
        return textField;
    }

    public String getTextFieldValue() {
        return textField.getText();
    }

    public JComboBox getIsilOrNotCombo() {
        return isilOrNotCombo;
    }

    public String getisilOrNotComboValue() {
        return (String) isilOrNotCombo.getSelectedItem();
    }
}
