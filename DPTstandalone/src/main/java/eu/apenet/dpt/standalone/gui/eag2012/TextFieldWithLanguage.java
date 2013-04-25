package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.EagScrollPane;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.EagScrollPaneHolder;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 05/12/2012
 *
 * To be used when a TextField also has a language
 *
 * @author Yoann Moranville
 */
public class TextFieldWithLanguage {
    private EagScrollPaneHolder eagScrollPaneHolder;
    private JTextField extraField;
    private JTextField secondExtraField;
    private JComboBox languageBox;
    private static final String[] languages = EagPanels.languages;
    private static final String[] languagesDisplay = EagPanels.languagesDisplay;

    public TextFieldWithLanguage(String text, String language, String extraText, String secondExtraText) {
        eagScrollPaneHolder = new EagScrollPaneHolder(text);
        languageBox = new JComboBox(languagesDisplay);
        if(Arrays.asList(languages).contains(language))
            languageBox.setSelectedItem(language);
        else
            languageBox.setSelectedItem("---");
        extraField = new JTextField(extraText);
        secondExtraField = new JTextField(secondExtraText);
    }

    public TextFieldWithLanguage(String text, String language, String extraText) {
        this(text, language, extraText, "");
    }

    public TextFieldWithLanguage(String text, String language) {
        this(text, language, "");
    }

    public EagScrollPane getTextField() {
        return eagScrollPaneHolder.getScrollPane();
    }

    public JTextField getExtraField() {
        return extraField;
    }

    public JTextField getSecondExtraField() {
        return secondExtraField;
    }

    public JComboBox getLanguageBox() {
        return languageBox;
    }

    public String getTextValue() {
        return eagScrollPaneHolder.getText();
    }

    public String getExtraValue() {
        return extraField.getText();
    }

    public String getSecondExtraValue() {
        return secondExtraField.getText();
    }

    public String getLanguage() {
        if(Arrays.asList(languages).contains((String) languageBox.getSelectedItem()))
            return (String)languageBox.getSelectedItem();
        return null;
    }
}
