package eu.apenet.dpt.standalone.gui.eag2012;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 05/12/2012
 *
 * To be used when a TextField also has a language
 *
 * @author Yoann Moranville
 */
public class TextFieldWithLanguage {
    private JTextField textField;
    private JTextField extraField;
    private JComboBox languageBox;
    private static final String[] languages = {"eng", "fre"};
    private static final String[] languagesDisplay = {"---", "eng", "fre"};

    public TextFieldWithLanguage(String text, String language, String extraText) {
        textField = new JTextField(text);
        languageBox = new JComboBox(languagesDisplay);
        if(Arrays.asList(languages).contains(language))
            languageBox.setSelectedItem(language);
        extraField = new JTextField(extraText);
    }

    public TextFieldWithLanguage(String text, String language) {
        this(text, language, "");
    }

    public JTextField getTextField() {
        return textField;
    }

    public JTextField getExtraField() {
        return extraField;
    }

    public JComboBox getLanguageBox() {
        return languageBox;
    }

    public String getTextValue() {
        return textField.getText();
    }

    public String getExtraValue() {
        return extraField.getText();
    }

    public String getLanguage() {
        if(Arrays.asList(languages).contains((String) languageBox.getSelectedItem()))
            return (String)languageBox.getSelectedItem();
        return null;
    }
}
