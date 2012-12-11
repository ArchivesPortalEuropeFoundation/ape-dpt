package eu.apenet.dpt.standalone.gui.eag2012;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 11/12/2012
 *
 * @author Yoann Moranville
 */
public class LanguageWithScript {

    private JComboBox languageBox;
    private JComboBox scriptBox;
    private static final String[] languages = {"eng", "fre"};
    private static final String[] languagesDisplay = {"---", "eng", "fre"};

    private static final String[] scripts = {"Latn"};
    private static final String[] scriptsDisplay = {"---", "Latn"};

    public LanguageWithScript(String language, String script) {
        languageBox = new JComboBox(languagesDisplay);
        if(Arrays.asList(languages).contains(language))
            languageBox.setSelectedItem(language);

        scriptBox = new JComboBox(scriptsDisplay);
        if(Arrays.asList(scripts).contains(script))
            scriptBox.setSelectedItem(script);
    }

    public JComboBox getLanguageBox() {
        return languageBox;
    }

    public String getLanguage() {
        if(Arrays.asList(languages).contains((String) languageBox.getSelectedItem()))
            return (String)languageBox.getSelectedItem();
        return null;
    }

    public JComboBox getScriptBox() {
        return scriptBox;
    }

    public String getScript() {
        if(Arrays.asList(scripts).contains((String) scriptBox.getSelectedItem()))
            return (String)scriptBox.getSelectedItem();
        return null;
    }
}
