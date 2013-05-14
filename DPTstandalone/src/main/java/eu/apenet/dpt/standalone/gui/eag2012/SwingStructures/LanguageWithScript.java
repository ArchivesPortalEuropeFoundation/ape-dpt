package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import eu.apenet.dpt.standalone.gui.eag2012.EagPanels;
import eu.apenet.dpt.utils.util.LanguageIsoList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 11/12/2012
 *
 * @author Yoann Moranville
 */
public class LanguageWithScript {

    private JComboBox languageBox;
    private JComboBox scriptBox;
    private static final String[] languages = EagPanels.languages;
    private static final String[] languagesDisplay = EagPanels.languagesDisplay;

    private static final String[] scripts = {"Arab", "Armn", "Cprt", "Cyrl", "Geor", "Grek", "Hebr", "Latn"};
    private static String[] scriptsDisplay;

    static {
        List<String> scriptList = new ArrayList<String>();
        scriptList.add("---");
        scriptList.addAll(Arrays.asList(scripts));
        scriptsDisplay = scriptList.toArray(new String[]{});
    }

    public LanguageWithScript(String language, String script) {
        languageBox = new JComboBox(languagesDisplay);
        if(Arrays.asList(languages).contains(LanguageIsoList.getLanguageStr(language)))
            languageBox.setSelectedItem(LanguageIsoList.getLanguageStr(language));
        else
            languageBox.setSelectedItem("---");
        scriptBox = new JComboBox(scriptsDisplay);
        if(Arrays.asList(scripts).contains(script))
            scriptBox.setSelectedItem(script);
    }

    public JComboBox getLanguageBox() {
        return languageBox;
    }

    public String getLanguage() {
        if(Arrays.asList(languages).contains((String) languageBox.getSelectedItem()))
            return LanguageIsoList.getIsoCode((String)languageBox.getSelectedItem());
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
