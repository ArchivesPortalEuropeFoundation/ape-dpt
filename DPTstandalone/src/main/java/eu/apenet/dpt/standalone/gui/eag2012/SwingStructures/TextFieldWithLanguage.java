package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import eu.apenet.dpt.standalone.gui.eag2012.EagPanels;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.EagScrollPane;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.EagScrollPaneHolder;
import eu.apenet.dpt.utils.util.LanguageIsoList;

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
public class TextFieldWithLanguage extends StructureWithLanguage {
    private JTextField textField;
    private JTextField extraField;
    private JTextField secondExtraField;

    public TextFieldWithLanguage(String text, String language, String extraText, String secondExtraText) {
        super(language);
        textField = new JTextField(text);
        extraField = new JTextField(extraText);
        secondExtraField = new JTextField(secondExtraText);
    }

    public TextFieldWithLanguage(String text, String language, String extraText) {
        this(text, language, extraText, "");
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

    public JTextField getSecondExtraField() {
        return secondExtraField;
    }

    public String getTextValue() {
        return textField.getText();
    }

    public String getExtraValue() {
        return extraField.getText();
    }

    public String getSecondExtraValue() {
        return secondExtraField.getText();
    }
}
