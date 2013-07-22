package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import eu.apenet.dpt.standalone.gui.eag2012.EagPanels;
import eu.apenet.dpt.utils.util.LanguageIsoList;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 26/04/2013
 *
 * @author Yoann Moranville
 */
public class TextAreaWithLanguage extends StructureWithLanguage {
    private EagScrollPaneHolder eagScrollPaneHolder;
    private JTextField extraField;
    private JTextField secondExtraField;

    public TextAreaWithLanguage(String text, String language, String extraText, String secondExtraText) {
        super(language);
        eagScrollPaneHolder = new EagScrollPaneHolder(text);
        extraField = new JTextField(extraText);
        secondExtraField = new JTextField(secondExtraText);
    }

    public TextAreaWithLanguage(String text, String language, String extraText) {
        this(text, language, extraText, "");
    }

    public TextAreaWithLanguage(String text, String language) {
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

    public String getTextValue() {
        return eagScrollPaneHolder.getText();
    }

    public String getExtraValue() {
        return extraField.getText();
    }

    public String getSecondExtraValue() {
        return secondExtraField.getText();
    }
}
